package sb.models;

import java.util.List;
import java.util.ArrayList;

public class CartContent {
   public String productName;
   public long count;
   public double price;

   public CartContent(String pn, long count, double price) {
      this.productName = pn;
      this.count = count;
      this.price = price;
   }
}
