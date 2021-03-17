package sb.models;

import java.io.Serializable;

public class CartItem implements Serializable {
   private long product;
   private long count;

   public CartItem() {
      this.product = -1;
      this.count = 0;
   }

   public CartItem(long product, long count) {
      this.product = product;
      this.count = count;
   }

   public long getProduct() {
      return this.product;
   }

   public long getCount() {
      return this.count;
   }

   public void setCount(long count) {
      this.count = count;
   }

}
