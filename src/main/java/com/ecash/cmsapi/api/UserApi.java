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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.cmsapi.vo.ResponseBodyVO;
import com.ecash.ecashcore.model.cms.User;
import com.ecash.ecashcore.service.UserService;
import com.querydsl.core.types.Predicate;

@RestController
public class UserApi extends BaseApi {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserService userService;

  @GetMapping(value = "/users/search")
  @PreAuthorize(value = "hasPermission(null, 'USER_LIST/VIEW')")
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

    User user  = userService.getById(id);
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
    userService.changePassword(user, newPassword);
    return ResponseEntity.ok(user); 
  }

  @RequestMapping(value = "/users/update-information", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> updateUserInformation(@RequestBody User user) {
    if (user == null) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.NOT_FOUND.value(), "User not found.", null, null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    String currentUsername = this.getCurrentUser();
    userService.updateInformation(user, currentUsername);
    return ResponseEntity.ok(user); 
  }
  
  @RequestMapping(value = "/users/reset-password", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
    String id = body.get("id");
    String newPassword = body.get("newPassword");
    String confirmNewPassword = body.get("confirmNewPassword");

    User user = userService.getById(id);
    if (user == null) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.NOT_FOUND.value(), "User not found.", null, null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    if (!newPassword.equals(confirmNewPassword)) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Change password error.", null, null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    String currentUsername = this.getCurrentUser();
    userService.resetPassword(user, currentUsername, newPassword);
    return ResponseEntity.ok(user); 
  }

  @RequestMapping(value = "/users/update-status", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> updateStatus(@RequestBody Map<String, String> body) {
    String id = body.get("id");
    String status = body.get("status");

    User user = userService.getById(id);
    if (user == null) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.NOT_FOUND.value(), "User not found.", null, null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    String currentUsername = this.getCurrentUser();
    userService.updateStatus(user, status, currentUsername);
    return ResponseEntity.ok(user); 
  }
  
  @RequestMapping(value = "/users/update-setting", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> updateSetting(@RequestBody Map<String, String> body) {
    String id = body.get("id");
    String key = body.get("key");
    String value = body.get("value");
    User user = userService.getById(id);
    if (user == null) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.NOT_FOUND.value(), "User not found.", null, null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    userService.updateSetting(user, key, value);
    return ResponseEntity.ok(user); 
  }
  
}
