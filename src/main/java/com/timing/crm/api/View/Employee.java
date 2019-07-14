package com.timing.crm.api.View;

import java.sql.Timestamp;

public class Employee {
        private Integer id;
        private String name;
        private String phone;
        private String optional_phone;
        private String zip;
        private String city;
        private String country;
        private String address;
        private String email;
        private String optional_email;
        private String notes;
        private String status;
        private Integer companyId;
        private Integer usersId;
        private Timestamp createdOn;
        private Timestamp updatedOn;
        private Integer userSupervisorId;
        private String userSupervisorName;


        //Constructores
        public Employee(){
        }

        //Methods
    public String getUserSupervisorName() {
        return userSupervisorName;
    }

    public void setUserSupervisorName(String userSupervisorName) {
        this.userSupervisorName = userSupervisorName;
    }

    public Integer getUserSupervisorId() {
        return userSupervisorId;
    }

    public void setUserSupervisorId(Integer userSupervisorId) {
        this.userSupervisorId = userSupervisorId;
    }

    public static void main(String[] args){
            System.out.print("employee");
        }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    public Integer getUsersId() {
        return usersId;
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

        public int getCompanyId() {
            return companyId;
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

        public void setCompanyId(Integer company_id) {
            this.companyId = company_id;
        }

        public void setUsersId(Integer usersId) {
        this.usersId = usersId;
        }
}
