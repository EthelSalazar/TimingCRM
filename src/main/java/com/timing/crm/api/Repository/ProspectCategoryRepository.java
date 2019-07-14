package com.timing.crm.api.Repository;

import com.timing.crm.api.View.ProspectCategory;
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
public class ProspectCategoryRepository {
    private final Logger logger = LoggerFactory.getLogger("ApproachTypeRepository");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Cacheable("prospectCategories")
    public List<ProspectCategory> getAllApproachType() {
        List<ProspectCategory> prospectCategories = new ArrayList<>();

        String sql = "SELECT id, description FROM PROSPECT_CATEGORY ";
        System.out.println(sql);
        logger.debug("SQL: {}: {}", sql);
        try {
            prospectCategories = jdbcTemplate.query(sql, new BeanPropertyRowMapper(ProspectCategory.class));
            logger.info("Result size: {}", prospectCategories.size());
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting PROSPECT_CATEGORY - " + e.getMessage());
        }
        return prospectCategories;
    }
}
