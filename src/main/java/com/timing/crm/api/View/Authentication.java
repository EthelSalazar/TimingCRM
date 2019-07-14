package com.timing.crm.api.View;

import java.sql.Timestamp;
import java.util.UUID;

public class Authentication {
    private Integer id;
    private String login;
    private UUID token;
    private Timestamp expirationTime;
    private Integer userId;

    //CONSTRUCTORES
    public Authentication(Integer id, String login, UUID token, Timestamp expirationTime) {
        this.id = id;
        this.login = login;
        this.token = token;
        this.expirationTime = expirationTime;
    }


    public Authentication() { }

    //METODOS


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

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Timestamp getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Timestamp expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
