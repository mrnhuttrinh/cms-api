package com.ecash.cmsapi.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil
{

  private DateUtil()
  {
    super();
  }

  public static Date getYesterday()
  {
    return Date
        .from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  public static Date today()
  {
    return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
  }
}
