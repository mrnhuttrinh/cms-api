package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.Customer;
import com.ecash.ecashcore.repository.CustomerRepository;
import com.querydsl.core.types.Predicate;

@RestController
public class CustomerApi {

  @Autowired
  private CustomerRepository customerRepository;

  @GetMapping(value = "/customers/search")
  @PreAuthorize(value = "hasPermission(authentication, null, 'FULL_CONTROL')")
  public Iterable<Customer> searchAll(@QuerydslPredicate(root = Customer.class) Predicate predicate,
      Pageable pageable) {
    return customerRepository.findAll(predicate, pageable);
  }
}
