package com.timing.crm.api.Repository;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.View.CallRegister;
import com.timing.crm.api.View.ErrorLead;
import com.timing.crm.api.View.Lead;
import com.timing.crm.api.View.Reports.FileUploadReport;
import com.timing.crm.api.View.Reports.StatusCount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.timing.crm.api.Helper.ValidatorHelper.getApproachCode;
import static com.timing.crm.api.Utils.Constants.EMPTY_ID;

@Repository
public class LeadRepository {
    private static final String INSERT_LEAD_DETAILS = "INSERT INTO LEADS "
            + "(ID,DATE_DATA,RATING,NAME,MARITAL_STATUS,SPOUSE_NAME,PHONE,ZIP,CITY,USER_REPDETAIL_ID,"
            + "APPROACH_TYPE_ID,PROSPECT_CATEGORY_ID,APPROACH_PLACE,APPROACH_NOTES,APPROACH_NOTES_414,"
            + "STATUS,COMPANY_ID,USER_EMPLOYEE_ID,FILE_UPLOAD_LOG_FK,CREATED_ON,UPDATED_ON)"
            + "VALUES (nextval('leads_id_seq'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now()) ";

    private static final String SELECT_LEADS_BY_ID =
            "SELECT id, date_data, rating, name, marital_status, spouse_name, phone, zip, city, user_repdetail_id userRepdetailId, " +
                    "approach_type_id approachType, (SELECT description FROM approach_type WHERE id = " +
                    "approach_type_id) approachTypeDescription, "  +
                    "prospect_category_id prospectCategory, (SELECT description FROM prospect_category WHERE id = " +
                    "prospect_category_id) prospectCategoryDescription, approach_place approachPlace, " +
                    "approach_notes approachNotes, approach_notes_414 approachNotes414, address, status, host, host_id, company_id, " +
                    "user_employee_id userEmployeeId, created_on, updated_on, (Select name from leads where id = ld.host_Id) hostName "
                    + "FROM leads ld WHERE id = ?";

    private static final String SELECT_LEADS_BY_COMPANYID =
            "SELECT id, date_data, rating, name, marital_status, spouse_name, phone, zip, city, user_repdetail_id userRepdetailId, " +
                    "approach_type_id approachType, prospect_category_id prospectCategory, approach_place approachPlace, " +
                    "approach_notes approachNotes, approach_notes_414 approachNotes414, address, status, host, host_id, " +
                    "company_id, user_employee_id userEmployeeId, created_on, updated_on, (Select name from leads where id = ld.host_Id) hostName  "
                    + "FROM leads ld WHERE company_id = ?";

    private static final String SELECT_LEADS_BY_STATUSANDCOMPANYID =
            "SELECT id, date_data, rating, name, marital_status, spouse_name, phone, zip, city, user_repdetail_id userRepdetailId, " +
                    "approach_type_id approachType, prospect_category_id prospectCategory, approach_place approachPlace, " +
                    "approach_notes approachNotes, approach_notes_414 approachNotes414, address, status, host, host_id, company_id, " +
                    "user_employee_id userEmployeeId, created_on, updated_on, (Select name from leads where id = ld.host_Id) hostName "
                    + "FROM leads ld WHERE status = ? and company_id = ?";

    private static final String SELECT_LEADS_BY_STATUSCOMPANYIDANDREPDETAILID =
            "SELECT ld.id, ld.date_data, ld.rating, ld.name, ld.marital_status, ld.spouse_name, ld.phone, ld.zip, "
                    + "ld.city, ld.user_repdetail_id userRepdetailId, ld.approach_type_id approachType, "
                    + "ld.prospect_category_id prospectCategory, ld.approach_place approachPlace, "
                    + "ld.approach_notes approachNotes, ld.approach_notes_414 approachNotes414, ld.address, "
                    + "ld.status, ld.host, ld.host_id, ld.company_id, ld.user_employee_id userEmployeeId, "
                    + "ld.created_on, ld.updated_on, (Select name from leads where id = ld.host_Id) hostName, "
                    + "(SELECT NAME FROM REP_DETAIL WHERE USERS_ID = ld.USER_REPDETAIL_ID) nameRepDetail "
                    + " FROM leads ld WHERE ld.user_repdetail_id IN " +
                         "  (SELECT USER_REPDETAIL_ID FROM EMPLOYEE_REPDETAIL " +
                    "           WHERE USER_EMPLOYEE_ID = :userEmployeeId OR USER_TELEMKTCONT_ID = :userEmployeeId) "
                    +" AND ld.status = :status "
                    +" AND ld.company_id = :companyId";

    private static final String SELECT_LEADS_BY_STATUSCOMPANYID_COORDOFUSEREMPLOYEE =
            "SELECT ld.id, ld.date_data, ld.rating, ld.name, ld.marital_status, ld.spouse_name, ld.phone, ld.zip, " +
                    "ld.city, ld.user_repdetail_id userRepdetailId, ld.approach_type_id approachType, ld.prospect_category_id prospectCategory, " +
                    "ld.approach_place approachPlace, ld.approach_notes approachNotes, ld.approach_notes_414 approachNotes414, ld.address, " +
                    "ld.status, ld.host, ld.host_id, ld.company_id, ld.user_employee_id userEmployeeId, ld.created_on, ld.updated_on, " +
                    "(Select name from leads where id = ld.host_Id) hostName, " +
                    "(SELECT NAME FROM REP_DETAIL WHERE USERS_ID = ld.USER_REPDETAIL_ID) nameRepDetail " +
                    "FROM leads ld WHERE ld.user_repdetail_id IN " +
                    "(SELECT USER_REPDETAIL_ID FROM EMPLOYEE_REPDETAIL WHERE USER_EMPLOYEE_ID IN " +
                    " (SELECT USERS_ID FROM EMPLOYEE WHERE user_supervisor_id = :userEmployeeId)) " +
                    "AND ld.status = :status AND ld.company_id = :companyId";

    private static final String SELECT_LEADS_BY_STATUSCOMPANYID_COORD =
            "SELECT ld.id, ld.date_data, ld.rating, ld.name, ld.marital_status, ld.spouse_name, ld.phone, ld.zip, " +
                    "ld.city, ld.user_repdetail_id userRepdetailId, ld.approach_type_id approachType, ld.prospect_category_id prospectCategory, " +
                    "ld.approach_place approachPlace, ld.approach_notes approachNotes, ld.approach_notes_414 approachNotes414, ld.address, " +
                    "ld.status, ld.host, ld.host_id, ld.company_id, ld.user_employee_id userEmployeeId, ld.created_on, ld.updated_on, " +
                    "(Select name from leads where id = ld.host_Id) hostName, " +
                    "(SELECT NAME FROM REP_DETAIL WHERE USERS_ID = ld.USER_REPDETAIL_ID) nameRepDetail " +
                    "FROM leads ld WHERE ld.user_repdetail_id IN " +
                    "(SELECT USER_REPDETAIL_ID FROM EMPLOYEE_REPDETAIL WHERE USER_SUPERVISOR_ID = :userEmployeeId) " +
                    "AND ld.status = :status AND ld.company_id = :companyId";

    private static final String UPDATE_LEADS_BY_ID = "UPDATE LEADS SET  phone = ?, city = ?, " +
            "zip = ?, address = ?, rating = ?, marital_status = ?, spouse_name = ?, host_id = ?, host = ?, status = ?, updated_on = now() " +
            "WHERE id = ?";

    private static final String UPDATE_LEADSTATUS_BY_ID = "UPDATE LEADS SET status = ?, updated_on = now() WHERE id = ?";

    private static final String INSERT_FILE_UPLOAD =
            "INSERT INTO FILE_UPLOAD_LOG (id,file_name,total_records,total_succesful,total_error,status,created_on) "
                    + "VALUES (?,?,0,0,0,0,now())";

    private static final String UPDATE_FILE_UPLOAD_LOG = "UPDATE file_upload_log SET status = ? WHERE id = ?";

    private static final String UPDATE_FILE_UPLOAD_LOG_TOTAL = "UPDATE file_upload_log SET total_records = ?, "
            + "total_succesful = ?, total_error = ? WHERE id = ?";

    private static final String INSERT_ERROR_DETAILS = "INSERT INTO FILE_UPLOAD_ERROR_DETAILS (id,"
            + "file_upload_log_fk,line_number,line_value,error_message,created_on) "
            + "VALUES (nextval('file_upload_id_seq'),?,?,?,?,now())";

    private static final String GET_ERROR_DETAILS =
            "select line_number lineNumber, line_value lineValue, error_message "
                    + "errorMessage from file_upload_error_details where file_upload_log_fk = ? order by line_number";

    private static final String GET_BASIC_FILE_UPLOAD_REPORT = "select DISTINCT"
                    + "  fu.id fileUploadId, fu.created_on uploadDate, fu.file_name fileName, fu.total_records totalLines, fu.total_succesful totalLinesSuccessful, "
                    + "  fu.total_error totalLinesError, e.name employeeName, rd.name representativeName, l.company_id companyName "
                    + "from  file_upload_log fu, leads l, rep_detail rd, employee e where fu.id = l.file_upload_log_fk "
                    + "  and l.company_id = ? and rd.users_id = l.user_repdetail_id and e.users_id = l.user_employee_id"
                    + "  and fu.created_on between to_timestamp(?,'dd/mm/yyyy HH24:MI:SS') and to_timestamp(?,'dd/mm/yyyy HH24:MI:SS')";

    private static final String GET_BASIC_FILE_UPLOAD_REPORT_EMPLOYEE_AND_REP = "select DISTINCT"
            + "  fu.id fileUploadId, fu.created_on uploadDate, fu.file_name fileName, fu.total_records totalLines, fu.total_succesful totalLinesSuccessful, "
            + "  fu.total_error totalLinesError, e.name employeeName, rd.name representativeName, l.company_id companyName "
            + "from  file_upload_log fu, leads l, rep_detail rd, employee e where fu.id = l.file_upload_log_fk "
            + "  and l.company_id = ? and rd.users_id = l.user_repdetail_id and l.user_repdetail_id = ? and e.users_id = l.user_employee_id "
            + "  and l.user_employee_id = ?"
            + "  and fu.created_on between to_timestamp(?,'dd/mm/yyyy HH24:MI:SS') and to_timestamp(?,'dd/mm/yyyy HH24:MI:SS')";

    private static final String GET_BASIC_FILE_UPLOAD_REPORT_EMPLOYEE = "select DISTINCT"
            + "  fu.id fileUploadId, fu.created_on uploadDate, fu.file_name fileName, fu.total_records totalLines, fu.total_succesful totalLinesSuccessful, "
            + "  fu.total_error totalLinesError, e.name employeeName, rd.name representativeName, l.company_id companyName "
            + "from  file_upload_log fu, leads l, rep_detail rd, employee e where fu.id = l.file_upload_log_fk "
            + "  and l.company_id = ? and rd.users_id = l.user_repdetail_id and e.users_id = l.user_employee_id "
            + "  and l.user_employee_id = ?"
            + "  and fu.created_on between to_timestamp(?,'dd/mm/yyyy HH24:MI:SS') and to_timestamp(?,'dd/mm/yyyy HH24:MI:SS')";

    private static final String  GET_BASIC_FILE_UPLOAD_REPORT_REP = "select DISTINCT"
            + "  fu.id fileUploadId, fu.created_on uploadDate, fu.file_name fileName, fu.total_records totalLines, fu.total_succesful totalLinesSuccessful, "
            + "  fu.total_error totalLinesError, e.name employeeName, rd.name representativeName, l.company_id companyName "
            + "from  file_upload_log fu, leads l, rep_detail rd, employee e where fu.id = l.file_upload_log_fk "
            + "  and l.company_id = ? and rd.users_id = l.user_repdetail_id and l.user_repdetail_id = ? and e.users_id = l.user_employee_id "
            + "  and fu.created_on between to_timestamp(?,'dd/mm/yyyy HH24:MI:SS') and to_timestamp(?,'dd/mm/yyyy HH24:MI:SS')";

    private static final String GET_REPRESENTATIVE_SUMMARY_REPORT =
            "select rs.result_call_id statusCall, count(*) total"
            + " from"
            + " ("
            + "    select * from call c, leads l where"
            + "    c.leads_id = l.id"
            + "    and l.user_repdetail_id = ?"
            + "    and l.created_on between to_timestamp(?,'dd/mm/yyyy HH24:MI:SS') and to_timestamp"
            + "    (?,'dd/mm/yyyy HH24:MI:SS') and c.created_on = (select max(created_on) from call m where"
            + "    c.leads_id = m.leads_id)"
            +"  ) as rs group by result_call_id";

    private static final String GET_TOTAL_LEADS_REPRESENTATIVE =
            " select count(*) totalLeads from leads where"
            +" user_repdetail_id = ?"
            +" and created_on between to_timestamp(?,'dd/mm/yyyy HH24:MI:SS') "
            + " and to_timestamp(?,'dd/mm/yyyy HH24:MI:SS')";

    private static final String GET_LEADS_BY_HOSTID =
            "SELECT id, date_data, rating, name, marital_status, spouse_name, phone, zip, city, user_repdetail_id userRepdetailId, " +
                    "approach_type_id approachType, prospect_category_id prospectCategory, approach_place approachPlace, " +
                    "approach_notes approachNotes, approach_notes_414 approachNotes414, address, status, host, host_id, " +
                    "company_id, user_employee_id userEmployeeId, created_on, updated_on, " +
                    "(Select name from leads where id = ld.host_Id) hostName  " +
                    "FROM leads ld " +
                    "WHERE host_Id = ? ";


    private static final String INSERT_SINGLE_LEAD = "INSERT INTO LEADS " +
            "(ID,DATE_DATA,RATING,NAME,MARITAL_STATUS,SPOUSE_NAME,PHONE,ZIP,CITY,USER_REPDETAIL_ID, " +
            "APPROACH_TYPE_ID,PROSPECT_CATEGORY_ID,APPROACH_PLACE,APPROACH_NOTES,APPROACH_NOTES_414, " +
            "STATUS,COMPANY_ID,USER_EMPLOYEE_ID,HOST_ID,Address,FILE_UPLOAD_LOG_FK,CREATED_ON,UPDATED_ON)" +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now()) ";

    private static final String GET_REPRESENTATIVE_SUMMARY_AND_DETAILS = "SELECT"
            + "  a.id appointmentId, a.date_appoint dateAppoint, a.result_appointment_id resultAppId, a.coord_comments coordComments,"
            + "  a.teleope_comments teleComments, a.status appointStatus, a.users_repdetail_id userRepdetailId, a.company_id companyId,"
            + "  a.users_employee_id userEmployeeId, l.id leadsId, a.comment_summary commentSummary, "
            + "  a.APPOINTMENT_PARENT_ID appointmentParentId, a.repdetail_comment repdetailComment, c.id callId, c.created_on callDate, "
            + "  c.result_call_id resultCallId, (SELECT description FROM result_call WHERE id = c.result_call_id) "
            + "  resultCallDescription, c.reasons_notqualify_id reasonsNotqualifyId, "
            + "  (SELECT description FROM reasons_notqualify WHERE id = c.reasons_notqualify_id) "
            + "  reasonsNotqualifyDescription, c.notes notes, "
            + "  c.FOLLOWUP_APPOINTMENT_ID followupAppointmentId, "
            + "  l.id leadsId, l.date_data dateData, l.rating rating, l.name leadName, l.marital_status maritalStatus,"
            + "  l.spouse_name spouseName, l.phone phone, l.zip zip, l.city city,"
            + "  l.address address, l.status leadStatus, l.approach_type_id approachType,"
            + "  (SELECT description FROM approach_type WHERE id = l.approach_type_id) approachTypeDescription,"
            + "  l.prospect_category_id prospectCategory, "
            + "  (SELECT description FROM prospect_category WHERE id = l.prospect_category_id) prospectCategoryDescription,"
            + "  l.approach_place approachPlace, l.approach_notes approachNotes, l.approach_notes_414 approachNotes414, l.host host,"
            + "  l.host_id hostId, l.created_on createdOn, l.updated_on updatedOn, (Select name from leads where id = l.host_Id) hostName,"
            + "  (SELECT name FROM rep_detail WHERE users_id = l.user_repdetail_id) nameRepDetail FROM"
            + "  leads l LEFT JOIN call c ON l.id = c.leads_id LEFT JOIN appointment a ON c.id = a.call_id "
            + "  WHERE l.user_repdetail_id = c.users_repdetail_id"
            + "  AND l.user_repdetail_id = ?"
            + "  AND l.created_on between to_timestamp(?,'dd/mm/yyyy HH24:MI:SS') and to_timestamp(?,'dd/mm/yyyy HH24:MI:SS') "
            + "  ORDER BY l.id, c.created_on DESC";

    private static final String GET_LEADS_BY_REPRESENTATIVE_DATE = "select id, date_data, rating, name, marital_status, spouse_name, phone, " +
            "zip, city, user_repdetail_id userRepdetailId, approach_type_id approachType, " +
            "(SELECT description FROM approach_type WHERE id = l.approach_type_id) approachTypeDescription, " +
            "prospect_category_id prospectCategory, (SELECT description FROM prospect_category WHERE id = l.prospect_category_id) prospectCategoryDescription, " +
            "approach_place approachPlace, approach_notes approachNotes, approach_notes_414 approachNotes414, address, status, host, host_id," +
            "company_id, user_employee_id userEmployeeId, created_on, updated_on, " +
            "(Select name from leads where id = l.host_Id) hostName  " +
            "from leads l " +
            "where l.user_repdetail_id = ? " +
            "and l.created_on between to_timestamp(?,'dd/mm/yyyy HH24:MI:SS') and to_timestamp(?,'dd/mm/yyyy HH24:MI:SS')";
//TODO: replace espacio en blanco

    private final Logger logger = LoggerFactory.getLogger("LeadRepository");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ErrorLead insertLeadDetails(Lead lead, Integer repId, Integer companyId, Integer userEmployeeId,
            Integer fileUploadId, Integer lineNumber) {

        logger.debug("SQL INSERT: {} - ", INSERT_LEAD_DETAILS);
        ErrorLead errorLead = new ErrorLead();
        // Inserting and check for duplicates
        if (lead != null) {
            try {

                jdbcTemplate.update(INSERT_LEAD_DETAILS, lead.getDateData(), lead.getRating(), lead.getName(),
                        lead.getMaritalStatus(), lead.getSpouseName(), lead.getPhone(), lead.getZip(), lead.getCity(),
                        repId, lead.getApproachType()==null?null:Integer.parseInt(lead.getApproachType()),
                        lead.getProspectCategory()==null?null:Integer.parseInt(lead.getProspectCategory()), lead.getApproachPlace(),
                        lead.getApproachNotes(), lead.getApproachNotes414(), 0, companyId, userEmployeeId, fileUploadId);
                logger.info("insertLeadDetails() - Lead Name: {}", lead.getName());

            } catch (DataAccessException e) {
                // Is a duplicate or constraint violation
                logger.error("Error while inserting data: {}", e.getLocalizedMessage());
                errorLead = new ErrorLead();
                errorLead.setLineNumber(lineNumber);
                errorLead.setErrorMessage("Duplicate record (Name, Phone, Representative)");
                errorLead.setLineValue(lead.getName() + "," + lead.getPhone());
            } catch (Exception e) {
                    // Is an invalid code
                    logger.error("Error while inserting data: {}", e.getLocalizedMessage());
                    errorLead = new ErrorLead();
                    errorLead.setLineNumber(lineNumber);
                    errorLead.setErrorMessage("Invalid Approach or Category value");
                    errorLead.setLineValue(lead.getName() + "," + lead.getPhone());
            }
        }
        return errorLead;
    }

    public Integer getFileUploadSequence(){
        Integer fileUploadId = new Integer(-1);

        final String sql = "SELECT nextval('file_upload_id_seq')";
        try {
            fileUploadId = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return fileUploadId;
    }

    public Integer getLeadIdSequence(){
        Integer leadsId = new Integer(-1);

        final String sql = "SELECT nextval('leads_id_seq')";
        try {
            leadsId = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return leadsId;
    }

    public Lead getLeadById(Integer id) {
        Object[] args = new Object[]{id};
        Lead lead = new Lead();
        logger.debug("SQL: {} - with id: {}", SELECT_LEADS_BY_ID, id);
        try {
            lead = jdbcTemplate.queryForObject(SELECT_LEADS_BY_ID, args, new BeanPropertyRowMapper<>(Lead.class));
        } catch (EmptyResultDataAccessException e){
            lead.setId(EMPTY_ID);
            logger.info("Lead with id: {} does not exist", id);
        } catch (DataAccessException e) {
            logger.error("Error while getting Leads - " + e.getMessage());
        }
        logger.info("lead: " + lead.toString());
        return lead;
    }

    public Lead modifyLead(Lead lead) {
        int result;
        Object[] args = new Object[]{lead.getPhone(), lead.getCity(), lead.getZip(), lead.getAddress(), lead.getRating(),
                lead.getMaritalStatus(), lead.getSpouseName(), lead.getHostId(), lead.getHost(), lead.getStatus(), lead.getId()};

        logger.debug("SQL: {} - with id: {}", UPDATE_LEADS_BY_ID, lead.getId());
        try {
            result = jdbcTemplate.update(UPDATE_LEADS_BY_ID, args);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while updating Lead - " + e.getMessage());
        }
        return lead;
    }

    public List<Lead> getLeadsByCompany(Integer companyId) {
        List<Lead> leads = new ArrayList<>();
        Object[] args = new Object[]{companyId};

        logger.debug("SQL: {} - with companyId: {}", SELECT_LEADS_BY_COMPANYID, companyId);
        try {
            leads = jdbcTemplate.query(SELECT_LEADS_BY_COMPANYID, args, new BeanPropertyRowMapper<>(Lead.class));
            logger.info("Result size: {}", leads.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting leads by CcompanyId - " + e.getMessage());
        }
        return leads;
    }

    public List<Lead> getLeadsByStatusAndCompany(Integer status, Integer companyId) {
        List<Lead> leads = new ArrayList<>();
        Object[] args = new Object[]{status, companyId};

        logger.debug("SQL: {} - with name: {}", SELECT_LEADS_BY_STATUSANDCOMPANYID, status);
        try {
            leads = jdbcTemplate.query(SELECT_LEADS_BY_STATUSANDCOMPANYID, args, new BeanPropertyRowMapper<>(Lead.class));
            logger.info("Result size: {}", leads.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting leads by status and companyId - " + e.getMessage());
        }
        return leads;
    }


    public List<Lead> getLeadsByStatusCompanyAndCoordOfTele(Integer status, Integer companyId, Integer userCoordId){
        String sql;
        List<Lead> leads = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("status",status);
        mapSqlParameterSource.addValue("companyId", companyId);
        mapSqlParameterSource.addValue("userEmployeeId", userCoordId);

        sql = SELECT_LEADS_BY_STATUSCOMPANYID_COORDOFUSEREMPLOYEE;
        logger.info("SQL: {} - with status {} and companyId {} and userEmployeeId: {}", sql, status, companyId, userCoordId);
        try {
            leads = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new BeanPropertyRowMapper(Lead.class));
            logger.debug("SQL: {} - with  status: {}, companyId: {}, userCoordId: {}", sql, status, companyId, userCoordId);
        } catch (DataAccessException e) {
            logger.error("Error while getting Leads by status and companyId" + e.getMessage());
        }
        return leads;
    }

    public List<Lead> getLeadsByStatusCompanyAndCoord(Integer status, Integer companyId, Integer userCoordId){
        String sql;
        List<Lead> leads = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("status",status);
        mapSqlParameterSource.addValue("companyId", companyId);
        mapSqlParameterSource.addValue("userEmployeeId", userCoordId);

        sql = SELECT_LEADS_BY_STATUSCOMPANYID_COORD;
        logger.info("SQL: {} - with status {} and companyId {} and userCoordId: {}", sql, status, companyId, userCoordId);
        try {
            leads = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new BeanPropertyRowMapper(Lead.class));
            logger.debug("SQL: {} - with  status: {}, companyId: {}, userCoordId: {}", sql, status, companyId, userCoordId);
        } catch (DataAccessException e) {
            logger.error("Error while getting Leads by status, companyId and CoordId" + e.getMessage());
        }
        return leads;
    }

    public List<Lead> getLeadsByStatusCompanyAndRepDetail(Integer status, Integer companyId, Integer userId){
        String sql;
        List<Lead> leads = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("status",status);
        mapSqlParameterSource.addValue("companyId", companyId);
        mapSqlParameterSource.addValue("userEmployeeId", userId);

        sql = SELECT_LEADS_BY_STATUSCOMPANYIDANDREPDETAILID;
        logger.info("SQL: {} - with status {} and companyId {} and userEmployeeId: {}", sql, status, companyId, userId);
        try {
            leads = namedParameterJdbcTemplate.query(sql,mapSqlParameterSource, new BeanPropertyRowMapper(Lead.class));
            logger.debug("SQL: {} - with  status: {}, companyId: {}", sql, status, companyId);
        } catch (DataAccessException e) {
            logger.error("Error while getting Leads by status and companyId" + e.getMessage());
        }
        return leads;
    }


    public Integer createFileUpload(String fileName) {
        int result = 0;
        Integer fileUploadId = getFileUploadSequence();
        Object[] args = new Object[]{fileUploadId,fileName};
        logger.debug("SQL: {} - with file name: {}", INSERT_FILE_UPLOAD, fileName);
        try {
            result = jdbcTemplate.update(INSERT_FILE_UPLOAD, args);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while Creating User - " + e.getMessage());
            throw new BadRequestException("Error creating file upload");
        }
        return fileUploadId;
    }

    public void updateFileUploadStatus(Integer fileUploadId, Integer status) {
        Object[] args = new Object[]{status, fileUploadId};

        String sql = UPDATE_FILE_UPLOAD_LOG;
        logger.debug("SQL: {} - with id: {}", sql, fileUploadId);
        try {
            jdbcTemplate.update(sql, args);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while updating file upload log - " + e.getMessage());
        }
    }

    public void updateFileUploadTotals(Integer fileUploadId, Integer totalRecords, Integer totalSuccessful, Integer
            totalError) {
        Object[] args = new Object[]{totalRecords, totalSuccessful, totalError, fileUploadId};

        String sql = UPDATE_FILE_UPLOAD_LOG_TOTAL;
        logger.debug("SQL: {} - with id: {}", sql, fileUploadId);
        try {
            jdbcTemplate.update(sql, args);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while updating file upload log - " + e.getMessage());
        }
    }

    public void insertErrorDetails(List<ErrorLead> errorLeadList, Integer fileUploadId) {
        logger.debug("SQL BATCH: {} - ", INSERT_ERROR_DETAILS);
        jdbcTemplate.batchUpdate(INSERT_ERROR_DETAILS,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ErrorLead errorLead = errorLeadList.get(i);
                        ps.setInt(1, fileUploadId);
                        ps.setInt(2, errorLead.getLineNumber());
                        ps.setString(3, errorLead.getLineValue());
                        ps.setString(4, errorLead.getErrorMessage());
                    }

                    @Override
                    public int getBatchSize() {
                        return errorLeadList.size();
                    }
                });
    }

    public List<ErrorLead> getErrorLeadsByFileId(Integer fileUploadId) {
        List<ErrorLead> errorLeadList = new ArrayList<>();
        Object[] args = new Object[]{fileUploadId};

        logger.debug("SQL: {} - with fileUploadId: {}", GET_ERROR_DETAILS, fileUploadId);
        try {
            errorLeadList = jdbcTemplate.query(GET_ERROR_DETAILS, args, new BeanPropertyRowMapper<>(ErrorLead.class));
            logger.info("Result size: {}", errorLeadList.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting error leads by file upload Id - " + e.getMessage());
        }
        return errorLeadList;
    }

    public List<FileUploadReport> getBasicFileUploadReport(Integer companyId, String startDate, String endDate,
            Integer userEmployeeId, Integer userRepDetailId) {
        List<FileUploadReport> fileUploadReportList = new ArrayList<>();
        String sql;
        Object[] args;

        if (userEmployeeId!=null && userRepDetailId!=null) {
            sql = GET_BASIC_FILE_UPLOAD_REPORT_EMPLOYEE_AND_REP;
            args = new Object[]{companyId, userRepDetailId, userEmployeeId, startDate, endDate};

        } else if (userEmployeeId!=null && userRepDetailId==null) {
            sql = GET_BASIC_FILE_UPLOAD_REPORT_EMPLOYEE;
            args = new Object[]{companyId, userEmployeeId, startDate, endDate};

        } else if (userEmployeeId==null && userRepDetailId!=null) {
            sql = GET_BASIC_FILE_UPLOAD_REPORT_REP;
            args = new Object[]{companyId, userRepDetailId, startDate, endDate};

        } else {
            sql = GET_BASIC_FILE_UPLOAD_REPORT;
            args = new Object[]{companyId, startDate, endDate};
        }

        logger.debug("SQL: {} - with companyId: {}", sql, companyId);
        try {
            fileUploadReportList = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<>(FileUploadReport.class));
            logger.info("Result size: {}", fileUploadReportList.size());
        } catch (DataAccessException e) {
            logger.error("Error while getBasicFileUploadReport - {}", e.getMessage());
        }
        return fileUploadReportList;
    }

    public List<StatusCount> getSummaryStatusCallByRepAndDate(Integer userRepDetailId, String startDate, String endDate) {
        List<StatusCount> statusCountList = new ArrayList<>();
        String sql;
        Object[] args;
        sql = GET_REPRESENTATIVE_SUMMARY_REPORT;
        args = new Object[]{userRepDetailId, startDate, endDate};

        logger.debug("SQL: {} - with userRepDetailId: {}", sql, userRepDetailId);
        try {
            statusCountList = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<>(StatusCount.class));
            logger.info("Result size: {}", statusCountList.size());
        } catch (DataAccessException e) {
            logger.error("Error while getSummaryStatusCallByRepAndDate - {}", e.getMessage());
        }
        return statusCountList;
    }

    public Integer getTotalLeadByRepAndDateRange(Integer userRepDetailId, String startDate, String endDate) {
        Integer totalLeads = 0;
        Object[] args;
        String sql = GET_TOTAL_LEADS_REPRESENTATIVE;
        args = new Object[]{userRepDetailId, startDate, endDate};

        logger.debug("SQL: {} - with userRepDetailId: {}", sql, userRepDetailId);
        try {
            totalLeads = jdbcTemplate.queryForObject(sql, args, Integer.class);
        } catch (DataAccessException e) {
            logger.error("Error while getSummaryStatusCallByRepAndDate - {}", e.getMessage());
        }

        return totalLeads;
    }

    public List<Lead> getLeadsByHostId(Integer hostId) {
        List<Lead> leads = new ArrayList<>();
        Object[] args;
        args = new Object[]{hostId};
        logger.debug("SQL: {}, hostId: {}", GET_LEADS_BY_HOSTID, hostId);
        try {
            leads = jdbcTemplate.query(GET_LEADS_BY_HOSTID, args, new BeanPropertyRowMapper<>(Lead.class));
            logger.info("Result size: {}", leads.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", leads.size());
            logger.error("Error while getting all leads by HostId - " + e.getMessage());
        }
        return leads;
    }

    public Lead insertSingleLead(Lead lead, Integer repId, Integer companyId, Integer userEmployeeId) {

        logger.debug("SQL INSERT: {} - ", INSERT_LEAD_DETAILS);
        Integer leadId = getLeadIdSequence();
        if (lead != null) {
            try {

                jdbcTemplate.update(INSERT_SINGLE_LEAD, leadId, lead.getDateData(), lead.getRating(), lead.getName(),
                        lead.getMaritalStatus(), lead.getSpouseName(), lead.getPhone(), lead.getZip(), lead.getCity(),
                        repId, Integer.parseInt(lead.getApproachType()), Integer.parseInt(lead.getProspectCategory()),
                        lead.getApproachPlace(), lead.getApproachNotes(), lead.getApproachNotes414(), 0, companyId, userEmployeeId,
                        lead.getHostId(), lead.getAddress(), null);
                lead.setId(leadId);
                logger.info("insertLeadDetails() - Lead Name: {}", lead.getName());

            } catch (DataAccessException e) {
                // Is a duplicate
                e.printStackTrace();
                logger.error("Error while updating Lead - " + e.getMessage());
            }
        }
        return lead;
    }

    public List<CallRegister> getRepresentativeSummaryAndDetail(Integer userRepDetailId, String startDate, String endDate){
        String sql;
        List<CallRegister> callRegisterList = new ArrayList<>();
        Object[] args = new Object[]{userRepDetailId, startDate, endDate};

        sql = GET_REPRESENTATIVE_SUMMARY_AND_DETAILS;
        logger.info("SQL: {} - with userRepDetailId {} and startDate: {} and endDate: {}", sql, userRepDetailId,
                startDate, endDate);
        try {
            callRegisterList = jdbcTemplate.query(sql,args, new CallRegisterRowMapper());
        } catch (DataAccessException e) {
            logger.error("Error while getting getRepresentativeSummaryAndDetail - {}", e.getMessage());
        }
        return callRegisterList;
    }

    public List<Lead> getLeadByRepAndDates(Integer userRepDetailId, String startDate, String endDate){
        String sql;
        List<Lead> leadList = new ArrayList<>();
        Object[] args = new Object[]{userRepDetailId, startDate, endDate};

        sql = GET_LEADS_BY_REPRESENTATIVE_DATE;
        logger.info("SQL: {} - with userRepDetailId {} and startDate: {} and endDate: {}", sql, userRepDetailId,
                startDate, endDate);
        try {
            leadList = jdbcTemplate.query(sql,args, new BeanPropertyRowMapper<>(Lead.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting getLeadByRepAndDates - {}", e.getMessage());
        }
        logger.info("Completing getLeadByRepAndDates");
        return leadList;

    }

    @SuppressWarnings("all")
    public List<Lead> getLeadByRepPhoneDatesOrName(Integer userRepDetailId, String phone, String startDate, String endDate, String name){
        String sql;
        List<Lead> leadList = new ArrayList<>();
        Object[] args;
        if (!StringUtils.isEmpty(phone)) {
            if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
                if (!StringUtils.isEmpty(name)) {
                    args = new Object[]{userRepDetailId, phone, "%" + name + "%", startDate, endDate};
                } else {
                    args = new Object[]{userRepDetailId, phone, startDate, endDate};
                }
            }else {
                if (!StringUtils.isEmpty(name)) {
                    args = new Object[]{userRepDetailId, phone, "%" + name + "%"};
                } else {
                    args = new Object[]{userRepDetailId, phone};
                }
            }
        }else {
            if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
                if (!StringUtils.isEmpty(name)) {
                    args = new Object[]{userRepDetailId, "%" + name + "%", startDate, endDate};
                } else {
                    args = new Object[]{userRepDetailId, startDate, endDate};
                }
            }else {
                if (!StringUtils.isEmpty(name)) {
                    args = new Object[]{userRepDetailId, "%" + name + "%"};
                } else {
                    args = new Object[]{userRepDetailId};
                }
            }
        }
        sql = "select id, date_data, rating, name, marital_status, spouse_name, phone, " +
                "zip, city, user_repdetail_id userRepdetailId, approach_type_id approachType, " +
                "(SELECT description FROM approach_type WHERE id = l.approach_type_id) approachTypeDescription, " +
                "prospect_category_id prospectCategory, (SELECT description FROM prospect_category WHERE id = l.prospect_category_id) prospectCategoryDescription, " +
                "approach_place approachPlace, approach_notes approachNotes, approach_notes_414 approachNotes414, address, status, host, host_id," +
                "company_id, user_employee_id userEmployeeId, created_on, updated_on, " +
                "(Select name from leads where id = l.host_Id) hostName  " +
                "from leads l " +
                "where l.user_repdetail_id = ? ";
        if (!StringUtils.isEmpty(phone)) {
            sql = sql + " and replace(replace(replace(replace(l.phone,'-',''),'(',''), ')',''),'+','') = replace(replace(replace(replace(?,'-',''),'(',''), ')',''),'+','')";
        }
        if (!StringUtils.isEmpty(name)){
            sql = sql + " and UPPER(l.name) like UPPER(?)";
        }
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            sql = sql + " and l.created_on between ?::timestamp and (?::timestamp + '1 day'::interval)";
        }
        logger.info("SQL: {} - with userRepDetailId {}, phone: {}, name{}, startDate: {} and endDate: {}", sql, userRepDetailId, phone, name, startDate, endDate);
        try {
            leadList = jdbcTemplate.query(sql,args, new BeanPropertyRowMapper<>(Lead.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting getLeadByRepPhoneDatesOrName - {}", e.getMessage());
        }
        logger.info("Completing getLeadByRepPhoneDatesOrName");
        return leadList;

    }

}
