package com.timing.crm.api.Repository;

import com.timing.crm.api.View.Schedule;
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
public class ScheduleRepository {
    private final Logger logger = LoggerFactory.getLogger("ScheduleRepository");


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Cacheable("schedules")
    public List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();

        String sql = "SELECT id, day, hour FROM schedule ";
        logger.debug("SQL: {}", sql);
        try {
            schedules = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Schedule.class));
            logger.info("Result size: {}", schedules.size());
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting Schedule - " + e.getMessage());
        }
        return schedules;
    }
}
