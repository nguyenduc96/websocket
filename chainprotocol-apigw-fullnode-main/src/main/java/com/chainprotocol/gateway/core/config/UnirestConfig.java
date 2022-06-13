package com.chainprotocol.gateway.core.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


import kong.unirest.Unirest;

@Configuration
public class UnirestConfig {

    @PostConstruct
    public void unirestConfig() {
        Unirest.config()
                .connectionTTL(-1, TimeUnit.SECONDS)
                .connectTimeout(60)
                .socketTimeout(30);
    }

}
