//package com.chainprotocol.gateway.filter;
//
//import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.HEADER_LIMIT;
//import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.HEADER_QUOTA;
//import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.HEADER_REMAINING;
//import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.HEADER_REMAINING_QUOTA;
//import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.HEADER_RESET;
//import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.REQUEST_START_TIME;
//import static java.util.concurrent.TimeUnit.MILLISECONDS;
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
//
//import com.google.common.collect.Maps;
//import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.Rate;
//import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitKeyGenerator;
//import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitUtils;
//import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimiter;
//import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
//import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.filters.AbstractRateLimitFilter;
//import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitExceededException;
//import com.netflix.zuul.context.RequestContext;
//
//import java.time.Duration;
//import java.util.Map;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.cloud.netflix.zuul.filters.Route;
//import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.UrlPathHelper;
//import lombok.extern.log4j.Log4j2;
//
//@Log4j2
//@Component
//public class IncomingRateLimitFilter extends AbstractRateLimitFilter {
//
//	private final RateLimiter rateLimiter;
//    private final RateLimitKeyGenerator rateLimitKeyGenerator;
//    private final ApplicationEventPublisher eventPublisher;
//
//    public void RateLimitPreFilter(final RateLimitProperties properties, final RouteLocator routeLocator,
//                              final UrlPathHelper urlPathHelper, final RateLimiter rateLimiter,
//                              final RateLimitKeyGenerator rateLimitKeyGenerator, final RateLimitUtils rateLimitUtils,
//                              final ApplicationEventPublisher eventPublisher) {
//        super(properties, routeLocator, urlPathHelper, rateLimitUtils);
//        this.rateLimiter = rateLimiter;
//        this.rateLimitKeyGenerator = rateLimitKeyGenerator;
//        this.eventPublisher = eventPublisher;
//    }
//
//    Override
//    public String filterType() {
//        return PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return properties.getPreFilterOrder();
//    }
//
//    @Override
//    public Object run() {
//        final RequestContext ctx = RequestContext.getCurrentContext();
//        final HttpServletResponse response = ctx.getResponse();
//        final HttpServletRequest request = ctx.getRequest();
//        final Route route = route(request);
//
//        policy(route, request).forEach(policy -> {
//            Map<String, String> responseHeaders = Maps.newHashMap();
//
//            final String key = rateLimitKeyGenerator.key(request, route, policy);
//            final Rate rate = rateLimiter.consume(policy, key, null);
//
//            final Long limit = policy.getLimit();
//            final Long remaining = rate.getRemaining();
//            if (limit != null) {
//                responseHeaders.put(HEADER_LIMIT, String.valueOf(limit));
//                responseHeaders.put(HEADER_REMAINING, String.valueOf(Math.max(remaining, 0)));
//            }
//
//            final Duration quota = policy.getQuota();
//            final Long remainingQuota = rate.getRemainingQuota();
//            if (quota != null) {
//                request.setAttribute(REQUEST_START_TIME, System.currentTimeMillis());
//                responseHeaders.put(HEADER_QUOTA, String.valueOf(quota.getSeconds()));
//                responseHeaders.put(HEADER_REMAINING_QUOTA, String.valueOf(MILLISECONDS.toSeconds(Math.max(remainingQuota, 0))));
//            }
//
//            responseHeaders.put(HEADER_RESET, String.valueOf(rate.getReset()));
//
//            if (!NONE.equals(properties.getResponseHeaders())) {
//                final String httpHeaderKey = key.replaceAll("[^A-Za-z0-9-.]", "_").replaceAll("__", "_");
//                for (Map.Entry<String, String> headersEntry : responseHeaders.entrySet()) {
//                    String header = VERBOSE.equals(properties.getResponseHeaders()) ? headersEntry.getKey() + "-" + httpHeaderKey : headersEntry.getKey();
//                    response.setHeader(header, headersEntry.getValue());
//                }
//            }
//
//            if ((limit != null && remaining < 0) || (quota != null && remainingQuota < 0)) {
//                ctx.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
//                ctx.put(RATE_LIMIT_EXCEEDED, "true");
//                ctx.setSendZuulResponse(false);
//
//                eventPublisher.publishEvent(new RateLimitExceededEvent(this, policy, rateLimitUtils.getRemoteAddress(request)));
//
//                throw new RateLimitExceededException();
//            }
//        });
//
//        return null;
//    }
//}
