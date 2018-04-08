package com.ecash.cmsapi.jobs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.ecash.ecashcore.service.TransactionService;
import com.ecash.ecashcore.vo.TransactionVO;
import com.ecash.ecashcore.vo.response.TransactionResponseVO;

public class MerchantSettlementItemWriter implements ItemWriter<TransactionVO>
{
  private static final Logger LOGGER = LoggerFactory.getLogger(MerchantSettlementItemWriter.class);

  @Autowired
  private TransactionService transactionService;

  public MerchantSettlementItemWriter()
  {
    super();
  }

  @Override
  public void write(List<? extends TransactionVO> items) throws Exception
  {
    TransactionResponseVO transactionResponseVO;
    for (final TransactionVO transaction : items)
    {
      transactionResponseVO = transactionService.merchantSettlement(transaction);
      if(transactionResponseVO != null) {
        LOGGER.info("Settlement MerchantStatement: {}", transactionResponseVO.toString());
      }
    }
  }

}
