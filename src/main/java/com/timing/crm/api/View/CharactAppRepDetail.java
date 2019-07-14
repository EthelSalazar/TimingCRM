package com.timing.crm.api.View;

import java.util.List;

public class CharactAppRepDetail {
    private Integer id;
    private String description;
    private Integer characteristicsAppId;
    private Integer userRepdetailId;

    public CharactAppRepDetail() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCharacteristicsAppId() {
        return characteristicsAppId;
    }

    public void setCharacteristicsAppId(Integer characteristicsAppId) {
        this.characteristicsAppId = characteristicsAppId;
    }

    public Integer getUserRepdetailId() {
        return userRepdetailId;
    }

    public void setUserRepdetailId(Integer userRepdetailId) {
        this.userRepdetailId = userRepdetailId;
    }
}

