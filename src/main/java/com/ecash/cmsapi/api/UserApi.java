package com.ecash.cmsapi.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.cmsapi.vo.ResponseBodyVO;
import com.ecash.ecashcore.model.User;
import com.ecash.ecashcore.service.UserService;
import com.querydsl.core.types.Predicate;

@RestController
public class UserApi extends BaseApi {
  
  @Autowired
  private AuthenticationManager authenticationManager;
  private UserService userService;

  @GetMapping(value = "/users/search")
  @PreAuthorize(value = "hasPermission(null, 'FULL_CONTROL')")
  public Iterable<User> searchAll(@QuerydslPredicate(root = User.class) Predicate predicate,
      Pageable pageable) {
    return userService.findAll(predicate, pageable);
  }
  
  @RequestMapping(value = "/users/change-password", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
    String id = body.get("id");
    String oldPassword = body.get("oldPassword");
    String newPassword = body.get("newPassword");
    String confirmNewPassword = body.get("confirmNewPassword");
    
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    User user = userService.getById(id);
    if (user == null) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.NOT_FOUND.value(), "User not found.", null, null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    final Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), oldPassword));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    if (!newPassword.equals(confirmNewPassword)) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Change password error.", null, null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    user.setPassword(newPassword);
    user.encodePassword(passwordEncoder);
    userService.save(user);
    return ResponseEntity.ok(user); 
  }
  
  @RequestMapping(value = "/users/reset-password", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
    String id = body.get("id");
    String newPassword = body.get("newPassword");
    String confirmNewPassword = body.get("confirmNewPassword");
    
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    User user = userService.getById(id);
    if (user == null) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.NOT_FOUND.value(), "User not found.", null, null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    if (!newPassword.equals(confirmNewPassword)) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Change password error.", null, null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    user.setPassword(newPassword);
    user.encodePassword(passwordEncoder);
    userService.save(user);
    return ResponseEntity.ok(user); 
  }
}
