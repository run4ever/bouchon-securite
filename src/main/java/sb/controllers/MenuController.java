package sb.controllers;

import sb.services.Database;
import sb.models.Category;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Controller
public class MenuController {

    @GetMapping("/menu")
    public @ResponseBody List<Category> getMenuList() {
         JdbcTemplate template = Database.getDb();
         List<Category> res = new ArrayList<Category>();

         res = template.query("SELECT * FROM category",
            new BeanPropertyRowMapper(Category.class));

         return res;
    }

}
