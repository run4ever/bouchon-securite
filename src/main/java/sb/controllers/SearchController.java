package sb.controllers;

import sb.services.Database;
import sb.models.Cart;
import sb.models.Product;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {
   @PostMapping("/search")
   public ModelAndView searchSubmit(@RequestParam(value="str", required=true) String str, HttpServletRequest req) {
      JdbcTemplate template = Database.getDb();
      ModelAndView mav = new ModelAndView("searchresult");
      List<Product> res = new ArrayList<Product>();
      String s = new String();

      res = template.query("SELECT * FROM product WHERE description LIKE '%" + str + "%' UNION SELECT * FROM product WHERE label LIKE '%" + str + "%'" ,
         new BeanPropertyRowMapper(Product.class));

      s = "Résultat de la recherche de <i>" + str + "</i> : <b>";
      s += res.size();
      s += "</b> produit";
      if (res.size() > 1)
         s += "s";

      mav.addObject("products", res);
      mav.addObject("str", s);

      Cart.addToModelAndView(mav, req);

      return mav;
   }
}
