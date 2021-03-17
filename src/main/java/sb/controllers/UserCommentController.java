package sb.controllers;

import sb.services.Database;
import sb.models.Captcha;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import java.security.Principal;

import java.util.List;
import java.util.ArrayList;
import java.sql.Date;

@Controller
public class UserCommentController {

   @PostMapping("/comment")
   public String addComment(
         @RequestParam(value="product", required=true) String product,
         @RequestParam(value="mark", required=true) String smark,
         @RequestParam(value="comment", required=true) String comment,
         @RequestParam(value="captchaval", required=true) int captchavalue,
         @RequestParam(value="captchares", required=true) String captchares,
         Principal principal
      ) {
      JdbcTemplate template = Database.getDb();
      Captcha cap = new Captcha(captchavalue);
      if (!cap.check(captchares))
         throw new IllegalArgumentException("Mauvais captcha");

      int mark = Integer.parseInt(smark);
      if (mark < 0 || mark > 5)
         throw new IllegalArgumentException("mark must be between 0 and 5");

      String cmt = comment.replace("<script>", "").replace("</string>", "");
      
      template.update("INSERT INTO usercomment (product, username, mark, datetime, content) VALUES (?,?,?,TODAY(),?)",
         Integer.parseInt(product), principal.getName(), mark, cmt);
      
      return "redirect:/product?id=" + product;
   }

}
