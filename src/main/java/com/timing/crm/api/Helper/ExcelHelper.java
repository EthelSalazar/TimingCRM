package com.timing.crm.api.Helper;

import com.timing.crm.api.View.Appointment;
import com.timing.crm.api.View.Call;
import com.timing.crm.api.View.CallRegister;
import com.timing.crm.api.View.Lead;
import com.timing.crm.api.View.Reports.RepresentativeSummaryAndDetail;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.timing.crm.api.Utils.Constants.CALL_LATER;
import static com.timing.crm.api.Utils.Constants.CIUDAD;
import static com.timing.crm.api.Utils.Constants.DIRECCION;
import static com.timing.crm.api.Utils.Constants.END_DATE;
import static com.timing.crm.api.Utils.Constants.ESTADO_CIVIL;
import static com.timing.crm.api.Utils.Constants.ESTRELLAS;
import static com.timing.crm.api.Utils.Constants.FECHA_CITA;
import static com.timing.crm.api.Utils.Constants.FECHA_DATA;
import static com.timing.crm.api.Utils.Constants.FECHA_HORA_LLAMADA;
import static com.timing.crm.api.Utils.Constants.HAS_REP_RP;
import static com.timing.crm.api.Utils.Constants.IS_UPSET;
import static com.timing.crm.api.Utils.Constants.LUGAR_ABORDAJE;
import static com.timing.crm.api.Utils.Constants.NOMBRE;
import static com.timing.crm.api.Utils.Constants.NOTAS_COORDINADOR;
import static com.timing.crm.api.Utils.Constants.NOTAS_OPERADOR;
import static com.timing.crm.api.Utils.Constants.NOTA_ABORDAJE;
import static com.timing.crm.api.Utils.Constants.NOTA_ABORDAJE_4_14;
import static com.timing.crm.api.Utils.Constants.NO_ANSWER;
import static com.timing.crm.api.Utils.Constants.NO_DEMO;
import static com.timing.crm.api.Utils.Constants.OTROS_COMENTARIOS;
import static com.timing.crm.api.Utils.Constants.PENDING_TO_CALL;
import static com.timing.crm.api.Utils.Constants.REP_NAME;
import static com.timing.crm.api.Utils.Constants.START_DATE;
import static com.timing.crm.api.Utils.Constants.TELEFONO_1;
import static com.timing.crm.api.Utils.Constants.TIPO_PROSPECCION;
import static com.timing.crm.api.Utils.Constants.TOTAL_APPOINTMENTS;
import static com.timing.crm.api.Utils.Constants.TOTAL_LEADS;
import static com.timing.crm.api.Utils.Constants.VENDEDOR;
import static com.timing.crm.api.Utils.Constants.WRONG_NUMBER;
import static com.timing.crm.api.Utils.Constants.ZIP_CODE;
import static com.timing.crm.api.Utils.Constants.STATUS;

public class ExcelHelper {

    public static Row writeRepresentativeSummaryContent(Sheet sheet, RepresentativeSummaryAndDetail summaryAndDetail,
            Workbook workbook) {


        HSSFFont headerFont = (HSSFFont) workbook.createFont();
        HSSFCellStyle headerStyle = (HSSFCellStyle) workbook.createCellStyle();

        // create header rows for totals
        Row representativeSummaryHeader = sheet.createRow(0);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        representativeSummaryHeader.createCell(0).setCellStyle(headerStyle);
        representativeSummaryHeader.getCell(0).setCellValue(REP_NAME);
        representativeSummaryHeader.createCell(1).setCellStyle(headerStyle);
        representativeSummaryHeader.getCell(1).setCellValue(summaryAndDetail.getRepresentativeSummary()
                .getRepresentativeName().toUpperCase());

        HSSFFont headerFont2 = (HSSFFont) workbook.createFont();
        HSSFCellStyle headerStyle2 = (HSSFCellStyle) workbook.createCellStyle();
        headerFont2.setBold(false);
        headerStyle2.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        headerStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle2.setFont(headerFont2);
        representativeSummaryHeader = sheet.createRow(1);
        representativeSummaryHeader.createCell(0).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(0).setCellValue(START_DATE);
        representativeSummaryHeader.createCell(1).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(1).setCellValue(summaryAndDetail.getRepresentativeSummary()
                .getDataStartDate());

        representativeSummaryHeader = sheet.createRow(2);
        representativeSummaryHeader.createCell(0).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(0).setCellValue(END_DATE);
        representativeSummaryHeader.createCell(1).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(1).setCellValue(summaryAndDetail.getRepresentativeSummary()
                .getDataEndDate());

        representativeSummaryHeader = sheet.createRow(3);
        representativeSummaryHeader.createCell(0).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(0).setCellValue(TOTAL_LEADS);
        representativeSummaryHeader.createCell(1).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(1).setCellValue(summaryAndDetail.getRepresentativeSummary()
                .getTotalLeads());

        representativeSummaryHeader = sheet.createRow(4);
        representativeSummaryHeader.createCell(0).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(0).setCellValue(TOTAL_APPOINTMENTS);
        representativeSummaryHeader.createCell(1).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(1).setCellValue(summaryAndDetail.getRepresentativeSummary()
                .getTotalAppointments());

        representativeSummaryHeader = sheet.createRow(5);
        representativeSummaryHeader.createCell(0).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(0).setCellValue(NO_ANSWER);
        representativeSummaryHeader.createCell(1).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(1).setCellValue(summaryAndDetail.getRepresentativeSummary().getNoAnswer());

        representativeSummaryHeader = sheet.createRow(6);
        representativeSummaryHeader.createCell(0).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(0).setCellValue(CALL_LATER);
        representativeSummaryHeader.createCell(1).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(1).setCellValue(summaryAndDetail.getRepresentativeSummary().getCallLater());

        representativeSummaryHeader = sheet.createRow(7);
        representativeSummaryHeader.createCell(0).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(0).setCellValue(WRONG_NUMBER);
        representativeSummaryHeader.createCell(1).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(1).setCellValue(summaryAndDetail.getRepresentativeSummary()
                .getWrongNumber());

        representativeSummaryHeader = sheet.createRow(8);
        representativeSummaryHeader.createCell(0).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(0).setCellValue(NO_DEMO);
        representativeSummaryHeader.createCell(1).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(1).setCellValue(summaryAndDetail.getRepresentativeSummary().getNoDemo());

        representativeSummaryHeader = sheet.createRow(9);
        representativeSummaryHeader.createCell(0).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(0).setCellValue(HAS_REP_RP);
        representativeSummaryHeader.createCell(1).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(1).setCellValue(summaryAndDetail.getRepresentativeSummary().getHasRepRP());

        representativeSummaryHeader = sheet.createRow(10);
        representativeSummaryHeader.createCell(0).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(0).setCellValue(IS_UPSET);
        representativeSummaryHeader.createCell(1).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(1).setCellValue(summaryAndDetail.getRepresentativeSummary().getIsUpset());

        representativeSummaryHeader = sheet.createRow(11);
        representativeSummaryHeader.createCell(0).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(0).setCellValue(PENDING_TO_CALL);
        representativeSummaryHeader.createCell(1).setCellStyle(headerStyle2);
        representativeSummaryHeader.getCell(1).setCellValue(summaryAndDetail.getRepresentativeSummary().getPendingLeads());

        return representativeSummaryHeader;
    }


    public static Row writeRepresentativeDetailContent(Sheet sheet, RepresentativeSummaryAndDetail details,
            Workbook workbook) {


        HSSFFont contentFont = (HSSFFont) workbook.createFont();
        contentFont.setBold(true);
        HSSFCellStyle contentStyle = (HSSFCellStyle) workbook.createCellStyle();
        contentStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.AQUA.getIndex());
        contentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        contentStyle.setFont(contentFont);

        List<CallRegister> callRegisterList;
        Call call = new Call();
        Appointment appointment = new Appointment();
        Lead lead = new Lead();
        Row representativeDetail = sheet.createRow(12);
        representativeDetail.createCell(0).setCellStyle(contentStyle);
        representativeDetail.getCell(0).setCellValue(FECHA_DATA);
        representativeDetail.createCell(1).setCellStyle(contentStyle);
        representativeDetail.getCell(1).setCellValue(ESTRELLAS);
        representativeDetail.createCell(2).setCellStyle(contentStyle);
        representativeDetail.getCell(2).setCellValue(NOMBRE);
        representativeDetail.createCell(3).setCellStyle(contentStyle);
        representativeDetail.getCell(3).setCellValue(ESTADO_CIVIL);
        representativeDetail.createCell(4).setCellStyle(contentStyle);
        representativeDetail.getCell(4).setCellValue(TELEFONO_1);
        representativeDetail.createCell(5).setCellStyle(contentStyle);
        representativeDetail.getCell(5).setCellValue(ZIP_CODE);
        representativeDetail.createCell(6).setCellStyle(contentStyle);
        representativeDetail.getCell(6).setCellValue(CIUDAD);
        representativeDetail.createCell(7).setCellStyle(contentStyle);
        representativeDetail.getCell(7).setCellValue(VENDEDOR);
        representativeDetail.createCell(8).setCellStyle(contentStyle);
        representativeDetail.getCell(8).setCellValue(TIPO_PROSPECCION);
        representativeDetail.createCell(9).setCellStyle(contentStyle);
        representativeDetail.getCell(9).setCellValue(LUGAR_ABORDAJE);
        representativeDetail.createCell(10).setCellStyle(contentStyle);
        representativeDetail.getCell(10).setCellValue(NOTA_ABORDAJE);
        representativeDetail.createCell(11).setCellStyle(contentStyle);
        representativeDetail.getCell(11).setCellValue(OTROS_COMENTARIOS);
        representativeDetail.createCell(12).setCellStyle(contentStyle);
        representativeDetail.getCell(12).setCellValue(NOTA_ABORDAJE_4_14);
        representativeDetail.createCell(13).setCellStyle(contentStyle);
        representativeDetail.getCell(13).setCellValue(DIRECCION);
        representativeDetail.createCell(14).setCellStyle(contentStyle);
        representativeDetail.getCell(14).setCellValue(STATUS);
        representativeDetail.createCell(15).setCellStyle(contentStyle);
        representativeDetail.getCell(15).setCellValue(FECHA_CITA);
        representativeDetail.createCell(16).setCellStyle(contentStyle);
        representativeDetail.getCell(16).setCellValue(FECHA_HORA_LLAMADA);
        representativeDetail.createCell(17).setCellStyle(contentStyle);
        representativeDetail.getCell(17).setCellValue(NOTAS_OPERADOR);
        representativeDetail.createCell(18).setCellStyle(contentStyle);
        representativeDetail.getCell(18).setCellValue(NOTAS_COORDINADOR);

        if (details!=null) {
            callRegisterList = details.getCallRegisterList();
            if (!CollectionUtils.isEmpty(callRegisterList)) {
                int rowNum = 13;
                for (CallRegister callRegister: callRegisterList) {
                    call = callRegister.getCall();
                    appointment = call.getAppointment();
                    lead = callRegister.getLead();
                    // create a new detail row
                    representativeDetail = sheet.createRow(rowNum++);

                    representativeDetail.createCell(0).setCellValue(lead.getDateData());
                    representativeDetail.createCell(1).setCellValue(lead.getRating());
                    representativeDetail.createCell(2).setCellValue(lead.getName());
                    representativeDetail.createCell(3).setCellValue(lead.getMaritalStatus());
                    representativeDetail.createCell(4).setCellValue(lead.getPhone());
                    representativeDetail.createCell(5).setCellValue(lead.getZip());
                    representativeDetail.createCell(6).setCellValue(lead.getCity());
                    representativeDetail.createCell(7).setCellValue(lead.getNameRepDetail());
                    representativeDetail.createCell(8).setCellValue(lead.getProspectCategoryDescription());
                    representativeDetail.createCell(9).setCellValue(lead.getApproachPlace());
                    representativeDetail.createCell(10).setCellValue(lead.getApproachNotes());
                    representativeDetail.createCell(11).setCellValue(call.getNotes());
                    representativeDetail.createCell(12).setCellValue(lead.getApproachNotes414());
                    representativeDetail.createCell(13).setCellValue(lead.getAddress());
                    representativeDetail.createCell(14).setCellValue(call.getResultCallDescription());
                    if (appointment!=null && appointment.getId()!=0) {
                        representativeDetail.createCell(15).setCellValue(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                                .format(appointment.getDateAppoint()));
                        representativeDetail.createCell(16).setCellValue(appointment.getTeleComments());
                        representativeDetail.createCell(17).setCellValue(appointment.getCoordComments());
                    } else {
                        representativeDetail.createCell(15).setCellValue("");
                        representativeDetail.createCell(16).setCellValue("");
                        representativeDetail.createCell(17).setCellValue("");
                    }
                }
            }
        }

        return representativeDetail;
    }


}
