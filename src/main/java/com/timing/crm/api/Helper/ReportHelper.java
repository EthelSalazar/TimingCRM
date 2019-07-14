package com.timing.crm.api.Helper;

import com.timing.crm.api.View.Reports.RepresentativeSummaryAndDetail;
import com.timing.crm.api.View.Reports.StatusCount;

import java.util.List;

import static com.timing.crm.api.Utils.Constants.CITA;
import static com.timing.crm.api.Utils.Constants.ESTA_MOLESTO_CON_RP;
import static com.timing.crm.api.Utils.Constants.LLAMAR_LUEGO;
import static com.timing.crm.api.Utils.Constants.NOCONTESTVB;
import static com.timing.crm.api.Utils.Constants.NO_CALIFICA_PARA_DEMO;
import static com.timing.crm.api.Utils.Constants.NUMERO_NO_EXISTE;
import static com.timing.crm.api.Utils.Constants.TIENE_REPRESENTANTE_RP;

public class ReportHelper {

    public static RepresentativeSummaryAndDetail summaryReportMapper(RepresentativeSummaryAndDetail representativeSummaryAndDetail, List<StatusCount>
            statusCountList, Integer totalLeads) {
        representativeSummaryAndDetail.getRepresentativeSummary().setTotalAppointments(0);
        representativeSummaryAndDetail.getRepresentativeSummary().setNoAnswer(0);
        representativeSummaryAndDetail.getRepresentativeSummary().setCallLater(0);
        representativeSummaryAndDetail.getRepresentativeSummary().setNoDemo(0);
        representativeSummaryAndDetail.getRepresentativeSummary().setWrongNumber(0);
        representativeSummaryAndDetail.getRepresentativeSummary().setHasRepRP(0);
        representativeSummaryAndDetail.getRepresentativeSummary().setIsUpset(0);
        for (StatusCount s: statusCountList) {

          if (CITA == s.getStatusCall()) {
              representativeSummaryAndDetail.getRepresentativeSummary().setTotalAppointments(s.getTotal());
          } else if (NOCONTESTVB == s.getStatusCall()) {
              representativeSummaryAndDetail.getRepresentativeSummary().setNoAnswer(s.getTotal());
          } else if (LLAMAR_LUEGO == s.getStatusCall()) {
              representativeSummaryAndDetail.getRepresentativeSummary().setCallLater(s.getTotal());
          } else if (NO_CALIFICA_PARA_DEMO == s.getStatusCall()) {
              representativeSummaryAndDetail.getRepresentativeSummary().setNoDemo(s.getTotal());
          } else if (TIENE_REPRESENTANTE_RP == s.getStatusCall()) {
              representativeSummaryAndDetail.getRepresentativeSummary().setHasRepRP(s.getTotal());
          } else if (ESTA_MOLESTO_CON_RP == s.getStatusCall()) {
              representativeSummaryAndDetail.getRepresentativeSummary().setIsUpset(s.getTotal());
          } else if (NUMERO_NO_EXISTE == s.getStatusCall()) {
              representativeSummaryAndDetail.getRepresentativeSummary().setWrongNumber(s.getTotal());
          }

        }
        representativeSummaryAndDetail.getRepresentativeSummary().setPendingLeads(totalLeads-(representativeSummaryAndDetail.getRepresentativeSummary().getTotalAppointments()
                +representativeSummaryAndDetail.getRepresentativeSummary().getNoAnswer()+representativeSummaryAndDetail.getRepresentativeSummary().getCallLater()
                +representativeSummaryAndDetail.getRepresentativeSummary().getNoDemo()+representativeSummaryAndDetail.getRepresentativeSummary().getWrongNumber()
                +representativeSummaryAndDetail.getRepresentativeSummary().getHasRepRP()+representativeSummaryAndDetail.getRepresentativeSummary().getIsUpset()));
        return representativeSummaryAndDetail;

    }
}