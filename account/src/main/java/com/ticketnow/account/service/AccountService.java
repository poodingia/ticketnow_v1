package com.ticketnow.account.service;

import com.ticketnow.account.config.KeycloakPropsConfig;
import com.ticketnow.account.dto.CustomerDTO;
import com.ticketnow.account.dto.CustomerProfileRequestDTO;
import com.ticketnow.account.dto.ProfileWithRoleDTO;
import com.ticketnow.account.exception.NotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final Keycloak keycloak;
    private final KeycloakPropsConfig keycloakPropsConfig;


    public AccountService(Keycloak keycloak, KeycloakPropsConfig keycloakPropsConfig) {
        this.keycloak = keycloak;
        this.keycloakPropsConfig = keycloakPropsConfig;
    }

    public void deleteAccount(String id) {
        UserRepresentation userRepresentation =
                keycloak.realm(keycloakPropsConfig.getRealm()).users().get(id).toRepresentation();
        if (userRepresentation != null) {
            RealmResource realmResource = keycloak.realm(keycloakPropsConfig.getRealm());
            UserResource userResource = realmResource.users().get(id);
            userRepresentation.setEnabled(false);
            userResource.update(userRepresentation);
        } else {
            throw new NotFoundException();
        }
    }

    public void updateAccount(String id, CustomerProfileRequestDTO request) {
        UserRepresentation userRepresentation =
                keycloak.realm(keycloakPropsConfig.getRealm()).users().get(id).toRepresentation();
        if (userRepresentation != null) {
            userRepresentation.setFirstName(request.firstName());
            userRepresentation.setLastName(request.lastName());
            RealmResource realmResource = keycloak.realm(keycloakPropsConfig.getRealm());
            UserResource userResource = realmResource.users().get(id);
            userResource.update(userRepresentation);
        } else {
            throw new NotFoundException();
        }
    }

    public String[] getAccountEmail(List<String> userId) {
        return userId.stream()
                .map(id -> {
                    UserRepresentation userRepresentation = keycloak
                            .realm(keycloakPropsConfig.getRealm())
                            .users()
                            .get(id)
                            .toRepresentation();
                    return userRepresentation.getEmail();
                })
                .toArray(String[]::new);
    }


    public CustomerDTO getAccount(String id) {
        return
                CustomerDTO.fromUserRepresentation(keycloak.realm(keycloakPropsConfig.getRealm()).users().get(id).toRepresentation());
    }

    public ProfileWithRoleDTO getAccountByEmail(String email) {
        UsersResource usersResource = keycloak.realm(keycloakPropsConfig.getRealm()).users();

        List<UserRepresentation> users = usersResource.search(email, true);
        UserRepresentation user = users.get(0);
        UserResource userResource = usersResource.get(user.getId());
        List<RoleRepresentation> realmRoles = userResource.roles().realmLevel().listEffective();
        boolean isAdmin = realmRoles.stream().anyMatch(role -> role.getName().equals("admin"));
        return ProfileWithRoleDTO.fromCustomerDTO(CustomerDTO.fromUserRepresentation(user), isAdmin);
    }

    public Boolean updateUserRole(String email, String roleName) {
        RealmResource realmResource = keycloak.realm(keycloakPropsConfig.getRealm());
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> users = usersResource.search(email, true);
        if (users.isEmpty()) {
            throw new RuntimeException("User with email " + email + " not found");
        }
        UserRepresentation user = users.get(0);
        UserResource userResource = usersResource.get(user.getId());

        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();

        if (userResource.roles().realmLevel().listAll().contains(role)) {
            log.info("User already has the role {}", roleName);
            userResource.roles().realmLevel().remove(Collections.singletonList(role));
            return true;
        }

        userResource.roles().realmLevel().add(Collections.singletonList(role));

        return true;
    }

    public List<ProfileWithRoleDTO> getAllAccounts(int page, int size) {
        List<UserRepresentation> users = keycloak.realm(keycloakPropsConfig.getRealm()).users().list(page * size, size);
        return users.stream()
                .filter(UserRepresentation::isEnabled)
                .map(user -> {
                    UserResource userResource = keycloak.realm(keycloakPropsConfig.getRealm()).users().get(user.getId());
                    List<RoleRepresentation> realmRoles = userResource.roles().realmLevel().listEffective();
                    boolean isAdmin = realmRoles.stream().anyMatch(role -> role.getName().equals("admin"));
                    return ProfileWithRoleDTO.fromCustomerDTO(CustomerDTO.fromUserRepresentation(user), isAdmin);
                })
                .toList();
    }
}
