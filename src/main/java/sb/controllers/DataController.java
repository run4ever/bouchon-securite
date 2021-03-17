package sb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

import java.security.MessageDigest;
import org.json.JSONObject;
import java.util.Base64;

import org.apache.commons.codec.digest.Crypt;
import java.util.Random;

@Controller
public class DataController {
   @GetMapping("/data")
   public String show() {
      return "data";
   }

   private String byteArrayToStr(byte[] array) {
      StringBuilder hexString = new StringBuilder();
      for (int i = 0; i < array.length; i++) {
         String hex = Integer.toHexString(0xFF & array[i]);
         if (hex.length() == 1) {
            hexString.append('0');
         }
         hexString.append(hex);
      }
      return hexString.toString();
   }

   private byte[] strToByteArray(String hexstr) {
      hexstr = hexstr.replaceAll("\\s", "");
      int len = hexstr.length();

      if (len % 2 != 0)
         return null;

      byte[] res = new byte[len / 2];

      for (int i = 0; i < len; i += 2) {
         res[ i / 2] = (byte) ((Character.digit(hexstr.charAt(i), 16) << 4)
                            + Character.digit(hexstr.charAt(i + 1), 16));
      }

      return res;
   }

   @PostMapping("/data/gethashes")
   public @ResponseBody String gethashes(HttpServletRequest req) {
      JSONObject json = new JSONObject();

      try {
         String pass = req.getParameter("pass");
         String salt = req.getParameter("salt");

         MessageDigest instance;
         byte[] messageDigest;

         instance = MessageDigest.getInstance("MD5");
         messageDigest = instance.digest(pass.getBytes());
         json.put("md5", byteArrayToStr(messageDigest));
         instance = MessageDigest.getInstance("SHA1");
         messageDigest = instance.digest(pass.getBytes());
         json.put("sha1", byteArrayToStr(messageDigest));
         instance = MessageDigest.getInstance("SHA-256");
         messageDigest = instance.digest(pass.getBytes());
         json.put("sha256", byteArrayToStr(messageDigest));
         instance = MessageDigest.getInstance("SHA-512");
         messageDigest = instance.digest(pass.getBytes());
         json.put("sha512", byteArrayToStr(messageDigest));

         if (salt == null || salt.isEmpty()) {
            Random randomGenerator = new Random();
            salt = String.valueOf(randomGenerator.nextInt(99999));
         }
         json.put("salt", salt);

         Crypt c = new Crypt();
         json.put("freebsdmd5", c.crypt(pass, "$1$" + salt + "$"));
         json.put("saltedsha256", c.crypt(pass, "$5$" + salt + "$"));
         json.put("saltedsha512", c.crypt(pass, "$6$" + salt + "$"));
      }
      catch (Exception e) {
         json.put("err", "internal error : " + e.getMessage());
      }
      return json.toString();
   }

   @PostMapping("/data/base64decode")
   public @ResponseBody String b64d(HttpServletRequest req) {
      JSONObject json = new JSONObject();

      try {
         String base = req.getParameter("src");

         byte[] data = Base64.getDecoder().decode(base);
         json.put("base64decodedhex", byteArrayToStr(data));
         String res = new String(data);
         json.put("base64decodedascii", res);
      }
      catch (Exception e) {
         json.put("err", "internal error : " + e.getMessage());
      }
      return json.toString();
   }

   @PostMapping("/data/base64encodeascii")
   public @ResponseBody String b64ea(String action, HttpServletRequest req) {
      JSONObject json = new JSONObject();

      try {
        String ascii = req.getParameter("src");
        String base64 = "";

        base64 = Base64.getEncoder().encodeToString(ascii.getBytes());

        json.put("base64encoded", base64);
      }
      catch (Exception e) {
         json.put("err", "internal error : " + e.getMessage());
      }
      return json.toString();
   }

   @PostMapping("/data/base64encodehex")
   public @ResponseBody String b64eh(String action, HttpServletRequest req) {
      JSONObject json = new JSONObject();

      try {
        String hex = req.getParameter("src");
        String base64 = "";

        base64 = Base64.getEncoder().encodeToString(strToByteArray(hex));

        json.put("base64encoded", base64);
      }
      catch (Exception e) {
         json.put("err", "internal error : " + e.getMessage());
      }
      return json.toString();
   }

}
