package com.timing.crm.api.Controller;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Services.AppointmentService;
import com.timing.crm.api.View.Appointment;
import com.timing.crm.api.View.CallRegister;
import com.timing.crm.api.View.Questions;
import com.timing.crm.api.View.ResultAppointment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1")
public class AppointmentController  extends BaseController {

    private final Logger logger = LoggerFactory.getLogger("AppointmentController");

    @Autowired
    private AppointmentService appointmentService;

    /*
    *   Get Appointment endpoint
    *   Input params
    *   -status: Un estatus de cita valido.
    *   -token: Usuario autenticado, respuesta depende del rol del usuario.
    */
    @RequestMapping(value = "/appointment", method = RequestMethod.PUT)
    @ApiOperation(value = "Modifies an appointment", notes = "Returns an appointment updated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful list of call register")}
    )
    public CallRegister modifyAppointment(
            @Valid @RequestBody CallRegister callRegister,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("modifyAppointment() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Modify Appointment with token: {}", token);
        callRegister = appointmentService.modifyAppointment(callRegister);
        logger.info("Completing Modify Appointments");
        return callRegister;
    }

    @RequestMapping(value = "/resultAppointment", method = RequestMethod.GET)
    @ApiOperation(value = "Returns result appointment configuration details", notes = "Returns a complete list of "
            + "result appointments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful result appointment retrieval")}
    )
    public List<ResultAppointment> getResultAppointment(
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("getResultAppointment() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get All result appointments with token: {}", token);
        List<ResultAppointment> resultAppointmentList;
        resultAppointmentList = appointmentService.getAllResultAppointment();
        logger.info("Completing Get All result appointments");
        return resultAppointmentList;
    }


    /*
    *   Get Appointment endpoint
    *   Input params
    *   -status: Un estatus de cita valido.
    *   -token: Usuario autenticado, respuesta depende del rol del usuario.
    *   -resultAppointmentId: resultadoId guardado por el representante luego de asistir a la cita
    */
    @RequestMapping(value = "/getAppointment", method = RequestMethod.GET)
    @ApiOperation(value = "Returns a list of Call Register", notes = "Returns a complete "
            + "list of Call Register by status resultAppointmentId and operator")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful list of call register")}
    )
    public List<CallRegister> getAppointment(
            @RequestHeader(value = "token") UUID token,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "resultAppointmentId", required = false) Integer resultAppointmentId)  {

        if (!isAuthenticated(token)) {
            logger.error("getAppointment() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get Appointments with status: {} and token: {}", status, token);
        List<CallRegister> callRegisterList;
        callRegisterList = appointmentService.getAppointments(status, resultAppointmentId, token);
        logger.info("Completing Get Appointments");
        return callRegisterList;
    }


    @RequestMapping(value = "/getAppointment/{APPOINTMENT_ID}", method = RequestMethod.GET)
    @ApiOperation(value = "Returns a Call Register", notes = "Returns a complete "
            + "Call Register detail by appointmentId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful call register detail")}
    )
    public CallRegister getAppointmentById(
            @PathVariable("APPOINTMENT_ID") Integer appointmentId,
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("getAppointmentById() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get Appointment with Id: {} and token: {}", appointmentId, token);
        CallRegister callRegister;
        callRegister = appointmentService.getAppointmentById(appointmentId, token);
        logger.info("Completing Get Appointment by Id");
        return callRegister;
    }

    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    @ApiOperation(value = "Returns a list of Questions", notes = "Returns a complete "
            + "list of Questions")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful list of Questions")}
    )
    public List<Questions> getQuestions(
            @RequestHeader(value = "token") UUID token) {

        if (!isAuthenticated(token)) {
            logger.error("getAppointment() - Invalid token");
            throw new ForbiddenException("Invalid token");
        }

        logger.info("Starting Get Questions with token: {}", token);
        List<Questions> questionsList;
        questionsList = appointmentService.getQuestionsAndValues();
        logger.info("Completing Get Questions");
        return questionsList;
    }

}
