package com.ecash.cmsapi.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.ecash.ecashcore.vo.CardStatementDetailVO;
import com.ecash.ecashcore.vo.CardStatementVO;
import com.ecash.ecashcore.vo.TransactionVO;

public class CardTransactionProcessor implements ItemProcessor<TransactionVO, CardStatementVO>
{
  private static final Logger LOG = LoggerFactory.getLogger(CardTransactionProcessor.class);

  @Override
  public CardStatementVO process(TransactionVO transaction) throws Exception
  {
    CardStatementVO cardStatementVO = new CardStatementVO();
    CardStatementDetailVO cardStatementDetailVO = new CardStatementDetailVO();
    cardStatementDetailVO.setCreatedBy("CMS SYSTEM");
    cardStatementDetailVO.setDescription(transaction.getTransactionDetailDetail());
    cardStatementDetailVO.setTransactionAmount(transaction.getAmount());
    cardStatementDetailVO.setTransactionDate(transaction.getDate());
    cardStatementDetailVO.setTransactionTypeCode(transaction.getTransactionType());
    cardStatementVO.setCardStatementDetailVO(cardStatementDetailVO);
    cardStatementVO.setCompany(transaction.getAccountCustomerOrganizationShortName());
    cardStatementVO.setCreatedBy("CMS SYSTEM");
    cardStatementVO.setCurrencyCode(transaction.getAccountCurrencyCode());
    cardStatementVO.setCurrentAmount(transaction.getAccountCurrentBalance());
    cardStatementVO.setDueDate(transaction.getDate());
    cardStatementVO.setPaymentAmount(transaction.getAmount());
    cardStatementVO.setCard_number(transaction.getCardNumber());
    LOG.info("Creating cardStatement: {}", cardStatementVO.toString());
    return cardStatementVO;
  }

}
