package com.chainprotocol.gateway.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cp.data.gw.redis")
public class CacheProperties {

    public String host;
    public Integer port;
    public String password;

}
