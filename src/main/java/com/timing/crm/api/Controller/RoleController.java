package com.timing.crm.api.Controller;

import com.timing.crm.api.Services.RoleService;
import com.timing.crm.api.View.Role;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
public class RoleController {

    private final Logger logger = LoggerFactory.getLogger("RoleController");

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ApiOperation(value = "Returns role details", notes = "Returns a complete list of roles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful roles retrieval"),
            @ApiResponse(code = 204, message = "role does not exists")}
    )
    public List<Role> rolesSelect() {
        logger.info("Starting Get All Roles");
        List<Role> roleList;
        roleList = roleService.getListRole();
        logger.info("Completing Get All role");
        return roleList;
    }
}
