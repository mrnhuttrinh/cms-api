package com.ecash.cmsapi.util.httpclient;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecash.cmsapi.exception.CmsException;

public class HttpClientUtils {

  protected static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

  private static final String APPLICATION_JSON = "application/json";

  private static ResponseData sendRequest(HttpRequestBase request) {
    ResponseData responseData = null;
    try {
      HttpClient httpclient = HttpClientBuilder.create().build();

      HttpResponse response = httpclient.execute(request);

      responseData = new ResponseData();
      responseData.setResponseCode(response.getStatusLine().getStatusCode());
      responseData.setResponseMessage(response.getStatusLine().getReasonPhrase());
      responseData.setResponseBody(IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8));
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }

    return responseData;
  }

  public static ResponseData sendPost(String url, String body, Map<String, String> headers) {
    HttpPost request = new HttpPost();

    try {
      if (url == null) {
        throw new CmsException("url must not be null.");
      }
      request.setURI(new URI(url));

      if (body != null) {
        request.setEntity(new StringEntity(body));
      }

      if (headers != null) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
          request.addHeader(header.getKey(), header.getValue());
        }
      }
    } catch (Exception e) {
      throw new CmsException(e.getMessage(), e);
    }

    return sendRequest(request);
  }

  public static ResponseData sendPostJson(String url, String body, Map<String, String> headers) {
    if (headers == null) {
      headers = new HashMap<>();
    }

    headers.put("Content-type", APPLICATION_JSON);
    headers.put("Content-Type", APPLICATION_JSON);
    headers.put("Accept", APPLICATION_JSON);

    return sendPost(url, body, headers);
  }

  public static ResponseData sendPostJsontoEcash(String url, String body, Map<String, String> headers,
      String authentication) {
    if (headers == null) {
      headers = new HashMap<>();
    }

    headers.put("Authorization", authentication);

    return sendPostJson(url, body, headers);
  }
}
