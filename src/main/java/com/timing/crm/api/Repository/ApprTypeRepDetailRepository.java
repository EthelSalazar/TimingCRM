package com.timing.crm.api.Repository;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.View.ApprTypeRepDetail;
import com.timing.crm.api.View.ApproachType;
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
public class ApprTypeRepDetailRepository {
    public String sql;
    public static final String SELECT_APPTYPEBYREP_SEQ = "SELECT nextval('apprtype_repdetail_id_seq')";
    private static final String INSERT_APPRTYPEREP_LOTE =
            "INSERT INTO apprtype_repdetail (id, approach_type_id, user_repdetail_id, created_on, updated_on ) VALUES " +
                    "(?,?,?,now(),now())";
    private static final String SELECT_APPRTYPEREP_BY_USERREPDETAILID = "SELECT APPREP.Id id, APPREP.APPROACH_TYPE_ID approachTypeId, " +
            "AT.DESCRIPTION description, APPREP.USER_REPDETAIL_ID userRepDetailId FROM APPRTYPE_REPDETAIL APPREP, APPROACH_TYPE AT " +
            "WHERE APPREP.APPROACH_TYPE_ID = AT.ID AND APPREP.USER_REPDETAIL_ID = ? ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Logger logger = LoggerFactory.getLogger("ApprTypeRepDetailRepository");

    public Integer getApproachTypesByRepSequence(){
        Integer apprType_RepDetail_id = -1;

        try {
            apprType_RepDetail_id = jdbcTemplate.queryForObject(SELECT_APPTYPEBYREP_SEQ, Integer.class);
        } catch (DataAccessException e) {
            logger.error("Error while getting SELECT_APPTYPEBYREP_SEQ - " + e.getMessage());
        }
        return apprType_RepDetail_id;
    }

    public List<ApprTypeRepDetail> createApproachTypesByRep(List<ApprTypeRepDetail> apprTypeRepDetails, Integer userRepDetailId) {
        int[] result;
        String sqlValues = "";
        String sqlFull;
        logger.debug("SQL: {}", INSERT_APPRTYPEREP_LOTE);

        try {
            result = jdbcTemplate.batchUpdate(INSERT_APPRTYPEREP_LOTE, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Integer apprType_RepDetail_id = getApproachTypesByRepSequence();
                    ApprTypeRepDetail apprTypeRepDetail = apprTypeRepDetails.get(i);
                    preparedStatement.setInt(1, apprType_RepDetail_id);
                    preparedStatement.setInt(2, apprTypeRepDetail.getApproachTypeId());
                    preparedStatement.setInt(3, userRepDetailId);
                }

                @Override
                public int getBatchSize() {
                    return apprTypeRepDetails.size();
                }
            });
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Creating createApproachTypesByRep - " + e.getMessage());
            throw new BadRequestException("Approach Type duplicated");
        }
        return apprTypeRepDetails;
    }
    public List<ApprTypeRepDetail> getApproachTypesByRep(Integer userRepDetailId) {
        List<ApprTypeRepDetail> apprTypeRepDetails = new ArrayList<>();
        Object[] args = new Object[]{userRepDetailId};
        logger.debug("SQL: {}", SELECT_APPRTYPEREP_BY_USERREPDETAILID);
        try {
            apprTypeRepDetails = jdbcTemplate.query(SELECT_APPRTYPEREP_BY_USERREPDETAILID, args, new BeanPropertyRowMapper(ApproachType.class));
            logger.info("Result size: {}", apprTypeRepDetails.size());
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting ApprTypeRepDetail - " + e.getMessage());
        }
        return apprTypeRepDetails;
    }


}
