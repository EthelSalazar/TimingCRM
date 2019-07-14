package com.timing.crm.api.View.Reports;

import com.timing.crm.api.View.CallRegister;

import java.util.List;

public class RepresentativeSummaryAndDetail {

    private RepresentativeSummary representativeSummary;
    private List<CallRegister> callRegisterList;


    public RepresentativeSummaryAndDetail() {
    }

    public RepresentativeSummary getRepresentativeSummary() {
        return representativeSummary;
    }

    public void setRepresentativeSummary(RepresentativeSummary representativeSummary) {
        this.representativeSummary = representativeSummary;
    }

    public List<CallRegister> getCallRegisterList() {
        return callRegisterList;
    }

    public void setCallRegisterList(List<CallRegister> callRegisterList) {
        this.callRegisterList = callRegisterList;
    }
}
