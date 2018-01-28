package com.ecash.cmsapi.jobs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.ecash.ecashcore.service.MerchantStatementService;
import com.ecash.ecashcore.vo.MerchantStatementVO;

public class MerchantStatementItemWriter implements ItemWriter<MerchantStatementVO>
{
  private static final Logger LOGGER = LoggerFactory.getLogger(MerchantStatementItemWriter.class);

  @Autowired
  private MerchantStatementService merchantStatementService;

  public MerchantStatementItemWriter()
  {
    super();
  }

  @Override
  public void write(List<? extends MerchantStatementVO> items) throws Exception
  {
    MerchantStatementVO merchantStatementSavedItem;
    for (final MerchantStatementVO merchantStatement : items)
    {
      merchantStatementSavedItem = merchantStatementService
          .upsertMerchantStatement(merchantStatement);
      LOGGER.info("Save MerchantStatement: {}", merchantStatementSavedItem.toString());
    }
  }

}
