package com.timing.crm.api.Repository;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.Services.ScheduleService;
import com.timing.crm.api.View.Schedule;
import com.timing.crm.api.View.ScheduleRepDetail;
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
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ScheduleRepDetailRepository {

    public String sql;
    public static final String SELECT_SCHEDULEBYREP_SEQ = "SELECT nextval('schedule_repdetail_id_seq')";
    private static final String INSERT_SCHEDULEBYREP_LOTE =
            "INSERT INTO schedule_repdetail (id, schedule_id, selected, user_repdetail_id, created_on, updated_on ) " +
                    " VALUES (?, ?, ?, ?, now(), now())";
    private static final String UPDATE_SCHEDULEREP_LOTE = "UPDATE schedule_repdetail " +
            "SET selected = ?, updated_on = now() WHERE id = ? ";
    private static final String GETALL_SCHEDULEBYUSERREPID = "SELECT SR.ID id, SR.SCHEDULE_ID scheduleId, SR.SELECTED selected, " +
            "SR.USER_REPDETAIL_ID userRepdetailId, S.DAY, S.HOUR FROM SCHEDULE_REPDETAIL SR, SCHEDULE S WHERE SR.SCHEDULE_ID = S.ID AND " +
            "SR.USER_REPDETAIL_ID = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ScheduleService scheduleService;

    private final Logger logger = LoggerFactory.getLogger("ScheduleRepDetailRepository");

    public Integer getScheduleByRepSequence(){
        Integer apprType_RepDetail_id = -1;

        try {
            apprType_RepDetail_id = jdbcTemplate.queryForObject(SELECT_SCHEDULEBYREP_SEQ, Integer.class);
        } catch (DataAccessException e) {
            logger.error("Error while getting SELECT_SCHEDULEBYREP_SEQ - " + e.getMessage());
        }
        return apprType_RepDetail_id;
    }

    public List<ScheduleRepDetail> createScheduleByRep(List<ScheduleRepDetail> scheduleRepDetails, Integer userRepDetailId) {
        int[] result = new int[0];
        logger.debug("SQL: {}", INSERT_SCHEDULEBYREP_LOTE);

        try {
            result = jdbcTemplate.batchUpdate(INSERT_SCHEDULEBYREP_LOTE, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Integer schedule_repdetail_id = getScheduleByRepSequence();
                    ScheduleRepDetail scheduleRepDetail = scheduleRepDetails.get(i);
                    preparedStatement.setInt(1, schedule_repdetail_id);
                    preparedStatement.setInt(2, scheduleRepDetail.getScheduleId());
                    if (scheduleRepDetail.getSelected() != null){
                        preparedStatement.setBoolean(3, scheduleRepDetail.getSelected());
                    }else{
                        preparedStatement.setNull(3, Types.BOOLEAN);
                    }
                    preparedStatement.setInt(4, userRepDetailId);
                    }

                @Override
                public int getBatchSize() {
                    return scheduleRepDetails.size();
                }
            });
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Creating createScheduleByRep - " + e.getMessage());
            throw new BadRequestException("Schedule duplicated");
        }
        return scheduleRepDetails;
    }

    public List<ScheduleRepDetail> createScheduleEmptyByRepDetail(Integer userRepDetailId) {
        int[] result = new int[0];
        List<Schedule> schedules = scheduleService.getListSchedule();
        List<ScheduleRepDetail> scheduleRepDetails = new ArrayList<>();
        logger.debug("SQL: {}", INSERT_SCHEDULEBYREP_LOTE);
        try {
            result = jdbcTemplate.batchUpdate(INSERT_SCHEDULEBYREP_LOTE, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Integer schedule_repdetail_id = getScheduleByRepSequence();
                    Schedule schedule = schedules.get(i);
                    preparedStatement.setInt(1, schedule_repdetail_id);
                    preparedStatement.setInt(2, schedule.getId());
                    preparedStatement.setNull(3, Types.BOOLEAN);
                    preparedStatement.setInt(4, userRepDetailId);
                }

                @Override
                public int getBatchSize() {
                    return schedules.size();
                }
            });
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Creating createScheduleByRep - " + e.getMessage());
            throw new BadRequestException("Schedule duplicated");
        }
        scheduleRepDetails = getScheduleByRep(userRepDetailId);
        return scheduleRepDetails;
    }

    public List<ScheduleRepDetail> modifyScheduleByRep(List<ScheduleRepDetail> scheduleRepDetails) {
        int[] result = new int[0];
        logger.debug("SQL: {}", UPDATE_SCHEDULEREP_LOTE);

        try {
            result = jdbcTemplate.batchUpdate(UPDATE_SCHEDULEREP_LOTE, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    ScheduleRepDetail scheduleRepDetail = scheduleRepDetails.get(i);
                    if (scheduleRepDetail.getSelected() != null){
                        preparedStatement.setBoolean(1, scheduleRepDetail.getSelected());
                    }else{
                        preparedStatement.setNull(1, Types.BOOLEAN);
                    }
                    preparedStatement.setInt(2, scheduleRepDetail.getId());
                }

                @Override
                public int getBatchSize() {
                    return scheduleRepDetails.size();
                }
            });
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Creating createScheduleByRep - " + e.getMessage());
            throw new BadRequestException("Schedule duplicated");
        }
        return scheduleRepDetails;
    }

    public List<ScheduleRepDetail> getScheduleByRep(Integer userRepDetailId) {
        List<ScheduleRepDetail> scheduleRepDetails = new ArrayList<>();
        Object[] args = new Object[]{userRepDetailId};
        logger.debug("SQL: {}", GETALL_SCHEDULEBYUSERREPID);
        try {
            scheduleRepDetails = jdbcTemplate.query(GETALL_SCHEDULEBYUSERREPID, args, new BeanPropertyRowMapper(Schedule.class));
            logger.info("Result size: {}", scheduleRepDetails.size());
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting ScheduleRepDetail - " + e.getMessage());
        }
        return scheduleRepDetails;
    }


}
