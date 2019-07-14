package com.timing.crm.api.Services.Reports;

import com.timing.crm.api.View.Reports.FileUploadReport;
import com.timing.crm.api.View.Reports.RepresentativeSummaryAndDetail;

import java.util.List;

public interface ReportService {

     List<FileUploadReport> getBasicFileUploadReport(Integer companyId, String startDate, String endDate,
            Integer userEmployeeId, Integer userRepDetailId);

     RepresentativeSummaryAndDetail getRepresentativeSummaryAndDetail(Integer userRepdetailId, String startDate, String
             endDate);

}
