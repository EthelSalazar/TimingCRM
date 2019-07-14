package com.timing.crm.api.View.Reports;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.timing.crm.api.Helper.CsvHelper.getRepSummaryFieldMapping;
import static com.timing.crm.api.Helper.CsvHelper.getRepSummaryHeader;

public class CsvViewBuilder extends AbstractCsvView {

    private static final String MODEL = "model";

    @Override
    protected void buildCsvDocument(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ICsvBeanWriter csvBeanWriter;
        if (model.get(MODEL) instanceof RepresentativeSummaryAndDetail) {
            RepresentativeSummaryAndDetail summary = (RepresentativeSummaryAndDetail) model.get(MODEL);
            if (summary!=null) {
                response.setHeader("Content-Disposition", "attachment: filename=\"ReportSummary.csv\"");
                csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference
                            .STANDARD_PREFERENCE);
                csvBeanWriter.writeHeader(getRepSummaryHeader());
                csvBeanWriter.write(summary, getRepSummaryFieldMapping());
                csvBeanWriter.close();
            }
        }

    }


}
