package com.ecash.cmsapi.api;

import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.cmsapi.vo.AccountUpdateVO;
import com.ecash.ecashcore.model.cms.Account;
import com.ecash.ecashcore.service.AccountService;
import com.ecash.ecashcore.vo.AccountVO;
import com.querydsl.core.types.Predicate;

@RestController
public class AccountApi extends BaseApi {

  @Autowired
  private AccountService accountService;
  
  @Autowired
  public HttpSession httpSession;

  @GetMapping(value = "/accounts/search")
  @PreAuthorize(value = "hasPermission(null, 'ACCOUNT_LIST/VIEW')")
  public Iterable<Account> searchAll(@QuerydslPredicate(root = Account.class) Predicate predicate,
      Pageable pageable) {
    return accountService.findAll(predicate, pageable);
  }
  
  @PreAuthorize(value = "hasPermission(null, 'ACCOUNT_DETAILS/UPDATE')")
  @RequestMapping(value = "${api.url.account.update}", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> updateAccountStatus(@RequestBody AccountUpdateVO request) {
    Account account = new Account();
    account.setId(request.getId());
    account.setStatus(request.getStatus());
    accountService.updateAccountStatus(account, getCurrentUser());
    return ResponseEntity.ok(request);
  }
  
  @PreAuthorize(value = "hasPermission(null, 'ACCOUNT_DETAILS/UPDATE')")
  @RequestMapping(value = "${api.url.account.lockAccounts}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> lockAccounts(@RequestBody List<AccountVO> accounts, @QuerydslPredicate(root = Account.class) Predicate predicate,
      Pageable pageable) {
    accountService.lock(accounts, predicate, pageable);
    return ResponseEntity.ok(Status.OK.toString());
  }
  
  @PreAuthorize(value = "hasPermission(null, 'ACCOUNT_DETAILS/UPDATE')")
  @RequestMapping(value = "${api.url.account.unlockAccounts}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> unlockAccounts(@RequestBody List<AccountVO> accounts, @QuerydslPredicate(root = Account.class) Predicate predicate,
      Pageable pageable) {
    accountService.unlockAccounts(accounts, predicate, pageable);
    return ResponseEntity.ok(Status.OK.toString());
  }
}
