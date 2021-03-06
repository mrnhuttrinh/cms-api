package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.cms.Merchant;
import com.ecash.ecashcore.service.MerchantService;
import com.querydsl.core.types.Predicate;

@RestController
public class MerchantApi extends BaseApi {

  @Autowired
  MerchantService merchantService;
  
  @GetMapping(value = "/merchants/search")
  @PreAuthorize(value = "hasPermission(null, 'MERCHANT_LIST/VIEW')")
  public Iterable<Merchant> searchAll(@QuerydslPredicate(root = Merchant.class) Predicate predicate,
      Pageable pageable) {
    return merchantService.findAll(predicate, pageable);
  }
}
