package sb.controllers;

import sb.models.Cart;
                  
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
   @GetMapping("/home")
   public ModelAndView home(HttpServletRequest req) {
      ModelAndView mav = new ModelAndView("home");
      Cart.addToModelAndView(mav, req);

      return mav;
   }
}
