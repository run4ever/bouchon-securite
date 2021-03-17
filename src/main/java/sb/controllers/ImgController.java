package sb.controllers;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.io.FileSystemResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Controller
public class ImgController {

   @GetMapping("/picture")
   @ResponseBody
   public FileSystemResource getImg(@RequestParam(value="name", required=true) String fname, HttpServletRequest request) {
      Logger log = LoggerFactory.getLogger(this.getClass());
      try {
         String path = request.getSession().getServletContext().getRealPath("/img/products/")
          + File.separator + fname;
         log.info(path);
         return new FileSystemResource(path);
      } catch (Exception e) {
         log.info("erreur : " + e.getMessage());
         e.printStackTrace();
         return null;
      }
   }

}
