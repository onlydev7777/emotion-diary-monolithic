package com.example.emotiondiarymember.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

  public static String getYearMonth(LocalDate date) {
    return date.format(DateTimeFormatter.ofPattern("yyyyMM"));
  }
}
