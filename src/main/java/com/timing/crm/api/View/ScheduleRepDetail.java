package com.timing.crm.api.View;

public class ScheduleRepDetail {
    private Integer id;
    private Integer scheduleId;
    private Integer userRepDetailId;
    private Boolean selected;
    private String day;
    private String hour;

    public ScheduleRepDetail() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getUserRepDetailId() {
        return userRepDetailId;
    }

    public void setUserRepDetailId(Integer userRepDetailId) {
        this.userRepDetailId = userRepDetailId;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
