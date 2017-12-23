package com.ecash.cmsapi.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.cmsapi.api.constant.ResponseConstant;
import com.ecash.cmsapi.vo.ResponseBodyVO;
import com.ecash.ecashcore.service.SyncService;
import com.ecash.ecashcore.vo.SyncVO;

@RestController
public class SyncApi extends BaseApi {

  @Autowired
  SyncService syncService;

  @RequestMapping(value = "${api.url.scms.syncdata}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> addCard(@RequestBody List<SyncVO> inputCards) {
    syncService.sync(inputCards);
    return ResponseEntity
        .ok(new ResponseBodyVO(HttpStatus.OK.value(), ResponseConstant.SUCCESS, "Save successfully!", null));
  }
}
