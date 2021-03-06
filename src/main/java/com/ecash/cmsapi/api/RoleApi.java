package com.ecash.cmsapi.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.cmsapi.vo.ResponseBodyVO;
import com.ecash.ecashcore.model.cms.Permission;
import com.ecash.ecashcore.model.cms.Role;
import com.ecash.ecashcore.service.RoleService;
import com.querydsl.core.types.Predicate;

@RestController
public class RoleApi extends BaseApi {

  @Autowired
  RoleService roleService;

  @GetMapping(value = "/roles/search")
  @PreAuthorize(value = "hasPermission(null, 'ROLE_LIST/VIEW')")
  public Iterable<Role> searchAll(@QuerydslPredicate(root = Role.class) Predicate predicate, Pageable pageable) {
    return roleService.findAll(predicate, pageable);
  }

  @PreAuthorize(value = "hasPermission(null, 'ROLE_DETAIL/ADD_PERMISSION')")
  @RequestMapping(value = "/roles/update-permission/{id}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> updateRolePermission(@PathVariable("id") String id,
      @RequestBody List<Permission> permissions) {
    Role role = roleService.findById(id);
    if (role == null) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.NOT_FOUND.value(), "Role not found.", null, null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    role = roleService.updateRolePermission(role, permissions);
    return ResponseEntity.ok(role);
  }

  @PreAuthorize(value = "hasPermission(null, 'ROLE_DETAIL/CREATE')")
  @RequestMapping(value = "/role", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> addNewRole(@RequestBody Map<String, Object> body) {
    try {
      String roleName = body.get("roleName").toString();
      Role newRole = roleService.addNewRole(roleName);
      return new ResponseEntity<Role>(newRole, HttpStatus.OK);
    } catch (Exception ex) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null, null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
  }
}
