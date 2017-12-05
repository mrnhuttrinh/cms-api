package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.User;
import com.ecash.ecashcore.service.UserService;
import com.querydsl.core.types.Predicate;

@RestController
public class UserApi extends BaseApi {

  @Autowired
  private UserService userService;

  @GetMapping(value = "/users/search")
  @PreAuthorize(value = "hasPermission(null, 'FULL_CONTROL')")
  public Iterable<User> searchAll(@QuerydslPredicate(root = User.class) Predicate predicate,
      Pageable pageable) {
    return userService.findAll(predicate, pageable);
  }
}
