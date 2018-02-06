package com.ecash.cmsapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecash.cmsapi.util.httpclient.HttpClientUtils;
import com.ecash.cmsapi.util.httpclient.ResponseData;
import com.ecash.ecashcore.util.JsonUtils;
import com.ecash.ecashcore.vo.request.DepositRequestVO;

@Service
@Transactional
public class CMSTransactionService {

  @Value("${ecash.url.deposit}")
  private String depositUrl;

  public String deposit(DepositRequestVO request) {
    ResponseData responseData = HttpClientUtils.sendPostJson(depositUrl, JsonUtils.objectToJsonString(request), null);
    return responseData.getResponseBody();
  }
}
