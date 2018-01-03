package com.ecash.cmsapi.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.ecash.ecashcore.vo.MerchantStatementDetailVO;
import com.ecash.ecashcore.vo.MerchantStatementVO;
import com.ecash.ecashcore.vo.TransactionVO;

public class MerchantStatementProcessor implements ItemProcessor<TransactionVO, MerchantStatementVO>
{
  private static final Logger LOG = LoggerFactory.getLogger(MerchantStatementProcessor.class);

  @Override
  public MerchantStatementVO process(TransactionVO transaction) throws Exception
  {
    MerchantStatementVO merchantStatementVO = new MerchantStatementVO();
    MerchantStatementDetailVO merchantStatementDetailVO = new MerchantStatementDetailVO();

    merchantStatementDetailVO.setCreatedBy("CMS SYSTEM");
    merchantStatementDetailVO.setDescription(transaction.getTransactionDetailDetail());
    merchantStatementDetailVO.setTransactionAmount(transaction.getAmount());
    merchantStatementDetailVO.setTransactionDate(transaction.getDate());
    merchantStatementDetailVO.setTransactionTypeCode(transaction.getTransactionType());

    merchantStatementVO.setMerchantStatementDetailVO(merchantStatementDetailVO);
    merchantStatementVO.setCompany(transaction.getAccountCustomerOrganizationShortName());
    merchantStatementVO.setCreatedBy("CMS SYSTEM");
    merchantStatementVO.setCurrencyCode(transaction.getAccountCurrencyCode());
    merchantStatementVO.setDueDate(transaction.getDate());
    merchantStatementVO.setCardId(transaction.getCardNumber());

    merchantStatementVO.setOpeningAmount(0d);
    merchantStatementVO.setClosingAmount(transaction.getAmount());
    merchantStatementVO.setMerchantId(transaction.getTransactionDetailMerchantId());
    LOG.info("Creating cardStatement: {}", merchantStatementVO);
    return merchantStatementVO;
  }

}
