package com.ecash.cmsapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ecash.ecashcore.model.Card;
import com.ecash.ecashcore.service.CardService;
import com.ecash.ecashcore.vo.request.UpdateCardStatusRequestVO;

@RestController
public class CardAPI extends BaseApi {

  @Autowired
  private CardService cardService;

  @RequestMapping(value = "${api.url.card.updateStatus}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<Card> updateCardStatus(@RequestBody UpdateCardStatusRequestVO request) {
    return ResponseEntity.ok(cardService.updateCardStatus(request));
  }
}
