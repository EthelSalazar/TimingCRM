package com.timing.crm.api.Repository;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.View.UserAndCompany;
import com.timing.crm.api.View.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.timing.crm.api.Utils.Constants.ACTIVO;
import static com.timing.crm.api.Utils.Constants.EMPTY_ID;

@Repository
public class UserRepository {
    private static final String SQL_CREATE_USER =
            "INSERT INTO users (id,login,password,company_id,role_id,user_name,status,created_on,updated_on)"
                    + " VALUES (?,?,?,?,?,?,?,now(),now()) ";

    private static final String SQL_MODIFY_USER =
            "UPDATE users SET login = ?, password = ?, company_id = ?, status = ?, role_id = ?, user_name = "
                    + "?, updated_on = now() WHERE id = ? ";

    private static final String SELECT_USER_BY_ID = "SELECT id, login, password, role_id, status, company_Id, user_name FROM users WHERE id = ?";

    private static final String SELECT_ALL_USERS = "SELECT id, login, password, role_id, status, company_Id, user_name FROM users ";

    private static final String GET_USER_BY_LOGIN_COMPANY_ROLE_STATUS =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE upper(login) "
                    + "like ? AND company_id = ? AND role_id = ? AND upper(status) = ?";

    private static final String GET_USER_LOGIN_COMPANY_ROLE =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE upper(login) "
                    + "like ? AND company_id = ? AND role_id = ?";

    private static final String GET_USER_BY_LOGIN_COMPANY_STATUS =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE upper(login) "
                    + "like ? AND company_id = ? AND upper(status) = ?";

    private static final String GET_USER_LOGIN_ROLE_STATUS =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE upper(login) "
                    + "like ? " + "AND role_id = ? AND upper(status) = ?";

    private static final String GET_USER_BY_LOGIN_COMPANY =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE upper(login) "
                    + "like ? AND company_id = ?";
    private static final String GET_USER_BY_LOGIN_ROLE =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE upper(login) "
                    + "like ? AND role_id = ?";

    private static final String GET_USER_LOGIN_STATUS =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE upper(login) "
                    + "like ? AND upper(status) = ?";

    private static final String GET_USER_BY_LOGIN =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE upper(login) "
                    + "like ? ";

    private static final String GET_USER_BY_COMPANY_ROLE_STATUS =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE "
                    + "company_id = ? AND role_id = ? AND upper(status) = ?";

    private static final String GET_USER_BY_COMPANY_ROLE =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE "
                    + "company_id = ? AND role_id = ?";

    private static final String GET_USER_BY_COMPANY_STATUS =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE "
                    + "company_id = ? AND upper(status) = ?";

    private static final String GET_USER_BY_COMPANY =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE "
                    + "company_id = ?";

    private static final String GET_USER_BY_ROLE_STATUS =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users  WHERE "
                    + "role_id = ? AND upper(status) = ?";

    private static final String GET_USER_BY_ROLE = "SELECT id, login, password, role_id, status, company_Id, "
            + "user_name FROM users  WHERE role_id = ? ";

    private static final String GET_USER_BY_STATUS =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users "
                    + "WHERE upper(status) = ?";

    private static final String GET_USER_BY_EXACT_LOGIN =
            "SELECT id, login, password, role_id, status, company_Id, user_name FROM users "
                    + "WHERE upper(login) = ? ";

    private static final String GET_USER_BY_TOKEN =
            "SELECT u.id userId, u.role_id roleId, u.company_id companyId FROM authentication a, users u "
                    + "WHERE a.users_id = u.id AND a.token = ?";

    private final Logger logger = LoggerFactory.getLogger("UserRepository");


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer getUserSequence(){
        Integer userId = -1;

        final String sql = "SELECT nextval('users_id_seq')";
        try {
            userId = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (DataAccessException e) {
            logger.error("Error while getUserSequence - {}", e.getMessage());
        }
        return userId;
    }

    public Users createUser(Users users) {
        int result = 0;
        Integer userId = getUserSequence();
        Object[] args = new Object[]{userId, users.getLogin().toUpperCase(), users.getPassword(), users.getCompany_id(), users.getRoleId(), users.getUserName(), ACTIVO};
        String sql = SQL_CREATE_USER;
        logger.info("SQL: {} - with id: {}, login: {}, role: {}, username:{}, password: {}", sql, userId, users.getLogin(), users.getRoleId(),
                users.getUserName(), users.getPassword());
        try {
            result = jdbcTemplate.update(sql, args);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Creating User - {}", e.getMessage());
            throw new BadRequestException("Error creating user");
        }
        users.setId(userId);
        users.setStatus(ACTIVO);
        return users;
    }

    public String modifyUser(Users users) {
        String result = "Successful";
        Object[] args = new Object[]{users.getLogin().toUpperCase(), users.getPassword(), users.getCompany_id(), users.getStatus(), users.getRoleId(), users.getUserName(), users.getId()};

        String sql = SQL_MODIFY_USER;
        logger.info("SQL: {} - with id: {}", sql, users.getId());
        try {
            jdbcTemplate.update(sql, args);
        } catch (DataAccessException e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        logger.info("Result repository: {}", result);
        return result;
    }

    public Users getUserbyId(Integer id) {
        Object[] args = new Object[]{id};
        Users result = new Users();
        String sql = SELECT_USER_BY_ID;
        logger.info("SQL: {} - with id: {}", sql, id);
        try {
            result = (Users) jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result: {}", result);
        } catch (EmptyResultDataAccessException e){
            result.setId(EMPTY_ID);
            logger.info("User with id: {} does not exist", id);
        } catch (DataAccessException e) {
        logger.error("Error while getting User - {}", e.getMessage());
           }
        return result;
    }

    public List<Users> getAllUsers() {
        List<Users> users = new ArrayList<>();

        String sql = SELECT_ALL_USERS;
        logger.info("SQL: {} ", sql);
        try {
            users = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting all getAllUsers - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByLoginCompanyIdRoleAndStatus(String login, Integer companyId, Integer role, String status) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{"%" + login.toUpperCase() + "%", companyId, role, status.toUpperCase()};

        String sql = GET_USER_BY_LOGIN_COMPANY_ROLE_STATUS;
        logger.info("SQL: {} - with Login: {}, CompanyId: {}, Role: {}, Status: {}", sql, login, companyId, role,
                status);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting all getUserByLoginCompanyIdRoleAndStatus - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByLoginCompanyIdAndRole(String login, Integer companyId, Integer role) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{"%" + login.toUpperCase() + "%", companyId, role};

        String sql = GET_USER_LOGIN_COMPANY_ROLE;
        logger.info("SQL: {} - with Login: {}, CompanyId: {}, Role: {}", sql, login, companyId, role);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByLoginCompanyIdAndRole - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByLoginCompanyIdAndStatus(String login, Integer companyId, String status) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{"%" + login.toUpperCase() + "%", companyId,status.toUpperCase()};

        String sql = GET_USER_BY_LOGIN_COMPANY_STATUS;
        logger.info("SQL: {} - with Login: {}, CompanyId: {}, Status: {}", sql, login, companyId, status);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByLoginCompanyIdAndStatus - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByLoginRoleAndStatus(String login, Integer role, String status) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{"%" + login.toUpperCase() + "%", role, status.toUpperCase()};

        String sql = GET_USER_LOGIN_ROLE_STATUS;
        logger.info("SQL: {} - with Login: {},  Role: {}, Status: {}", sql, login, role, status);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByLoginRoleAndStatus - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByLoginAndCompanyId(String login, Integer companyId) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{"%" + login.toUpperCase() + "%", companyId};

        String sql = GET_USER_BY_LOGIN_COMPANY;
        logger.info("SQL: {} - with Login: {}, CompanyId: {}", sql, login, companyId);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByLoginAndCompanyId - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByLoginAndRole(String login, Integer role) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{"%" + login.toUpperCase() + "%", role};

        String sql = GET_USER_BY_LOGIN_ROLE;
        logger.info("SQL: {} - with Login: {}, Role: {}", sql, login, role);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByLoginAndRole - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByLoginAndStatus(String login, String status) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{"%" + login.toUpperCase() + "%", status.toUpperCase()};

        String sql = GET_USER_LOGIN_STATUS;
        logger.info("SQL: {} - with Login: {}, Status: {}", sql, login, status);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByLoginAndStatus - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByLogin(String login) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{"%" + login.toUpperCase() + "%"};

        String sql = GET_USER_BY_LOGIN;
        logger.info("SQL: {} - with Login: {}", sql, login);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByLogin - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByCompanyIdRoleAndStatus(Integer companyId, Integer role, String status) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{companyId, role, status.toUpperCase()};

        String sql = GET_USER_BY_COMPANY_ROLE_STATUS;
        logger.info("SQL: {} - with CompanyId: {}, Role: {}, Status: {}", sql, companyId, role, status);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByCompanyIdRoleAndStatus - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByCompanyIdAndRole(Integer companyId, Integer role) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{companyId, role};

        String sql = GET_USER_BY_COMPANY_ROLE;
        logger.info("SQL: {} - with CompanyId: {}, Role: {}", sql, companyId, role);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByCompanyIdAndRole - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByCompanyIdAndStatus(Integer companyId, String status) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{companyId, status.toUpperCase()};

        String sql = GET_USER_BY_COMPANY_STATUS;
        logger.debug("SQL: {} - with CompanyId: {}, Status: {}", sql, companyId, status);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByCompanyIdAndStatus - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByCompanyId(Integer companyId) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{companyId};

        String sql = GET_USER_BY_COMPANY;
        logger.info("SQL: {} - with CompanyId: {}", sql, companyId);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByCompanyId - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByRoleAndStatus(Integer role, String status) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{role, status.toUpperCase()};

        String sql = GET_USER_BY_ROLE_STATUS;
        logger.info("SQL: {} - with Role: {}, Status: {}", sql, role, status);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByRoleAndStatus - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByRole(Integer role) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{role};

        String sql = GET_USER_BY_ROLE;
        logger.info("SQL: {} - with Role: {}", sql, role);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", users.size());
            logger.error("Error while getting all getUserByRole - {}", e.getMessage());
        }
        return users;
    }

    public List<Users> getUserByStatus(String status) {
        List<Users> users = new ArrayList<>();
        Object[] args = new Object[]{status.toUpperCase()};

        String sql = GET_USER_BY_STATUS;
        logger.info("SQL: {} - with Status: {}", sql, status);
        try {
            users = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Result size: {}", users.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting all getUserByStatus - {}", e.getMessage());
        }
        return users;
    }

    public Users getUserByExactLogin(String login) {
        Object[] args = new Object[]{login.toUpperCase()};
        Users user = new Users();
        String sql = GET_USER_BY_EXACT_LOGIN;
        logger.debug("SQL: {} - with login: {}", sql, login);
        try {
            user = (Users) jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper(Users.class));
            logger.info("Found a valid user with login: {}, password: {}", user.getLogin(), user.getPassword());
        } catch (EmptyResultDataAccessException e){
            user.setId(EMPTY_ID);
            logger.info("User with login: {} does not exist", login);
        } catch (DataAccessException e) {
            logger.error("Error while getting USER BY LOGIN FOR AUTHENTICATION - {}", e.getMessage());
            user.setId(EMPTY_ID);
        }

        return user;
    }

    // Method to get userId, roleId and companyId
    public UserAndCompany getUserByToken(UUID token) {
        Object[] args = new Object[]{token};
        UserAndCompany userAndCompany = new UserAndCompany();
        String sql = GET_USER_BY_TOKEN;
        logger.info("SQL: {} - with token: {}", sql, token);
        try {
            userAndCompany =  (UserAndCompany) jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper(UserAndCompany.class));
            logger.info("Found a valid userId: {}", userAndCompany.getUserId());
        } catch (EmptyResultDataAccessException e){
            userAndCompany.setUserId(EMPTY_ID);
            userAndCompany.setCompanyId(EMPTY_ID);
            logger.error("User with token: {} does not exist", token);
        } catch (DataAccessException e) {
            logger.error("Error while getting USER BY TOKEN - {}", e.getMessage());
            userAndCompany.setUserId(EMPTY_ID);
            userAndCompany.setCompanyId(EMPTY_ID);
        }

        return userAndCompany;

    }
}
