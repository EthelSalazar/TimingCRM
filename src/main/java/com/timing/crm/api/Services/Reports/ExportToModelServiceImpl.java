package com.timing.crm.api.Services.Reports;

import com.timing.crm.api.Services.ExportToModelService;
import com.timing.crm.api.View.Reports.RepresentativeSummaryAndDetail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import static com.timing.crm.api.Helper.ExportToModelHelper.getResolverName;

@Service
public class ExportToModelServiceImpl implements ExportToModelService {

    private static final String MODEL = "model";
    private final Logger logger = LoggerFactory.getLogger("ExportToModelServiceImpl");

    @Autowired
    ReportService reportService;


    @Override
    public ModelAndView generateRepresentativeSummaryAndDetail(String acceptHeader, Integer userRepdetailId, String startDate,
            String endDate) {
        ModelAndView report;
        logger.info("Starting generate Excel / CSV Representative Summary Report with userRepdetailId: {}, startDate: "
                + "{}, endDate: {}", userRepdetailId, startDate, endDate);
        RepresentativeSummaryAndDetail representativeSummaryAndDetail =
                reportService.getRepresentativeSummaryAndDetail(userRepdetailId,startDate,endDate);
        String resolver = getResolverName(acceptHeader);
        report = new ModelAndView(resolver, MODEL, representativeSummaryAndDetail);
        logger.info("Completing generate Excel / CSV Representative Summary Report");
        return report;
    }
}
