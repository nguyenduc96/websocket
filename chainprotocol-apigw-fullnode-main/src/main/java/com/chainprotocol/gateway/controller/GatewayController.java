package com.chainprotocol.gateway.controller;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.chainprotocol.gateway.constant.RedisConstant;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties.Policy;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("gw")
@CrossOrigin("*")
public class GatewayController {
    
    @Autowired
    @Qualifier(RedisConstant.DB.GW_MAPPING.REDIS_TEMPLATE)
    protected RedisTemplate<String, Object> mappingRedisTemplate;

    @Autowired
    private RateLimitProperties properties;

    @RequestMapping(value = "hello-world", method = RequestMethod.GET)
    public String helloWorld() {
        log.info("hello-world");
        String throttlingKey = "API_THROTTLING_TXN_ID_" + 1;
        String throttlingTxnId = String.valueOf(mappingRedisTemplate.opsForValue().get(throttlingKey));
        if (throttlingTxnId != null && !"null".equals(throttlingTxnId)) {
            log.warn("429 TOO_MANY_REQUESTS");
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
        }
        mappingRedisTemplate.opsForValue().set(
                throttlingKey,
                throttlingKey,
                3,
                TimeUnit.SECONDS
        );
        
        return "success";
    }
    
    @RequestMapping(value = "" +
            "", method = RequestMethod.POST)
    public String changePolicy(@RequestBody com.chainprotocol.gateway.model.Policy request) throws Exception {
        log.info("change policy");
        //
        List<Policy> policies = properties.getPoliciesOrNull(request.getServiceId());
        if(policies == null || policies.size() <= 0) {
        	log.error("serviceId doesn't exist");
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(request.getLimit() != null && request.getLimit() > 0) {
        	policies.get(0).setLimit(request.getLimit());
        }
        if(request.getRefreshInterval() != null && request.getRefreshInterval() > 0) {
        	policies.get(0).setRefreshInterval(Duration.ofSeconds(request.getRefreshInterval()));
        }
        properties.setPolicy(request.getServiceId(), policies);
        return "success";
    }

}
