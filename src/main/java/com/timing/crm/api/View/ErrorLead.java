package com.timing.crm.api.View;

public class ErrorLead {

    private Integer lineNumber;
    private String lineValue;
    private String errorMessage;

    public ErrorLead() {
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLineValue() {
        return lineValue;
    }

    public void setLineValue(String lineValue) {
        this.lineValue = lineValue;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
