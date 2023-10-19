package com.springbootmicroservices.userservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.ToString;
import org.keycloak.json.StringListMapDeserializer;
import org.keycloak.representations.idm.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
@Data
@ToString
public class KeycloakUserRepresentation {

    protected String username;
    protected Boolean enabled;
    protected Boolean totp;
    protected Boolean emailVerified;
    protected String firstName;
    protected String lastName;
    protected String email;

    protected List<CredentialRepresentation> credentials;

    protected List<String> requiredActions;

    protected List<String> realmRoles;
    protected Map<String, List<String>> clientRoles;


}
