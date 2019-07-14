package com.timing.crm.api.Services;

import com.timing.crm.api.View.Appointment;
import com.timing.crm.api.View.CallRegister;
import com.timing.crm.api.View.Questions;
import com.timing.crm.api.View.ResultAppointment;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    List<ResultAppointment> getAllResultAppointment();

    List<CallRegister> getAppointments(Integer status, Integer resultAppointmentId, UUID token);

    CallRegister modifyAppointment(CallRegister callRegister);

    CallRegister getAppointmentById(Integer appointmentId, UUID token);

    List<Questions> getQuestionsAndValues();

}
