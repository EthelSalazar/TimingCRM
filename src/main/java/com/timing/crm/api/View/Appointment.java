package com.timing.crm.api.View;

import java.sql.Timestamp;
import java.util.List;

public class Appointment {
    private Integer id;
    private Timestamp dateAppoint;
    private Integer resultAppId;
    private String coordComments;
    private String teleComments;
    private String repdetailComment;
    private Integer userRepdetailId;
    private Integer companyId;
    private Integer userEmployeeId;
    private Integer leadsId;
    private Integer callId;
    private Integer status;
    private String commentSummary;
    private Integer appointmenteParentId;
    private List<QuestionAppointment> questionsAppointment;

    public Appointment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getDateAppoint() {
        return dateAppoint;
    }

    public void setDateAppoint(Timestamp dateAppoint) {
        this.dateAppoint = dateAppoint;
    }

    public Integer getResultAppId() {
        return resultAppId;
    }

    public void setResultAppId(Integer resultAppId) {
        this.resultAppId = resultAppId;
    }

    public String getCoordComments() {
        return coordComments;
    }

    public void setCoordComments(String coordComments) {
        this.coordComments = coordComments;
    }

    public String getTeleComments() {
        return teleComments;
    }

    public void setTeleComments(String teleComments) {
        this.teleComments = teleComments;
    }

    public Integer getUserRepdetailId() {
        return userRepdetailId;
    }

    public void setUserRepdetailId(Integer userRepdetailId) {
        this.userRepdetailId = userRepdetailId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getUserEmployeeId() {
        return userEmployeeId;
    }

    public void setUserEmployeeId(Integer userEmployeeId) {
        this.userEmployeeId = userEmployeeId;
    }

    public Integer getLeadsId() {
        return leadsId;
    }

    public void setLeadsId(Integer leadsId) {
        this.leadsId = leadsId;
    }

    public Integer getCallId() {
        return callId;
    }

    public void setCallId(Integer callId) {
        this.callId = callId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<QuestionAppointment> getQuestionsAppointment() {
        return questionsAppointment;
    }

    public void setQuestionsAppointment(List<QuestionAppointment> questionsAppointment) {
        this.questionsAppointment = questionsAppointment;
    }

    public String getCommentSummary() {
        return commentSummary;
    }

    public void setCommentSummary(String commentSummary) {
        this.commentSummary = commentSummary;
    }

    public Integer getAppointmenteParentId() {
        return appointmenteParentId;
    }

    public void setAppointmenteParentId(Integer appointmenteParentId) {
        this.appointmenteParentId = appointmenteParentId;
    }

    public String getRepdetailComment() {
        return repdetailComment;
    }

    public void setRepdetailComment(String repdetailComment) {
        this.repdetailComment = repdetailComment;
    }
}
