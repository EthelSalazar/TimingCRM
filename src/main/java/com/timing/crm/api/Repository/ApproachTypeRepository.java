package com.timing.crm.api.Repository;

import com.timing.crm.api.View.ApproachType;
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
public class ApproachTypeRepository {
    private final Logger logger = LoggerFactory.getLogger("ApproachTypeRepository");


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Cacheable("approachTypes")
    public List<ApproachType> getAllApproachType() {
        List<ApproachType> approachTypes = new ArrayList<>();

        String sql = "SELECT id, description FROM APPROACH_TYPE ";
        System.out.println(sql);
        logger.debug("SQL: {}: {}", sql);
        try {
            approachTypes = jdbcTemplate.query(sql, new BeanPropertyRowMapper(ApproachType.class));
            logger.info("Result size: {}", approachTypes.size());
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting APPROACH_TYPE - " + e.getMessage());
        }
        return approachTypes;
    }
}
