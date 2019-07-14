package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.UserService;
import com.timing.crm.api.View.Users;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(value = "/v1")
public class UserController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger("UserController");

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ApiOperation(value = "Create a new user", notes = "Returns User details created")
    public Users createNewUser(
            @Valid @RequestBody Users users) {
        logger.info("Starting Create a New User with login: {}, password: {}", users.getLogin(), users.getPassword());
        return userService.createUser(users);
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    @ApiOperation(value = "Update a user", notes = "Returns User details updated")
    public String modifyUser(
            @Valid @RequestBody Users users,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("modifyUser() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Update user with id: {}, login: {}", users.getId(), users.getLogin());
        return userService.modifyUser(users);
    }


    @ApiOperation(value = "Returns users details", notes = "Returns a complete list of user details")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful users retrieval"),
            @ApiResponse(code = 204, message = "Users does not exists")}
    )

    public List<Users> usersSelect(
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "companyId", required = false) Integer companyId,
            @RequestParam(value = "role", required = false) Integer role,
            @RequestParam(value = "status", required = false) String status,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("usersSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get All users, with optional parameters login: {}, company Id: {}, role: {}, status: {}",
                login, companyId, role, status);
        List<Users> userResult;
        userResult = userService.getListUsers(login, companyId, role, status);
        logger.info("Completing Get All users");
        return userResult;
    }

    @RequestMapping(value = "/users/{USERS_ID}", method = RequestMethod.GET)
    @ApiOperation(value = "Returns user's detail", notes = "Given a user Id returns its details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful User retrieval"),
            @ApiResponse(code = 204, message = "User does not exists")}
    )
    public Users userSelect(
            @PathVariable("USERS_ID") Integer userId,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("userSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get User with id: {}", userId);
        Users usersResult;
        usersResult = userService.getUserbyId(userId);
        logger.info("Completing Get User");
        return usersResult;
    }


}
