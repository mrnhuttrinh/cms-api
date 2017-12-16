package com.ecash.cmsapi.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import com.ecash.ecashcore.model.cms.Role;

public class EcashGrantedAuthority implements GrantedAuthority {

  private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

  private final Role role;
  private final String roleName;
  private static final String ROLE_ = "ROLE_";

  public EcashGrantedAuthority(Role role) {
    this.role = role;
    this.roleName = ROLE_ + role.getName();
  }

  public String getAuthority() {
    return roleName;
  }

  public Role getRole() {
    return role;
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj instanceof EcashGrantedAuthority) {
      return this.roleName.equals(((EcashGrantedAuthority) obj).getRoleName());
    }

    return false;
  }

  public int hashCode() {
    return roleName.hashCode();
  }

  public String toString() {
    return roleName;
  }

  public String getRoleName() {
    return roleName;
  }
}
