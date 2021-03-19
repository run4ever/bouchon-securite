package sb.controllers;

import sb.models.LostPassword;
import sb.services.Database;
import sb.models.Product;
import sb.models.Category;
import sb.models.Cart;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProductsController {

    @GetMapping("/products")
    public ModelAndView showProduct(@RequestParam(value="cat", required=true) Integer id, HttpServletRequest req) throws IllegalAccessException {
        ModelAndView mav = new ModelAndView("showproducts");
        JdbcTemplate template = Database.getDb();

//        Category cat = (Category) template.queryForObject("SELECT * from category WHERE id=" + id,
//          new BeanPropertyRowMapper(Category.class));

        if(id<=0){
            throw new IllegalAccessException("id must be >0");
        }
        Category cat = (Category) template.queryForObject("SELECT * FROM category WHERE id = ?",
                new Object[] { id },
                new BeanPropertyRowMapper(Category.class)
        );

        mav.addObject("category", cat);

        List<Product> res = new ArrayList<Product>();

        res = template.query("SELECT * FROM product WHERE category=?",
            new Object[] { id },
            new BeanPropertyRowMapper(Product.class));

        mav.addObject("products", res);
        Cart.addToModelAndView(mav, req);
        return mav;
    }
}
