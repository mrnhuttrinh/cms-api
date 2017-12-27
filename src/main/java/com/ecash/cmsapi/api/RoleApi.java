package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.cms.Role;
import com.ecash.ecashcore.service.RoleService;
import com.querydsl.core.types.Predicate;

@RestController
public class RoleApi extends BaseApi {

  @Autowired
  RoleService roleService;
  
  @GetMapping(value = "/roles/search")
  @PreAuthorize(value = "hasPermission(null, 'FULL_CONTROL')")
  public Iterable<Role> searchAll(@QuerydslPredicate(root = Role.class) Predicate predicate,
      Pageable pageable) {
    return roleService.findAll(predicate, pageable);
  }
}
