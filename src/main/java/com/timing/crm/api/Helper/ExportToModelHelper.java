package com.timing.crm.api.Helper;

public class ExportToModelHelper {

    private static final String CSV_VIEW_BUILDER = "CsvViewBuilder";
    private static final String TEXT_CSV = "text/csv";

    public static String getResolverName(String acceptHeader) {
        String resolverName = null;
        if (acceptHeader.equalsIgnoreCase(TEXT_CSV)) {
            resolverName = CSV_VIEW_BUILDER;
        }
        return resolverName;
    }
}
