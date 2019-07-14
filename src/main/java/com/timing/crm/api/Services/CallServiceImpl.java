package com.timing.crm.api.Services;

import com.timing.crm.api.Repository.AppointmentRepository;
import com.timing.crm.api.Repository.CallRepository;
import com.timing.crm.api.Repository.LeadRepository;
import com.timing.crm.api.Repository.UserRepository;
import com.timing.crm.api.Utils.Constants;
import com.timing.crm.api.View.Appointment;
import com.timing.crm.api.View.Call;
import com.timing.crm.api.View.CallRegister;
import com.timing.crm.api.View.Lead;
import com.timing.crm.api.View.ReasonNotQualify;
import com.timing.crm.api.View.ResultCall;
import com.timing.crm.api.View.UserAndCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class CallServiceImpl implements CallService{
    private final Logger logger = LoggerFactory.getLogger("CallServiceImpl");

    @Autowired
    private CallRepository callRepository;
    @Autowired
    private LeadRepository leadRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${call.call-again}")
    private Integer callAgain;
    @Value("${call.call-no-Answer}")
    private Integer callNoAnswer;
    @Value("${call.num-wrong}")
    private Integer callNumWrong;

    public CallRegister createCall(CallRegister callRegister, UUID token){
        Lead lead = callRegister.getLead();
        Call call = callRegister.getCall();
        Appointment appointment = callRegister.call.getAppointment();
        List<Call> callsByLead;
        UserAndCompany userAndCompany = userRepository.getUserByToken(token);
        logger.info("Starting createCall with id: {}", call.getId());
        callsByLead = callRepository.getCallByLead(call.getLeadId());
        if (call.getResultCallId() == Constants.NO_INTERESADO || call.getResultCallId() == Constants.NO_CALIFICA_PARA_DEMO || call.getResultCallId() == Constants.TIENE_REPRESENTANTE_RP || call.getResultCallId() == Constants.ESTA_MOLESTO_CON_RP || call.getResultCallId() == Constants.NUMERO_NO_EXISTE ){
            logger.info("1st descalificado");
            lead.setStatus(Constants.DESCALIFICADO);
        } else if (call.getResultCallId() == Constants.CITA){
            logger.info("cita");
            lead.setStatus(Constants.CLIENTEPROCESADO);
        } else if (lead.getStatus() == Constants.STATUS_INIT){
            logger.info("status init lead");
            if (call.getResultCallId() == Constants.EQUIVOCADO){
                if(userAndCompany.getRoleId() == Constants.COORDINATOR){
                    lead.setStatus(Constants.DESCALIFICADO);
                }else {
                    lead.setStatus(Constants.ENVALIDACION);
                }
            } else if (call.getResultCallId() == Constants.NOCONTESTVB){
                lead.setStatus(Constants.ENESPERADERESPUESTA);
            } else if (call.getResultCallId() == Constants.LLAMAR_LUEGO){
                lead.setStatus(Constants.ENRELLAMADA);
            }
        }else if (lead.getStatus() == Constants.ENVALIDACION){
            logger.info("status en validacion");
            if (call.getResultCallId() == Constants.EQUIVOCADO){
                lead.setStatus(Constants.DESCALIFICADO);
            } else if ((call.getResultCallId() == Constants.LLAMAR_LUEGO || call.getResultCallId() == Constants.NOCONTESTVB) && callsByLead.size() >= callNumWrong){
                lead.setStatus(Constants.DESCALIFICADO);
            }
        }else if (lead.getStatus() == Constants.ENESPERADERESPUESTA){
            logger.info("status en espera respuesta");
            if (call.getResultCallId() == Constants.LLAMAR_LUEGO){
                lead.setStatus(Constants.ENRELLAMADA);
            } else if (call.getResultCallId() == Constants.NOCONTESTVB && callsByLead.size() >= callNoAnswer){
                lead.setStatus(Constants.DESCALIFICADO);
            }
        }else if (lead.getStatus() == Constants.ENRELLAMADA){
            logger.info("status en rellamada");
            if ((call.getResultCallId() == Constants.LLAMAR_LUEGO || call.getResultCallId() == Constants.NOCONTESTVB) && callsByLead.size() >= callAgain){
                lead.setStatus(Constants.DESCALIFICADO);
            }
        }

        lead = leadRepository.modifyLead(lead);
        call = callRepository.createCall(call);
        if (call.getResultCallId() == Constants.CITA){
            logger.info("antes de create appointment callId: {}", call.getId());
            appointment.setCallId(call.getId());
            logger.info("antes de create appointment");
            if (userAndCompany.getRoleId() == Constants.COORDINATOR){
                appointment.setStatus(Constants.PORCONFIRMAR);
            } else {
                appointment.setStatus(Constants.PORGARANTIZAR);
            }
            appointment = appointmentRepository.createAppointment(appointment);
        }

        callRegister.setLead(lead);
        callRegister.setCall(call);
        callRegister.call.setAppointment(appointment);
        return callRegister;
    }

    public List<ResultCall> getListResultCall(){
        logger.info("Starting getListResultCall()");
        return callRepository.getAllResultCall();
    }

    public List<ReasonNotQualify> getAllReasonNotQualify(){
        logger.info("Starting getAllReasonNotQualify()");
        return callRepository.getAllReasonNotQualify();
    }

    public Call createTrackingCall(Call trackingCall, UUID token){
        logger.info("starting createTrackingCall with tracking Call notes:{} ", trackingCall.getNotes());
        Appointment appointment = trackingCall.getAppointment();
        UserAndCompany userAndCompany = userRepository.getUserByToken(token);
        trackingCall = callRepository.createCall(trackingCall);
        logger.info("Create appointment from trackingCallId: {}", trackingCall.getId());
        if (appointment != null){
            appointment.setCallId(trackingCall.getId());
            logger.info("antes de create appointment");
            if (userAndCompany.getRoleId() == Constants.COORDINATOR){
                appointment.setStatus(Constants.PORCONFIRMAR);
            } else {
                appointment.setStatus(Constants.PORGARANTIZAR);
            }
            appointment = appointmentRepository.createAppointment(appointment);
            trackingCall.setAppointment(appointment);
        }
        return trackingCall;
    }

}
