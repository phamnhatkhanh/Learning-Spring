package com.springbootmicroservices.userservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootmicroservices.userservice.config.KeycloakConfig;
import com.springbootmicroservices.userservice.dto.KeycloakUser;
import com.springbootmicroservices.userservice.dto.KeycloakUserRepresentation;
import com.springbootmicroservices.userservice.dto.LoginRequest;
import com.springbootmicroservices.userservice.service.KeycloakService;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final Keycloak keycloak;

    @Autowired
    private WebClient.Builder keycloakWebClient;
    @Autowired
    private ModelMapper modelMapper;

    public KeycloakServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public AccessTokenResponse loginWithKeycloak(LoginRequest request) {
        Keycloak loginKeycloak = buildKeycloak(request.getUsername(), request.getPassword());

        AccessTokenResponse accessTokenResponse = null;

        try{
            accessTokenResponse = loginKeycloak.tokenManager().getAccessToken();
            return accessTokenResponse;
        }catch (Exception e){
            return null;
        }
    }

    private Keycloak buildKeycloak(String username, String password) {

        return KeycloakBuilder.builder()
                .realm(KeycloakConfig.realm)
                .serverUrl(KeycloakConfig.serverUrl)
                .clientId(KeycloakConfig.clientId)
                .clientSecret(KeycloakConfig.clientSecret)
                .username(username)
                .password(password)
                .build();

    }
    public Mono<String> getTokenKeycloack() {
            keycloakWebClient
                    .baseUrl("http://localhost:8181/realms/master")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .build();

            return keycloakWebClient.build().post()
                    .uri("/protocol/openid-connect/token")
//                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                            .with("client_id", "micro-keycloak")
                            .with("client_secret", "r6JRl0ZNzTMko1UpnJnSZ6ILTgFrzvLz"))
                    .retrieve()
                    .bodyToMono(String.class);


    }

    public Mono<HttpStatus> getData(String accessToken,KeycloakUser keycloakUser) {

        LOGGER.info("KeycloakServiceImpl | createUserWithKeycloak is started");

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(keycloakUser.getFirstName());
        userRepresentation.setLastName(keycloakUser.getLastName());
        userRepresentation.setEmail(keycloakUser.getEmail());
        userRepresentation.setUsername(keycloakUser.getUsername());
        HashMap<String, List<String>> clientRoles = new HashMap<>();
        clientRoles.put(KeycloakConfig.clientId,Collections.singletonList(keycloakUser.getRole()));
        userRepresentation.setClientRoles(clientRoles);

        userRepresentation.setEnabled(true);

        LOGGER.info("KeycloakServiceImpl | createUserWithKeycloak | userRepresentation is completed");

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType("password");
        credentialRepresentation.setValue(keycloakUser.getPassword());
        credentialRepresentation.setTemporary(false);

        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        KeycloakUserRepresentation responeCustomer = modelMapper.map(userRepresentation,KeycloakUserRepresentation.class);

        LOGGER.info("KeycloakServiceImpl | getData | credentialRepresentation is completed");


        ObjectMapper objectMapper = new ObjectMapper();
        try{
            // convert user object to json string and return it
            String userJson = objectMapper.writeValueAsString(responeCustomer);

            LOGGER.info("KeycloakServiceImpl | getData | usersResource : " + userJson);

            keycloakWebClient
                    .baseUrl("http://localhost:8181/admin/realms/master")
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();


            // Make the second request using the obtained token
            return keycloakWebClient.build().post()
                    .uri("/users")
                    .bodyValue(userJson)
                    .exchange()
                    .flatMap(response -> {
                        System.out.println("response.statusCode().value()");
                        System.out.println(response.statusCode().value());

                        if(response.statusCode().value() == 201){
                            return Mono.just(HttpStatus.CREATED);
                        }else{
                            return Mono.just(HttpStatus.BAD_REQUEST);
                        }


                    });
        }
        catch (Exception e) {
            // catch various errors
            e.printStackTrace();
            return Mono.just(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void createUserWithKeycloak(KeycloakUser keycloakUser) {
        AtomicInteger statusCode = new AtomicInteger();
        Mono<HttpStatus> dataResponseMono = this.getTokenKeycloack().flatMap(respone ->
                {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(respone);
                        String token = jsonNode.get("access_token").asText();
                        LOGGER.info("KeycloakServiceImpl | createUserWithKeycloak | {}", token);
                        return this.getData(token, keycloakUser);
                    } catch (Exception e) {

                        e.printStackTrace();
                        return Mono.just(HttpStatus.BAD_REQUEST);
                    }
                }
        );
        dataResponseMono.subscribe(res ->{
            statusCode.set(res.value());
            System.out.println("KeycloakServiceImpl | createUserWithKeycloak | dataresponseMono");
            System.out.println(res);

        }, error ->{
            statusCode.set(400);
        });

    }
}
