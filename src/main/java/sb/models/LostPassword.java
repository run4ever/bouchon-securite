package sb.models;

import java.sql.Timestamp;
import java.security.MessageDigest;
import java.io.IOException;

public class LostPassword {
   private long id;
   private String username;
   private Timestamp expiration;
   private String token;

   public LostPassword(long id, String username, Timestamp expiration, String token) {
      this.id = id;
      this.username = username;
      this.expiration = expiration;
      this.token = token;
   }

   public LostPassword() {
      this.id = -1;
      this.username = "";
      this.expiration = new Timestamp(System.currentTimeMillis() + 1000*60*60*10);
      this.token = "";
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public Timestamp getExpiration() {
      return this.expiration;
   }

   public void setExpiration(Timestamp expiration) {
      this.expiration = expiration;
   }

   public String getToken() {
      return this.token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public boolean generateToken() {
      this.expiration = new Timestamp(System.currentTimeMillis() + 1000*60*60*10);
      try {
         MessageDigest instance = MessageDigest.getInstance("MD5");
         byte[] messageDigest = instance.digest(String.valueOf(System.currentTimeMillis()).getBytes());
         StringBuilder hexString = new StringBuilder();
         for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1) {
               hexString.append('0');
            }
            hexString.append(hex);
         }

         this.token = hexString.toString();
         return true;
      }
      catch (Exception e) {
         this.token = "internal error";
         return false;
      }
   }
}
