package com.cosmos.fileservice.config;

import org.joda.time.DateTimeZone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cosmos.fileservice.service.DateService;
import com.cosmos.fileservice.service.JodaDateService;
@Configuration
public class DateServiceConfig {

  @Bean
  DateService dateService() {
    return new JodaDateService(defaultTimeZone());
  }

  @Bean
  DateTimeZone defaultTimeZone() {
    return DateTimeZone.UTC;
  }
}