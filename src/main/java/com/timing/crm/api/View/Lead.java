package com.timing.crm.api.View;

public class Lead {

    private Integer id;
    private String dateData; // not null
    private String rating;
    private String name;  // not null
    private String maritalStatus;
    private String spouseName;
    private String phone; // not null,
    private String zip;  // not null,
    private String city;  // not null,
    private String address;
    private Integer status;
    private String approachType;  // not null,
    private String prospectCategory; // not null,
    private String approachTypeDescription;  // not null,
    private String prospectCategoryDescription;
    private String approachPlace;
    private String approachNotes;
    private String approachNotes414;
    private Boolean host;
    private Integer hostId;
    private String hostName;
    private Integer companyId;
    private Integer userRepdetailId;
    private Integer userEmployeeId;
    private String createdOn;
    private String updatedOn;
    private String nameRepDetail;

    public Lead() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateData() {
        return dateData;
    }

    public void setDateData(String dateData) {
        this.dateData = dateData;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getApproachType() {
        return approachType;
    }

    public void setApproachType(String approachType) {
        this.approachType = approachType;
    }

    public String getProspectCategory() {
        return prospectCategory;
    }

    public void setProspectCategory(String prospectCategory) {
        this.prospectCategory = prospectCategory;
    }

    public String getApproachPlace() {
        return approachPlace;
    }

    public void setApproachPlace(String approachPlace) {
        this.approachPlace = approachPlace;
    }

    public String getApproachNotes() {
        return approachNotes;
    }

    public void setApproachNotes(String approachNotes) {
        this.approachNotes = approachNotes;
    }

    public String getApproachNotes414() {
        return approachNotes414;
    }

    public void setApproachNotes414(String approachNotes414) {
        this.approachNotes414 = approachNotes414;
    }

    public Boolean getHost() {
        return host;
    }

    public void setHost(Boolean host) {
        this.host = host;
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

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

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getNameRepDetail() {
        return nameRepDetail;
    }

    public void setNameRepDetail(String nameRepDetail) {
        this.nameRepDetail = nameRepDetail;
    }


    public String getApproachTypeDescription() {
        return approachTypeDescription;
    }

    public void setApproachTypeDescription(String approachTypeDescription) {
        this.approachTypeDescription = approachTypeDescription;
    }

    public String getProspectCategoryDescription() {
        return prospectCategoryDescription;
    }

    public void setProspectCategoryDescription(String prospectCategoryDescription) {
        this.prospectCategoryDescription = prospectCategoryDescription;
    }
}

