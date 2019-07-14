package com.timing.crm.api.View;

public class QuestionAppointment {
    private Integer id;
    private String answer;
    private Integer questionId;
    private String questionDescription;
    private Integer appointmentId;

    public QuestionAppointment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionDescription() { return questionDescription;  }

    public void setQuestionDescription(String questionDescription) { this.questionDescription = questionDescription; }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }
}
