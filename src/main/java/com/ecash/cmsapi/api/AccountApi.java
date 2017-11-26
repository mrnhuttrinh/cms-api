package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.Account;
import com.ecash.ecashcore.repository.AccountRepository;
import com.querydsl.core.types.Predicate;

@RestController
public class AccountApi extends BaseApi {

  @Autowired
  private AccountRepository accountRepository;

  @GetMapping(value = "/accounts/search")
  public Iterable<Account> searchAll(@QuerydslPredicate(root = Account.class) Predicate predicate,
      Pageable pageable) {
    return accountRepository.findAll(predicate, pageable);
  }
}
