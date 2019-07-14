package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.AuthenticationService;
import com.timing.crm.api.Services.UserService;
import com.timing.crm.api.View.Users;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1")
public class AuthenticationController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger("AuthenticationController");

    @RequestMapping(value = "/authentication", method = RequestMethod.GET)
    @ApiOperation(value = "Returns user's detail", notes = "Given a user login and password and returns its details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful User Validation"),
            @ApiResponse(code = 204, message = "Login is not valid")}
    )
    public Users validarUser (
            @RequestHeader(value = "login") String login,
            @RequestHeader(value = "password") String password){
        logger.info("Starting Valid Login with login: {}, password: {}", login, password);
        Users usersResult;
        usersResult = authenticationService.validateUser(login, password);
        logger.info("Completing Validate User with username: {}, login: {}, token: {} ", usersResult.getUserName(), usersResult.getLogin(),usersResult.getToken());
        return usersResult;
    }


    @RequestMapping(value = "/authentication/{LOGIN}", method = RequestMethod.GET)
    @ApiOperation(value = "Returns Boolean", notes = "Given a user login and verify if exist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful User Validation"),
            @ApiResponse(code = 204, message = "Login is not valid")}
    )
    public Boolean loginExist(
            @PathVariable("LOGIN") String login) {
                Boolean exist;
                logger.info("Starting Exist Login with login: {}", login);
                exist = authenticationService.loginExist(login);
                logger.info("Completing loginExist");
                return exist;
    }


    @RequestMapping(value = "/validSession", method = RequestMethod.GET)
    @ApiOperation(value = "Returns Boolean", notes = "Given a token and verify if exist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful User Validation")}
    )
    public Boolean validToken(
            @RequestHeader(value = "token") UUID token) {

        logger.info("Starting validToken with token: {}", token);
        Boolean valid;
        valid = isAuthenticated(token);
        logger.info("Completing valid token: {}", valid);
        if (!valid) {
            logger.error("validToken() - Invalid token, throwing forbidden exception");
            throw new ForbiddenException("Invalid token");
        }
        return valid;
    }

}
