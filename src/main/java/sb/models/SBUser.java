package sb.models;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sb.services.Database;

public class SBUser implements UserDetails {
   private int id;
   private String username;
   private String password;

   public SBUser() {
      this.id = -1;
      this.username = "";
      this.password = "";
   }

   public Collection<? extends GrantedAuthority> getAuthorities() {
      List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
      list.add(new SimpleGrantedAuthority("USER"));
      return list;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String pass) {
      this.password = pass;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String user) {
      this.username = user;
   }

   public boolean isAccountNonExpired() {
      return true;
   }

   public boolean isAccountNonLocked() {
      return true;
   }

   public boolean isCredentialsNonExpired() {
      return true;
   }

   public boolean isEnabled() {
      return true;
   }

   public static SBUser fromUsername(String username) {
      JdbcTemplate template = Database.getDb();
      try {
      SBUser res = (SBUser) template.queryForObject("SELECT * FROM users WHERE username = ?",
         new Object[] { String.valueOf(username) },
         new BeanPropertyRowMapper(SBUser.class));

         return res;
      } catch (Exception e) {
         return null;
      }
   }

}
