package com.accounting.accounting_tool;

import com.accounting.accounting_tool.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
// Make available the class that I created to externalize the keys
@EnableConfigurationProperties(RsaKeyProperties.class)
public class AccountingToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountingToolApplication.class, args);
	}

}
