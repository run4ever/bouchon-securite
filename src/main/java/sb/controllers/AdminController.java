package sb.controllers;

import javassist.NotFoundException;
import org.apache.commons.codec.digest.Crypt;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import sb.services.EmbeddedLdap;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.BufferedReader;
import java.lang.Process;
import java.io.StringReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchResult;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpressionException;
import java.io.StringReader;
import org.xml.sax.InputSource;

import java.util.HashMap;
import java.util.Map;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import java.io.UnsupportedEncodingException;

import java.util.regex.Pattern;
import java.util.Date;


@Controller
public class AdminController {
   private String secret = "SuperBouchons_2018!";

   private void logout(HttpServletRequest request, ModelAndView mav) {
      HttpSession session = request.getSession(false);
      if (session == null)
      {
         mav.addObject("msg", "erreur interne");
         return;
      }
      String login = (String)session.getAttribute("login");
      if (login != null && !login.isEmpty())
      {
         session.setAttribute("login", "");
         mav.addObject("msg", "d??connect?? !");
      }
   }

   private String checklogin(String login, String password) throws NotFoundException {
      Logger log = LoggerFactory.getLogger(this.getClass());
      
      try {
         InMemoryDirectoryServer server = EmbeddedLdap.getServer();

         //Objectif : ??viter qu'un user se connecte avec admin / * (acc??s possible en LDAP)

         //si mdp == * renvoyer une exception
         if(password.equals("*")){
            throw new NotFoundException("* is a forbidden value in password field");
         }

         //liste blanche des caract??res autoris??s pour user name
         Pattern reg = Pattern.compile("^[a-zA-Z0-9]+$");
         if(!reg.matcher(login).matches()){
            throw new NotFoundException("This user does not exist - forbidden chars");
         }

         //on ne filtre plus sur les password, on les traitera plus bas
//         String filter = "(&(uid=" + login + ")(userPassword=" + password + "))";
         String filter = "(uid=" + login + ")";
         log.info(filter);

         // 0 - remplacer les mots de passe en clair du fichier ldiff par leur hash dans le fichier data.ldiff
         // 1 - requete ldap pour r??cup??rer le hash de l'utilisateur dans la base ldiff
         SearchResult rs = server.search("ou=admins,dc=superbouchons,dc=org",
                 SearchScope.SUB, filter, null);
         String storedpasswd = rs.getSearchEntries().get(0).getAttributeValue("userPassword");

         if (rs.getEntryCount() == 1) {
            log.info("Connexion admin : " + login);
            // 2 - crypter le mot de passe saisi par l'utilisateur, avec la classe Crypt
            Crypt c = new Crypt();
            String hashPwd = c.crypt(password,storedpasswd);
            //le 2??me param??tre de crypt : il a juste besoin de $5$86456 mais pour ??viter de mettre en dur (car change pour chaque user)
            // on met tout le mot de passe stock?? et crypt r??cup??rera le d??but dont il a besoin pour crypter avec le meme sel

            // 3 - on compare le mot de passe hach?? stock?? et le hash du mot de passe saisi
            if(storedpasswd.equals(hashPwd)){
               return login;
            }
            else return "";
         } else
            return "";
      }
      catch (LDAPSearchException e) {
         log.info(e.getMessage());
         return "";
      }
   }

   private String checkToken(HttpServletRequest request) {
      Logger log = LoggerFactory.getLogger(this.getClass());
      String header = request.getHeader("Authorization");

      if (header == null || !header.startsWith("Bearer ")) {
         log.info("No JWT token found in request headers " + header);
         return "";
      }

      String authToken = header.substring(7);
      log.info("JWT: " + authToken);
      try {
         Algorithm algorithmHS = Algorithm.HMAC256(secret);
         JWTVerifier verifier = JWT.require(algorithmHS)
            .withIssuer("SB-auth")
            .build(); //Reusable verifier instance
         DecodedJWT jwt = verifier.verify(authToken);
         Claim loginclaim = jwt.getClaim("login");
         if (loginclaim.isNull()) {
            log.info("No login");
            return "";
         }
         return loginclaim.asString();
      } catch (UnsupportedEncodingException exception){
         log.info("UTF-8 encoding not supported");
         return "";
      } catch (JWTVerificationException e){
         log.info(e.getMessage());
         return "";
      }
   }

   @GetMapping("/admin")
   public String adminPage() {
      return "admin";
   }

   @PostMapping("/admin/auth")
   public @ResponseBody String adminAction(HttpServletRequest request, @RequestParam(value="login", required=true) String login, @RequestParam(value="pass", required=true) String password) throws NotFoundException {
      Logger log = LoggerFactory.getLogger(this.getClass());
      JSONObject json = new JSONObject();

      String res = checklogin(login, password);
      if (res.length() == 0) {
         json.put("success", false);
      } else {
         json.put("success", true);

         try {
            Algorithm algorithmHS = Algorithm.HMAC256(secret);

            String token = JWT.create()
                  .withClaim("login", login)
                  .withIssuer("SB-auth")
                  .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 2))) // 2 hours
                  .sign(algorithmHS);

            json.put("token", token);
         } catch (UnsupportedEncodingException e){
            log.info(e.getMessage());
            json.put("success", false);
         } catch (JWTCreationException e){
            log.info(e.getMessage());
            json.put("success", false);
         }

      }
      return json.toString();
   }

   @PostMapping("/admin/img")
   public @ResponseBody String adminimgAction(HttpServletRequest req, @RequestParam(value = "file") MultipartFile file) {
      String msg = "";
      JSONObject json = new JSONObject();
      Logger log = LoggerFactory.getLogger(this.getClass());
      final List<String> contentTypes = Arrays.asList("png", "jpg", "jpeg", "gif");

      if (checkToken(req).isEmpty()) {
         json.put("msg", "Bad token");
         return json.toString();
      }

      if (file.isEmpty())
         msg = "Fichier vide !";
      else
      {
         String fname = file.getOriginalFilename();
         Integer pointIndex = fname.indexOf(".");
         String extension = fname.substring(pointIndex+1,fname.length()).toLowerCase();
         if(contentTypes.contains(extension)){
            String path = req.getSession().getServletContext().getRealPath("/img/products")
                    + File.separator + fname;

            File dest = new File(path);
            try {
               file.transferTo(dest);
               msg = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/img/products/" + fname;
            } catch (Exception e) {
               log.info("Erreur : " + e.getMessage());
               msg = "Erreur : " + e.getMessage();
            }
         }
         else{
            json.put("msg", "Bad extension");
            return json.toString();
         }
      }
      json.put("msg", msg);
      return json.toString();
   }

   @PostMapping("/admin/suppliers")
   public @ResponseBody String searchParse(HttpServletRequest request, @RequestParam(value="param", required=true) String param) {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      Logger log = LoggerFactory.getLogger(this.getClass());
      log.info(param);
      JSONObject json = new JSONObject();

      try {
         DocumentBuilder builder = factory.newDocumentBuilder();
         
         Document doc = builder.parse(new InputSource(new StringReader(param)));

         Element racine = doc.getDocumentElement();
         XPathFactory xpf = XPathFactory.newInstance();
         XPath path = xpf.newXPath();
         String expression = "/searchForm/val[1]";
         String str = (String)path.evaluate(expression, racine);

         json.put("msg", "Pas de r??sultats pour " + str);
      }
      catch (final ParserConfigurationException e) {
         e.printStackTrace();
         json.put("msg", "erreur interne");
      }
      catch (final SAXException e) {
         log.info(e.getMessage());   
         e.printStackTrace();
         json.put("msg", "erreur interne");
      }
      catch (final IOException e) {
         e.printStackTrace();
         json.put("msg", "erreur interne");
      }
      catch (final XPathExpressionException e) {
         e.printStackTrace();
         json.put("msg", "erreur interne");
      }
      return json.toString();
   }

   @PostMapping("/admin/backup")
   public @ResponseBody String backup(HttpServletRequest request, @RequestParam(value="backupname", required=true) String name) throws NotFoundException {
      Runtime runtime = Runtime.getRuntime();
      String OS = System.getProperty("os.name").toLowerCase();
      String res = "";
      Logger log = LoggerFactory.getLogger(this.getClass());
      JSONObject json = new JSONObject();

      //Objectif : se prot??ger d'une saisie de " & dir & echo " dans le champ sauvegarde de l'interface admin

      // 1 - filtrer en liste blanche les caract??res autoris??s pour file name
      Pattern reg = Pattern.compile("^[a-zA-Z0-9]+$");
      if(!reg.matcher(name).matches()){
         throw new NotFoundException("Bad file name");
      }

      String fname = LocalDate.now().toString() + "-" + name + ".zip";
      String cmd = "myzip dirsrc \"" + fname + "\"";

      log.info("cmd [" + cmd + "]");

      try {
         Process p;

         // 2 - remplacer l'appel au shell par la commande myzip
         if (OS.indexOf("win") >= 0)
//            p = runtime.exec(new String[]{"cmd.exe", "/C", cmd});
            p = runtime.exec(new String[]{"myzip.exe", "dirsrc", fname});
         else
//            p = runtime.exec(new String[]{"/bin/sh", "-c", cmd});
            p = runtime.exec(new String[]{"myzip", "dirsrc", fname});

         p.waitFor();
         BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
         BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
         String ligne = "";

         while ((ligne = output.readLine()) != null) {
            res += ligne + "\n";
         }

         while ((ligne = error.readLine()) != null) {
            res += ligne + "\n";
         }
         json.put("msg", res);

      } catch (IOException e) {
         log.info(e.getMessage());
         e.printStackTrace();
         json.put("err", "erreur interne");
      } catch (InterruptedException e) {
         log.info(e.getMessage());
         e.printStackTrace();
         json.put("err", "erreur interne");
      }
      return json.toString();
   }

   @PostMapping("/admin/email")
   public @ResponseBody String checkEmail(HttpServletRequest request, @RequestParam(value="email", required=true) String addr) {
      JSONObject json = new JSONObject();
      String res;
      boolean result;
      Pattern p = Pattern.compile("^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$");

      result = p.matcher(addr).matches();
      res = "L'adresse " + addr + " est " + (result ? "valide" : "invalide");

      json.put("msg", res);
      return json.toString();
   }

}
