package com.ecash.cmsapi.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.ecash.ecashcore.model.Permission;

public class EcashPermissionEvaluator implements PermissionEvaluator {

  @Override
  public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
    // if ((auth == null) || (targetDomainObject == null) || !(permission instanceof
    // String)) {
    // return false;
    // }

    if ((auth == null) || !(permission instanceof String)) {
      return false;
    }

    String targetType = targetDomainObject != null ? targetDomainObject.getClass().getSimpleName().toUpperCase() : null;

    if (targetType != null) {
      return hasPrivilege(auth, targetType, permission.toString().toUpperCase());
    } else {
      return hasPrivilege(auth, permission.toString().toUpperCase());
    }
  }

  @Override
  public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
    // if ((auth == null) || (targetType == null) || !(permission instanceof
    // String)) {
    // return false;
    // }

    if ((auth == null) || !(permission instanceof String)) {
      return false;
    }

    targetType = targetType != null ? targetType.toUpperCase() : null;

    if (targetType != null) {
      return hasPrivilege(auth, targetType.toUpperCase(), permission.toString().toUpperCase());
    } else {
      return hasPrivilege(auth, permission.toString().toUpperCase());
    }
  }

  private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
    for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
      if (grantedAuth.getAuthority().startsWith(targetType)) {
        if (grantedAuth.getAuthority().contains(permission)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean hasPrivilege(Authentication auth, String permission) {
    for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
      EcashGrantedAuthority authority = (EcashGrantedAuthority) grantedAuth;
      for (Permission grantedPermission : authority.getRole().getPermissions()) {
        if (grantedPermission.getName().contains(permission)) {
          return true;
        }
      }
    }
    return false;
  }
}
