package com.chainprotocol.gateway.filter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.chainprotocol.gateway.constant.RedisConstant;
import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class OutgoingFilter extends ZuulFilter {
	
	@Autowired
    @Qualifier(RedisConstant.DB.CACHE.REDIS_TEMPLATE)
    protected RedisTemplate<String, Map<String, String>> cacheRedisTemplate;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().sendZuulResponse();
    }

    @Override
    public Object run() throws ZuulException {
        log.info("outgoing");
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse servletResponse = context.getResponse();
        if(servletResponse.getStatus() == HttpStatus.SC_OK) {
        	// counting theo userId: APIGW_FULLNODE_${USERID}_&{SERVICEID}_COUNTING
        	
        }
        
        return null;
    }
}
