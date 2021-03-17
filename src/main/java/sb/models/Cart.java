package sb.models;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.util.WebUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import java.io.IOException;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cart implements Serializable {
   private List<CartItem> items;
   private long discount;  // percent

   public Cart() {
      this.items = new ArrayList<CartItem>();
      this.discount = 0;
   }

   public void setDiscount(long discount) {
      if (discount < 0 || discount > 100)
         return;
      this.discount = discount;
   }

   public long getDiscount() {
      return this.discount;
   }

   public void addItem(CartItem it) {
      this.items.add(it);
   }

   public List<CartItem> getItems() {
      return this.items;
   }

   public boolean changeProductCount(long product, long count)  {
      for (CartItem it: this.items) {
         if (it.getProduct() == product)
         {
            it.setCount(count);
            return true;
         }
      }
      return false;
   }

   public int getSize() {
      return this.items.size();
   }

   public float getOriginalPrice() {
      float res = 0;
      for (CartItem it: this.items) {
         Product prod = Product.fromId(it.getProduct());
         res += prod.getPrice() * it.getCount();
      }
      return (res / 100);
   }

   public float getTotalPrice() {
      return this.getOriginalPrice() - (this.getOriginalPrice() * this.discount / 100);
   }

   public void empty() {
      this.discount = 0;
      this.items.clear();
   }

   public boolean saveToCookie(HttpServletResponse response) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      try {
         ObjectOutputStream oos = new ObjectOutputStream(baos);
         oos.writeObject(this);

         String str = Base64.getEncoder().encodeToString(baos.toByteArray());
         response.addCookie(new Cookie("cart", str));
         return true;
      }
      catch (Exception e) {
         Logger log = LoggerFactory.getLogger(this.getClass());
         log.info(e.getMessage());
         return false;
      }
   }
 
   public static Cart loadFromCookie(HttpServletRequest req) {
      Cookie cookie = WebUtils.getCookie(req, "cart");

      if (cookie == null)
         return new Cart();
      try {
         byte[] data = Base64.getDecoder().decode(cookie.getValue());
         ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
         Object o = ois.readObject();
         ois.close();
         return (Cart) o;
      }
      catch (Exception e) { 
         Logger log = LoggerFactory.getLogger("Cart");
         log.info(e.getMessage());
         e.printStackTrace();
         return new Cart();
      }
   }

   public static void addToModelAndView(ModelAndView mav, HttpServletRequest req) {
      int itemcount = 0;
      int totalprice = 0;

      Cart cart = Cart.loadFromCookie(req);
      mav.addObject("cartcount", cart.getSize());
      mav.addObject("carttotalprice", cart.getTotalPrice());
   }
}
