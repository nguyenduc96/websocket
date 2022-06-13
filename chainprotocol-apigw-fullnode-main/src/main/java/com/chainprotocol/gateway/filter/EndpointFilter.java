package com.chainprotocol.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class EndpointFilter extends ZuulFilter {
    
    @Override
    public int filterOrder() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public String filterType() {
        // TODO Auto-generated method stub
        return FilterConstants.ROUTE_TYPE;
    }
    
    @Override
    public boolean shouldFilter() {
        // TODO Auto-generated method stub
        return !RequestContext.getCurrentContext().sendZuulResponse();
    }

    @Override
    public Object run() throws ZuulException {
        // TODO Auto-generated method stub
        log.info("endpoint");
        return null;
    }
}
