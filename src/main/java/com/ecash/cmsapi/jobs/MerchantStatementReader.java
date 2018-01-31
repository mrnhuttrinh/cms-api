package com.ecash.cmsapi.jobs;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemReader;

import com.ecash.cmsapi.util.DateTimeUtils;
import com.ecash.ecashcore.service.MerchantStatementService;
import com.ecash.ecashcore.vo.MerchantStatementVO;

public class MerchantStatementReader implements ItemReader<MerchantStatementVO>
{
  private int nextTransactionIndex;
  private List<MerchantStatementVO> listMerchantStatement;
  private MerchantStatementService merchantStatementService;
  private boolean isRefreshMerchantStatementList;

  public MerchantStatementReader()
  {
    super();
  }

  public MerchantStatementReader(MerchantStatementService merchantStatementService)
  {
    this.merchantStatementService = merchantStatementService;
    loadListMerchantStatement(DateTimeUtils.getYesterday(), DateTimeUtils.today());
    this.nextTransactionIndex = 0;
    this.isRefreshMerchantStatementList = false;
  }

  @Override
  public MerchantStatementVO read() throws Exception
  {
    MerchantStatementVO merchantStatement = null;
    if (nextTransactionIndex < listMerchantStatement.size())
    {
      merchantStatement = listMerchantStatement.get(nextTransactionIndex);
      nextTransactionIndex++;
    }
    else
    {
      if (this.isRefreshMerchantStatementList)
      {
        loadListMerchantStatement(DateTimeUtils.getYesterday(), DateTimeUtils.today());
        if (listMerchantStatement.isEmpty())
        {
          this.isRefreshMerchantStatementList = true;
        }
        else
        {
          nextTransactionIndex = 0;
          merchantStatement = listMerchantStatement.get(nextTransactionIndex);
          nextTransactionIndex++;
          this.isRefreshMerchantStatementList = false;
        }
      }
      else
      {
        this.isRefreshMerchantStatementList = true;
      }
    }
    return merchantStatement;
  }

  private void loadListMerchantStatement(Date fromDate, Date toDate)
  {
    this.listMerchantStatement = this.merchantStatementService
        .findMerchantStatementByDueDateBetween(fromDate,
            toDate);
  }

}
