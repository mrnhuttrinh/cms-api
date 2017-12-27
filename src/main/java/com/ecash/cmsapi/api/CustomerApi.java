package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.cms.Customer;
import com.ecash.ecashcore.service.CustomerService;
import com.querydsl.core.types.Predicate;

@RestController
public class CustomerApi extends BaseApi {

  @Autowired
  private CustomerService customerService;

  @GetMapping(value = "/customers/search")
  @PreAuthorize(value = "hasPermission(null, 'CUSTOMER_LIST/VIEW')")
  public Iterable<Customer> searchAll(@QuerydslPredicate(root = Customer.class) Predicate predicate,
      Pageable pageable) {
    return customerService.findAll(predicate, pageable);
  }
}
