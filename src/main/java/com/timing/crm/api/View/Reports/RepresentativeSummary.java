package com.timing.crm.api.View.Reports;

public class RepresentativeSummary {

    private String representativeName;
    private String dataStartDate;
    private String dataEndDate;
    private Integer totalLeads;
    private Integer totalAppointments;
    private Integer noAnswer;
    private Integer callLater;
    private Integer wrongNumber;
    private Integer noDemo;
    private Integer hasRepRP;
    private Integer isUpset;
    private Integer pendingLeads;

    public RepresentativeSummary() {
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getDataStartDate() {
        return dataStartDate;
    }

    public void setDataStartDate(String dataStartDate) {
        this.dataStartDate = dataStartDate;
    }

    public String getDataEndDate() {
        return dataEndDate;
    }

    public void setDataEndDate(String dataEndDate) {
        this.dataEndDate = dataEndDate;
    }

    public Integer getTotalLeads() {
        return totalLeads;
    }

    public void setTotalLeads(Integer totalLeads) {
        this.totalLeads = totalLeads;
    }

    public Integer getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(Integer totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public Integer getNoAnswer() {
        return noAnswer;
    }

    public void setNoAnswer(Integer noAnswer) {
        this.noAnswer = noAnswer;
    }

    public Integer getCallLater() {
        return callLater;
    }

    public void setCallLater(Integer callLater) {
        this.callLater = callLater;
    }

    public Integer getWrongNumber() {
        return wrongNumber;
    }

    public void setWrongNumber(Integer wrongNumber) {
        this.wrongNumber = wrongNumber;
    }

    public Integer getNoDemo() {
        return noDemo;
    }

    public void setNoDemo(Integer noDemo) {
        this.noDemo = noDemo;
    }

    public Integer getHasRepRP() {
        return hasRepRP;
    }

    public void setHasRepRP(Integer hasRepRP) {
        this.hasRepRP = hasRepRP;
    }

    public Integer getIsUpset() {
        return isUpset;
    }

    public void setIsUpset(Integer isUpset) {
        this.isUpset = isUpset;
    }

    public Integer getPendingLeads() {
        return pendingLeads;
    }

    public void setPendingLeads(Integer pendingLeads) {
        this.pendingLeads = pendingLeads;
    }
}
