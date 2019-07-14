package com.timing.crm.api.Services;

import org.springframework.web.servlet.ModelAndView;

public interface ExportToModelService {

    ModelAndView generateRepresentativeSummaryAndDetail(String acceptHeader, Integer userRepdetailId, String startDate,
            String endDate);
}
