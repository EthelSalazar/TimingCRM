package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.EmployeeService;
import com.timing.crm.api.View.Employee;
import com.timing.crm.api.View.EmployeeRepDetail;
import io.swagger.annotations.ApiOperation;
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
public class EmployeeController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger("EmployeeController");
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/employee", method = RequestMethod.POST)
    @ApiOperation(value = "Create a new employee", notes = "Returns employee details created")
    public Employee createNewEmployee(
            @Valid @RequestBody Employee employee,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("createNewEmployee() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Create a New Employee with Name: {}, userId: {}", employee.getName(), employee.getUsersId());
        return employeeService.createEmployee(employee);
    }

    @RequestMapping(value = "/employeeList", method = RequestMethod.PUT)
    @ApiOperation(value = "Returns employeesRepDetail detail", notes = "Modify a complete list of Employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Employees updated"),
            @ApiResponse(code = 204, message = "Employees do not exists")}
    )
    public List<Employee> modifyEmployeeList(
            @Valid @RequestBody() List<Employee> employeeList,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("adminEmplSelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting modifyEmployeeList, with size employeeList: {}", employeeList.size());
        employeeList = employeeService.modifyEmployeeList(employeeList);
        logger.info("Completing modifyEmployeeList EmployeeList.size: {}", employeeList.size());
        return employeeList;
    }

    @RequestMapping(value = "/employee", method = RequestMethod.PUT)
    @ApiOperation(value = "Update an Employee", notes = "Returns employee details updated")
    public Employee modifyEmployee(
            @Valid @RequestBody Employee employee,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("modifyEmployee() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Update Employee with id: {}, name: {}", employee.getId(), employee.getName());
        return employeeService.modifyEmployee(employee);
    }

    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    @ApiOperation(value = "Returns employee's detail", notes = "Returns employee details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful employee retrieval"),
            @ApiResponse(code = 204, message = "Employee does not exists")}
    )
    public Employee employeeSingle(
           @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("employeeSingle() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get Employee with token: {}", token);
        Employee employeeResult;
        employeeResult = employeeService.getEmployeebyDetail(token);
        logger.info("Completing Get Employee");
        return employeeResult;
    }

    @RequestMapping(value = "/employeeList", method = RequestMethod.GET)
    @ApiOperation(value = "Returns employees detail", notes = "Given a complete list of Employees returns its details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful employees retrieval"),
            @ApiResponse(code = 204, message = "Employees do not exists")}
    )
    public List<Employee> employeeList(
            @RequestParam(value = "roles", required = false) List<Integer> roles,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "supervId", required = false) Integer supervId,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "filterByUser", required = false) Boolean filterByRoleUser,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("employeeList() - Invalid token");
            logger.info("employeeList() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get All Employee, with optional parameters roles.size: {}, status: {}, token {}, email{}", roles.size(), status, token, email);
        List<Employee> employeeResult;
        employeeResult = employeeService.getListEmployee(token,roles,status, supervId, email, phone, filterByRoleUser);
        logger.info("Completing Get All Employees");
        return employeeResult;
    }

}
