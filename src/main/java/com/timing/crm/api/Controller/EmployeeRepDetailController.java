package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.EmployeeRepDetailService;
import com.timing.crm.api.View.EmployeeRepDetail;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1")
public class EmployeeRepDetailController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger("EmployeeRepDetailController");


    @Autowired
    EmployeeRepDetailService employeeRepDetailService;

    @RequestMapping(value = "/employeeRepDetail", method = RequestMethod.GET)
    @ApiOperation(value = "Returns employeesRepDetail detail", notes = "Given a complete list of EmployeesRepDetails returns its details by Company and Some Roles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful employeeRepDetail retrieval"),
            @ApiResponse(code = 204, message = "employeeRepDetail do not exists")}
    )
    public List<EmployeeRepDetail> adminRepToEmplSelect(
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("adminRepToEmplSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting adminRepToEmplSelect, with mandatory parameters token: {}", token);
        List<EmployeeRepDetail> employeeRepDetailResult;
        employeeRepDetailResult = employeeRepDetailService.adminRepToEmplSelect(token);
        logger.info("Completing adminRepToEmplSelect EmployeeRepdetail.size: {}", employeeRepDetailResult.size());
        return employeeRepDetailResult;
    }

    @RequestMapping(value = "/employeeRepDetail", method = RequestMethod.PUT)
    @ApiOperation(value = "Returns employeesRepDetail detail", notes = "Modify a complete list of EmployeesRepDetails")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful employeeRepDetail updated"),
            @ApiResponse(code = 204, message = "employeeRepDetail do not exists")}
    )
    public List<EmployeeRepDetail> modifyEmployeeRepDetail(
            @Valid @RequestBody() List<EmployeeRepDetail> employeeRepDetails,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("adminRepToEmplSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting modifyEmployeeRepDetail, with size employeeRepDetailList: {}", employeeRepDetails.size());
        employeeRepDetails = employeeRepDetailService.modifyEmployeeRepDetail(employeeRepDetails);
        logger.info("Completing modifyEmployeeRepDetail EmployeeRepdetail.size: {}", employeeRepDetails.size());
        return employeeRepDetails;
     }
}
