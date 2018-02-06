package com.ecash.cmsapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecash.cmsapi.util.httpclient.HttpClientUtils;
import com.ecash.cmsapi.util.httpclient.ResponseData;
import com.ecash.ecashcore.exception.EcashException;
import com.ecash.ecashcore.util.JsonUtils;
import com.ecash.ecashcore.vo.request.DepositRequestVO;

@Service
@Transactional
public class CMSTransactionService {

  @Value("${ecash.url.deposit}")
  private String depositUrl;

  @Value("${ecash.header.authentication}")
  private String authentication;

  public String deposit(DepositRequestVO request) {
    ResponseData responseData = HttpClientUtils.sendPostJsontoEcash(depositUrl, JsonUtils.objectToJsonString(request),
        null, authentication);
    if (responseData != null) {
      if (responseData.getResponseBody() != null) {
        return responseData.getResponseBody();
      } else {
        throw new EcashException(responseData.getResponseMessage());
      }
    } else {
      throw new EcashException("Error when calling ecash.");
    }
  }
}
