package com.visiting.config;



import com.visiting.service.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  private static final Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

  JWTService jwtService;

  public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    Cookie[] cookies = request.getCookies();

    if ( cookies == null || cookies.length == 0 ) {
      chain.doFilter(request, response);
      return;
    }

    Cookie tokenCookie = null;
    for( Cookie cookie : cookies) {
      if ( cookie.getName().equals("token")) {
        tokenCookie = cookie;
        break;
      }
    }

    if ( tokenCookie == null) {
      chain.doFilter(request, response);
      return;
    }

    if ( jwtService == null ) {
      ServletContext servletContext = request.getServletContext();
      WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
      jwtService = webApplicationContext.getBean(JWTService.class);
    }

    UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(tokenCookie.getValue());
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthenticationToken(String jwtToken) {

    try {
      String payload = jwtService.verifyToken(jwtToken);
      JsonParser parser = JsonParserFactory.getJsonParser();
      Map<String, Object> payloadMap = parser.parseMap(payload);
      String user = payloadMap.get("name").toString();
      String role = payloadMap.get("role").toString();

      List<GrantedAuthority> roles = new ArrayList<>();
      GrantedAuthority ga = new GrantedAuthority() {
        @Override
        public String getAuthority() {
          return "ROLE_" + role;
        }
      };
      roles.add(ga);

      return new UsernamePasswordAuthenticationToken(user, null, roles);
    }
    catch( Exception e) {
      return null;
    }
  }
}
