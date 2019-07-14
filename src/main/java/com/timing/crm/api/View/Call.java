package com.timing.crm.api.View;

import java.sql.Timestamp;

public class Call {
    private Integer id;
    private Timestamp callDate;
    private Integer resultCallId;
    private String resultCallDescription;
    private Integer reasonsNotqualifyId;
    private String reasonsNotqualifyDescription;
    private Integer leadId;
    private Integer userRepdetailId;
    private Integer userEmployeeId;
    private Integer companyId;
    private String notes;
    private Appointment appointment;
    private Integer followupAppointmentId;

    //Constructor

    public Call() {
    }

    //Methods

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCallDate() { return callDate; }

    public void setCallDate(Timestamp callDate) { this.callDate = callDate; }

    public Integer getResultCallId() {
        return resultCallId;
    }

    public void setResultCallId(Integer resultCallId) {
        this.resultCallId = resultCallId;
    }

    public String getReasonsNotqualifyDescription() { return reasonsNotqualifyDescription; }

    public void setReasonsNotqualifyDescription(String reasonsNotqualifyDescription) {
        this.reasonsNotqualifyDescription = reasonsNotqualifyDescription;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public String getResultCallDescription() { return resultCallDescription; }

    public void setResultCallDescription(String resultCallDescription) { this.resultCallDescription = resultCallDescription; }

    public Integer getUserRepdetailId() {
        return userRepdetailId;
    }

    public void setUserRepdetailId(Integer userRepdetailId) {
        this.userRepdetailId = userRepdetailId;
    }

    public Integer getUserEmployeeId() {
        return userEmployeeId;
    }

    public void setUserEmployeeId(Integer userEmployeeId) {
        this.userEmployeeId = userEmployeeId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Integer getReasonsNotqualifyId() {
        return reasonsNotqualifyId;
    }

    public void setReasonsNotqualifyId(Integer reasonsNotqualifyId) {
        this.reasonsNotqualifyId = reasonsNotqualifyId;
    }

    public Integer getFollowupAppointmentId() {
        return followupAppointmentId;
    }

    public void setFollowupAppointmentId(Integer followupAppointmentId) {
        this.followupAppointmentId = followupAppointmentId;
    }
}
