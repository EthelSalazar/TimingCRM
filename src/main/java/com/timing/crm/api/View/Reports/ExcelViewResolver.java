package com.timing.crm.api.View.Reports;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class ExcelViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        ExcelViewBuilder excelViewBuilder = new ExcelViewBuilder();
        return excelViewBuilder;

    }
}
