package sb.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import sb.models.SBUser;

@Service
@Configurable
public class SBAuthenticationProvider implements AuthenticationProvider {

   @Override
   public Authentication authenticate(Authentication authentication) throws AuthenticationException {
      Logger log = LoggerFactory.getLogger(this.getClass());
      String name = authentication.getName();
      String password = authentication.getCredentials().toString();

      SBUser user = SBUser.fromUsername(name);
      if (user == null) {
         log.info("User " + name + " not found");
         throw new BadCredentialsException("Invalid username or password");
      }

      if (!user.isEnabled()) {
         throw new DisabledException("User disabled");
      }

      if (!password.equals(user.getPassword())) {
         throw new BadCredentialsException("Invalid username or password");
      }

      List<GrantedAuthority> grantedAuths = new ArrayList<>();
      grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
      return new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
          UsernamePasswordAuthenticationToken.class);
    }
}
