package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.CompanyService;
import com.timing.crm.api.View.Company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(value = "/v1")
public class CompanyController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger("CompanyController");

    @Autowired
    private CompanyService companyService;


    @RequestMapping(value = "/company", method = RequestMethod.POST)
    @ApiOperation(value = "Create a new company", notes = "Returns company details created")
    public Company createNewCompany(
            @Valid @RequestBody Company company) {

        logger.info("Starting Create a New Company with Name: {}", company.getName());
        return companyService.createCompany(company);
    }

    @RequestMapping(value = "/company/{COMPANY_ID}", method = RequestMethod.PUT)
    @ApiOperation(value = "Update a company", notes = "Returns company details updated")
    public Company modifyCompany(
            @PathVariable("COMPANY_ID") Integer companyId,
            @Valid @RequestBody Company company,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("modifyCompany() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Update Company with id: {}, name: {}", companyId, company.getName());
        company.setId(companyId);
        return companyService.modifyCompany(company);
    }


    @RequestMapping(value = "/company", method = RequestMethod.GET)
    @ApiOperation(value = "Returns companies details", notes = "Returns a complete list of companies details")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful Companies retrieval"),
        @ApiResponse(code = 204, message = "Company does not exists")}
    )
    public List<Company> companiesSelect(
            @ApiParam(value = "Company name (optional)")
            @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "Company email (optional)")
            @RequestParam(value = "email", required = false) String email) {

        logger.info("Starting Get All Companies, with optional parameters name: {}, email: {}", name, email);
        List<Company> companyResult;
        companyResult = companyService.getAllCompany(name, email);
        logger.info("Completing Get All Companies");
        return companyResult;
    }


    @RequestMapping(value = "/company/{COMPANY_ID}", method = RequestMethod.GET)
    @ApiOperation(value = "Returns company's detail", notes = "Given a Company Id returns its details")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful Company retrieval"),
        @ApiResponse(code = 204, message = "Company does not exists")}
    )
    public Company companySelect(
            @PathVariable("COMPANY_ID") Integer companyId,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("companySelect() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get Company with id: {}", companyId);
        Company companyResult;
        companyResult = companyService.getCompanyById(companyId);
        logger.info("Completing Get Company");
        return companyResult;
    }
}
