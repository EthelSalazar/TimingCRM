package com.timing.crm.api.View;

public class UserAndCompany {

    private Integer userId;
    private Integer companyId;
    private Integer roleId;

    public UserAndCompany() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getRoleId() { return roleId; }

    public void setRoleId(Integer rolId) { this.roleId = rolId; }
}
