package com.timing.crm.api.View;

public class CallRegister {
    public Call call;
    public Lead lead;

    public CallRegister() {
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

}
