package com.timing.crm.api.Repository;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.View.CharactAppRepDetail;
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
public class CharactAppRepDetailRepository {

    public String sql;
    public static final String SELECT_CHARACTAPPBYREP_SEQ = "SELECT nextval('charactapp_repdetail_id_seq')";
    private static final String INSERT_CHARACTAPPBYREP_LOTE =
            "INSERT INTO CHARACTAPP_REPDETAIL (id, characteristicsapp_id, user_repdetail_id, created_on, updated_on ) VALUES " +
                    " (?, ?, ?, now(), now())";
    private static final String SELECT_CHARACTAPPBYREP_BY_REPDETAIL = "SELECT CRD.ID id, CAP.ID characteristicsAppId, CAP.DESCRIPTION " +
            "FROM CHARACTAPP_REPDETAIL CRD, CHARACTERISTICSAPP CAP WHERE CRD.CHARACTERISTICSAPP_ID = CAP.ID AND CRD.user_repdetail_id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CharacteristicsAppRepository characteristicsAppRepository;

    private final Logger logger = LoggerFactory.getLogger("CharactAppRepDetailRepository");

    public Integer getCharactAppByRepSequence(){
        Integer apprType_RepDetail_id = -1;

        try {
            apprType_RepDetail_id = jdbcTemplate.queryForObject(SELECT_CHARACTAPPBYREP_SEQ, Integer.class);
        } catch (DataAccessException e) {
            logger.error("Error while getting SELECT_CHARACTAPPBYREP_SEQ - " + e.getMessage());
        }
        return apprType_RepDetail_id;
    }

    public List<CharactAppRepDetail> createCharactAppByRep(List<CharactAppRepDetail> charactAppRepDetails, Integer userRepDetailId) {
        int[] result;
        String sqlValues = "";
        String sqlFull;
        logger.debug("SQL: {}", INSERT_CHARACTAPPBYREP_LOTE);

        try {

            result = jdbcTemplate.batchUpdate(INSERT_CHARACTAPPBYREP_LOTE, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Integer charactapp_repdetail_id = getCharactAppByRepSequence();
                    CharactAppRepDetail charactAppRepDetail = charactAppRepDetails.get(i);
                    preparedStatement.setInt(1, charactapp_repdetail_id);
                    preparedStatement.setInt(2, charactAppRepDetail.getCharacteristicsAppId());
                    preparedStatement.setInt(3, userRepDetailId);
                }

                @Override
                public int getBatchSize() {
                    return charactAppRepDetails.size();
                }
            });
            logger.info("Result: {}", result);
        }  catch (DataAccessException e) {
            logger.error("Error while Creating createCharactAppByRep - " + e.getMessage());
            throw new BadRequestException("Characteristics duplicated");
        }
        return charactAppRepDetails;
    }

    public List<CharactAppRepDetail> getAllCharactAppRepDetail(Integer userRepDetailId) {
        List<CharactAppRepDetail> charactAppRepDetails = new ArrayList<>();
        Object[] args = new Object[]{userRepDetailId};
        logger.debug("SQL: {}", SELECT_CHARACTAPPBYREP_BY_REPDETAIL);
        try {
            charactAppRepDetails = jdbcTemplate.query(SELECT_CHARACTAPPBYREP_BY_REPDETAIL, args, new BeanPropertyRowMapper(CharactAppRepDetail.class));
            logger.info("Result size: {}", charactAppRepDetails.size());
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting CharactAppRepDetails - " + e.getMessage());
        }
        return charactAppRepDetails;
    }

}
