package com.timing.crm.api.View;

public class QuestionValues {
    private Integer id;
    private String description;
    private Integer questionId;
    private Boolean desqualifying;
    private Integer resultCallId;

    public QuestionValues() {
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

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
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
