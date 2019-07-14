package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.ApproachTypeService;
import com.timing.crm.api.View.ApproachType;
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
public class ApproachTypeController extends BaseController{
    private final Logger logger = LoggerFactory.getLogger("ApproachTypeController");

    @Autowired
    private ApproachTypeService approachTypeService;

    @RequestMapping(value = "/approachType", method = RequestMethod.GET)
    @ApiOperation(value = "Returns approachType details", notes = "Returns a complete list of approachType")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful approachType retrieval"),
            @ApiResponse(code = 204, message = "approachType does not exists")}
    )
    public List<ApproachType> approachTypeSelect(
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("approachTypeSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get All ApproachType");
        List<ApproachType> AppTypeList;
        AppTypeList = approachTypeService.getListApproachType();
        logger.info("Completing Get All ApproachType");
        return AppTypeList;
    }
}




