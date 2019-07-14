package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Repository.UserRepository;
import com.timing.crm.api.Services.LeadService;
import com.timing.crm.api.View.Lead;
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
public class LeadController  extends BaseController{
    private final Logger logger = LoggerFactory.getLogger("LeadController");

    @Autowired
    private LeadService leadService;

    @RequestMapping(value = "/lead", method = RequestMethod.GET)
    @ApiOperation(value = "Returns leads details", notes = "Returns a complete list of leads details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful leads retrieval"),
            @ApiResponse(code = 204, message = "Lead does not exists")}
    )
    public List<Lead> leadsSelect(
            @ApiParam(value = "status (optional)")
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "hostId", required = false) Integer hostId,
            @RequestParam(value = "userRepDetailId", required = false) Integer userRepDetailId,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("leadsSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get All Leads, with optional parameters status: {}, hostId: {}, userRepDetailId: {}, " +
                        "startDate:{}, endDate:{}, phone: {}, name:{}", status, hostId, userRepDetailId, startDate, endDate, phone, name);
        List<Lead> leadResult;
        leadResult = leadService.getListLeads(status,token, hostId, userRepDetailId, startDate, endDate, phone, name);
        logger.info("Completing Get All Leads");
        return leadResult;
    }

    @RequestMapping(value = "/lead", method = RequestMethod.PUT)
    @ApiOperation(value = "Update an Lead", notes = "Returns lead details updated")
    public Lead modifyLead(
            @Valid @RequestBody Lead lead,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("modifyLead() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Update Lead with id: {}, name: {}", lead.getId(), lead.getName());
        return leadService.modifyLead(lead);
    }

    @RequestMapping(value = "/lead/{LEAD_ID}", method = RequestMethod.GET)
    @ApiOperation(value = "Returns lead's detail", notes = "Given a Lead Id returns its details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful leads retrieval"),
            @ApiResponse(code = 204, message = "Lead does not exists")}
    )
    public Lead leadSingle(
            @PathVariable("LEAD_ID") Integer leadId,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("leadSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get Lead with id: {}", leadId);
        Lead leadResult;
        leadResult = leadService.getLeadById(leadId);
        logger.info("Completing Get Lead");
        return leadResult;
    }

    @RequestMapping(value = "/lead", method = RequestMethod.POST)
    @ApiOperation(value = "Create a Lead", notes = "Returns lead details created")
    public Lead createLead(
            @Valid @RequestBody Lead lead,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("createLead() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Create Lead with id: {}, name: {}", lead.getId(), lead.getName());
        return leadService.createLead(lead, token);
    }

}
