package com.timing.crm.api.Repository;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.View.Appointment;
import com.timing.crm.api.View.Call;
import com.timing.crm.api.View.CallRegister;
import com.timing.crm.api.View.QuestionAppointment;
import com.timing.crm.api.View.QuestionRaw;
import com.timing.crm.api.View.ResultAppointment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class AppointmentRepository {
    private final Logger logger = LoggerFactory.getLogger("AppointmentRepository");

    private static final String SELECT_ALL_RESULT_APPOINTMENT = "SELECT id, description FROM RESULT_APPOINTMENT";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String INSERT_APPOINTMENTS = "INSERT INTO APPOINTMENT " +
            "(id,DATE_APPOINT,result_appointment_id,COORD_COMMENTS,TELEOPE_COMMENTS, USERS_REPDETAIL_ID, COMPANY_ID, LEADS_ID, " +
            "users_employee_id,call_id, status, comment_summary, APPOINTMENT_PARENT_ID, created_on, updated_on) " +
            " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now()) ";

    private static final String INSERT_QUESTIONAPPOINTMENTS = "INSERT INTO question_appointment " +
            "(id, questions_id, appointment_id, answer, created_on, updated_on) " +
            "VALUES (?,?,?,?,now(),now()) ";

    private static final String GET_APPOINTMENTS_BY_STATUS_AND_OPERATOR = "SELECT"
            + "  a.id appointmentId, a.date_appoint dateAppoint, a.result_appointment_id resultAppId, a.coord_comments coordComments,"
            + "  a.teleope_comments teleComments, a.status appointStatus, a.users_repdetail_id userRepdetailId, a.company_id companyId,"
            + "  a.users_employee_id userEmployeeId, a.leads_id leadsId, a.call_id callId, a.comment_summary commentSummary, "
            + "  a.APPOINTMENT_PARENT_ID appointmentParentId, a.repdetail_comment repdetailComment, c.created_on callDate, "
            + "  c.result_call_id resultCallId, (SELECT description FROM result_call WHERE id = c.result_call_id) "
            + "  resultCallDescription, c.reasons_notqualify_id reasonsNotqualifyId, (SELECT description FROM "
            + "  reasons_notqualify WHERE id = c.reasons_notqualify_id) reasonsNotqualifyDescription, c.notes notes, "
            + "  c.FOLLOWUP_APPOINTMENT_ID followupAppointmentId, l.date_data dateData, "
            + "  l.id leadsId, l.rating rating, l.name leadName, l.marital_status maritalStatus, l.spouse_name spouseName, l.phone phone,"
            + "  l.zip zip, l.city city, l.address address, l.status leadStatus, l.approach_type_id approachType, "
            + "  (SELECT description FROM approach_type WHERE id = l.approach_type_id) approachTypeDescription, "
            + "  l.prospect_category_id prospectCategory, "
            + "  (SELECT description FROM prospect_category WHERE id = l.prospect_category_id) prospectCategoryDescription,"
            + "  l.approach_place approachPlace, l.approach_notes approachNotes, l.approach_notes_414 approachNotes414, l.host host,"
            + "  l.host_id hostId, l.created_on createdOn, l.updated_on updatedOn, (Select name from leads where id = l.host_Id) hostName,"
            + "  (SELECT name FROM rep_detail WHERE users_id = l.user_repdetail_id) nameRepDetail FROM"
            + "  appointment a, call c, leads l WHERE  a.call_id = c.id"
            + "  AND a.leads_id = c.leads_id AND a.users_repdetail_id = c.users_repdetail_id"
            + "  AND a.company_id = c.company_id"
            + "  AND a.leads_id = l.id  AND a.company_id = l.company_id"
            + "  AND a.users_repdetail_id = l.user_repdetail_id "
            + "  AND a.status = :status"
            + "  AND l.user_repdetail_id IN (SELECT user_repdetail_id FROM employee_repdetail "
            + " WHERE user_employee_id = :employeeId OR USER_TELEMKTCONT_ID = :employeeId) ";

    private static final String GET_APPOINTMENTS_BY_STATUS_AND_REPRESENTATIVE = "SELECT"
            + "  a.id appointmentId, a.date_appoint dateAppoint, a.result_appointment_id resultAppId, a.coord_comments coordComments,"
            + "  a.teleope_comments teleComments, a.status appointStatus, a.users_repdetail_id userRepdetailId, a.company_id companyId,"
            + "  a.users_employee_id userEmployeeId, a.leads_id leadsId, c.id callId, a.comment_summary commentSummary, "
            + "  a.APPOINTMENT_PARENT_ID appointmentParentId, a.repdetail_comment repdetailComment, c.created_on callDate, "
            + "  c.result_call_id resultCallId, (SELECT description FROM result_call WHERE id = c.result_call_id) "
            + "  resultCallDescription, c.reasons_notqualify_id reasonsNotqualifyId, (SELECT description FROM reasons_notqualify WHERE id = c.reasons_notqualify_id) "
            + "  reasonsNotqualifyDescription, c.notes notes, c.FOLLOWUP_APPOINTMENT_ID followupAppointmentId, "
            + "  l.id leadsId, l.date_data dateData, l.rating rating, l.name leadName, l.marital_status maritalStatus, "
            + "  l.spouse_name spouseName, l.phone phone, "
            + "  l.zip zip, l.city city, l.address address, l.status leadStatus, l.approach_type_id approachType, "
            + "  (SELECT description FROM approach_type WHERE id = l.approach_type_id) approachTypeDescription, "
            + "  l.prospect_category_id prospectCategory, "
            + "  (SELECT description FROM prospect_category WHERE id = l.prospect_category_id) prospectCategoryDescription,"
            + "  l.approach_place approachPlace, l.approach_notes approachNotes, l.approach_notes_414 approachNotes414, l.host host,"
            + "  l.host_id hostId, l.created_on createdOn, l.updated_on updatedOn, (Select name from leads where id = l.host_Id) hostName,"
            + "  (SELECT name FROM rep_detail WHERE users_id = l.user_repdetail_id) nameRepDetail FROM"
            + "  appointment a, call c, leads l WHERE  a.call_id = c.id"
            + "  AND a.leads_id = c.leads_id AND a.users_repdetail_id = c.users_repdetail_id"
            + "  AND a.company_id = c.company_id"
            + "  AND a.leads_id = l.id  AND a.company_id = l.company_id"
            + "  AND a.users_repdetail_id = l.user_repdetail_id "
            + "  AND a.status = :status"
            + "  AND l.user_repdetail_id = :representativeId";

    private static final String GET_APPOINTMENTS_BY_STATUS_COMPANY = "SELECT"
            + "  a.id appointmentId, a.date_appoint dateAppoint, a.result_appointment_id resultAppId, a.coord_comments coordComments,"
            + "  a.teleope_comments teleComments, a.status appointStatus, a.users_repdetail_id userRepdetailId, a.company_id companyId,"
            + "  a.users_employee_id userEmployeeId, a.leads_id leadsId, a.call_id callId, a.comment_summary commentSummary, "
            + "  a.APPOINTMENT_PARENT_ID appointmentParentId, a.repdetail_comment repdetailComment, c.created_on callDate, "
            + "  c.result_call_id resultCallId, (SELECT description FROM result_call WHERE id = c.result_call_id) "
            + "  resultCallDescription, c.reasons_notqualify_id reasonsNotqualifyId, (SELECT description FROM "
            + "  reasons_notqualify WHERE id = c.reasons_notqualify_id) reasonsNotqualifyDescription, "
            + "  c.notes notes, c.FOLLOWUP_APPOINTMENT_ID followupAppointmentId, l.date_data dateData, "
            + "  l.id leadsId, l.rating rating, l.name leadName, l.marital_status maritalStatus, l.spouse_name spouseName, l.phone phone,"
            + "  l.zip zip, l.city city, l.address address, l.status leadStatus, l.approach_type_id approachType, "
            + "  (SELECT description FROM approach_type WHERE id = l.approach_type_id) approachTypeDescription, "
            + "  l.prospect_category_id prospectCategory, (SELECT description FROM prospect_category WHERE id = l.prospect_category_id) prospectCategoryDescription,"
            + "  l.approach_place approachPlace, l.approach_notes approachNotes, l.approach_notes_414 approachNotes414, l.host host,"
            + "  l.host_id hostId, l.created_on createdOn, l.updated_on updatedOn, (Select name from leads where id = l.host_Id) hostName,"
            + "  (SELECT name FROM rep_detail WHERE users_id = l.user_repdetail_id) nameRepDetail FROM"
            + "  appointment a, call c, leads l WHERE  a.call_id = c.id"
            + "  AND a.leads_id = c.leads_id AND a.users_repdetail_id = c.users_repdetail_id"
            + "  AND a.company_id = c.company_id"
            + "  AND a.leads_id = l.id  AND a.company_id = l.company_id"
            + "  AND a.users_repdetail_id = l.user_repdetail_id "
            + "  AND a.company_id = :companyId"
            + "  AND a.status = :status";

    private static final String GET_APPOINTMENTS_BY_STATUS_COMPANY_COORDOFTELM = "SELECT"
            + "  a.id appointmentId, a.date_appoint dateAppoint, a.result_appointment_id resultAppId, a.coord_comments coordComments,"
            + "  a.teleope_comments teleComments, a.status appointStatus, a.users_repdetail_id userRepdetailId, a.company_id companyId,"
            + "  a.users_employee_id userEmployeeId, a.leads_id leadsId, a.call_id callId, a.comment_summary commentSummary, "
            + "  a.APPOINTMENT_PARENT_ID appointmentParentId, a.repdetail_comment repdetailComment, c.created_on callDate, "
            + "  c.result_call_id resultCallId, c.reasons_notqualify_id reasonsNotqualifyId, l.date_data dateData, " +
            "c.notes notes, c.FOLLOWUP_APPOINTMENT_ID followupAppointmentId, "
            + "  l.id leadsId, l.rating rating, l.name leadName, l.marital_status maritalStatus, l.spouse_name spouseName, l.phone phone,"
            + "  l.zip zip, l.city city, l.address address, l.status leadStatus, l.approach_type_id approachType, "
            + "  l.prospect_category_id prospectCategory,"
            + "  l.approach_place approachPlace, l.approach_notes approachNotes, l.approach_notes_414 approachNotes414, l.host host,"
            + "  l.host_id hostId, l.created_on createdOn, l.updated_on updatedOn, (Select name from leads where id = l.host_Id) hostName,"
            + "  (SELECT name FROM rep_detail WHERE users_id = l.user_repdetail_id) nameRepDetail FROM"
            + "  appointment a, call c, leads l WHERE  a.call_id = c.id"
            + "  AND a.leads_id = c.leads_id AND a.users_repdetail_id = c.users_repdetail_id"
            + "  AND a.company_id = c.company_id"
            + "  AND a.leads_id = l.id  AND a.company_id = l.company_id"
            + "  AND a.users_repdetail_id = l.user_repdetail_id "
            + "  AND a.company_id = :companyId"
            + "  AND a.status = :status"
            + "  AND a.users_repdetail_id in ( SELECT USER_REPDETAIL_ID FROM EMPLOYEE_REPDETAIL WHERE USER_EMPLOYEE_ID IN "
            + " (SELECT USERS_ID FROM EMPLOYEE WHERE user_supervisor_id = :userId))";

    private static final String GET_APPOINTMENTS_BY_STATUS_AND_COORD = "SELECT"
            + "  a.id appointmentId, a.date_appoint dateAppoint, a.result_appointment_id resultAppId, a.coord_comments coordComments,"
            + "  a.teleope_comments teleComments, a.status appointStatus, a.users_repdetail_id userRepdetailId, a.company_id companyId,"
            + "  a.users_employee_id userEmployeeId, a.leads_id leadsId, a.call_id callId, a.comment_summary commentSummary, "
            + "  a.APPOINTMENT_PARENT_ID appointmentParentId, a.repdetail_comment repdetailComment, c.created_on callDate, "
            + "  c.result_call_id resultCallId, (SELECT description FROM result_call WHERE id = c.result_call_id) "
            + "  resultCallDescription, c.reasons_notqualify_id reasonsNotqualifyId, (SELECT description FROM reasons_notqualify WHERE "
            + "  id = c.reasons_notqualify_id) reasonsNotqualifyDescription, c.notes notes, "
            + "  c.FOLLOWUP_APPOINTMENT_ID followupAppointmentId,  l.date_data dateData, "
            + "  l.id leadsId, l.rating rating, l.name leadName, l.marital_status maritalStatus, l.spouse_name spouseName, l.phone phone,"
            + "  l.zip zip, l.city city, l.address address, l.status leadStatus, l.approach_type_id approachType,"
            + " (SELECT description FROM approach_type WHERE id = l.approach_type_id) approachTypeDescription, "
            + " l.prospect_category_id prospectCategory, "
            + "  (SELECT description FROM prospect_category WHERE id = l.prospect_category_id) prospectCategoryDescription, "
            + "  l.approach_place approachPlace, l.approach_notes approachNotes, l.approach_notes_414 approachNotes414, l.host host,"
            + "  l.host_id hostId, l.created_on createdOn, l.updated_on updatedOn, (Select name from leads where id = l.host_Id) hostName,"
            + "  (SELECT name FROM rep_detail WHERE users_id = l.user_repdetail_id) nameRepDetail "
            + "  FROM  appointment a, call c, leads l "
            + "  WHERE  a.call_id = c.id"
            + "  AND a.leads_id = c.leads_id AND a.users_repdetail_id = c.users_repdetail_id"
            + "  AND a.company_id = c.company_id"
            + "  AND a.leads_id = l.id  AND a.company_id = l.company_id"
            + "  AND a.users_repdetail_id = l.user_repdetail_id"
            + "  AND a.status = :status"
            + "  AND l.user_repdetail_id IN (SELECT user_repdetail_id FROM employee_repdetail "
            + " WHERE USER_SUPERVISOR_ID = :userId) ";

    private static final String GET_APPOINTMENTS_BY_RESULTAPP_AND_OPERATOR = "SELECT"
            + "  a.id appointmentId, a.date_appoint dateAppoint, a.result_appointment_id resultAppId, a.coord_comments coordComments,"
            + "  a.teleope_comments teleComments, a.status appointStatus, a.users_repdetail_id userRepdetailId, a.company_id companyId,"
            + "  a.users_employee_id userEmployeeId, a.leads_id leadsId, a.call_id callId, a.comment_summary commentSummary, "
            + "  a.APPOINTMENT_PARENT_ID appointmentParentId, a.repdetail_comment repdetailComment, c.created_on callDate, " +
            "c.result_call_id resultCallId, (SELECT description FROM result_call WHERE id = c.result_call_id) resultCallDescription, " +
            "c.reasons_notqualify_id reasonsNotqualifyId, (SELECT description FROM reasons_notqualify " +
            "WHERE id = c.reasons_notqualify_id) reasonsNotqualifyDescription, l.date_data dateData, c.notes notes, c.FOLLOWUP_APPOINTMENT_ID followupAppointmentId, "
            + "  l.id leadsId, l.rating rating, l.name leadName, l.marital_status maritalStatus, l.spouse_name spouseName, l.phone phone,"
            + "  l.zip zip, l.city city, l.address address, l.status leadStatus, l.approach_type_id approachType, " +
            "(SELECT description FROM approach_type WHERE id = l.approach_type_id) approachTypeDescription, " +
            "l.prospect_category_id prospectCategory, " +
            "(SELECT description FROM prospect_category WHERE id = l.prospect_category_id) prospectCategoryDescription,"
            + "  l.approach_place approachPlace, l.approach_notes approachNotes, l.approach_notes_414 approachNotes414, l.host host,"
            + "  l.host_id hostId, l.created_on createdOn, l.updated_on updatedOn, (Select name from leads where id = l.host_Id) hostName,"
            + "  (SELECT name FROM rep_detail WHERE users_id = l.user_repdetail_id) nameRepDetail FROM"
            + "  appointment a, call c, leads l WHERE  a.call_id = c.id"
            + "  AND a.leads_id = c.leads_id AND a.users_repdetail_id = c.users_repdetail_id"
            + "  AND a.company_id = c.company_id"
            + "  AND a.leads_id = l.id  AND a.company_id = l.company_id"
            + "  AND a.users_repdetail_id = l.user_repdetail_id "
            + "  AND a.result_appointment_id = :resultAppointmentId"
            + "  AND l.user_repdetail_id IN (SELECT user_repdetail_id FROM employee_repdetail "
            + " WHERE user_employee_id = :employeeId OR USER_TELEMKTCONT_ID = :employeeId) ";

    private static final String GET_APPOINTMENTS_BY_RESULTAPP_AND_REPRESENTATIVE = "SELECT"
            + "  a.id appointmentId, a.date_appoint dateAppoint, a.result_appointment_id resultAppId, a.coord_comments coordComments,"
            + "  a.teleope_comments teleComments, a.status appointStatus, a.users_repdetail_id userRepdetailId, a.company_id companyId,"
            + "  a.users_employee_id userEmployeeId, a.leads_id leadsId, a.call_id callId, a.comment_summary commentSummary, "
            + "  a.APPOINTMENT_PARENT_ID appointmentParentId, a.repdetail_comment repdetailComment, c.created_on callDate, " +
            "c.result_call_id resultCallId, (SELECT description FROM result_call WHERE id = c.result_call_id) resultCallDescription, " +
            "c.reasons_notqualify_id reasonsNotqualifyId, (SELECT description FROM reasons_notqualify " +
            "WHERE id = c.reasons_notqualify_id) reasonsNotqualifyDescription, l.date_data dateData, c.notes notes, c.FOLLOWUP_APPOINTMENT_ID followupAppointmentId, "
            + "  l.id leadsId, l.rating rating, l.name leadName, l.marital_status maritalStatus, l.spouse_name spouseName, l.phone phone,"
            + "  l.zip zip, l.city city, l.address address, l.status leadStatus, l.approach_type_id approachType, "
            + " (SELECT description FROM approach_type WHERE id = l.approach_type_id) approachTypeDescription, "
            + " l.prospect_category_id prospectCategory, "
            + " (SELECT description FROM prospect_category WHERE id = l.prospect_category_id) "
            + "prospectCategoryDescription,"
            + "  l.approach_place approachPlace, l.approach_notes approachNotes, l.approach_notes_414 approachNotes414, l.host host,"
            + "  l.host_id hostId, l.created_on createdOn, l.updated_on updatedOn, (Select name from leads where id = l.host_Id) hostName,"
            + "  (SELECT name FROM rep_detail WHERE users_id = l.user_repdetail_id) nameRepDetail FROM"
            + "  appointment a, call c, leads l WHERE  a.call_id = c.id"
            + "  AND a.leads_id = c.leads_id AND a.users_repdetail_id = c.users_repdetail_id"
            + "  AND a.company_id = c.company_id"
            + "  AND a.leads_id = l.id  AND a.company_id = l.company_id"
            + "  AND a.users_repdetail_id = l.user_repdetail_id "
            + "  AND a.result_appointment_id = :resultAppointmentId"
            + "  AND l.user_repdetail_id = :representativeId";

    private static final String GET_APPOINTMENTS_BY_RESULTAPP_COMPANY = "SELECT"
            + "  a.id appointmentId, a.date_appoint dateAppoint, a.result_appointment_id resultAppId, a.coord_comments coordComments,"
            + "  a.teleope_comments teleComments, a.status appointStatus, a.users_repdetail_id userRepdetailId, a.company_id companyId,"
            + "  a.users_employee_id userEmployeeId, a.leads_id leadsId, a.call_id callId, a.comment_summary commentSummary, "
            + "  a.APPOINTMENT_PARENT_ID appointmentParentId, a.repdetail_comment repdetailComment, c.created_on callDate, " +
            "c.result_call_id resultCallId, (SELECT description FROM result_call WHERE id = c.result_call_id) " +
            "resultCallDescription, c.reasons_notqualify_id reasonsNotqualifyId, " +
            "(SELECT description FROM reasons_notqualify WHERE id = c.reasons_notqualify_id) reasonsNotqualifyDescription, l.date_data dateData, " +
            "c.notes notes, c.FOLLOWUP_APPOINTMENT_ID followupAppointmentId, "
            + "  l.id leadsId, l.rating rating, l.name leadName, l.marital_status maritalStatus, l.spouse_name spouseName, l.phone phone,"
            + "  l.zip zip, l.city city, l.address address, l.status leadStatus, l.approach_type_id approachType, "
            + "  l.prospect_category_id prospectCategory,"
            + "  l.approach_place approachPlace, l.approach_notes approachNotes, l.approach_notes_414 approachNotes414, l.host host,"
            + "  l.host_id hostId, l.created_on createdOn, l.updated_on updatedOn, (Select name from leads where id = l.host_Id) hostName,"
            + "  (SELECT name FROM rep_detail WHERE users_id = l.user_repdetail_id) nameRepDetail FROM"
            + "  appointment a, call c, leads l WHERE  a.call_id = c.id"
            + "  AND a.leads_id = c.leads_id AND a.users_repdetail_id = c.users_repdetail_id"
            + "  AND a.company_id = c.company_id"
            + "  AND a.leads_id = l.id  AND a.company_id = l.company_id"
            + "  AND a.users_repdetail_id = l.user_repdetail_id "
            + "  AND a.company_id = :companyId"
            + "  AND a.result_appointment_id = :resultAppointmentId";

    private static final String GET_APPOINTMENTS_BY_RESULTAPP_AND_COORD = "SELECT"
            + "  a.id appointmentId, a.date_appoint dateAppoint, a.result_appointment_id resultAppId, a.coord_comments coordComments,"
            + "  a.teleope_comments teleComments, a.status appointStatus, a.users_repdetail_id userRepdetailId, a.company_id companyId,"
            + "  a.users_employee_id userEmployeeId, a.leads_id leadsId, a.call_id callId, a.comment_summary commentSummary, "
            + "  a.APPOINTMENT_PARENT_ID appointmentParentId, a.repdetail_comment repdetailComment, c.created_on callDate, " +
            "c.result_call_id resultCallId, (SELECT description FROM result_call WHERE id = c.result_call_id) resultCallDescription, " +
            "c.reasons_notqualify_id reasonsNotqualifyId, (SELECT description FROM reasons_notqualify " +
            "WHERE id = c.reasons_notqualify_id) reasonsNotqualifyDescription, l.date_data dateData, c.notes notes, c.FOLLOWUP_APPOINTMENT_ID followupAppointmentId, "
            + "  l.id leadsId, l.rating rating, l.name leadName, l.marital_status maritalStatus, l.spouse_name spouseName, l.phone phone,"
            + "  l.zip zip, l.city city, l.address address, l.status leadStatus, l.approach_type_id approachType,"
            + " (SELECT description FROM approach_type WHERE id = l.approach_type_id) approachTypeDescription, "
            + " l.prospect_category_id prospectCategory, "
            + "  (SELECT description FROM prospect_category WHERE id = l.prospect_category_id) prospectCategoryDescription, "
            + "  l.approach_place approachPlace, l.approach_notes approachNotes, l.approach_notes_414 approachNotes414, l.host host,"
            + "  l.host_id hostId, l.created_on createdOn, l.updated_on updatedOn, (Select name from leads where id = l.host_Id) hostName,"
            + "  (SELECT name FROM rep_detail WHERE users_id = l.user_repdetail_id) nameRepDetail "
            + "  FROM  appointment a, call c, leads l "
            + "  WHERE  a.call_id = c.id"
            + "  AND a.leads_id = c.leads_id AND a.users_repdetail_id = c.users_repdetail_id"
            + "  AND a.company_id = c.company_id"
            + "  AND a.leads_id = l.id  AND a.company_id = l.company_id"
            + "  AND a.users_repdetail_id = l.user_repdetail_id"
            + "  AND a.result_appointment_id = :resultAppointmentId"
            + "  AND l.user_repdetail_id IN (SELECT user_repdetail_id FROM employee_repdetail "
            + " WHERE USER_SUPERVISOR_ID = :userId) ";


    private static final String GET_APPOINTMENT_BY_ID = "SELECT"
            + "  a.id appointmentId, a.date_appoint dateAppoint, a.result_appointment_id resultAppId, a.coord_comments coordComments,"
            + "  a.teleope_comments teleComments, a.status appointStatus, a.users_repdetail_id userRepdetailId, a.company_id companyId,"
            + "  a.users_employee_id userEmployeeId, a.leads_id leadsId, a.call_id callId, a.comment_summary commentSummary, "
            + "  a.APPOINTMENT_PARENT_ID appointmentParentId, a.repdetail_comment repdetailComment, c.created_on callDate, " +
            "c.result_call_id resultCallId, (SELECT description FROM result_call WHERE id = c.result_call_id) resultCallDescription, " +
            "c.reasons_notqualify_id reasonsNotqualifyId, (SELECT description FROM reasons_notqualify " +
            "WHERE id = c.reasons_notqualify_id) reasonsNotqualifyDescription, l.date_data dateData, c.notes notes, c.FOLLOWUP_APPOINTMENT_ID followupAppointmentId, "
            + "  l.id leadId, l.rating rating, l.name leadName, l.marital_status maritalStatus, l.spouse_name spouseName, l.phone phone,"
            + "  l.zip zip, l.city city, l.address address, l.status leadStatus, l.approach_type_id approachType,"
            + " (SELECT description FROM approach_type WHERE id = l.approach_type_id) approachTypeDescription, "
            + " l.prospect_category_id prospectCategory, (SELECT description FROM prospect_category WHERE id = l.prospect_category_id) prospectCategoryDescription, "
            + "  l.approach_place approachPlace, l.approach_notes approachNotes, l.approach_notes_414 approachNotes414, l.host host,"
            + "  l.host_id hostId, l.created_on createdOn, l.updated_on updatedOn, (Select name from leads where id = l.host_Id) hostName,"
            + " (SELECT description FROM result_call WHERE id = c.result_call_id) resultCallDescription, "
            + "  (SELECT name FROM rep_detail WHERE users_id = l.user_repdetail_id) nameRepDetail FROM "
            + "  appointment a, call c, leads l WHERE  a.call_id = c.id "
            + "  AND a.leads_id = c.leads_id AND a.users_repdetail_id = c.users_repdetail_id "
            + "  AND a.company_id = c.company_id "
            + "  AND a.leads_id = l.id  AND a.company_id = l.company_id "
            + "  AND a.id = :appointmentId";

    private static final String SELECT_QUESTIONVALUE = "SELECT Q.ID questionId, Q.DESCRIPTION questionDescription, " +
            "Q.TYPE questionType, Q.DEPENDING, Q.QUESTION_DEPENDENT_ID questionDependingId, Q.extra_label extraLabel, Q.special_validation specialValidation, " +
            "QV.ID valueId, QV.DESCRIPTION valueDescription, QV.DESQUALIFYING, QV.RESULT_CALL_ID resultCallId " +
            "FROM QUESTIONS Q " +
            "LEFT JOIN QUESTION_VALUES QV ON QV.QUESTIONS_ID = Q.ID " +
            "ORDER BY questionId";

    private static final String GET_QUESTION_AND_ANSWER_BY_APPOINTMENT = "SELECT "
            + "  qa.id id, qa.questions_id questionId, q.description questionDescription, qa.answer answer, "
            + "  qa.appointment_id appointmentId FROM"
            + "  questions q, question_appointment qa WHERE q.id = qa.questions_id"
            + "  AND qa.appointment_id = :appointmentId";

    private static final String UPDATE_APPOINTMENT = "UPDATE appointment SET result_appointment_id = ?, " +
            "coord_comments = ?, teleope_comments = ?, status = ?, date_appoint = ?, comment_summary = ?, " +
            "APPOINTMENT_PARENT_ID = ?, updated_on = NOW() " +
            " WHERE id = ?";


    public Integer getAppointmentSequence(){
        Integer employeeId = -1;

        final String sql = "SELECT nextval('appointment_id_seq')";
        try {
            employeeId = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting AppointmentSequence - {}", e.getMessage());

        }
        return employeeId;
    }


    public Integer getQstAppointmentSequence(){
        Integer employeeId = new Integer(-1);

        final String sql = "SELECT nextval('question_appointment_id_seq')";
        try {
            employeeId = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting AppointmentSequence - " + e.getMessage());

        }
        return employeeId;
    }


    public Appointment createAppointment(Appointment appointment) {
        int result = 0;
        Integer appId = getAppointmentSequence();
        Object[] args = new Object[]{appId, appointment.getDateAppoint(), appointment.getResultAppId(), appointment.getCoordComments(),
        appointment.getTeleComments(), appointment.getUserRepdetailId(), appointment.getCompanyId(), appointment.getLeadsId(),
        appointment.getUserEmployeeId(), appointment.getCallId(), appointment.getStatus(), appointment.getCommentSummary(),
        appointment.getAppointmenteParentId()};
        logger.info("SQL: {} - with id: {}", INSERT_APPOINTMENTS, appId);
        try {
            result = jdbcTemplate.update(INSERT_APPOINTMENTS, args);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Creating Appointment - {}", e.getMessage());
            throw new BadRequestException("Call duplicated");
        }
        logger.info("Size: {}, appId: {}", appointment.getQuestionsAppointment().size(), appId);
        logger.info("Result repository: " + result);
        appointment.setId(appId);
        if (appointment.getQuestionsAppointment().size()>0){
            int[] resultLote = new int[0];
            logger.debug("SQL: {}", INSERT_QUESTIONAPPOINTMENTS);

            try {
                resultLote = jdbcTemplate.batchUpdate(INSERT_QUESTIONAPPOINTMENTS, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        Integer question_appointment_id = getQstAppointmentSequence();
                        QuestionAppointment questionAppointment = appointment.getQuestionsAppointment().get(i);
                        appointment.getQuestionsAppointment().get(i).setAppointmentId(appId);
                        appointment.getQuestionsAppointment().get(i).setId(question_appointment_id);
                        preparedStatement.setInt(1, question_appointment_id);
                        preparedStatement.setInt(2, questionAppointment.getQuestionId());
                        preparedStatement.setInt(3, appId);
                        preparedStatement.setString(4, questionAppointment.getAnswer());
                    }

                    @Override
                    public int getBatchSize() {
                        return appointment.getQuestionsAppointment().size();
                    }
                });
                logger.info("Result: {}", result);
            } catch (DataAccessException e) {
                logger.error("Error while Creating answers of appointment - " + e.getMessage());
                throw new BadRequestException("ANswer duplicated");
            }
        }
        return appointment;
    }


    public Appointment modifyAppointment(Appointment appointment){
        String sql;
        int result;

        Object[] args = new Object[]{appointment.getResultAppId(), appointment.getCoordComments(),
                appointment.getTeleComments(), appointment.getStatus(), appointment.getDateAppoint(), appointment.getCommentSummary(),
                appointment.getAppointmenteParentId(), appointment.getId()};

        sql = UPDATE_APPOINTMENT;
        logger.info("SQL: {} with appId {}", sql, appointment.getId());
        try {
            result = jdbcTemplate.update(sql,args);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting appointments by status and userId - {}", e.getMessage());
        }
        return appointment;
    }


    @Cacheable("resultCalls")
    public List<ResultAppointment> getAllResultAppointment() {
        List<ResultAppointment> resultAppointments = new ArrayList<>();

        String sql = SELECT_ALL_RESULT_APPOINTMENT;
        logger.info("SQL: {}", sql);
        try {
            resultAppointments = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ResultAppointment.class));
            logger.info("Result size: {}", resultAppointments.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting all result appointments - {}", e.getMessage());
        }
        return resultAppointments;
    }


    public List<CallRegister> getAppointmetsByStatusAndOperator(Integer status, Integer userId){
        String sql;
        List<CallRegister> callRegisterList = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("status",status);
        mapSqlParameterSource.addValue("employeeId", userId);

        sql = GET_APPOINTMENTS_BY_STATUS_AND_OPERATOR;
        logger.info("SQL: {} - with status {} and userId: {}", sql, status, userId);
        try {
            callRegisterList = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new CallRegisterRowMapper());
        } catch (DataAccessException e) {
            logger.error("Error while getting appointments by status and userId - {}", e.getMessage());
        }
        return addQuestionAndAnswers(callRegisterList);
    }


    // For Managers
    public List<CallRegister> getAppointmetsByStatusAndCompany(Integer status, Integer companyId, Integer userId){
        String sql;
        List<CallRegister> callRegisterList = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("status",status);
        mapSqlParameterSource.addValue("companyId", companyId);

        sql = GET_APPOINTMENTS_BY_STATUS_COMPANY;
        logger.info("SQL: {} - with status: {}, company: {} and manager userId: {}", sql, status, companyId, userId);
        try {
            callRegisterList = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new CallRegisterRowMapper());
        } catch (DataAccessException e) {
            logger.error("Error while getting appointments by status and company - {}", e.getMessage());
        }
        return addQuestionAndAnswers(callRegisterList);
    }


    // For Coordinator
    public List<CallRegister> getAppointmetsByStatusAndCompanyAndCoord(Integer status, Integer companyId, Integer userId){
        String sql;
        List<CallRegister> callRegisterList = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("status",status);
        mapSqlParameterSource.addValue("companyId", companyId);
        mapSqlParameterSource.addValue("userId", userId);

        sql = GET_APPOINTMENTS_BY_STATUS_AND_COORD;
        logger.info("SQL: {} - with status: {}, company: {}, coordinator userId: {}", sql, status, companyId, userId);
        try {
            callRegisterList = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new CallRegisterRowMapper());
        } catch (DataAccessException e) {
            logger.error("Error while getting appointments by status, company, and user: {}", e.getMessage());
        }
        return addQuestionAndAnswers(callRegisterList);
    }


    // For representative
    public List<CallRegister> getAppointmetsByStatusAndRepresentative(Integer status, Integer userId){
        String sql;
        List<CallRegister> callRegisterList = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("status",status);
        mapSqlParameterSource.addValue("representativeId", userId);

        sql = GET_APPOINTMENTS_BY_STATUS_AND_REPRESENTATIVE;
        logger.info("SQL: {} - with status {} and userId: {}", sql, status, userId);
        try {
            callRegisterList = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new CallRegisterRowMapper());
        } catch (DataAccessException e) {
            logger.error("Error while getting appointments by status and userId - {}", e.getMessage());
        }
        return addQuestionAndAnswers(callRegisterList);
    }


    public List<CallRegister> getAppointmetsByStatusAndCompanyAndCoordOfTeleMk(Integer status, Integer companyId, Integer userId){
        String sql;
        List<CallRegister> callRegisterList = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("status",status);
        mapSqlParameterSource.addValue("companyId", companyId);
        mapSqlParameterSource.addValue("userId", userId);

        sql = GET_APPOINTMENTS_BY_STATUS_COMPANY_COORDOFTELM;
        logger.info("SQL: {} - with status: {}, company: {}, coordinator userId: {}", sql, status, companyId, userId);
        try {
            callRegisterList = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new CallRegisterRowMapper());
        } catch (DataAccessException e) {
            logger.error("Error while getting appointments by status, company, and COORD: {}", e.getMessage());
        }
        return addQuestionAndAnswers(callRegisterList);
    }

    public List<CallRegister> getAppointmetsByResultAppAndOperator( Integer resultAppointmentId, Integer userId){
        String sql;
        List<CallRegister> callRegisterList = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("resultAppointmentId",resultAppointmentId);
        mapSqlParameterSource.addValue("employeeId", userId);

        sql = GET_APPOINTMENTS_BY_RESULTAPP_AND_OPERATOR;
        logger.info("SQL: {} - with result appointment id {} and userId: {}", sql, resultAppointmentId, userId);
        try {
            callRegisterList = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new CallRegisterRowMapper());
        } catch (DataAccessException e) {
            logger.error("Error while getting appointments by result appointment id and userId - {}", e.getMessage());
        }
        return addQuestionAndAnswers(callRegisterList);
    }


    // For Managers
    public List<CallRegister> getAppointmetsByResultAppAndCompany(Integer resultAppointmentId, Integer companyId, Integer userId){
        String sql;
        List<CallRegister> callRegisterList = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("resultAppointmentId",resultAppointmentId);
        mapSqlParameterSource.addValue("companyId", companyId);

        sql = GET_APPOINTMENTS_BY_RESULTAPP_COMPANY;
        logger.info("SQL: {} - with result appointment id: {}, company: {} and manager userId: {}", sql, resultAppointmentId, companyId, userId);
        try {
            callRegisterList = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new CallRegisterRowMapper());
        } catch (DataAccessException e) {
            logger.error("Error while getting appointments by result appointment id and company - {}", e.getMessage());
        }
        return addQuestionAndAnswers(callRegisterList);
    }


    // For Coordinator
    public List<CallRegister> getAppointmetsByResultAppAndCompanyAndCoord(Integer resultAppointmentId, Integer companyId, Integer userId){
        String sql;
        List<CallRegister> callRegisterList = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("resultAppointmentId",resultAppointmentId);
        mapSqlParameterSource.addValue("companyId", companyId);
        mapSqlParameterSource.addValue("userId", userId);

        sql = GET_APPOINTMENTS_BY_RESULTAPP_AND_COORD;
        logger.info("SQL: {} - with result appointment id: {}, company: {}, coordinator userId: {}", sql, resultAppointmentId, companyId, userId);
        try {
            callRegisterList = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new CallRegisterRowMapper());
        } catch (DataAccessException e) {
            logger.error("Error while getting appointments by result appointment id, company, and user: {}", e.getMessage());
        }
        return addQuestionAndAnswers(callRegisterList);
    }


    // For representative
    public List<CallRegister> getAppointmetsByResultAppAndRepresentative(Integer resultAppointmentId, Integer userId){
        String sql;
        List<CallRegister> callRegisterList = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("resultAppointmentId",resultAppointmentId);
        mapSqlParameterSource.addValue("representativeId", userId);

        sql = GET_APPOINTMENTS_BY_RESULTAPP_AND_REPRESENTATIVE;
        logger.info("SQL: {} - with result appointment id {} and userId: {}", sql, resultAppointmentId, userId);
        try {
            callRegisterList = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new CallRegisterRowMapper());
        } catch (DataAccessException e) {
            logger.error("Error while getting appointments by result appointment id and userId - {}", e.getMessage());
        }
        return addQuestionAndAnswers(callRegisterList);
    }


    public CallRegister getAppointmentById(Integer appointmentId, Integer userId){
        String sql;
        CallRegister callRegister = new CallRegister();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("appointmentId",appointmentId);

        sql = GET_APPOINTMENT_BY_ID;
        logger.info("SQL: {} - with appointmentId {} and userId: {}", sql, appointmentId, userId);
        try {
            callRegister = namedParameterJdbcTemplate.queryForObject(sql,mapSqlParameterSource, new CallRegisterRowMapper());
        } catch (DataAccessException e) {
            logger.error("Error while getting appointment by Id: {}", e.getMessage());
        }
        List<CallRegister> callRegisterList = addQuestionAndAnswers(Arrays.asList(callRegister));
        if (!CollectionUtils.isEmpty(callRegisterList)) callRegister = callRegisterList.get(0);
        return callRegister;
    }


    public List<QuestionRaw> getQuestionsRaw(){
        List<QuestionRaw> questionRawList = new ArrayList<>();
        String sql;
        sql = SELECT_QUESTIONVALUE;
        logger.info("SQL: {} ", sql);
        try {
            questionRawList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuestionRaw.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting appointments by status and userId - {}", e.getMessage());
        }
        return questionRawList;
    }


    public List<QuestionAppointment> getQuestionAndAnswerByAppointment(Integer appointmentId){
        String sql;
        List<QuestionAppointment> questionAndAnswerList = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("appointmentId",appointmentId);

        sql = GET_QUESTION_AND_ANSWER_BY_APPOINTMENT;
        logger.info("SQL: {} - with appointmentId: {}", sql, appointmentId);
        try {
            questionAndAnswerList = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new
                    BeanPropertyRowMapper<>(QuestionAppointment.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting question and answers by appointments  - {}", e.getMessage());
        }
        return questionAndAnswerList;
    }


    // Helper method to add a list of question and answer per appointment
    private List<CallRegister> addQuestionAndAnswers(List<CallRegister> callRegisterList) {
        Call call;
        Appointment appointment;
        Integer appointmentId;
        for (CallRegister callRegister: callRegisterList) {
            call = callRegister.getCall();
            appointment = call.getAppointment();
            appointmentId = appointment.getId();
            appointment.setQuestionsAppointment(getQuestionAndAnswerByAppointment(appointmentId));
        }
        return callRegisterList;
    }
}
