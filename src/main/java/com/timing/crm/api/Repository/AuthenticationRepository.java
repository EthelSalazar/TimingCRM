package com.timing.crm.api.Repository;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.View.Authentication;
import com.timing.crm.api.View.Users;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.timing.crm.api.Utils.Constants.EMPTY_ID;

@Repository
public class AuthenticationRepository {

    private final Logger logger = LoggerFactory.getLogger("AuthenticationRepository");

    @Value("${token.interval}")
    private String tokenInterval;

    @Value("${token.increment}")
    private String tokenIncrement;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer getAuthenticationSequence(){
        Integer AuthenticationId = new Integer(-1);

        final String sql = "SELECT nextval('authentication_id_seq')";
        try {
            AuthenticationId = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting AuthenticationSequence - " + e.getMessage());
        }
        return AuthenticationId;
    }

    public void createAuthentication(Integer users_id, UUID token) {
        int result = 0;
        Integer authenticationId = getAuthenticationSequence();
        Object[] args = new Object[]{authenticationId, users_id, token};
        String sql = "INSERT INTO authentication (id,users_id,token, expiration_time, created_on, updated_on) "
                + "VALUES (?,?,?, now() + INTERVAL " + tokenInterval + ", now(), now())";
        logger.debug("SQL: {} - with users_id: {}", sql, users_id);
        try {
            result = jdbcTemplate.update(sql, args);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Creating Authentication - " + e.getMessage());
            throw new BadRequestException("Error creating token");
        }
    }

    public Authentication getAuthenticationByToken(UUID token) {
        Object[] args = new Object[]{token};
        Authentication authentication = new Authentication();
        String sql = "SELECT id, user_id, token, expiration_time FROM authentication WHERE token = ?";
        System.out.println(sql);
        logger.debug("SQL: {} - with token: {}", sql, token);
        try {
            authentication = (Authentication) jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper(Authentication.class));
            logger.info("Result authentication: {}", authentication);
        } catch (EmptyResultDataAccessException e){
            authentication.setId(EMPTY_ID);
            logger.info("authentication with token: {} does not exist", token);
        } catch (DataAccessException e) {
            logger.error("Error while getting authentication - " + e.getMessage());
            authentication.setId(EMPTY_ID);
        }
        return authentication;
    }


    // Metodo para validar que el token recibido no esta expirado
    // si token es valido entonces se incementa su expiracion
    public Boolean isAuthenticated(UUID token) {
        Integer valid = -1;
        Object[] args = new Object[]{token};
        String sql = "SELECT count(*) FROM authentication WHERE token = ? AND EXTRACT(EPOCH FROM (now() - expiration_time)) < 0";
        logger.debug("SQL: {} - with token: {}", sql, token);
        try {
            valid =  jdbcTemplate.queryForObject(sql, args, Integer.class);
            logger.info("Authentication result: {}", valid);
        } catch (EmptyResultDataAccessException e){
            logger.info("Invalid token: {}", token);
        } catch (DataAccessException e) {
            logger.error("Error while checking authentication - " + e.getMessage());
        }
        if (valid>0) {
            incrementInterval(token);
        }
        return (valid>0);
    }

    public void incrementInterval(UUID token) {
        int result = 0;
        Object[] args = new Object[]{token};
        String sqlUpdate = "UPDATE authentication SET expiration_time = expiration_time + INTERVAL " + tokenIncrement
                + " , updated_on = now() WHERE token = ?";
        logger.debug("SQL: {} - with token: {}", sqlUpdate, token);
        try {
            result = jdbcTemplate.update(sqlUpdate, args);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while updating token expiration - " + e.getMessage());
        }
        logger.info("Token time incremented successfully");
    }
}
