package com.ecash.cmsapi.jobs;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemReader;

import com.ecash.cmsapi.util.DateUtil;
import com.ecash.ecashcore.service.TransactionService;
import com.ecash.ecashcore.vo.TransactionVO;

public class TransactionReaderForMerchant implements ItemReader<TransactionVO>
{
  private int nextTransactionIndex;
  private List<TransactionVO> listTransaction;
  private TransactionService transactionService;
  private boolean isRefreshTransactionList;

  public TransactionReaderForMerchant()
  {
    super();
  }

  public TransactionReaderForMerchant(TransactionService transactionService)
  {
    this.transactionService = transactionService;
    loadListTransaction(DateUtil.getYesterday(), DateUtil.today());
    this.nextTransactionIndex = 0;
    this.isRefreshTransactionList = false;
  }

  @Override
  public TransactionVO read() throws Exception
  {
    TransactionVO transaction = null;
    if (nextTransactionIndex < listTransaction.size())
    {
      transaction = listTransaction.get(nextTransactionIndex);
      nextTransactionIndex++;
    }
    else
    {
      if (this.isRefreshTransactionList)
      {
        loadListTransaction(DateUtil.getYesterday(), DateUtil.today());
        nextTransactionIndex = 0;
        transaction = listTransaction.get(nextTransactionIndex);
        nextTransactionIndex++;
        this.isRefreshTransactionList = false;
      }
      else
      {
        this.isRefreshTransactionList = true;
      }
    }
    return transaction;
  }

  private void loadListTransaction(Date fromDate, Date toDate)
  {
    this.listTransaction = this.transactionService.findMerchantTransactionByDateBetween(fromDate,
        toDate);
  }

}
