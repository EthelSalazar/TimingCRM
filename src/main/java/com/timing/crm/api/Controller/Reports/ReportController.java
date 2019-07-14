package com.timing.crm.api.Controller.Reports;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.AuthenticationService;
import com.timing.crm.api.Services.ExportToModelService;
import com.timing.crm.api.Services.Reports.ReportService;
import com.timing.crm.api.View.Reports.FileUploadReport;
import com.timing.crm.api.View.Reports.RepresentativeSummaryAndDetail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/v1/reports")
public class ReportController {

    private static final String INVALID_TOKEN = "Invalid token";
    private final Logger reportsLogger = LoggerFactory.getLogger("ReportController");

    @Autowired
    ReportService reportService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ExportToModelService exportToModelService;

    @RequestMapping(value = "/fileupload", method = RequestMethod.GET)
    @ApiOperation(value = "Returns file upload basic details", notes = "Basic file upload report")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful get report details")}
    )
    public List<FileUploadReport> getFileUploadBasicReport (
            @RequestHeader(value = "token") UUID token,
            @RequestParam(value = "companyId") Integer companyId,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "userEmployeeId", required = false) Integer userEmployeeId,
            @RequestParam(value = "userRepDetailId", required = false) Integer userRepDetailId) {

        if (!authenticationService.isAuthenticated(token)){
            reportsLogger.error("getFileUploadBasicReport() - Invalid token");
            throw new ForbiddenException(INVALID_TOKEN);
        }
        reportsLogger.info("Starting get basic file upload report with companyId: {}, startDate: {}, endDate: {}",
                companyId, startDate, endDate);
        List<FileUploadReport> fileUploadReportList;
        fileUploadReportList = reportService.getBasicFileUploadReport(companyId,startDate,endDate,userEmployeeId,
                userRepDetailId);
        reportsLogger.info("Completing getting basic file upload report");
        return fileUploadReportList;
    }


    @RequestMapping(value = "/representativeSummary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns a representative summary statement", notes = "activity report")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful get report details")}
    )
    public RepresentativeSummaryAndDetail getRepSummary (
            @RequestHeader(value = "token") UUID token,
            @RequestParam(value = "userRepdetailId") Integer userRepdetailId,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate) {

        if (!authenticationService.isAuthenticated(token)){
            reportsLogger.error("getRepSummary() - Invalid token");
            throw new ForbiddenException(INVALID_TOKEN);
        }
        reportsLogger.info("Starting representative summary report with userRepdetailId: {}, startDate: {}, "
                        + "endDate: {}", userRepdetailId, startDate, endDate);
        RepresentativeSummaryAndDetail representativeSummaryAndDetail = reportService.getRepresentativeSummaryAndDetail
                (userRepdetailId,startDate,endDate);
        reportsLogger.info("Completing representative summary report");
        return representativeSummaryAndDetail;
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/representativeSummary", method = RequestMethod.GET)
    @ApiOperation(value = "Returns a representative summary statement report", notes = "csv/excel activity report")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful get csv report details")}
    )
    public ModelAndView getRepSummaryModel (
            @RequestHeader(value = "token") UUID token,
            @RequestHeader(value = "Accept") String acceptHeader,
            @RequestParam(value = "userRepdetailId") Integer userRepdetailId,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate) {

        if (!authenticationService.isAuthenticated(token)){
            reportsLogger.error("CSV getRepSummary() - Invalid token");
            throw new ForbiddenException(INVALID_TOKEN);
        }
        reportsLogger.info("Getting csv representative summary report with acceptHeader: {}, userRepdetailId: {}, "
                + "startDate: {}, endDate: {}", acceptHeader, userRepdetailId, startDate, endDate);
        return exportToModelService.generateRepresentativeSummaryAndDetail(acceptHeader, userRepdetailId, startDate, endDate);
    }


}
