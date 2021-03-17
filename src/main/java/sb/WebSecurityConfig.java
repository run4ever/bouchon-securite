package sb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import sb.services.SBAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .headers()
                .xssProtection().xssProtectionEnabled(false);
        http
            .authorizeRequests()
                .anyRequest().permitAll()
                .and()
            .formLogin()
               //.loginProcessingUrl("/perform_login")
                .loginPage("/login").permitAll()
                .and()
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
            ;
        http.csrf().ignoringAntMatchers("/admin/*", "/data/*");
    }

    @Autowired
    private SBAuthenticationProvider authProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.authenticationProvider(authProvider);
    }
}
