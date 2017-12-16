package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.cms.Transaction;
import com.ecash.ecashcore.service.TransactionService;
import com.querydsl.core.types.Predicate;

@RestController
public class TransactionApi extends BaseApi {
  
  @Autowired
  TransactionService transactionService;
  
  @GetMapping(value = "/transactions/search")
  @PreAuthorize(value = "hasPermission(null, 'FULL_CONTROL')")
  public Iterable<Transaction> searchAll(@QuerydslPredicate(root = Transaction.class) Predicate predicate,
      Pageable pageable) {
    return transactionService.findAll(predicate, pageable);
  }
}
