package com.timing.crm.api.Repository;

import com.timing.crm.api.View.Role;
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
public class RoleRepository {
    private final Logger logger = LoggerFactory.getLogger("RoleRepository");


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Cacheable("roles")
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();

        String sql = "SELECT id, description FROM role ";
        logger.debug("SQL: {}", sql);
        try {
            roles = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class));
            logger.info("Result size: {}", roles.size());
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting Roles - " + e.getMessage());
        }
        return roles;
    }
}
