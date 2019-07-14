package com.timing.crm.api.View;

public class Company {
    private Integer id;
    private String name;
    private String phone;
    private String optional_phone;
    private String zip;
    private String city;
    private String country;
    private String email;
    private String optional_email;
    private String notes;
    private String status;
    private String address;
    private String economyc_activity;

    public Company() {
    }

    Company(Integer ID, String NAME, String PHONE, String OPTIONAL_PHONE, String ZIP, String CITY, String COUNTRY, String EMAIL,
            String OPTIONAL_EMAIL, String NOTES, String STATUS, String address, String economyc_activity){
        this.id = ID;
        this.name = NAME;
        this.phone = PHONE;
        this.optional_phone = OPTIONAL_PHONE;
        this.zip = ZIP;
        this.city = CITY;
        this.country = COUNTRY;
        this.email = EMAIL;
        this.optional_email = OPTIONAL_EMAIL;
        this.notes = NOTES;
        this.status = STATUS;
        this.address = address;
        this.economyc_activity = economyc_activity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEconomyc_activity() {
        return economyc_activity;
    }

    public void setEconomyc_activity(String economyc_activity) {
        this.economyc_activity = economyc_activity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getOptional_phone() {
        return optional_phone;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getOptional_email() {
        return optional_email;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOptional_phone(String optional_phone) {
        this.optional_phone = optional_phone;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOptional_email(String optional_email) {
        this.optional_email = optional_email;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
