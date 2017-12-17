package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.cms.Wallet;
import com.ecash.ecashcore.service.WalletService;
import com.querydsl.core.types.Predicate;

@RestController
public class WalletAPI extends BaseApi {
  
  @Autowired
  WalletService walletService;
  @GetMapping(value = "/wallets/search")
  @PreAuthorize(value = "hasPermission(null, 'FULL_CONTROL')")
  public Iterable<Wallet> searchAll(
      @QuerydslPredicate(root = Wallet.class) Predicate predicate,
      Pageable pageable) {
    return walletService.findAll(predicate, pageable);
  }
}
