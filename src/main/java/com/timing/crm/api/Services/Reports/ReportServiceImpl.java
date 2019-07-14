package com.timing.crm.api.Services.Reports;

import com.timing.crm.api.Repository.LeadRepository;
import com.timing.crm.api.Repository.RepDetailRepository;
import com.timing.crm.api.View.CallRegister;
import com.timing.crm.api.View.RepDetail;
import com.timing.crm.api.View.Reports.FileUploadReport;
import com.timing.crm.api.View.Reports.RepresentativeSummary;
import com.timing.crm.api.View.Reports.RepresentativeSummaryAndDetail;
import com.timing.crm.api.View.Reports.StatusCount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.timing.crm.api.Helper.ReportHelper.summaryReportMapper;

@Service
public class ReportServiceImpl implements ReportService {

    private final Logger logger = LoggerFactory.getLogger("ReportServiceImpl");

    @Autowired
    LeadRepository leadRepository;

    @Autowired
    RepDetailRepository repDetailRepository;

    @Override
    public List<FileUploadReport> getBasicFileUploadReport(Integer companyId, String startDate, String endDate,
            Integer userEmployeeId, Integer userRepDetailId) {
        logger.info("Starting getBasicFileUploadReport with companyId: {}", companyId);
        return leadRepository.getBasicFileUploadReport(companyId, startDate, endDate, userEmployeeId, userRepDetailId);
    }


    @Override
    public RepresentativeSummaryAndDetail getRepresentativeSummaryAndDetail(Integer userRepdetailId, String startDate, String endDate) {
        logger.info("Starting getRepresentativeSummary with userRepdetailId: {}, startDate: {}, endDate: {}",
                userRepdetailId, startDate, endDate);
        RepresentativeSummaryAndDetail representativeSummaryAndDetail = new RepresentativeSummaryAndDetail();
        RepresentativeSummary representativeSummary = new RepresentativeSummary();
        List<CallRegister> callRegisterList;
        List<StatusCount> statusCountList;
        RepDetail repDetail;
        Integer totalLeads;
        statusCountList = leadRepository.getSummaryStatusCallByRepAndDate(userRepdetailId, startDate, endDate);
        callRegisterList = leadRepository.getRepresentativeSummaryAndDetail(userRepdetailId, startDate, endDate);
        repDetail = repDetailRepository.getRepDetailByUserId(userRepdetailId);
        totalLeads = leadRepository.getTotalLeadByRepAndDateRange(userRepdetailId, startDate, endDate);
        representativeSummary.setRepresentativeName(repDetail.getName());
        representativeSummary.setTotalLeads(totalLeads);
        representativeSummary.setDataStartDate(startDate);
        representativeSummary.setDataEndDate(endDate);
        representativeSummaryAndDetail.setRepresentativeSummary(representativeSummary);
        representativeSummaryAndDetail.setCallRegisterList(callRegisterList);
        logger.info("Completing getRepresentativeSummary with userRepdetailId: {}", userRepdetailId);
        return summaryReportMapper(representativeSummaryAndDetail, statusCountList, totalLeads);
    }
}
