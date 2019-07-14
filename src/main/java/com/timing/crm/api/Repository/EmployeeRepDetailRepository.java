package com.timing.crm.api.Repository;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.View.EmployeeRepDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepDetailRepository {
    public String sql;
    public static final String SELECT_EMPLOYEEREP_SEQ = "SELECT nextval('Employee_RepDetail_id_seq')";
    private static final String INSERT_EMPLOYEEREP_LOTE =
            "INSERT INTO employee_repdetail (ID, USER_EMPLOYEE_ID, USER_REPDETAIL_ID, USER_SUPERVISOR_ID, CREATED_ON, UPDATED_ON, COMPANY_ID) VALUES ";
    private static final String UPDATE_EMPLOYEEREP_LOTE =
            "UPDATE employee_repdetail SET USER_EMPLOYEE_ID = ?, company_id = ?, USER_REPDETAIL_ID = ?, USER_SUPERVISOR_ID = ?, user_telemktcont_id = ?, " +
                    "updated_on = now() WHERE id = ?";
    private static final String SELECT_EMPLOYEEREPDETANDNAMEREP_BY_COMPANY = "SELECT erd.id id, erd.USER_EMPLOYEE_ID userEmployeeId, " +
            "erd.USER_SUPERVISOR_ID userSupervisorId, erd.USER_REPDETAIL_ID userRepdetailId, erd.user_telemktcont_id userTelemarketerContId, " +
            "erd.company_id companyId, rd.name nameRepDetail, TL.name nameEmployee, SUP.name nameSupervisor, TLCONT.name nameTelemarketerCont " +
            " FROM rep_detail rd, employee_repdetail erd " +
            "LEFT JOIN employee TL ON erd.USER_EMPLOYEE_ID = TL.users_Id " +
            "LEFT JOIN employee SUP ON erd.USER_SUPERVISOR_ID = SUP.users_Id " +
            "LEFT JOIN employee TLCONT ON erd.user_telemktcont_id = TLCONT.users_Id " +
            "WHERE erd.USER_REPDETAIL_ID = rd.USERS_ID " +
            "AND erd.company_id =  ?";
    private static final String SELECT_EMPLOYEEREPDET_BY_COMPANYANDUSERSUPERVISOR = "SELECT erd.id id, erd.USER_EMPLOYEE_ID userEmployeeId, " +
            "erd.USER_SUPERVISOR_ID userSupervisorId, erd.USER_REPDETAIL_ID userRepdetailId, erd.user_telemktcont_id userTelemarketerContId," +
            " erd.company_id companyId, rd.name nameRepDetail, TL.name nameEmployee, SUP.name nameSupervisor, TLCONT.name nameTelemarketerCont " +
            "FROM rep_detail rd, employee_repdetail erd " +
            "LEFT JOIN employee TL ON erd.USER_EMPLOYEE_ID = TL.users_Id " +
            "LEFT JOIN employee SUP ON erd.USER_SUPERVISOR_ID = SUP.users_Id " +
            "LEFT JOIN employee TLCONT ON erd.user_telemktcont_id = TLCONT.users_Id " +
            "WHERE erd.USER_REPDETAIL_ID = rd.USERS_ID AND erd.company_id = ? AND erd.USER_SUPERVISOR_ID = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Logger logger = LoggerFactory.getLogger("EmployeeRepDetailRepository");

    public Integer getEmployeeRepSequence(){
        Integer apprType_RepDetail_id = -1;

        try {
            apprType_RepDetail_id = jdbcTemplate.queryForObject(SELECT_EMPLOYEEREP_SEQ, Integer.class);
        } catch (DataAccessException e) {
            logger.error("Error while getting SELECT_EMPLOYEEREP_SEQ - " + e.getMessage());
        }
        return apprType_RepDetail_id;
    }

    public Integer createEmployeeRepDetail(List<EmployeeRepDetail> employeeRepDetails) {
        int result;
        Integer employee_repdetail_id;
        String sqlValues = "";
        String sqlFull;
        logger.debug("SQL: {}", INSERT_EMPLOYEEREP_LOTE);

        if(employeeRepDetails.size()>0) {
            for (EmployeeRepDetail employeeRepDetail : employeeRepDetails) {
                employee_repdetail_id = getEmployeeRepSequence();
                if (sqlValues == null || sqlValues.equals("")) {
                    sqlValues = "(" + employee_repdetail_id.toString() + ","
                            + employeeRepDetail.getUserEmployeeId() + "," + employeeRepDetail.getUserRepdetailId() + "," + employeeRepDetail.getUserSupervisorId()
                            + ", now(), now(), " + employeeRepDetail.getCompanyId() + " )";
                } else {
                    sqlValues = sqlValues + ", (" + employee_repdetail_id.toString() + ","
                            + employeeRepDetail.getUserEmployeeId() + "," + employeeRepDetail.getUserRepdetailId() + "," + employeeRepDetail.getUserSupervisorId()
                            + ", now(), now(), " + employeeRepDetail.getCompanyId() + " )";
                }
            }
        }

        sqlFull = INSERT_EMPLOYEEREP_LOTE + sqlValues;
        System.out.println("sqlFull: " + sqlFull);
        logger.debug("SQL with values: {}", sqlFull);
        try {
            result = jdbcTemplate.update(sqlFull);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Creating createEmployeeRep - " + e.getMessage());
            throw new BadRequestException("EmployeeRepDetail duplicated");
        }
        return result;
    }

    public void modifyEmployeeRepDetail(List<EmployeeRepDetail> employeeRepDetails) {
        int[] result = new int[0];
        logger.debug("SQL: {}", UPDATE_EMPLOYEEREP_LOTE);
        try {
            result = jdbcTemplate.batchUpdate(UPDATE_EMPLOYEEREP_LOTE, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    EmployeeRepDetail employeeRepDetail = employeeRepDetails.get(i);
                    if (employeeRepDetail.getUserEmployeeId() != null){
                        preparedStatement.setInt(1, employeeRepDetail.getUserEmployeeId());
                    } else {
                        preparedStatement.setNull(1, java.sql.Types.INTEGER);
                    }

                    preparedStatement.setInt(2, employeeRepDetail.getCompanyId());

                    if (employeeRepDetail.getUserRepdetailId() != null){
                        preparedStatement.setInt(3, employeeRepDetail.getUserRepdetailId());
                    } else {
                        preparedStatement.setNull(3, java.sql.Types.INTEGER);
                    }

                    if (employeeRepDetail.getUserSupervisorId() != null){
                        preparedStatement.setInt(4, employeeRepDetail.getUserSupervisorId());
                    } else {
                        preparedStatement.setNull(4, java.sql.Types.INTEGER);
                    }

                    if (employeeRepDetail.getUserTelemarketerContId() != null){
                        preparedStatement.setInt(5, employeeRepDetail.getUserTelemarketerContId());
                    } else {
                        preparedStatement.setNull(5, java.sql.Types.INTEGER);
                    }
                    preparedStatement.setInt(6, employeeRepDetail.getId());
                }

                @Override
                public int getBatchSize() {
                   return employeeRepDetails.size();
                }
            });
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while modifying EmployeeRepDetail - " + e.getMessage());
            throw new BadRequestException("Error to modify EmployeeRepDetail");
        }
        logger.info("modifyEmployeeRepDetail result: {}" + result);
    }

    public List<EmployeeRepDetail> employeeRepDetailByCompany (Integer companyId){
        List<EmployeeRepDetail> employeeRepDetails = new ArrayList<>();
        Object[] args = new Object[]{companyId};
        logger.info("SQL: {} - with companyId {}", SELECT_EMPLOYEEREPDETANDNAMEREP_BY_COMPANY, companyId);
        try {
            employeeRepDetails = jdbcTemplate.query(SELECT_EMPLOYEEREPDETANDNAMEREP_BY_COMPANY, args, new BeanPropertyRowMapper(EmployeeRepDetail.class));
            logger.debug("SQL: {} - with  companyId: {}", sql, companyId);
        } catch (DataAccessException e) {
            logger.error("Error while getting EmployeeRepDetail by companyId - " + e.getMessage());
        }
        return employeeRepDetails;
    }

    public List<EmployeeRepDetail> employeeRepDetByCompanyAndUserSupId (Integer companyId, Integer userSupervisorId){
        List<EmployeeRepDetail> employeeRepDetails = new ArrayList<>();
        Object[] args = new Object[]{companyId, userSupervisorId};
        logger.info("SQL: {} - with companyId {}, userSupervisorId: {}", SELECT_EMPLOYEEREPDET_BY_COMPANYANDUSERSUPERVISOR, companyId, userSupervisorId);
        try {
            employeeRepDetails = jdbcTemplate.query(SELECT_EMPLOYEEREPDET_BY_COMPANYANDUSERSUPERVISOR, args, new BeanPropertyRowMapper(EmployeeRepDetail.class));
            logger.debug("SQL: {} - with  companyId: {}, userSupervisorId: {} ", sql, companyId, userSupervisorId);
        } catch (DataAccessException e) {
            logger.error("Error while getting EmployeeRepDetail by companyId and userSupervisorId - " + e.getMessage());
        }
        return employeeRepDetails;
    }
}
