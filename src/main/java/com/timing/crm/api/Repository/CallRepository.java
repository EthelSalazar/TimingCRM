package com.timing.crm.api.Repository;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.View.Call;
import com.timing.crm.api.View.ReasonNotQualify;
import com.timing.crm.api.View.ResultCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CallRepository {
    private final Logger logger = LoggerFactory.getLogger("CallRepository");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_CALLS = "INSERT INTO call (id,result_call_id,notes,leads_id,users_repdetail_id,company_id," +
            "users_employee_id, REASONS_NOTQUALIFY_ID, FOLLOWUP_APPOINTMENT_ID, created_on, updated_on) " +
            " VALUES (?,?,?,?,?,?,?,?, ?, now(),now()) ";
    private static final String SELECT_CALL_BY_LEADID = "SELECT id,result_call_id,notes,leads_id,users_repdetail_id,company_id, " +
            "users_employee_id, REASONS_NOTQUALIFY_ID, FOLLOWUP_APPOINTMENT_ID followupAppointmentId, created_on, updated_on " +
            "FROM CALL WHERE leads_id = ? ";

    public Integer getCallSequence(){
        Integer employeeId = new Integer(-1);

        final String sql = "SELECT nextval('call_id_seq')";
        try {
            employeeId = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting CallSequence - " + e.getMessage());

        }
        return employeeId;
    }

    public Call createCall(Call call) {
        int result = 0;
        Integer callId = getCallSequence();
        Object[] args = new Object[]{callId, call.getResultCallId(), call.getNotes(), call.getLeadId(), call.getUserRepdetailId(),
        call.getCompanyId(), call.getUserEmployeeId(), call.getReasonsNotqualifyId(), call.getFollowupAppointmentId()};
        logger.debug("SQL: {} - with id: {}", INSERT_CALLS, call.getId());
        try {
            result = jdbcTemplate.update(INSERT_CALLS, args);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Creating Call - " + e.getMessage());
            throw new BadRequestException("Call duplicated");
        }
        logger.info("finish createCall with id: {} ", callId);
        call.setId(callId);
        return call;
    }

    public List<Call> getCallByLead(Integer leadId) {
        List<Call> calls = new ArrayList<>();
        Object[] args = new Object[]{leadId};
        logger.debug("SQL: {} - with leadId: {}", INSERT_CALLS, leadId);
        try {
            calls = jdbcTemplate.query(SELECT_CALL_BY_LEADID, args , new BeanPropertyRowMapper<>(Call.class));
            logger.info("Result size: {}", calls.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting call by leadId - " + e.getMessage());
        }
        return calls;
    }

    @Cacheable("resultCalls")
    public List<ResultCall> getAllResultCall() {
        List<ResultCall> resultCalls = new ArrayList<>();

        String sql = "SELECT id, description FROM Result_call ";
        System.out.println(sql);
        logger.debug("SQL: {}: {}", sql);
        try {
            resultCalls = jdbcTemplate.query(sql, new BeanPropertyRowMapper(ResultCall.class));
            logger.info("Result size: {}", resultCalls.size());
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting RESULT_CALL - " + e.getMessage());
        }
        return resultCalls;
    }

    @Cacheable("reasonNotQualifies")
    public List<ReasonNotQualify> getAllReasonNotQualify() {
        List<ReasonNotQualify> reasonNotQualifies = new ArrayList<>();

        String sql = "SELECT id, description FROM REASONS_NOTQUALIFY ";
        System.out.println(sql);
        logger.debug("SQL: {}: {}", sql);
        try {
            reasonNotQualifies = jdbcTemplate.query(sql, new BeanPropertyRowMapper(ResultCall.class));
            logger.info("Result size: {}", reasonNotQualifies.size());
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting REASONS_NOTQUALIFY - " + e.getMessage());
        }
        return reasonNotQualifies;
    }

}
