package com.ecash.cmsapi.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.cmsapi.vo.ResponseBodyVO;
import com.ecash.ecashcore.model.cms.Customer;
import com.ecash.ecashcore.pojo.NewCustomerPOJO;
import com.ecash.ecashcore.pojo.UpdateCustomerPOJO;
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

  @PreAuthorize(value = "hasPermission(null, 'CUSTOMER_DETAILS/UPDATE')")
  @RequestMapping(value = "${api.url.customers.lockCustomers}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> lockCustomers(@RequestBody List<Customer> customers) {
    customerService.lockCustomers(customers);
    return ResponseEntity.ok(customers);
  }
  
  @PreAuthorize(value = "hasPermission(null, 'CUSTOMER_DETAILS/UPDATE')")
  @RequestMapping(value = "${api.url.customers.unlockCustomers}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> unlockCustomers(@RequestBody List<Customer> customers) {
    customerService.unlockCustomers(customers);
    return ResponseEntity.ok(customers);
  }

  @PreAuthorize(value = "hasPermission(null, 'CUSTOMER_DETAILS/CREATE')")
  @RequestMapping(value = "${api.url.customers.create}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> addNewCustomer(@RequestBody NewCustomerPOJO customer) {
    try {
      Customer result = customerService.addNewCustomer(customer, this.getCurrentUser());
      return new ResponseEntity<Customer>(result, HttpStatus.OK);
    } catch (Exception ex) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null, null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
  }

  @PreAuthorize(value = "hasPermission(null, 'CUSTOMER_DETAILS/UPDATE')")
  @RequestMapping(value = "${api.url.customers.update}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> updateCustomer(@RequestBody UpdateCustomerPOJO customer) {
    try {
      Customer result = customerService.updateCustomer(customer, this.getCurrentUser());
      return new ResponseEntity<Customer>(result, HttpStatus.OK);
    } catch (Exception ex) {
      ResponseBodyVO error = new ResponseBodyVO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null, null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
  }
}
