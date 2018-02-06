package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.cmsapi.service.CMSTransactionService;
import com.ecash.ecashcore.model.cms.Transaction;
import com.ecash.ecashcore.service.TransactionService;
import com.ecash.ecashcore.vo.request.DepositRequestVO;
import com.querydsl.core.types.Predicate;

@RestController
public class TransactionApi extends BaseApi {

  @Autowired
  TransactionService transactionService;

  @Autowired
  CMSTransactionService cmsTransactionService;

  @GetMapping(value = "/transactions/search")
  @PreAuthorize(value = "hasPermission(null, 'TRANSACTION_LIST/VIEW')")
  public Iterable<Transaction> searchAll(@QuerydslPredicate(root = Transaction.class) Predicate predicate,
      Pageable pageable) {
    return transactionService.findAll(predicate, pageable);
  }

  @PostMapping(value = "${api.url.deposit}", produces = "application/json; charset=UTF-8")
  public ResponseEntity<String> deposit(@RequestBody DepositRequestVO request) {

    String transaction = cmsTransactionService.deposit(request);
    return ResponseEntity.ok(transaction);
  }
}
