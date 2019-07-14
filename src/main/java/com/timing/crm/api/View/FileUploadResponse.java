package com.timing.crm.api.View;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class FileUploadResponse {

  private String fileName;
  private Integer fileUploadId;
  private Integer totalLines;
  private Integer totalLinesSuccessful;
  private Integer totalLinesError;
  private String message;
  @JsonIgnore
  private List<Lead> leads;
  @JsonIgnore
  private List<ErrorLead> leadsInError;

  public FileUploadResponse() {
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Integer getTotalLines() {
    return totalLines;
  }

  public void setTotalLines(Integer totalLines) {
    this.totalLines = totalLines;
  }

  public Integer getTotalLinesSuccessful() {
    return totalLinesSuccessful;
  }

  public void setTotalLinesSuccessful(Integer totalLinesSuccessful) {
    this.totalLinesSuccessful = totalLinesSuccessful;
  }

  public Integer getTotalLinesError() {
    return totalLinesError;
  }

  public void setTotalLinesError(Integer totalLinesError) {
    this.totalLinesError = totalLinesError;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<Lead> getLeads() {
    return leads;
  }

  public void setLeads(List<Lead> leads) {
    this.leads = leads;
  }

  public Integer getFileUploadId() {
    return fileUploadId;
  }

  public void setFileUploadId(Integer fileUploadId) {
    this.fileUploadId = fileUploadId;
  }

  public List<ErrorLead> getLeadsInError() {
    return leadsInError;
  }

  public void setLeadsInError(List<ErrorLead> leadsInError) {
    this.leadsInError = leadsInError;
  }
}
