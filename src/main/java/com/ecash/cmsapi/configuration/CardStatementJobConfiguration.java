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

import com.ecash.cmsapi.jobs.CardStatementItemWriter;
import com.ecash.cmsapi.jobs.CardTransactionProcessor;
import com.ecash.cmsapi.jobs.JobCompletionNotificationListener;
import com.ecash.cmsapi.jobs.TransactionReader;
import com.ecash.ecashcore.service.TransactionService;
import com.ecash.ecashcore.vo.CardStatementVO;
import com.ecash.ecashcore.vo.TransactionVO;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@Import({ BatchSchedulerConfiguration.class })
public class CardStatementJobConfiguration
{
  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private TransactionService transactionService;

  @Bean
  ItemReader<TransactionVO> reader()
  {
    return new TransactionReader(transactionService);
  }

  @Bean
  public CardTransactionProcessor processor()
  {
    return new CardTransactionProcessor();
  }

  @Bean
  public ItemWriter<CardStatementVO> writer()
  {
    return new CardStatementItemWriter();
  }

  @Bean
  public JobExecutionListener listener()
  {
    return new JobCompletionNotificationListener();
  }

  @Bean
  public Job processCardStatementJob()
  {
    return jobBuilderFactory.get("processCardStatementJob")
        .incrementer(new RunIdIncrementer())
        .listener(listener())
        .flow(cardStatementStep()).end().build();
  }

  @Bean
  public Step cardStatementStep()
  {
    return stepBuilderFactory.get("cardStatementStep").<TransactionVO, CardStatementVO> chunk(1)
        .reader(reader()).processor(processor()).writer(writer())
        .build();
  }

}
