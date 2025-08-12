package com.accounting.accounting_tool.config;

import com.accounting.accounting_tool.common.DateFormatValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateConfig
{
    @Bean
    public DateFormatValidator dateFormatValidator()
    {
        return new DateFormatValidator();
    }
}
