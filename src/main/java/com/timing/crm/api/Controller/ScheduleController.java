package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.ScheduleService;
import com.timing.crm.api.View.Schedule;
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
public class ScheduleController extends BaseController{

    private final Logger logger = LoggerFactory.getLogger("ScheduleController");

    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    @ApiOperation(value = "Returns Schedule details", notes = "Returns a complete list of Schedule")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Schedule retrieval"),
            @ApiResponse(code = 204, message = "Schedule does not exists")}
    )
    public List<Schedule> rolesSelect(
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("rolesSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get All Schedule");
        List<Schedule> scheduleList;
        scheduleList = scheduleService.getListSchedule();
        logger.info("Completing Get All Schedule");
        return scheduleList;
    }
}
