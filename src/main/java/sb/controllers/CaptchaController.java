package sb.controllers;

import sb.models.Captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import javax.servlet.ServletOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CaptchaController {
   @GetMapping("/captcha")
   public void getCaptcha(@RequestParam(value="id", required=true) int id, HttpServletResponse response) {
      ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
      Captcha cap = new Captcha(id);
      String str = cap.getChallenge();
         Logger log = LoggerFactory.getLogger(this.getClass());

      try {
         int width = 100;
         int height = 30;
         BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
         Graphics2D ig2 = bi.createGraphics();
         ig2.setBackground(Color.WHITE);
         ig2.clearRect(0, 0, width, height);

         Font font = new Font("TimesRoman", Font.BOLD, 20);
         ig2.setFont(font);
         FontMetrics fontMetrics = ig2.getFontMetrics();
         int stringWidth = fontMetrics.stringWidth(str);
         int stringHeight = fontMetrics.getAscent();
         ig2.setPaint(Color.black);
         ig2.drawString(str, (width - stringWidth) / 2, height / 2 + stringHeight / 4);

         ImageIO.write(bi, "jpeg", jpegOutputStream);
      
         byte[] imgByte = jpegOutputStream.toByteArray();

         response.setHeader("Cache-Control", "no-store");
         response.setHeader("Pragma", "no-cache");
         response.setDateHeader("Expires", 0);
         response.setContentType("image/jpeg");
         ServletOutputStream responseOutputStream = response.getOutputStream();
         responseOutputStream.write(imgByte);
         responseOutputStream.flush();
         responseOutputStream.close();
      } catch (Exception e) {
         log.info(e.getMessage());
         return;
      }
   }
}
