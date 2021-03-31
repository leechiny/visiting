package com.visiting.config;

import com.visiting.controller.VisitingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
    builder.inMemoryAuthentication()
      .withUser("admin")
      .password("{noop}password")
      .authorities("ROLE_ADMIN");
    // TODO: Password should be encoded

  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .authorizeRequests()
      .antMatchers(HttpMethod.OPTIONS,"/api/basicAuth/**").permitAll()
      .antMatchers("/api/basicAuth/**")
      .hasAnyRole("ADMIN", "USER")
      .and().httpBasic();

    http
      .csrf().disable()
      .authorizeRequests()
      .antMatchers(HttpMethod.OPTIONS,"/api/**").permitAll()
      .antMatchers("/api/create/**").permitAll()
      .antMatchers("/api/query/**").permitAll()
      .antMatchers("/api/report").hasAnyRole("ADMIN", "USER")
      .anyRequest().authenticated()
      .and()
      .addFilter( new JWTAuthorizationFilter(authenticationManager()));

  }

}
