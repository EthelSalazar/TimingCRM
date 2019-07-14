package com.timing.crm.api.Services;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Repository.AppointmentRepository;
import com.timing.crm.api.Repository.CallRepository;
import com.timing.crm.api.Repository.LeadRepository;
import com.timing.crm.api.Repository.UserRepository;
import com.timing.crm.api.View.Appointment;
import com.timing.crm.api.View.Call;
import com.timing.crm.api.View.CallRegister;
import com.timing.crm.api.View.Lead;
import com.timing.crm.api.View.QuestionRaw;
import com.timing.crm.api.View.Questions;
import com.timing.crm.api.View.ResultAppointment;
import com.timing.crm.api.View.UserAndCompany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.timing.crm.api.Helper.ValidatorHelper.questionMapper;
import static com.timing.crm.api.Utils.Constants.COORDINATOR;
import static com.timing.crm.api.Utils.Constants.MANAGER;
import static com.timing.crm.api.Utils.Constants.REPRESENTATIVE;
import static com.timing.crm.api.Utils.Constants.TELEMARKETER;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final Logger logger = LoggerFactory.getLogger("AppointmentServiceImpl");

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private CallRepository callRepository;

    @Override
    public List<ResultAppointment> getAllResultAppointment() {
        logger.info("Getting getAllResultAppointment()");
        return appointmentRepository.getAllResultAppointment();
    }

    @Override
    public CallRegister modifyAppointment(CallRegister callRegister) {
        Lead lead = callRegister.getLead();
        Call call = callRegister.getCall();
        Appointment appointment = call.getAppointment();
        logger.info("Starting modify lead with id: {}", lead.getId());
        lead = leadRepository.modifyLead(lead);
        /// la llamada no se modifica
        logger.info("Starting modify appointment with id: {}", appointment.getId());
        appointment = appointmentRepository.modifyAppointment(appointment);
        logger.info("Completing modify appointment with leadId: {}, appId: {}", lead.getId(), appointment.getId());
        return callRegister;
    }

    @Override
    public List<CallRegister> getAppointments(Integer status, Integer resultAppointmentId, UUID token) {
        logger.info("Starting getAppointments with status: {}, resultAppointmentId: {} and token: {}", status, resultAppointmentId, token);
        List<CallRegister> callRegisterList = new ArrayList<>();
        UserAndCompany userAndCompany = userRepository.getUserByToken(token);
        if (userAndCompany==null) {
            logger.error("Invalid credentials - UserAndCompany is null");
            throw new ForbiddenException("Invalid credentials");
        }

        if (!StringUtils.isEmpty(status)) {
            // Check roles
            if (userAndCompany.getRoleId() == TELEMARKETER) {
                callRegisterList = appointmentRepository.getAppointmetsByStatusAndOperator(status, userAndCompany.getUserId());
            } else if (userAndCompany.getRoleId() == MANAGER) {
                callRegisterList = appointmentRepository.getAppointmetsByStatusAndCompany(status, userAndCompany.getCompanyId(),
                        userAndCompany.getUserId());
            } else if (userAndCompany.getRoleId() == COORDINATOR) {
                callRegisterList = appointmentRepository.getAppointmetsByStatusAndCompanyAndCoord(status,
                        userAndCompany.getCompanyId(), userAndCompany.getUserId());
            } else if (userAndCompany.getRoleId() == REPRESENTATIVE) {
                callRegisterList = appointmentRepository.getAppointmetsByStatusAndRepresentative(status,
                        userAndCompany.getUserId());
            } else {
                logger.error("Invalid roleId in getAppointments().");
                throw new ForbiddenException("Invalid credentials");
            }
        } else if (!StringUtils.isEmpty(resultAppointmentId)) {
            // Check roles
            if (userAndCompany.getRoleId() == TELEMARKETER) {
                callRegisterList = appointmentRepository.getAppointmetsByResultAppAndOperator(resultAppointmentId, userAndCompany.getUserId());
            } else if (userAndCompany.getRoleId() == MANAGER) {
                callRegisterList = appointmentRepository.getAppointmetsByResultAppAndCompany(resultAppointmentId, userAndCompany.getCompanyId(),
                        userAndCompany.getUserId());
            } else if (userAndCompany.getRoleId() == COORDINATOR) {
                callRegisterList = appointmentRepository.getAppointmetsByResultAppAndCompanyAndCoord(resultAppointmentId,
                        userAndCompany.getCompanyId(), userAndCompany.getUserId());
            } else if (userAndCompany.getRoleId() == REPRESENTATIVE) {
                callRegisterList = appointmentRepository.getAppointmetsByResultAppAndRepresentative(resultAppointmentId,
                        userAndCompany.getUserId());
            } else {
                logger.error("Invalid roleId in getAppointments().");
                throw new ForbiddenException("Invalid credentials");
            }
        } else {
            logger.error("getAppointments() - invalid parameters");
        }

        logger.info("Completing getAppointments with status: {}, resultAppointmentId: {} and token: {}", status, resultAppointmentId, token);
        return callRegisterList;
    }

    @Override
    public CallRegister getAppointmentById(Integer appointmentId, UUID token) {
        logger.info("Starting getAppointmentById with id: {} and token: {}", appointmentId, token);
        CallRegister callRegister;
        UserAndCompany userAndCompany = userRepository.getUserByToken(token);
        callRegister = appointmentRepository.getAppointmentById(appointmentId, userAndCompany.getUserId());
        logger.info("Completing getAppointmentById with id: {} and token: {}", appointmentId, token);
        return callRegister;
    }

    public List<Questions> getQuestionsAndValues(){
        logger.info("Starting getQuestionsAndValues");
        List<Questions> questionsList = new ArrayList<>();
        List<QuestionRaw> questionRawList = new ArrayList<>();
        questionRawList = appointmentRepository.getQuestionsRaw();
        questionsList = questionMapper(questionRawList);
        logger.info("Completing getQuestionsAndValues");
        return questionsList;
    }

}
