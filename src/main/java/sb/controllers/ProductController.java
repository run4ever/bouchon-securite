package sb.controllers;

import sb.services.Database;
import sb.models.Captcha;
import sb.models.Cart;
import sb.models.UserComment;
import sb.models.Material;
import sb.models.Category;
import sb.models.Product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.ArrayList;
import java.sql.Date;

@Controller
public class ProductController {

   @GetMapping("/product")
   public ModelAndView showProduct(@RequestParam(value="id", required=true) String id, HttpServletRequest req) {
      JdbcTemplate template = Database.getDb();
      Product res = (Product) template.queryForObject("SELECT * FROM product WHERE id=" + id,
         new BeanPropertyRowMapper(Product.class));

      Category cat = (Category) template.queryForObject("SELECT * from category WHERE id=" + res.getCategory(),
         new BeanPropertyRowMapper(Category.class));    

      Material mat = (Material) template.queryForObject("SELECT * from material WHERE id=" + res.getMaterial(),
         new BeanPropertyRowMapper(Material.class));

      List<UserComment> comm = new ArrayList<UserComment>();
      comm = template.query("SELECT * FROM usercomment WHERE product = " + id,
         new BeanPropertyRowMapper(UserComment.class));

      List<String> usercomments = new ArrayList<String>();
      for (UserComment uc: comm)
      {
         String s = "<b>" + uc.getUsername() + "</b> ";
         s += "(" + uc.getDatetime() + ") ";
         s += "[" + uc.getMark() + " / 5] ";
         s += "<i>" + uc.getContent() + "</i>";
         usercomments.add(s);
      }

      Captcha cap = new Captcha();

      ModelAndView mav = new ModelAndView("showproduct");
      mav.addObject("product", res);
      mav.addObject("category", cat);
      mav.addObject("material", mat);
      mav.addObject("captchaval", cap.getValue());
      mav.addObject("usercomments", usercomments);

      Cart.addToModelAndView(mav, req);

      return mav;
   }
}
