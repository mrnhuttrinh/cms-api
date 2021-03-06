package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.exception.DataNotFoundException;
import com.ecash.ecashcore.model.cms.Wallet;
import com.ecash.ecashcore.service.WalletService;
import com.ecash.ecashcore.vo.WalletVO;
import com.querydsl.core.types.Predicate;

@RestController
public class WalletAPI extends BaseApi {
  
  @Autowired
  WalletService walletService;
  
  @PostMapping(value = "/createwallet", produces = "application/json; charset=UTF-8")
  @PreAuthorize(value = "hasPermission(null, 'WALLET_DETAIL/CREATE')")
  public WalletVO createWallet(@RequestBody WalletVO data) {
    Wallet wallet = new Wallet();
    wallet.setCard(data.getCard());
    wallet = walletService.createWallet(wallet);
    return new WalletVO(wallet);
  }
  
  @DeleteMapping(value = "/deletewallet/{id}", produces = "application/json; charset=UTF-8")
  @PreAuthorize(value = "hasPermission(null, 'WALLET_DETAIL/DISCONNECT')")
  public WalletVO disconnectWallet(@PathVariable String id) {
    Wallet wallet = walletService.disconnectWallet(id);
    if (wallet == null) {
      throw new DataNotFoundException("Not found Wallet");
    }
    return new WalletVO(wallet);
  }

}
