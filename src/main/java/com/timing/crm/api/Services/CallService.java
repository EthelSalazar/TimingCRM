package com.timing.crm.api.Services;

import com.timing.crm.api.View.Call;
import com.timing.crm.api.View.CallRegister;
import com.timing.crm.api.View.Lead;
import com.timing.crm.api.View.ReasonNotQualify;
import com.timing.crm.api.View.ResultCall;

import java.util.List;
import java.util.UUID;

public interface CallService {

    CallRegister createCall(CallRegister callRegister, UUID token);

    List<ResultCall> getListResultCall();

    List<ReasonNotQualify> getAllReasonNotQualify();

    Call createTrackingCall(Call trackingCall, UUID token);
}
