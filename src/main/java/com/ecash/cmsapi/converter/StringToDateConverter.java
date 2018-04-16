package com.ecash.cmsapi.converter;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import com.ecash.ecashcore.util.DateTimeUtils;

public class StringToDateConverter implements Converter<String, Date> {

  @Override
  public Date convert(String value) {
    Date date = null;
    try {
      Timestamp stamp = new Timestamp(Long.valueOf(value));
      date = new Date(stamp.getTime());
    } catch (NumberFormatException e) {
      date = DateTimeUtils.parseDate(value, DateTimeUtils.REPORT_PARAM_FORMAT, null).toDate();
      if (date == null) {
        date = DateTimeUtils.parseDate(value).toDate();
      }
    }
    return date;
  }
}
