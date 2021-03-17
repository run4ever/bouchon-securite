package sb.models;

import sb.services.Database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class Product {
   private long id;
   private String label;
   private String description;
   private int category; // Category
   private int material; // Material
   private int diameter;
   private int height;
   private int price;
   private String img; // webapp/img/products/

   public Product(long id, String label, String description, int category, int material, int diameter, int height, int price, String img) {
      this.id = id;
      this.label = label;
      this.description = description;
      this.category = category;
      this.material = material;
      this.diameter = diameter;
      this.height = height;
      this.price = price;
      this.img = img;
   }

   public Product() {
      this.id = -1;
      this.label = "";
      this.description = "";
      this.category = 0;
      this.material = 0;
      this.diameter = 0;
      this.height = 0;
      this.price = 0;
      this.img = "";
   }

   public void setId(long id) {
      this.id = id;
   }

   public long getId() {
      return id;
   }

   public void setLabel(String str) {
      this.label = str;
   }

   public String getLabel() {
      return this.label;
   }

   public void setDescription(String str) {
      this.description = str;
   }

   public String getDescription() {
      return this.description;
   }

   public void setCategory(int cat) {
      if (cat < 0)
         return; // TODO
      this.category = cat;
   }

   public int getCategory() {
      return this.category;
   }

   public void setMaterial(int param) {
      if (param < 0)
         return; // TODO
      this.material = param;
   }
  
   public int getMaterial() {
      return this.material;
   }

   public void setDiameter(int param) {
      if (param < 0)
         return; // TODO
      this.diameter = param;
   }
  
   public int getDiameter() {
      return this.diameter;
   }

   public void setHeight(int param) {
      if (param < 0)
         return; // TODO
      this.height = param;
   }
  
   public int getHeight() {
      return this.height;
   }

   public void setPrice(int param) {
      if (param < 0)
         return; // TODO
      this.price = param;
   }
   
   public int getPrice() {
      return this.price;
   }

   public void setImg(String img) {
      this.img = img;
   }

   public String getImg() {
      return this.img;
   }

   public static Product fromId(long id) {
      JdbcTemplate template = Database.getDb();
      Product res = (Product) template.queryForObject("SELECT * FROM product WHERE id=" + id,
         new BeanPropertyRowMapper(Product.class));

      return res;
   }
}
