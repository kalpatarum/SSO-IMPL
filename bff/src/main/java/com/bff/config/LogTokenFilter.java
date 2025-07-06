package com.bff.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LogTokenFilter extends AbstractGatewayFilterFactory<LogTokenFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(LogTokenFilter.class);

    public LogTokenFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String token = headers.getFirst(HttpHeaders.AUTHORIZATION);

            if (token != null && token.startsWith("Bearer ")) {
                logger.info("🔐 Injected to {} Access Token: {}", exchange.getRequest().getPath(), token.substring(0, Math.min(token.length(), 80)) + "...");
            } else {
                logger.warn("⚠️ No Authorization header present");
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Optional config if needed
    }
}
