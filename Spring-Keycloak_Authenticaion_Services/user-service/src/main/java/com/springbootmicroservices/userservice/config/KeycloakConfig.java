package com.springbootmicroservices.userservice.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;
@Configuration
public class KeycloakConfig {


//    public final static String serverUrl = "http://localhost:8181/realms/master/protocol/openid-connect/token";
    public final static String serverUrl = "http://localhost:8181";
    public final static String realm = "master";
    public final static String clientId = "spring-boot-microservice-keycloak";
    public final static String clientSecret = "r6JRl0ZNzTMko1UpnJnSZ6ILTgFrzvLz";
    final static String userName = "admin";
    final static String password = "password";

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver(){
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(userName)
                .password(password)
                .build();

    }
}
