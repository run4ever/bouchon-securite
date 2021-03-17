package sb.controllers;

import sb.models.Product;
import sb.models.CartContent;
import sb.models.CartItem;
import sb.models.Cart;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;
import java.sql.Date;

@Controller
public class CartController {

   @PostMapping("/addtocart")
   public String showProduct(@RequestParam(value="id", required=true) long product, @RequestParam(value="nb", required=true) long count, HttpServletRequest req, HttpServletResponse response) {
      if (count <= 0)
         throw new IllegalArgumentException("Mauvais nombre");
      CartItem item = new CartItem(product, count);
      Cart cart = Cart.loadFromCookie(req);
      cart.addItem(item);
      if (cart.saveToCookie(response) == false)
         return "redirect:/product?id=" + product;
         //return "error";

      return "redirect:/product?id=" + product;
   }

   @GetMapping("/viewcart")
   public ModelAndView viewCart(HttpServletRequest req, HttpServletResponse response) {
      ModelAndView mav = new ModelAndView("viewcart");
      Cart.addToModelAndView(mav, req);
      Cart cart = Cart.loadFromCookie(req);

      mav.addObject("cart", cart);

      List <CartContent> content = new ArrayList<CartContent>();
      for (CartItem it: cart.getItems())
      {
         Product prod = Product.fromId(it.getProduct());
         content.add(new CartContent(prod.getLabel(), it.getCount(), (double) prod.getPrice() * it.getCount() / 100));
      }

      mav.addObject("contentlist", content);

      return mav;
   }

   @GetMapping("/emptycart")
   public String emptyCart(HttpServletRequest req, HttpServletResponse response) {
      Cart cart = Cart.loadFromCookie(req);
      cart.empty();
      cart.saveToCookie(response);
      return "redirect:/viewcart";
   }
}
