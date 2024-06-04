package com.accounting.accounting_tool.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/*
* Those are used to externalize the both keys
*
* This allows to create a POJO, without the
* boilerplate of getter and setter and also the constructors
* to retrieve the information in the object.
* */
@ConfigurationProperties(prefix = "rsa")
public record RsaKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey)
{ }
