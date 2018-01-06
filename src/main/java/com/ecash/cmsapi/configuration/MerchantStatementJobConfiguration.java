package com.ecash.cmsapi.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.ecash.cmsapi.jobs.MerchantStatementItemWriter;
import com.ecash.cmsapi.jobs.MerchantStatementProcessor;
import com.ecash.cmsapi.jobs.TransactionReaderForMerchant;
import com.ecash.ecashcore.service.TransactionService;
import com.ecash.ecashcore.vo.MerchantStatementVO;
import com.ecash.ecashcore.vo.TransactionVO;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@Import({ BatchSchedulerConfiguration.class })
public class MerchantStatementJobConfiguration
{
  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  JobExecutionListener listener;

  @Bean
  ItemReader<TransactionVO> merchantStatementReader()
  {
    return new TransactionReaderForMerchant(transactionService);
  }

  @Bean
  public MerchantStatementProcessor merchantStatementProcessor()
  {
    return new MerchantStatementProcessor();
  }

  @Bean
  public ItemWriter<MerchantStatementVO> merchantStatementWriter()
  {
    return new MerchantStatementItemWriter();
  }

  @Bean(name = "merchantStatementJob")
  public Job processMerchantStatementJob()
  {
    return jobBuilderFactory.get("processMerchantStatementJob")
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .flow(merchantStatementStep()).end().build();
  }

  @Bean
  public Step merchantStatementStep()
  {
    return stepBuilderFactory.get("merchantStatementStep")
        .<TransactionVO, MerchantStatementVO> chunk(10)
        .reader(merchantStatementReader()).processor(merchantStatementProcessor())
        .writer(merchantStatementWriter())
        .build();
  }

}
