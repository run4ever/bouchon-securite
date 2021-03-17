package sb.models;

import java.util.Random;
import java.security.MessageDigest;

public class Captcha {
   private int value;
   private String secret = "SuperBouchonsCaptcha2017!";

   public Captcha() {
      Random randomGenerator = new Random();
      this.value = randomGenerator.nextInt(9999);
   }

   public Captcha(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }

   public String getChallenge() {
      try {
         MessageDigest md = MessageDigest.getInstance("MD5");
         String tohash = secret + value;
         byte[] thedigest = md.digest(tohash.getBytes("UTF-8"));
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < thedigest.length; ++i) {
            sb.append(Integer.toHexString(0xFF & thedigest[i]));
         }
         return sb.toString().substring(0, 6);
      }
      catch (Exception e) {
         return e.getMessage();
      }
   }

   public boolean check(String result) {
      String chall = getChallenge();
      if (chall.equals(result))
         return true;
      return false;
   }

}
