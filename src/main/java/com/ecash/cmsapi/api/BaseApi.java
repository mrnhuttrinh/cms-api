package com.ecash.cmsapi.api;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.cmsapi.api.constant.ResponseConstant;
import com.ecash.cmsapi.exception.BadRequestException;
import com.ecash.cmsapi.exception.NotAuthenticatedException;
import com.ecash.cmsapi.security.JwtTokenUtil;
import com.ecash.cmsapi.vo.ResponseBodyVO;
import com.ecash.ecashcore.exception.DataNotFoundException;
import com.ecash.ecashcore.exception.InvalidInputException;
import com.ecash.ecashcore.exception.TransactionException;

@RestController
@RequestMapping(value = "${api.url.rootPath}")
public class BaseApi {
  protected static final Logger LOGGER = LoggerFactory.getLogger(BaseApi.class);
  @Value("${jwt.header}")
  private String tokenHeader;

  @Value("${jwt.token_prefix}")
  private String tokenPrefix;


  @Autowired
  private JwtTokenUtil jwtTokenUtil;


  @Autowired
  public HttpSession httpSession;

  @ExceptionHandler(DataNotFoundException.class)
  public ResponseEntity<?> dataNotFoundExceptionHandler(Exception ex) {
    LOGGER.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ResponseBodyVO(HttpStatus.NOT_FOUND.value(), ResponseConstant.ERROR, ex.getMessage(), null));
  }

  @ExceptionHandler(TransactionException.class)
  public ResponseEntity<?> transactionExceptionHandler(Exception ex) {
    LOGGER.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ResponseBodyVO(HttpStatus.BAD_REQUEST.value(), ResponseConstant.ERROR, ex.getMessage(), null));
  }

  @ExceptionHandler(InvalidInputException.class)
  public ResponseEntity<?> invalidInputExceptionHandler(Exception ex) {
    return badRequestExceptionHandler(ex);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<?> badRequestExceptionHandler(Exception ex) {
    LOGGER.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ResponseBodyVO(HttpStatus.BAD_REQUEST.value(), ResponseConstant.ERROR, ex.getMessage(), null));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> internalServerErrorExceptionHandler(Exception ex) {
    LOGGER.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new ResponseBodyVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseConstant.ERROR, ex.getMessage(), null));
  }

  @ExceptionHandler(NotAuthenticatedException.class)
  public ResponseEntity<?> notAuthenticatedExceptionHandler(Exception ex) {
    LOGGER.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
        new ResponseBodyVO(HttpStatus.FORBIDDEN.value(), ResponseConstant.AUTHENTICATION_ERROR, ex.getMessage(), null));
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<?> usernameNotFoundExceptionExceptionHandler(Exception ex) {
    LOGGER.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ResponseBodyVO(HttpStatus.NOT_FOUND.value(), ResponseConstant.ERROR, ex.getMessage(), null));
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<?> authenticationExceptionHandler(Exception ex) {
    LOGGER.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ResponseBodyVO(HttpStatus.UNAUTHORIZED.value(), ResponseConstant.ERROR, ex.getMessage(), null));
  }
  
  protected String getCurrentUser() {
    String requestHeader = (String) httpSession.getAttribute(this.tokenHeader);
    String authToken = requestHeader.substring(7);
    
    String username = jwtTokenUtil.getUsernameFromToken(authToken);
    return username;
  }
}
