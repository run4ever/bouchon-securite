package sb.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;

import sb.models.SBUser;
import sb.services.Database;
import sb.models.LostPassword;

@Controller
public class UserController {

   @GetMapping("/lostpassword")
   public @ResponseBody String lostPassword(@RequestParam(value="username", required=true) String username, HttpServletRequest req) {
      JdbcTemplate template = Database.getDb();
      JSONObject json = new JSONObject();
      String res = "E-mail envoyé sur l'adresse enregistrée lors de l'inscription";

      SBUser user = SBUser.fromUsername(username);
      if (user == null) {
         json.put("msg", "Ce compte n'existe pas");
         return json.toString();
      }

      LostPassword lp = new LostPassword();
      lp.setUsername(username);
      lp.generateToken();

      template.update("INSERT INTO lostpassword (username, expiration, token) VALUES (?, ?, ?)",
         lp.getUsername(), lp.getExpiration(), lp.getToken());

      json.put("msg", res);
      if (username.equals("user"))
      {
         String mail = "Cliquez sur le lien suivant pour définir un nouveau mot de passe : \n";
         mail += req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/recoverpassword?token=" + lp.getToken() + " \n";
         mail += "Ce lien est valable 10 minutes";
         json.put("email", mail);
      }
      return json.toString();
   }

   @GetMapping("/recoverpassword")
   public @ResponseBody String recoverPassword(@RequestParam(value="token", required=true) String token) {
      JdbcTemplate template = Database.getDb();

      try {
         String query = ("SELECT * FROM lostpassword WHERE token = ?");
         LostPassword lp = (LostPassword) template.queryForObject(query,
            new Object[] { String.valueOf(token) },
            new BeanPropertyRowMapper(LostPassword.class)
         );

         long curtime = System.currentTimeMillis();

         if (lp.getExpiration().getTime() < curtime)
            return "Token expiré";

         return "Nouveau mot de passe : ";
      }
      catch (Exception e) {
         return "Mauvais token";
      }
   }
}
