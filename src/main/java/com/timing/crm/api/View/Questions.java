package com.timing.crm.api.View;

import java.util.List;

public class Questions {
    private Integer id;
    private String description;
    private Integer type;
    private Boolean depending;
    private Integer questionDependingId;
    private String extraLabel;
    private String specialValidation;
    private List<QuestionValues> valuesList;

    public Questions() {
    }

    public String getSpecialValidation() {
        return specialValidation;
    }

    public void setSpecialValidation(String specialValidation) {
        this.specialValidation = specialValidation;
    }

    public String getExtraLabel() {
        return extraLabel;
    }

    public void setExtraLabel(String extraLabel) {
        this.extraLabel = extraLabel;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<QuestionValues> getValuesList() {
        return valuesList;
    }

    public void setValuesList(List<QuestionValues> valuesList) {
        this.valuesList = valuesList;
    }

    public Boolean getDepending() {
        return depending;
    }

    public void setDepending(Boolean depending) {
        this.depending = depending;
    }

    public Integer getQuestionDependingId() {
        return questionDependingId;
    }

    public void setQuestionDependingId(Integer questionDependingId) {
        this.questionDependingId = questionDependingId;
    }
}
