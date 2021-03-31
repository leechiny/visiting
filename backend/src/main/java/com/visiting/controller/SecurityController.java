package com.visiting.controller;

import com.visiting.service.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SecurityController {

  private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

  @Autowired
  JWTService jwtService;

  @RequestMapping(value = "/basicAuth/validate")
  public Map<String, String> userIsValid(HttpServletResponse response) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal();
    String role = authentication.getAuthorities().toArray()[0].toString().substring(5);

    String token = jwtService.generateToken(currentUser.getUsername(), role);
    Map<String, String> map = new HashMap<>();
    map.put("result", "ok");

    Cookie cookie = new Cookie("token", token);
    cookie.setPath("/api");
    cookie.setHttpOnly(true);
    // TODO: Uncomment when turn on ssl
    //  cookie.setSecure(true);
    cookie.setMaxAge(18000);
    response.addCookie(cookie);

    return map;
  }

  @GetMapping(value = "/currentUserRole")
  public Map<String, String> getCurrentUserRole() {
    Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    String role = "";

    if ( roles != null && roles.size() > 0) {
      GrantedAuthority ga = roles.iterator().next();
      role = ga.getAuthority().substring(5);
    }

    Map<String,String> results = new HashMap<>();
    results.put("role", role);
    return results;
  }

  @GetMapping(value = "/logout")
  public String logout(HttpServletResponse response) {
    Cookie cookie = new Cookie("token", null);
    cookie.setPath("/api");
    cookie.setHttpOnly(true);
    // TODO: Uncomment when turn on ssl
    //  cookie.setSecure(true);
    cookie.setMaxAge(0);
    response.addCookie(cookie);

    SecurityContextHolder.getContext().setAuthentication(null);
    return "";
  }

}
