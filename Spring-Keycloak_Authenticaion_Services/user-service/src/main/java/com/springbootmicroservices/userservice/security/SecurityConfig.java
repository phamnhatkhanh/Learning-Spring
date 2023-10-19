package com.springbootmicroservices.userservice.security;

import com.springbootmicroservices.userservice.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.adapters.authorization.spi.HttpRequest;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.io.IOException;


@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig{




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("User Service | UserContextFilter | doFilter |Organization Service Incoming Correlation id: {}");
        http.csrf().disable();
        http
                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/api/v1/users/info").hasAnyRole("ADMIN")
                        .anyRequest()
                        .permitAll()
                );
//                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
//                .addFilterAfter(createPolicyEnforcerFilter(), BearerTokenAuthenticationFilter.class);
//                .and() //
                // Enable OAuth2 Resource Server Support
//                .oauth2ResourceServer() //
//                // Enable custom JWT handling
//                .jwt().jwtAuthenticationConverter(keycloakJwtAuthenticationConverter) //
//                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)
//                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
//                );<groupId>javax.ws.rs</groupId>

//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt
//                                .jwkSetUri("https://idp.example.com/.well-known/jwks.json")
//                        )
//                )


        return http.build();
    }

//    private ServletPolicyEnforcerFilter createPolicyEnforcerFilter() {
//        return new ServletPolicyEnforcerFilter(new ConfigurationResolver() {
//            @Override
//            public PolicyEnforcerConfig resolve(HttpRequest request) {
//                try {
//                    return JsonSerialization.readValue(getClass().getResourceAsStream("/policy-enforcer.json"), PolicyEnforcerConfig.class);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//    }
//
//    @Bean
//    JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
//    }

//    @buildAutowired
//    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        KeycloakAuthenticationProvider provider = keycloakAuthenticationProvider();
//        provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
//        auth.authenticationProvider(provider);
//    }
//
//    @Bean
//    @Override
//    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
//    }

}
