package com.timing.crm.api.View;

public class QuestionRaw {

    private Integer questionId;
    private String questionDescription;
    private Boolean depending;
    private Integer questionDependingId;
    private Integer questionType;
    private String extraLabel;
    private String specialValidation;
    private Integer valueId;
    private String valueDescription;
    private Boolean desqualifying;
    private Integer resultCallId;


    public QuestionRaw() {
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

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public Integer getValueId() {
        return valueId;
    }

    public void setValueId(Integer valueId) {
        this.valueId = valueId;
    }

    public String getValueDescription() {
        return valueDescription;
    }

    public void setValueDescription(String valueDescription) {
        this.valueDescription = valueDescription;
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

    public Boolean getDesqualifying() {
        return desqualifying;
    }

    public void setDesqualifying(Boolean desqualifying) {
        this.desqualifying = desqualifying;
    }

    public Integer getResultCallId() {
        return resultCallId;
    }

    public void setResultCallId(Integer resultCallId) {
        this.resultCallId = resultCallId;
    }
}
