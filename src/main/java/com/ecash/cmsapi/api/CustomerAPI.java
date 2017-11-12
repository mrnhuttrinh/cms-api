package com.ecash.cmsapi.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.Customer;
import com.ecash.ecashcore.service.AccountHistoryService;
import com.ecash.ecashcore.service.CustomerService;
import com.ecash.ecashcore.vo.AccountHistoryVO;
import com.ecash.ecashcore.vo.AccountVO;
import com.ecash.ecashcore.vo.CustomerVO;

@RestController
public class CustomerAPI extends BaseApi {

  @Autowired
  CustomerService customerService;

  @Autowired
  AccountHistoryService accountHistoryService;

  @RequestMapping(value = "${api.url.customer}/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> getCustomerById(@PathVariable("id") String id) {
    Customer customer = customerService.getById(id);
    if (customer != null) {
      CustomerVO result = new CustomerVO(customer);
      return new ResponseEntity<CustomerVO>(result, HttpStatus.OK);
    }
    return new ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND);
  }

  @RequestMapping(value = "${api.url.customer}/{id}/accounts", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> getAccountByCustomerById(
      @PathVariable("id") String id) {
    List<AccountVO> acounts = customerService.getAccountsByCustomerId(id);
    return new ResponseEntity<List<AccountVO>>(acounts, HttpStatus.OK);
  }

  @RequestMapping(value = "${api.url.customer}/{id}/history", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> getAccountHistoryByAccountId(
      @PathVariable("id") String id) {
    List<AccountHistoryVO> result = customerService
        .getAccountHistoryByCustomerId(id);
    return new ResponseEntity<List<AccountHistoryVO>>(result, HttpStatus.OK);
  }

  @RequestMapping(value = "${api.url.customer}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> getCustomerList(Pageable pageable) {
    Page<CustomerVO> result = customerService.getCustomers(pageable);
    return new ResponseEntity<Page<CustomerVO>>(result, HttpStatus.OK);
  }

}
