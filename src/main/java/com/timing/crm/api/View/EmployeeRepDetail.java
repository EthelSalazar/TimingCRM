package com.timing.crm.api.View;

public class EmployeeRepDetail {
    private Integer id;
    private Integer userEmployeeId;
    private Integer userTelemarketerContId;
    private Integer userRepdetailId;
    private Integer userSupervisorId;
    private String nameEmployee;
    private String nameTelemarketerCont;
    private String nameRepDetail;
    private String nameSupervisor;
    private Integer companyId;

    //COnstructores


    public EmployeeRepDetail() {
    }

    //Methods


    public Integer getUserSupervisorId() {
        return userSupervisorId;
    }

    public void setUserSupervisorId(Integer userSupervisorId) {
        this.userSupervisorId = userSupervisorId;
    }

    public String getNameSupervisor() {
        return nameSupervisor;
    }

    public void setNameSupervisor(String nameSupervisor) {
        this.nameSupervisor = nameSupervisor;
    }

    public String getNameRepDetail() {
        return nameRepDetail;
    }

    public void setNameRepDetail(String nameRepDetail) {
        this.nameRepDetail = nameRepDetail;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getNameEmployee() {
        return nameEmployee;
    }

    public void setNameEmployee(String nameEmployee) {
        this.nameEmployee = nameEmployee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserEmployeeId() {
        return userEmployeeId;
    }

    public void setUserEmployeeId(Integer userEmployeeId) {
        this.userEmployeeId = userEmployeeId;
    }

    public Integer getUserRepdetailId() {
        return userRepdetailId;
    }

    public void setUserRepdetailId (Integer userRepdetailId) {
        this.userRepdetailId = userRepdetailId;
    }

    public Integer getUserTelemarketerContId() {
        return userTelemarketerContId;
    }

    public void setUserTelemarketerContId(Integer userTelemarketerContId) {
        this.userTelemarketerContId = userTelemarketerContId;
    }

    public String getNameTelemarketerCont() {
        return nameTelemarketerCont;
    }

    public void setNameTelemarketerCont(String nameTelemarketerCont) {
        this.nameTelemarketerCont = nameTelemarketerCont;
    }
}
