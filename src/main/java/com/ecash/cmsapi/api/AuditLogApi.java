package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.cms.AuditLog;
import com.ecash.ecashcore.service.AuditLogService;
import com.querydsl.core.types.Predicate;

@RestController
public class AuditLogApi extends BaseApi {
  
  @Autowired
  private AuditLogService auditLogService;

  @GetMapping(value = "/auditLogs/search")
  public Iterable<AuditLog> searchAll(@QuerydslPredicate(root = AuditLog.class) Predicate predicate,
      Pageable pageable) {
    return auditLogService.findAll(predicate, pageable);
  }
}
