package com.ecash.cmsapi.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.ecash.ecashcore.vo.MerchantStatementVO;
import com.ecash.ecashcore.vo.TransactionVO;

public class MerchantStatementProcessor implements ItemProcessor<MerchantStatementVO, TransactionVO>
{
  private static final Logger LOG = LoggerFactory.getLogger(MerchantStatementProcessor.class);

  @Override
  public TransactionVO process(MerchantStatementVO merchantStatementVO) throws Exception
  {
    TransactionVO transactionVO = new TransactionVO();
    transactionVO.setAmount(merchantStatementVO.getClosingAmount());
    transactionVO.setDate(merchantStatementVO.getDueDate());
    transactionVO.setCardNumber(merchantStatementVO.getMerchantId());
    transactionVO.setRelatedTransactionId(merchantStatementVO.getId());
    LOG.info("Settlement merchantStatement: {}", transactionVO.toString());
    return transactionVO;
  }

}
