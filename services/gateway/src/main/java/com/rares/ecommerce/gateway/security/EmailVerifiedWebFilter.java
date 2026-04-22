package com.rares.ecommerce.gateway.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class EmailVerifiedWebFilter implements WebFilter {

    private static final Set<String> EXEMPT_PATH_PREFIXES = Set.of(
            "/api/v1/auth/forgot-password",
            "/api/v1/auth/reset-password",
            "/api/v1/auth/verify-email",
            "/api/v1/auth/send-email-verification",
            "/api/v1/customers/register",
            "/api/v1/products",
            "/api/v1/products/"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (!shouldValidate(path)) {
            return chain.filter(exchange);
        }

        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication())
                .filter(authentication -> authentication instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .flatMap(authentication -> {
                    Boolean emailVerified = authentication.getToken().getClaimAsBoolean("email_verified");
                    if (Boolean.TRUE.equals(emailVerified)) {
                        return chain.filter(exchange);
                    }

                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    private boolean shouldValidate(String path) {
        if (!path.startsWith("/api/v1/")) {
            return false;
        }
        if (isPublicProductsPath(path)) {
            return false;
        }
        return EXEMPT_PATH_PREFIXES.stream().noneMatch(path::startsWith);
    }

    private boolean isPublicProductsPath(String path) {
        return path.equals("/api/v1/products") || path.startsWith("/api/v1/products/");
    }
}
