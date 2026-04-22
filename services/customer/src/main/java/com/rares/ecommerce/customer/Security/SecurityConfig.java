package com.rares.ecommerce.customer.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/customers/v3/api-docs").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/customers/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/customers/me").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/customers/exists/*", "/api/v1/customers/*").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/customers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/customers/*").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

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
        });

        return converter;
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
