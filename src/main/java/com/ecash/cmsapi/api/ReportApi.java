package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.service.ReportService;
import com.ecash.ecashcore.vo.ReportVO;

@RestController
public class ReportApi extends BaseApi
{
  @Autowired
  private ReportService reportService;

  @GetMapping(value = "${api.url.report.general}")
  @PreAuthorize(value = "hasPermission(null, 'REPORT/VIEW')")
  public ResponseEntity<?> getGeneralReport(
      @RequestParam(name = "fromDate", required = true, defaultValue = "") String fromDate,
      @RequestParam(name = "toDate", required = true, defaultValue = "") String toDate)
  {
    ReportVO result = reportService.getReportGeneral(fromDate, toDate);
    return ResponseEntity.ok(result);
  }
}
