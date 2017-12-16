package com.ecash.cmsapi.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecash.ecashcore.model.cms.Role;
import com.ecash.ecashcore.model.cms.User;
import com.ecash.ecashcore.repository.UserRepository;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      // try find user by email
      user = userRepository.findByEmail(username);
    }
    List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());

    return buildUserForAuthentication(user, authorities);
  }

  private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
    return new EcashUser(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
  }

  private List<GrantedAuthority> buildUserAuthority(List<Role> userRoles) {
    Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

    // Build user's authorities
    for (Role userRole : userRoles) {
      setAuths.add(new EcashGrantedAuthority(userRole));
    }

    List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);
    return result;
  }
}
