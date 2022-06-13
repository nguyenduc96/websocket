package com.chainprotocol.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.chainprotocol.gateway.core.config.EnableUnirestJSON;
import com.chainprotocol.gateway.core.config.GwApplication;


@GwApplication
@EnableUnirestJSON
@EnableTransactionManagement
@EnableZuulProxy
@SpringCloudApplication
public class Application {

    public static void main(String[] args)  {
        SpringApplication.run(Application.class, args);
    }

}
