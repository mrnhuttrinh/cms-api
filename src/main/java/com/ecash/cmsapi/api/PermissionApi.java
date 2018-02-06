package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.cms.Permission;
import com.ecash.ecashcore.service.PermissionService;
import com.querydsl.core.types.Predicate;

@RestController
public class PermissionApi extends BaseApi {

  @Autowired PermissionService permissionService;
  
  @GetMapping(value = "/permissions/search")
  @PreAuthorize(value = "hasPermission(null, 'PERMISSION_LIST/VIEW')")
  public Iterable<Permission> searchAll(@QuerydslPredicate(root = Permission.class) Predicate predicate,
      Pageable pageable) {
    return permissionService.findAll(predicate, pageable);
  }
}
