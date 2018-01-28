package com.ecash.cmsapi.jobs;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatementJob
{
  private static final Logger LOGGER = LoggerFactory.getLogger(StatementJob.class);

  @Autowired
  private SimpleJobLauncher jobLauncher;

  @Autowired
  private Job processCardStatementJob;

  @Qualifier("merchantStatementJob")
  @Autowired
  private Job processMerchantStatementJob;

  @Scheduled(cron = "${card.cron.expression}")
  public void performCardStatementJob() throws Exception
  {
    LOGGER.info("Job Started at: {}", new Date());
    JobParameters param = new JobParametersBuilder().addString("CardStatementJob",
        String.valueOf(System.currentTimeMillis())).toJobParameters();
    JobExecution execution = jobLauncher.run(processCardStatementJob, param);
    LOGGER.info("Job finished with status: {}", execution.getStatus());
  }

  @Scheduled(cron = "${merchant.cron.expression}")
  public void performMerchantStatementJob() throws Exception
  {
    LOGGER.info("Job Started at: {}", new Date());
    JobParameters param = new JobParametersBuilder().addString("MerchantStatementJob",
        String.valueOf(System.currentTimeMillis())).toJobParameters();
    JobExecution execution = jobLauncher.run(processMerchantStatementJob, param);
    LOGGER.info("Job finished with status: {}", execution.getStatus());
  }

}
