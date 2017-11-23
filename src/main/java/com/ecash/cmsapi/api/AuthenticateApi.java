package com.ecash.cmsapi.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.cmsapi.security.JwtTokenUtil;

@RestController
public class AuthenticateApi extends BaseApi {

  @Value("${jwt.header}")
  private String tokenHeader;

  @Value("${jwt.token_prefix}")
  private String tokenPrefix;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  public HttpSession httpSession;

  @RequestMapping(value = "/authenticate", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> authenticateUser(@RequestBody Map<String, Object> body,HttpServletRequest request, HttpServletResponse response) {
    try {
      String username = body.get("username").toString();
      String password = body.get("password").toString();
  
      // Perform the security
      UsernamePasswordAuthenticationToken newUserPassword = new UsernamePasswordAuthenticationToken(username, password);
      final Authentication authentication = authenticationManager.authenticate(newUserPassword);
      SecurityContextHolder.getContext().setAuthentication(authentication);
  
      // Reload password post-security so we can generate token
      final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      final String token = jwtTokenUtil.generateToken(userDetails);
  
      httpSession.setAttribute(tokenHeader, tokenPrefix + " " + token);
      return ResponseEntity.ok(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @RequestMapping(value = "/refresh-token", method = RequestMethod.GET)
  public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
    String requestHeader = (String) httpSession.getAttribute(this.tokenHeader);
    String authToken = requestHeader.substring(7);

    if (jwtTokenUtil.canTokenBeRefreshed(authToken)) {
      String refreshedToken = jwtTokenUtil.refreshToken(authToken);
      httpSession.setAttribute(tokenHeader, tokenPrefix + " " + refreshedToken);
      return ResponseEntity.ok(HttpStatus.OK);
    } else {
      return ResponseEntity.badRequest().body(null);
    }
  }
  
  @RequestMapping(value = "/sign-out", method = RequestMethod.POST)
  public ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response) {
    String requestHeader = (String) httpSession.getAttribute(this.tokenHeader);
    String authToken = requestHeader.substring(7);

    if (jwtTokenUtil.canTokenBeRefreshed(authToken)) {
      httpSession.removeAttribute(tokenHeader);
      return ResponseEntity.ok(HttpStatus.OK);
    } else {
      return ResponseEntity.badRequest().body(null);
    }
  }
}
