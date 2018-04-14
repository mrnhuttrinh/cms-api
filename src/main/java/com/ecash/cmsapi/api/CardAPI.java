package com.ecash.cmsapi.api;

import java.util.List;

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

import com.ecash.ecashcore.model.cms.Card;
import com.ecash.ecashcore.service.CardService;
import com.ecash.ecashcore.vo.request.UpdateCardStatusRequestVO;
import com.querydsl.core.types.Predicate;

@RestController
public class CardAPI extends BaseApi {

  @Autowired
  private CardService cardService;

  @PreAuthorize(value = "hasPermission(null, 'CARD_DETAIL/UPDATE')")
  @RequestMapping(value = "${api.url.card.updateStatus}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
  public ResponseEntity<Card> updateCardStatus(@RequestBody UpdateCardStatusRequestVO request) {
    return ResponseEntity.ok(cardService.updateCardStatus(request));
  }
  
  @GetMapping(value = "/cards/search")
  @PreAuthorize(value = "hasPermission(null, 'CARD_LIST/VIEW')")
  public Iterable<Card> searchAll(@QuerydslPredicate(root = Card.class) Predicate predicate,
      Pageable pageable) {
    return cardService.findAll(predicate, pageable);
  }
  
  @GetMapping(value = "/cards/exampleQueryDsl")
  public List<Card> exampleQueryDsl() {
    return cardService.exampleQueryDsl();
  }
}
