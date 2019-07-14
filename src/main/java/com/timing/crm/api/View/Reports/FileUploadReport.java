package com.timing.crm.api.View.Reports;

// View class for reports
public class FileUploadReport {

    private Integer fileUploadId;
    private String uploadDate;
    private String fileName;
    private Integer totalLines;
    private Integer totalLinesSuccessful;
    private Integer totalLinesError;
    private String employeeName;
    private String representativeName;
    private String companyName;

    public FileUploadReport() {
    }

    public Integer getFileUploadId() {
        return fileUploadId;
    }

    public void setFileUploadId(Integer fileUploadId) {
        this.fileUploadId = fileUploadId;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
