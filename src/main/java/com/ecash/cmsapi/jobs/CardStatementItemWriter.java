package com.ecash.cmsapi.jobs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.ecash.ecashcore.service.CardStatementService;
import com.ecash.ecashcore.vo.CardStatementVO;

public class CardStatementItemWriter implements ItemWriter<CardStatementVO>
{
  private static final Logger LOGGER = LoggerFactory.getLogger(CardStatementItemWriter.class);

  @Autowired
  private CardStatementService cardStatementService;

  public CardStatementItemWriter()
  {
    super();
  }

  @Override
  public void write(List<? extends CardStatementVO> items) throws Exception
  {
    CardStatementVO cardStatementSavedItem;
    for (final CardStatementVO cardStatement : items)
    {
      cardStatementSavedItem = cardStatementService
          .upsertCardStatement(cardStatement);
      LOGGER.info("Save CardStatement: %s", cardStatementSavedItem);
    }
  }

}
