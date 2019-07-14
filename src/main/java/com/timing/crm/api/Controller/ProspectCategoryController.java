package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.ProspectCategoryService;
import com.timing.crm.api.View.ProspectCategory;
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
public class ProspectCategoryController extends BaseController{

    private final Logger logger = LoggerFactory.getLogger("ApproachTypeController");

    @Autowired
    private ProspectCategoryService prospectCategoryService;

    @RequestMapping(value = "/prospectCategory", method = RequestMethod.GET)
    @ApiOperation(value = "Returns prospectCategory details", notes = "Returns a complete list of prospectCategory")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful prospectCategory retrieval"),
            @ApiResponse(code = 204, message = "prospectCategory does not exists")}
    )
    public List<ProspectCategory> approachTypeSelect(
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("approachTypeSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get All ApproachType");
        List<ProspectCategory> prospectCategories;
        prospectCategories = prospectCategoryService.getListProspectCategory();
        logger.info("Completing Get All ApproachType");
        return prospectCategories;
    }

}
