package com.timing.crm.api.View;

public class ApprTypeRepDetail {
    private Integer id;
    private Integer approachTypeId;
    private Integer userRepDetailId;
    private String description;

    public ApprTypeRepDetail() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApproachTypeId() {
        return approachTypeId;
    }

    public void setApproachTypeId(Integer approachTypeId) {
        this.approachTypeId = approachTypeId;
    }

    public Integer getUserRepDetailId() {
        return userRepDetailId;
    }

    public void setUserRepDetailId(Integer userRepDetailId) {
        this.userRepDetailId = userRepDetailId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
