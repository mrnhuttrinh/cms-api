package com.ecash.cmsapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecash.cmsapi.exception.CmsException;
import com.ecash.cmsapi.util.httpclient.HttpClientUtils;
import com.ecash.cmsapi.util.httpclient.ResponseData;
import com.ecash.ecashcore.util.JsonUtils;
import com.ecash.ecashcore.vo.request.DepositRequestVO;

@Service
@Transactional
public class CMSTransactionService {

  @Value("${ecash.url.deposit}")
  private String depositUrl;

  @Value("${ecash.header.authentication}")
  private String authentication;

  public ResponseData deposit(DepositRequestVO request) {
    ResponseData responseData = HttpClientUtils.sendPostJsontoEcash(depositUrl, JsonUtils.objectToJsonString(request),
        null, authentication);
    if (responseData != null) {
      if (responseData.getResponseBody() != null) {
        return responseData;
      } else {
        throw new CmsException(responseData.getResponseMessage());
      }
    } else {
      throw new CmsException("Error when calling ecash.");
    }
  }
}
