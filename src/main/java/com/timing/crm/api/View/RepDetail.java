package com.timing.crm.api.View;

import java.util.List;

public class RepDetail {
    private Integer id;
    private String name;
    private String email;
    private String optionalEmail;
    private String phone;
    private String optionalPhone;
    private String zip;
    private String city;
    private String country;
    private String address;
    private Integer repDetailPredecessor;
    private Integer companyId;
    private Integer usersId;
    private String areaTrabajo;
    private String dataFrequency; /*DIARIO-SEMANAL*/
    private Integer dataQtAprox;
    private Boolean cooking;
    private String addressCooking;
    private Boolean reportShared;
    private Boolean reportCall;
    private Boolean reportMessage;
    private String status;
    private List<ApprTypeRepDetail> approachTypeList;
    private List<ScheduleRepDetail> scheduleList;
    private List<CharactAppRepDetail> characteristicsAppList;
    private String cityAppointments;
    private String cityAuthAppointments;


    //Constructores

    public RepDetail() { }


    //methods


    public List<CharactAppRepDetail> getCharacteristicsAppList() {
        return characteristicsAppList;
    }

    public void setCharacteristicsAppList(List<CharactAppRepDetail> characteristicsAppList) {
        this.characteristicsAppList = characteristicsAppList;
    }

    public List<ScheduleRepDetail> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<ScheduleRepDetail> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public String getCityAppointments() {
        return cityAppointments;
    }

    public void setCityAppointments(String cityAppointments) {
        this.cityAppointments = cityAppointments;
    }

    public String getCityAuthAppointments() {
        return cityAuthAppointments;
    }

    public void setCityAuthAppointments(String cityAuthAppointments) {
        this.cityAuthAppointments = cityAuthAppointments;
    }

    public List<ApprTypeRepDetail> getApproachTypeList() {
        return approachTypeList;
    }

    public void setApproachTypeList(List<ApprTypeRepDetail> approachTypeList) {
        this.approachTypeList = approachTypeList;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOptionalEmail(String optionalEmail) {
        this.optionalEmail = optionalEmail;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOptionalPhone(String optionalPhone) {
        this.optionalPhone = optionalPhone;
    }

    public void setRepDetailPredecessor(Integer repDetailPredecessor) {
        this.repDetailPredecessor = repDetailPredecessor;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public void setUsersId(Integer usersId) {
        this.usersId = usersId;
    }

    public void setAreaTrabajo(String areaTrabajo) {
        this.areaTrabajo = areaTrabajo;
    }

    public void setDataFrequency(String dataFrequency) {
        this.dataFrequency = dataFrequency;
    }

    public void setDataQtAprox(Integer dataQtAprox) {
        this.dataQtAprox = dataQtAprox;
    }

    public void setCooking(Boolean cooking) {
        this.cooking = cooking;
    }

    public void setAddressCooking(String addressCooking) {
        this.addressCooking = addressCooking;
    }

    public void setReportShared(Boolean reportShared) {
        this.reportShared = reportShared;
    }

    public void setReportCall(Boolean reportCall) {
        this.reportCall = reportCall;
    }

    public void setReportMessage(Boolean reportMessage) {
        this.reportMessage = reportMessage;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getOptionalEmail() {
        return optionalEmail;
    }

    public String getPhone() {
        return phone;
    }

    public String getOptionalPhone() {
        return optionalPhone;
    }

    public Integer getRepDetailPredecessor() {
        return repDetailPredecessor;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public Integer getUsersId() {
        return usersId;
    }

    public String getAreaTrabajo() {
        return areaTrabajo;
    }

    public String getDataFrequency() {
        return dataFrequency;
    }

    public Integer getDataQtAprox() {
        return dataQtAprox;
    }

    public Boolean getCooking() {
        return cooking;
    }

    public String getAddressCooking() {
        return addressCooking;
    }

    public Boolean getReportShared() {
        return reportShared;
    }

    public Boolean getReportCall() {
        return reportCall;
    }

    public Boolean getReportMessage() {
        return reportMessage;
    }

    public String getStatus() {
        return status;
    }

}



