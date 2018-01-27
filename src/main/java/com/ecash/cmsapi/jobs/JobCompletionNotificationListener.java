package com.ecash.cmsapi.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport
{

  private static final Logger log = LoggerFactory
      .getLogger(JobCompletionNotificationListener.class);

  @Override
  public void afterJob(JobExecution jobExecution)
  {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED)
    {
      log.info("JOB {}-{} FINISHED", jobExecution.getJobConfigurationName(),
          jobExecution.getJobId());
    }
    else if (jobExecution.getStatus().equals(BatchStatus.FAILED))
    {
      log.error("JOB {}-{} FAILED", jobExecution.getJobConfigurationName(),
          jobExecution.getJobId());
    }
    else
    {
      log.debug("JOB {}-{} {}", jobExecution.getJobConfigurationName(),
          jobExecution.getJobId(), jobExecution.getStatus());
    }
  }
}