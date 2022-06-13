package com.chainprotocol.gateway.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.ClientEndpointConfig;

import com.netflix.util.Pair;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.chainprotocol.gateway.util.StringUtils;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimiter;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitEvent;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitExceededEvent;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import kong.unirest.Unirest;
import kong.unirest.UnirestParsingException;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class IncomingFilter extends ZuulFilter {


    @Autowired
	private ProxyRequestHelper helper;
	
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * set pre filter order greater than 5 , then can used ctx.get(SERVICE_ID_KEY) get it. because PreDecorationFilter order was 5, that set SERVICE_ID_KEY .
     */
    @Override
    public int filterOrder() {
        return 6;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("incoming");
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String cnnn =request.getHeader("connection");

//        Route route = context.
        String serviceId = context.get("serviceId").toString();
        try {
        	String bearer = request.getHeader("Authorization").replace("Bearer ", "");
            String resultString = Unirest.post("http://localhost:8082/auth")
                    .fields(Map.of("user", bearer, "serviceId", serviceId))
                    .header("Authorization", request.getHeader("Authorization"))
                    .asString()
                    .ifFailure(response -> {
                        UnirestParsingException exception = null;
                        if (response.getParsingError().isPresent()) {
                            exception = response.getParsingError().get();
                        }
                        log.error("http error {}\n{}", response.getStatus(), response.getBody(), exception);
                    })
                    .getBody();
            Boolean result = StringUtils.getBoolean(resultString);
            if(result) {
                log.info("serviceId: {}", serviceId);
            } else {
                log.warn("User not found");
                context.setResponseStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                context.setResponseBody("User not found");
                context.setSendZuulResponse(false);
            }
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        return null;
    }
    
    @EventListener
    public void exceededEvent(RateLimitExceededEvent event) {
    	log.info("22222222222222222222222: {}", event.getPolicy().toString());
        // custom code
    }

    @EventListener
    public void observe(RateLimitEvent event) {
    	log.info("11111111111111111111111: {}", event.getPolicy().getLimit());
    	log.info("11111111111111111111111: {}", event.getRate().getRemaining());
        // custom code
    }
    
}
