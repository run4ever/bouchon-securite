package sb.models;

import java.sql.Date;

public class UserComment {
   private long id;
   private long product;
   private String username;
   private int mark;
   private Date datetime;
   private String content;

   public UserComment(long id, long product, String username, int mark, Date datetime, String content) {
      this.id = id;
      this.product = product;
      this.username = username;
      this.mark = mark;
      this.datetime = datetime;
      this.content = content;
   }

   public UserComment() {
      this.id = -1;
      this.product = -1;
      this.username = "";
      this.mark = -1;
      this.datetime = new Date(1970,0,1);
      this.content = "";
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public long getProduct() {
      return this.product;
   }

   public void setProduct(long product) {
      this.product = product;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public int getMark() {
      return this.mark;
   }

   public void setMark(int mark) {
      this.mark = mark;
   }

   public Date getDatetime() {
      return this.datetime;
   }

   public void setDatetime(Date datetime) {
      this.datetime = datetime;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   } 
}
