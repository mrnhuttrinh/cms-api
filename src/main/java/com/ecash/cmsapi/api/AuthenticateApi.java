package com.ecash.cmsapi.api;

import java.util.Date;

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

import com.ecash.cmsapi.redis.CacheService;
import com.ecash.cmsapi.redis.RedisService;
import com.ecash.cmsapi.security.JwtTokenUtil;
import com.ecash.cmsapi.vo.LoginVO;
import com.ecash.cmsapi.vo.ResponseBodyVO;
import com.ecash.ecashcore.model.cms.User;
import com.ecash.ecashcore.service.UserService;

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

  @Autowired
  public UserService userService;

  @RequestMapping(value = "/authenticate", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginVO vo, HttpServletRequest request,
      HttpServletResponse response) {
    String username = vo.getUsername();
    String password = vo.getPassword();
    String language = vo.getLanguage();

    try {
      String cacheToken = jwtTokenUtil.getTokenFromRedisCache(username);
      if (cacheToken != null && jwtTokenUtil.canTokenBeRefreshed(cacheToken)) {
        ResponseBodyVO error = new ResponseBodyVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "User was online.", null,
            null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
      }
    } catch (Exception e) {
      // TODO
    }

    // Perform the security
    final Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Reload password post-security so we can generate token
    final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    final String token = jwtTokenUtil.generateToken(userDetails);

    User user = userService.getByUsername(userDetails.getUsername());
    user.setLastLogin(new Date());
    userService.save(user);
    if (language != null && !language.equals("")) {
      userService.updateSetting(user, "language", language);
    }

    httpSession.setAttribute(tokenHeader, tokenPrefix + " " + token);
    jwtTokenUtil.setTokenToRedisCache(username, token);
    ResponseBodyVO data = new ResponseBodyVO(HttpStatus.OK.value(), "Login successfully.", null, user);
    return ResponseEntity.ok(data);
  }

  @RequestMapping(value = "/refresh-token", method = RequestMethod.GET)
  public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
    String requestHeader = (String) httpSession.getAttribute(this.tokenHeader);
    String authToken = requestHeader.substring(7);

    String username = jwtTokenUtil.getUsernameFromToken(authToken);
    User user = userService.getByUsername(username);

    if (jwtTokenUtil.canTokenBeRefreshed(authToken) && user != null) {
      user.setLastLogin(new Date());
      userService.save(user);
      String refreshedToken = jwtTokenUtil.refreshToken(authToken);
      httpSession.setAttribute(tokenHeader, tokenPrefix + " " + refreshedToken);
      jwtTokenUtil.setTokenToRedisCache(username, refreshedToken);
      ResponseBodyVO data = new ResponseBodyVO(HttpStatus.OK.value(), "Refresh token successfully.", null, user);
      return ResponseEntity.ok(data);
    } else {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error on refesh token.",
          null, null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
  }

  @RequestMapping(value = "/sign-out", method = RequestMethod.POST)
  public ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response) {
    String requestHeader = (String) httpSession.getAttribute(this.tokenHeader);
    String authToken = requestHeader.substring(7);

    if (jwtTokenUtil.canTokenBeRefreshed(authToken)) {
      String username = jwtTokenUtil.getUsernameFromToken(authToken);
      httpSession.removeAttribute(tokenHeader);
      jwtTokenUtil.deleteTokenToRedisCache(username);
      ResponseBodyVO data = new ResponseBodyVO(HttpStatus.OK.value(), "Sign out successfully.", null, null);
      return ResponseEntity.ok(data);
    } else {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error on signout.", null,
          null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
  }
}
