package com.timing.crm.api.Services;

import com.timing.crm.api.View.Users;

import java.util.UUID;

public interface AuthenticationService {


    Users validateUser(String login, String password);

    Boolean loginExist(String login);

    Boolean isAuthenticated(UUID token);

}
