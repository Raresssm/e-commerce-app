package com.rares.ecommerce.order.Config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            String authorizationHeader = currentAuthorizationHeader();
            if (StringUtils.hasText(authorizationHeader)) {
                request.getHeaders().set(HttpHeaders.AUTHORIZATION, authorizationHeader);
            }
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    @Bean
    public RequestInterceptor bearerTokenRequestInterceptor() {
        return template -> {
            String authorizationHeader = currentAuthorizationHeader();
            if (StringUtils.hasText(authorizationHeader)) {
                template.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
            }
        };
    }

    private String currentAuthorizationHeader() {
        var attributes = RequestContextHolder.getRequestAttributes();
        if (!(attributes instanceof ServletRequestAttributes servletAttributes)) {
            return null;
        }
        return servletAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
    }
}
