package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.MerchantTerminal;
import com.ecash.ecashcore.repository.MerchantTerminalRepository;
import com.querydsl.core.types.Predicate;

@RestController
public class MerchantTerminalApi extends BaseApi {

  @Autowired
  private MerchantTerminalRepository merchantTerminalRepository;
  
  @GetMapping(value = "/merchantTerminals/search")
  @PreAuthorize(value = "hasPermission(null, 'FULL_CONTROL')")
  public Iterable<MerchantTerminal> searchAll(@QuerydslPredicate(root = MerchantTerminal.class) Predicate predicate,
      Pageable pageable) {
    return merchantTerminalRepository.findAll(predicate, pageable);
  }
}
