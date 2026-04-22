package com.rares.ecommerce.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity serverhttpsecurity,
            EmailVerifiedWebFilter emailVerifiedWebFilter
    ) {
        serverhttpsecurity
                .cors(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .addFilterAfter(emailVerifiedWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers(
                                "/eureka/**",
                                "/swagger-ui.html",
                                "/webjars/swagger-ui/**",      // Allows the HTML/CSS/JS
                                "/v3/api-docs/**",             // Allows the Gateway's JSON docs
                                "/api/v1/*/v3/api-docs",      // Allows the Microservices' JSON docs
                                "/aggregate/**",
                                "/api/v1/customers/register"
                        ).permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/auth/forgot-password", "/api/v1/auth/reset-password").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/auth/verify-email").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/products", "/api/v1/products/*").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/customers/me").hasAnyRole("USER", "ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/customers/exists/*", "/api/v1/customers/*").hasAnyRole("USER", "ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/auth/send-email-verification").hasAnyRole("USER", "ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/orders").hasAnyRole("USER", "ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/orders/**", "/api/v1/order-lines/**").hasAnyRole("USER", "ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/payments").hasAnyRole("USER", "ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/products/purchase").hasAnyRole("USER", "ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/products").hasRole("ADMIN")
                        .pathMatchers("/api/v1/customers/**").hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter())))
                );

        return serverhttpsecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
        return converter;
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Set<String> roles = new LinkedHashSet<>();
        collectRoles((Map<String, Object>) jwt.getClaim("realm_access"), roles);

        Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            for (Object clientAccess : resourceAccess.values()) {
                if (clientAccess instanceof Map<?, ?> accessMap) {
                    collectRoles((Map<String, Object>) accessMap, roles);
                }
            }
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase(Locale.ROOT)));
        }
        return authorities;
    }

    @SuppressWarnings("unchecked")
    private void collectRoles(Map<String, Object> accessContainer, Set<String> roles) {
        if (accessContainer == null) {
            return;
        }
        Object rawRoles = accessContainer.get("roles");
        if (rawRoles instanceof Collection<?> roleList) {
            for (Object role : roleList) {
                if (role instanceof String roleName && !roleName.isBlank()) {
                    roles.add(roleName);
                }
            }
        }
    }
}
