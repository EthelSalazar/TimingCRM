package com.timing.crm.api.Repository;

import com.timing.crm.api.View.CharacteristicsApp;
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
public class CharacteristicsAppRepository {

    private final Logger logger = LoggerFactory.getLogger("CharacteristicsAppRepository");


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Cacheable("characteristicsApps")
    public List<CharacteristicsApp> getAllCharacteristicsApp() {
        List<CharacteristicsApp> characteristicsApps = new ArrayList<>();

        String sql = "SELECT id, description FROM CHARACTERISTICSAPP ";
        logger.debug("SQL: {}", sql);
        try {
            characteristicsApps = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CharacteristicsApp.class));
            logger.info("Result size: {}", characteristicsApps.size());
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting CharacteristicsApp - " + e.getMessage());
        }
        return characteristicsApps;
    }


}
