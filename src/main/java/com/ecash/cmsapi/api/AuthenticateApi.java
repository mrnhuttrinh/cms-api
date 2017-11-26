package com.ecash.cmsapi.api;

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
import com.ecash.cmsapi.vo.LoginVO;
import com.ecash.cmsapi.vo.ResponseBodyVO;

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
  public ResponseEntity<?> authenticateUser(@RequestBody LoginVO vo, HttpServletRequest request,
      HttpServletResponse response) {
    String username = vo.getUsername();
    String password = vo.getPassword();

    // Perform the security
    final Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Reload password post-security so we can generate token
    final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    final String token = jwtTokenUtil.generateToken(userDetails);

    httpSession.setAttribute(tokenHeader, tokenPrefix + " " + token);
    ResponseBodyVO data = new ResponseBodyVO(HttpStatus.OK.value(), "Login successfully.", null, null);
    return ResponseEntity.ok(data);
  }

  @RequestMapping(value = "/refresh-token", method = RequestMethod.GET)
  public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
    String requestHeader = (String) httpSession.getAttribute(this.tokenHeader);
    String authToken = requestHeader.substring(7);

    if (jwtTokenUtil.canTokenBeRefreshed(authToken)) {
      String refreshedToken = jwtTokenUtil.refreshToken(authToken);
      httpSession.setAttribute(tokenHeader, tokenPrefix + " " + refreshedToken);
      ResponseBodyVO data = new ResponseBodyVO(HttpStatus.OK.value(), "Refresh token successfully.", null, null);
      return ResponseEntity.ok(data);
    } else {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error on refesh token.", null, null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
  }

  @RequestMapping(value = "/sign-out", method = RequestMethod.POST)
  public ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response) {
    String requestHeader = (String) httpSession.getAttribute(this.tokenHeader);
    String authToken = requestHeader.substring(7);

    if (jwtTokenUtil.canTokenBeRefreshed(authToken)) {
      httpSession.removeAttribute(tokenHeader);
      ResponseBodyVO data = new ResponseBodyVO(HttpStatus.OK.value(), "Sign out successfully.", null, null);
      return ResponseEntity.ok(data);
    } else {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error on signout.", null, null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
  }
}
