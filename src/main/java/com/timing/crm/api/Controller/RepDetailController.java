package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Repository.CharactAppRepDetailRepository;
import com.timing.crm.api.Services.RepDetailService;
import com.timing.crm.api.View.CharactAppRepDetail;
import com.timing.crm.api.View.RepDetail;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1")
public class RepDetailController extends BaseController{

    private final Logger logger = LoggerFactory.getLogger("RepDetailController");

    @Autowired
    private RepDetailService repDetailService;
    @Autowired
    private CharactAppRepDetailRepository charactAppRepDetailRepository;

    @RequestMapping(value = "/repDetail", method = RequestMethod.POST)
    @ApiOperation(value = "Create a new repDetail", notes = "Returns repDetail details created")
    public RepDetail createRepDetail(
            @Valid @RequestBody RepDetail repDetail,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("createRepDetail() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Create a New representative with Name: {}", repDetail.getName());
        return repDetailService.createRepDetail(repDetail);
    }


    @RequestMapping(value = "/repDetail", method = RequestMethod.PUT)
    @ApiOperation(value = "Update a repDetail", notes = "Returns repDetail details updated")
    public RepDetail modifyRepDetail(
            @Valid @RequestBody RepDetail repDetail,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("modifyRepDetail() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Update representative with token: {}, id: {}", token, repDetail.getId());
        return repDetailService.modifyRepDetail(repDetail);
    }

    @RequestMapping(value = "/repDetail", method = RequestMethod.GET)
    @ApiOperation(value = "Returns represent's detail", notes = "Given a representative token returns its details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful repDetail retrieval"),
            @ApiResponse(code = 204, message = "repDetail does not exists")}
    )
    public RepDetail getRepDetailSelect(
            @ApiParam(value = "RepDetail repDetailId (optional)")
            @RequestParam(value = "repDetailId", required = false) Integer repDetailId,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("getRepDetailSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get repDetail with token: {}", token);
        RepDetail repDetailResult;
        repDetailResult = repDetailService.getRepDetailbyId(token, repDetailId);
        logger.info("Completing Get repDetail");
        return repDetailResult;
    }

    @RequestMapping(value = "/repDetailList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns repDetail details", notes = "Returns a complete list of repDetail details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful repDetail retrieval"),
            @ApiResponse(code = 204, message = "repDetail does not exists")}
    )
    public List<RepDetail> repDetailSelect (
            @ApiParam(value = "RepDetail Phone (optional)")
            @RequestParam(value = "phone", required = false) String phone,
            @ApiParam(value = "RepDetail email (optional)")
            @RequestParam(value = "email", required = false) String email,
            @ApiParam(value = "RepDetail companyId (optional)")
            @RequestParam(value = "companyId", required = false) Integer companyId,
            @ApiParam(value = "RepDetail status (optional)")
            @RequestParam(value = "status", required = false) String status,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("repDetailSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get All RepDetails, with optional parameters phone: {}, email: {}", phone, email);
        List<RepDetail> repDetailResult;
        repDetailResult = repDetailService.getListRepDetail(phone, email, token, status);
        logger.info("Completing Get All RepDetails");
        return repDetailResult;
    }

    @RequestMapping(value = "/charactAppRepDetail", method = RequestMethod.GET)
    @ApiOperation(value = "Returns CharactAppRepDetail details", notes = "Returns a complete list of CharactAppRepDetail details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful CharactAppRepDetail retrieval"),
            @ApiResponse(code = 204, message = "CharactAppRepDetail does not exists")}
    )
    public List<CharactAppRepDetail> getListCharactAppRepDetail(
            @ApiParam(value = "RepDetail repDetailId (optional)")
            @RequestParam(value = "repDetailId", required = false) Integer repDetailId,
            @RequestHeader(value = "token") UUID token){

        if (!isAuthenticated(token)) {
            logger.error("getListCharactAppRepDetail() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }
        logger.info("Starting Get All CharactAppRepDetail, with optional parameters repDetailId: {}", repDetailId);
        List<CharactAppRepDetail> charactAppRepDetails;
        charactAppRepDetails = charactAppRepDetailRepository.getAllCharactAppRepDetail(repDetailId);
        logger.info("Completing Get All CharactAppRepDetail");
        return charactAppRepDetails;
    }
}
