package com.timing.crm.api.View.Reports;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class CsvViewResolver implements ViewResolver {

   @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        CsvViewBuilder csvViewBuilder = new CsvViewBuilder();
        return csvViewBuilder;

    }

}
