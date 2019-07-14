package com.timing.crm.api.Helper;

import static com.timing.crm.api.Utils.Constants.CALL_LATER;
import static com.timing.crm.api.Utils.Constants.END_DATE;
import static com.timing.crm.api.Utils.Constants.HAS_REP_RP;
import static com.timing.crm.api.Utils.Constants.IS_UPSET;
import static com.timing.crm.api.Utils.Constants.NO_ANSWER;
import static com.timing.crm.api.Utils.Constants.NO_DEMO;
import static com.timing.crm.api.Utils.Constants.PENDING_TO_CALL;
import static com.timing.crm.api.Utils.Constants.REP_NAME;
import static com.timing.crm.api.Utils.Constants.START_DATE;
import static com.timing.crm.api.Utils.Constants.TOTAL_APPOINTMENTS;
import static com.timing.crm.api.Utils.Constants.TOTAL_LEADS;
import static com.timing.crm.api.Utils.Constants.WRONG_NUMBER;

public class CsvHelper {

    // Header for Representative Summary Report
    private static final String[] repSummaryHeader = {REP_NAME, START_DATE, END_DATE, TOTAL_LEADS,
            TOTAL_APPOINTMENTS, NO_ANSWER, CALL_LATER, WRONG_NUMBER, NO_DEMO, HAS_REP_RP,
            IS_UPSET, PENDING_TO_CALL};

    // Field names for Representative Summary Report - from RepresentativeSummaryAndDetail class
    private static final String[] repSummaryFields = {"representativeName", "dataStartDate", "dataEndDate",
            "totalLeads", "totalAppointments", "noAnswer", "callLater", "wrongNumber", "noDemo", "hasRepRP",
            "isUpset", "pendingLeads"};

    public static String[] getRepSummaryHeader() {
        return repSummaryHeader.clone();
    }

    public static String[] getRepSummaryFieldMapping() {
        return repSummaryFields.clone();
    }
}
