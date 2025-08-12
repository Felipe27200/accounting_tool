package com.accounting.accounting_tool;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/*
* This class is necessary to make the deployment on Wildfly
* */
public class ServletInitializer extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure (SpringApplicationBuilder application)
    {
        return application.sources(AccountingToolApplication.class);
    }
}
