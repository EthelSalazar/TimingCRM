package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.CharacteristicsAppService;
import com.timing.crm.api.View.CharacteristicsApp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1")
public class CharacteristicsAppController extends BaseController{

    private final Logger logger = LoggerFactory.getLogger("CharacteristicsAppController");

    @Autowired
    private CharacteristicsAppService characteristicsAppService;

    @RequestMapping(value = "/CharacteristicsApp", method = RequestMethod.GET)
    @ApiOperation(value = "Returns CharacteristicsApp details", notes = "Returns a complete list of CharacteristicsApp")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful CharacteristicsApp retrieval"),
            @ApiResponse(code = 204, message = "CharacteristicsApp does not exists")}
    )
    public List<CharacteristicsApp> rolesSelect(
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("rolesSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get All CharacteristicsApp");
        List<CharacteristicsApp> characteristicsAppList;
        characteristicsAppList = characteristicsAppService.getListCharacteristicsApp();
        logger.info("Completing Get All CharacteristicsApp");
        return characteristicsAppList;
    }
}
