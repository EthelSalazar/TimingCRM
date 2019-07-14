package com.timing.crm.api.View;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.UUID;

public class Users {
    private Integer id;
    private String login;
    private String password;
    private Integer roleId;
    private String status;
    private Integer company_id;
    private UUID token;
    private String userName;

    //Constructores

    public Users (){ }

    public Users(Integer id, String login, String password, Integer roleId, String status, Integer company_id, UUID token, String userName) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.roleId = roleId;
        this.status = status;
        this.company_id = company_id;
        this.token = token;
        this.userName = userName;
    }
    //metodos


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }
}
