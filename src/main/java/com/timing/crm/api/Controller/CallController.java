package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.CallService;
import com.timing.crm.api.View.Call;
import com.timing.crm.api.View.CallRegister;
import com.timing.crm.api.View.Lead;
import com.timing.crm.api.View.ReasonNotQualify;
import com.timing.crm.api.View.ResultCall;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1")
public class CallController extends BaseController{
    private final Logger logger = LoggerFactory.getLogger("LeadController");

    @Autowired
    private CallService callService;

    @RequestMapping(value = "/call", method = RequestMethod.POST)
    @ApiOperation(value = "Create a new call", notes = "Returns call details created")
    public CallRegister createNewCall(
            @Valid @RequestBody CallRegister callRegister,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("createNewEmployee() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Create a New Call with callResult: {}, leadId: {}", callRegister.call.getResultCallId(), callRegister.lead.getId());
        return callService.createCall(callRegister, token);
    }


    @RequestMapping(value = "/resultCall", method = RequestMethod.GET)
    @ApiOperation(value = "Returns resultCall details", notes = "Returns a complete list of resultCall")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful resultCall retrieval"),
            @ApiResponse(code = 204, message = "resultCall does not exists")}
    )
    public List<ResultCall> resultCallSelect(
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("resultCallSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get All resultCall");
        List<ResultCall> resultCalls;
        resultCalls = callService.getListResultCall();
        logger.info("Completing Get All resultCall");
        return resultCalls;
    }

    @RequestMapping(value = "/reasonNotQualifies", method = RequestMethod.GET)
    @ApiOperation(value = "Returns reasonNotQualify details", notes = "Returns a complete list of reasonNotQualify")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful reasonNotQualify retrieval"),
            @ApiResponse(code = 204, message = "reasonNotQualify does not exists")}
    )
    public List<ReasonNotQualify> reasonNotQualifySelect(
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("reasonNotQualifySelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get All ReasonNotQualify");
        List<ReasonNotQualify> reasonNotQualifies;
        reasonNotQualifies = callService.getAllReasonNotQualify();
        logger.info("Completing Get All ReasonNotQualify");
        return reasonNotQualifies;
    }

    @RequestMapping(value = "/trackingCall", method = RequestMethod.POST)
    @ApiOperation(value = "Create a new tracking call", notes = "Returns a tracking call details created")
    public Call createTrackingCall(
            @Valid @RequestBody Call call,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("createNewTrackingCall() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Create a New Call with follow Up appointent Id: {}, leadId: {}", call.getFollowupAppointmentId(), call.getId());
        return callService.createTrackingCall(call, token);
    }
}
