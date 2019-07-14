package com.timing.crm.api.Controller;

import com.timing.crm.api.Services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public abstract class BaseController {

    @Autowired
    AuthenticationService authenticationService;

    protected Boolean isAuthenticated(UUID token) {
        return authenticationService.isAuthenticated(token);
    }
}
