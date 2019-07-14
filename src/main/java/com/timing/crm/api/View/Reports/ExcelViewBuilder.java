package com.timing.crm.api.View.Reports;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.timing.crm.api.Helper.ExcelHelper.writeRepresentativeDetailContent;
import static com.timing.crm.api.Helper.ExcelHelper.writeRepresentativeSummaryContent;

public class ExcelViewBuilder extends AbstractXlsView {

    private static final String MODEL = "model";

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws Exception {

            if (map.get(MODEL) instanceof RepresentativeSummaryAndDetail) {
                httpServletResponse.setHeader("Content-Disposition", "attachment: filename=\"ReportSummary.xls\"");
                Sheet sheet = workbook.createSheet("Summary");
                sheet.setDefaultColumnWidth(28);

                writeRepresentativeSummaryContent(sheet, (RepresentativeSummaryAndDetail) map.get(MODEL), workbook);
                writeRepresentativeDetailContent(sheet, (RepresentativeSummaryAndDetail) map.get(MODEL), workbook);
            }
    }
}
