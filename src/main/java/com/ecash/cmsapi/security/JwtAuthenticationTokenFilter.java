package com.ecash.cmsapi.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecash.ecashcore.model.cms.User;
import com.ecash.ecashcore.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

  private final Log logger = LogFactory.getLog(this.getClass());

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  
  @Autowired
  private UserService userService;

  @Value("${jwt.header}")
  private String tokenHeader;
  
  @Value("${jwt.token_prefix}")
  private String tokenPrefix;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    String requestHeader = null;

    if (session != null) {
      logger.debug("Getting auth from session");
      logger.debug("Session id: " + session.getId());
      requestHeader = (String) session.getAttribute(this.tokenHeader);
    }

    String username = null;
    String authToken = null;
    if (requestHeader != null && requestHeader.startsWith(tokenPrefix + " ")) {
      authToken = requestHeader.substring(7);
      try {
        username = jwtTokenUtil.getUsernameFromToken(authToken);
      } catch (IllegalArgumentException e) {
        logger.error("an error occured during getting username from token", e);
      } catch (ExpiredJwtException e) {
        logger.warn("the token is expired and not valid anymore", e);
      }
    } else {
      logger.warn("couldn't find bearer string, will ignore the header");
      SecurityContextHolder.getContext().setAuthentication(null);
    }

    logger.info("checking authentication for user " + username);
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      // It is not compelling necessary to load the use details from the database. You
      // could also store the information
      // in the token and read it from it. It's up to you ;)
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      // For simple validation it is completely sufficient to just check the token
      // integrity. You don't have to call
      // the database compellingly. Again it's up to you ;)
      if (jwtTokenUtil.validateToken(authToken, userDetails)) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        logger.info("authenticated user " + username + ", setting security context");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(tokenHeader, tokenPrefix + " " + authToken);
      }
    } else if (username != null && SecurityContextHolder.getContext().getAuthentication() != null) {
      String cacheToken = jwtTokenUtil.getTokenFromRedisCache(username);
      if (cacheToken == null || cacheToken == "") {
        SecurityContextHolder.getContext().setAuthentication(null);
        session.removeAttribute(tokenHeader);
      } else {
        String refreshedToken = jwtTokenUtil.refreshToken(authToken);
        session.setAttribute(tokenHeader, tokenPrefix + " " + refreshedToken);
        User user = userService.getByUsername(username);
        jwtTokenUtil.setTokenToRedisCache(user.getId(), refreshedToken);
      }
    }

    chain.doFilter(request, response);
  }
}