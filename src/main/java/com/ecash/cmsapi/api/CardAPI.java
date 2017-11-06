package com.ecash.cmsapi.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.service.CardService;
import com.ecash.ecashcore.vo.InputCardVO;

@RestController
public class CardAPI extends BaseApi {

  @Autowired
  private CardService cardService;

  @RequestMapping(value = "${api.url.scms.syncdata}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<?> addCard(@RequestBody List<InputCardVO> inputCards) {
    cardService.saveCardInput(inputCards);
    return ResponseEntity.ok("{\"status\": \"success\"}");
  }

}
