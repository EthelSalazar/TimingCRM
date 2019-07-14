package com.timing.crm.api.Repository;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.View.Employee;
import com.timing.crm.api.View.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;

import static com.timing.crm.api.Utils.Constants.ACTIVO;
import static com.timing.crm.api.Utils.Constants.EMPTY_ID;
import static com.timing.crm.api.Utils.Constants.INACTIVO;

@Repository
public class EmployeeRepository {
    private final Logger logger = LoggerFactory.getLogger("EmployeeRepository");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer getEmployeeSequence(){
        Integer employeeId = new Integer(-1);

        final String sql = "SELECT nextval('employee_id_seq')";
        try {
            employeeId = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting EmployeeSequence - " + e.getMessage());

        }
        return employeeId;
    }

    public Employee createEmployee(Employee employee) {
        int result = 0;
        Integer employeeId = getEmployeeSequence();
        Object[] args = new Object[]{employeeId, employee.getName(), employee.getPhone(), employee.getOptional_phone(),
                employee.getZip(), employee.getCity(), employee.getCountry(), employee.getEmail(), employee.getOptional_email(),
                employee.getNotes(), ACTIVO, employee.getCompanyId(), employee.getUsersId(),
                employee.getAddress(), employee.getUserSupervisorId()};
        String sql = "INSERT INTO employee (id,name,phone,optional_phone,zip,city,country,email,optional_email,notes," +
                "status,company_id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, now(), now());";
        logger.debug("SQL: {} - with name: {}", sql, employee.getName());
        try {
            result = jdbcTemplate.update(sql, args);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Creating Employee - " + e.getMessage());
            throw new BadRequestException("Employee duplicated");
        }
        employee.setId(employeeId);
        employee.setStatus(INACTIVO);
        return employee;
    }


    public Employee modifyEmployee(Employee employee) {
        int result;
        Object[] args = new Object[]{employee.getName(), employee.getPhone(), employee.getOptional_phone(),
                employee.getZip(), employee.getCity(), employee.getCountry(), employee.getEmail(), employee.getOptional_email(),
               employee.getNotes(), employee.getStatus(), employee.getCompanyId(), employee.getUsersId(),
                employee.getAddress(),  employee.getUserSupervisorId(), employee.getId()};

        String sql = "UPDATE employee SET name = ?, phone = ?, optional_phone = ?, zip = ?, city = ?,country = ?, " +
                "email = ?, optional_email = ?, notes = ?, status = ?, company_id = ?, users_id = ?, address = ?, " +
                "USER_SUPERVISOR_ID = ?, updated_on = now() WHERE id = ? ;";
        logger.info("SQL: {} with id {}", sql, employee.getId());
        try {
            result = jdbcTemplate.update(sql, args);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while updating Employee - " + e.getMessage());

        }
        return employee;
    }

    public void modifyEmployeeList(List<Employee> employeeList) {
        int[] result = new int[0];
        String sql = "UPDATE employee SET name = ?, phone = ?, optional_phone = ?, zip = ?, city = ?, country = ?, address = ?," +
                " email = ?, optional_email = ?, notes = ?, status = ?, company_id = ?, users_id = ?, user_supervisor_id = ?, updated_on = now() " +
                " WHERE id = ?";
        logger.debug("SQL: {}", sql);
        try {
            result = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Employee employee = employeeList.get(i);
                    preparedStatement.setString(1, employee.getName());
                    preparedStatement.setString(2, employee.getPhone());
                    preparedStatement.setString(3, employee.getOptional_phone());
                    preparedStatement.setString(4, employee.getZip());
                    preparedStatement.setString(5, employee.getCity());
                    preparedStatement.setString(6, employee.getCountry());
                    preparedStatement.setString(7, employee.getAddress());
                    preparedStatement.setString(8, employee.getEmail());
                    preparedStatement.setString(9, employee.getOptional_email());
                    preparedStatement.setString(10, employee.getNotes());
                    preparedStatement.setString(11, employee.getStatus());
                    preparedStatement.setInt(12, employee.getCompanyId());
                    preparedStatement.setInt(13, employee.getUsersId());
                    if (employee.getUserSupervisorId() == null){
                        preparedStatement.setNull(14,java.sql.Types.INTEGER);
                    }else {
                        preparedStatement.setInt(14, employee.getUserSupervisorId());
                    }
                    preparedStatement.setInt(15, employee.getId());
                }

                @Override
                public int getBatchSize() {
                    return employeeList.size();
                }
            });
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while modifying Employee in Lote - " + e.getMessage());
            throw new BadRequestException("Error to modify Employee in Lote");
        }
        logger.info("modify Employee in Lote result: {}" + result);
    }

    public Employee getEmployeebyId(Integer id) {
        Object[] args = new Object[]{id};
        Employee employee = new Employee();
        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email,  notes, status, " +
                "company_Id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on, " +
                "(select name from employee sup where sup.users_id = e.USER_SUPERVISOR_ID) userSupervisorName " +
                "FROM employee e WHERE id = ?";
        logger.debug("SQL: {} - with id: {}", sql, id);
        try {
            employee = (Employee) jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper(Employee.class));
        } catch (EmptyResultDataAccessException e){
            employee.setId(EMPTY_ID);
            logger.info("Employee with id: {} does not exist", id);
        } catch (DataAccessException e) {
            logger.error("Error while getting Employee - " + e.getMessage());
        }
        return employee;
    }

    public Employee getEmployeeByUserId(Integer userId){
        Object[] args = new Object[]{userId};
        Employee employee = new Employee();

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status, " +
                "company_id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on, " +
                "(select name from employee sup where sup.users_id = e.USER_SUPERVISOR_ID) userSupervisorName " +
                "FROM employee e WHERE users_id = ?";
        logger.debug("SQL: {} - with userId: {}", sql, userId);
        try {
            employee = (Employee) jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper(Employee.class));
            logger.info("Employee Id {}", employee.getId());
        } catch (DataAccessException e) {
            logger.error("Error while getting Employee by userId - " + e.getMessage());
        }
        return employee;
    }


    public List<Employee> getAllEmployee() {
        List<Employee> employees = new ArrayList<>();

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status, " +
                "company_id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on," +
                "(select name from employee sup where sup.users_id = e.USER_SUPERVISOR_ID) userSupervisorName " +
                "FROM employee e";
        logger.debug("SQL: {}", sql);
        try {
            employees = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Employee.class));
            logger.info("Result size: {}", employees.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting Companies - " + e.getMessage());
        }
        return employees;
    }

    public List<Employee> getEmployeeByNameEmailAndCompanyId(String name, String email, Integer companyId) {
        List<Employee> employees = new ArrayList<>();
        Object[] args = new Object[]{"%" + name.toUpperCase() + "%", email.toUpperCase(), companyId};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, " +
                "status, company_id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on," +
                "(select name from employee sup where sup.users_id = e.USER_SUPERVISOR_ID) userSupervisorName  " +
                "FROM employee e WHERE upper(name) like ? AND upper(email) = ? AND company_id = ?";
        logger.debug("SQL: {} - with name: {}, email: {}, companyId: {}", sql, name.toUpperCase(), email, companyId);
        try {
            employees = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Employee.class));
            logger.debug("SQL: {} - with name: {}, email: {}, companyId: {}", sql, name.toUpperCase(), email.toUpperCase(), companyId);
        } catch (DataAccessException e) {
            logger.error("Error while getting Employee by name, email, and companyId - " + e.getMessage());
        }
        return employees;
    }


    public List<Employee> getEmployeeByEmailAndCompanyId(String email, Integer companyId){
        List<Employee> employees = new ArrayList<>();
        Object[] args = new Object[]{email, companyId};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status, " +
                "company_id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on, " +
                "(select name from employee sup where sup.users_id = e.USER_SUPERVISOR_ID) userSupervisorName  " +
                "FROM employee e WHERE email = ? AND company_id = ?";
        logger.debug("SQL: {} - with email {}, companyId {}", sql, email, companyId);
        try {
            employees = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Employee.class));
            logger.debug("SQL: {} - with email: {}, companyId: {}", sql, email.toUpperCase(), companyId);
        } catch (DataAccessException e) {
            logger.error("Error while getting Employee by email, and companyId - " + e.getMessage());
        }
        return employees;
    }

    public List<Employee> getEmployeeByEmailPhoneAndCompanyId(String email, String phone, Integer companyId){
        List<Employee> employees = new ArrayList<>();
        Object[] args = new Object[]{email, phone, companyId};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status, " +
                "company_id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on, " +
                "(select name from employee sup where sup.users_id = e.USER_SUPERVISOR_ID) userSupervisorName " +
                "FROM employee e WHERE email = ? AND phone = ? AND company_id = ?";
        logger.debug("SQL: {} - with email {}, phone: {}, companyId {}", sql, email, phone, companyId);
        try {
            employees = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Employee.class));
            logger.debug("SQL: {} - with email: {}, phone: {}, companyId: {}", sql, email.toUpperCase(), phone, companyId);
        } catch (DataAccessException e) {
            logger.error("Error while getting Employee by email, phone, and companyId - " + e.getMessage());
        }
        return employees;
    }
    public List<Employee> getEmployeeByPhoneAndCompanyId(String phone, Integer companyId){
        List<Employee> employees = new ArrayList<>();
        Object[] args = new Object[]{phone, companyId};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status, " +
                "company_id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on, " +
                "(select name from employee sup where sup.users_id = e.USER_SUPERVISOR_ID) userSupervisorName " +
                "FROM employee e WHERE phone = ? AND company_id = ?";
        logger.debug("SQL: {} - with email {}, companyId {}", sql, phone, companyId);
        try {
            employees = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Employee.class));
            logger.debug("SQL: {} - with email: {}, companyId: {}", sql, phone.toUpperCase(), companyId);
        } catch (DataAccessException e) {
            logger.error("Error while getting Employee by email, and companyId - " + e.getMessage());
        }
        return employees;
    }
    public List<Employee> getEmployeeByCompanyId(Integer companyId){
        List<Employee> employees = new ArrayList<>();
        Object[] args = new Object[]{companyId};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status, " +
                "company_id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on, " +
                "(select name from employee sup where sup.users_id = e.USER_SUPERVISOR_ID) userSupervisorName " +
                "FROM employee e WHERE company_id = ?";
        logger.debug("SQL: {} - with companyId {}", sql, companyId);
        try {
            employees = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Employee.class));
            logger.debug("SQL: {} - with  companyId: {}", sql, companyId);
        } catch (DataAccessException e) {
            logger.error("Error while getting Employee by companyId - " + e.getMessage());
        }
        return employees;
    }

    public List<Employee> getEmployeeByCompanyIdAndStatus(Integer companyId, String status){
        List<Employee> employees = new ArrayList<>();
        Object[] args = new Object[]{companyId, status.toUpperCase()};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status, " +
                "company_id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on, " +
                "(select name from employee sup where sup.users_id = e.USER_SUPERVISOR_ID) userSupervisorName " +
                "FROM employee e WHERE company_id = ? " +
                " AND users_id IN (SELECT id FROM users WHERE status = ? AND company_id = e.company_id)";
        logger.debug("SQL: {} - with companyId {}", sql, companyId);
        try {
            employees = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Employee.class));
            logger.debug("SQL: {} - with  companyId: {}", sql, companyId);
        } catch (DataAccessException e) {
            logger.error("Error while getting Employee by companyId - " + e.getMessage());
        }
        return employees;
    }

    public List<Employee> getEmployeeByCompanyIdRolesAndStatus(Integer companyId, List<Integer> RolesId, String status){
        List<Employee> employees = new ArrayList<>();
        Object[] args = new Object[]{companyId, status.toUpperCase()};
        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status, " +
                "company_id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on, " +
                "(select name from employee sup where sup.users_id = e.USER_SUPERVISOR_ID) userSupervisorName  " +
                "FROM employee e WHERE company_id = ? " +
                "AND users_id IN (SELECT id FROM users WHERE UPPER (status) = ? ";
        String sqlRoles = "";
        for (int indice = 0; indice<RolesId.size(); indice++)
        {
           if (sqlRoles == null || sqlRoles.equals("")){
               sqlRoles = " AND ( role_id = " + RolesId.get(indice);
            } else {
               sqlRoles = sqlRoles + " OR role_id = " + RolesId.get(indice);
                }
        }
        if (sqlRoles != null && sqlRoles != ""){
            sqlRoles = sqlRoles + " )";
        }
        sql = sql + sqlRoles + ")";
        logger.debug("SQL: {} - with companyId {} RoleId.size {} status {}", sql, companyId, RolesId.size(), status);
        try {
            employees = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Employee.class));
            logger.debug("SQL: {} - with  companyId: {}, role.size {} status {}", sql, companyId, RolesId.size(), status);
        } catch (DataAccessException e) {
            logger.error("Error while getting Employee by companyId roles and status- " + e.getMessage());
        }
        return employees;
    }

    public List<Employee> getEmployeeByCompanyIdRolesStatusAndSupervId(Integer companyId, List<Integer> RolesId, String status, Integer userSupervId){
        List<Employee> employees = new ArrayList<>();
        Object[] args = new Object[]{companyId, userSupervId, status.toUpperCase()};
        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status, " +
                "company_id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on, " +
                "(select name from employee sup where sup.users_id = e.USER_SUPERVISOR_ID) userSupervisorName  " +
                "FROM employee e WHERE company_id = ? " +
                " AND user_supervisor_id = ? " +
                "AND users_id IN (SELECT id FROM users WHERE UPPER (status) = ? ";
        String sqlRoles = "";
        logger.info("getEmployeeByCompanyIdRolesStatusAndSupervId  companyId{}, roles.size: {}, status: {}, userSupervId: {} ", companyId, RolesId.size(), status, userSupervId);
        for (int indice = 0; indice<RolesId.size(); indice++)
        {
            if (sqlRoles == null || sqlRoles.equals("")){
                sqlRoles = " AND ( role_id = " + RolesId.get(indice);
            } else {
                sqlRoles = sqlRoles + " OR role_id = " + RolesId.get(indice);
            }
        }
        if (sqlRoles != null && sqlRoles != ""){
            sqlRoles = sqlRoles + " )";
        }
        sql = sql + sqlRoles + ")";
        logger.info("SQL: {} - with companyId {} RoleId.size {} status {}", sql, companyId, RolesId.size(), status);
        try {
            employees = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Employee.class));
            logger.debug("SQL: {} - with  companyId: {}, role.size {} status {}", sql, companyId, RolesId.size(), status);
        } catch (DataAccessException e) {
            logger.error("Error while getting Employee by companyId roles and status- " + e.getMessage());
        }
        return employees;
    }

    public List<Employee> getEmployeeByCompanyIdAndRoles(Integer companyId, List<Integer> RolesId){
        List<Employee> employees = new ArrayList<>();
        Object[] args = new Object[]{companyId};
        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status, " +
                "company_id, users_id, address, USER_SUPERVISOR_ID, created_on, updated_on, " +
                "(select name from employee sup where sup.users_id = e.USER_SUPERVISOR_ID) userSupervisorName  " +
                "FROM employee e WHERE company_id = ? " +
                "AND users_id IN (SELECT id FROM users WHERE ";
        String sqlRoles = "";
        for (int indice = 0; indice<RolesId.size(); indice++)
        {
            if (sqlRoles == null || sqlRoles.equals("")){
                sqlRoles = "( role_id = " + RolesId.get(indice);
            } else {
                sqlRoles = sqlRoles + " OR role_id = " + RolesId.get(indice);
            }
        }
        if (sqlRoles != null && sqlRoles != ""){
            sqlRoles = sqlRoles + " )";
        }
        sql = sql + sqlRoles + ")";
        logger.debug("SQL: {} - with companyId {} and RoleId {}", sql, companyId);
        try {
            employees = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(Employee.class));
            logger.debug("SQL: {} - with  companyId: {}, role1: {}", sql, companyId);
        } catch (DataAccessException e) {
            logger.error("Error while getting Employee by companyId and roles but not in employee_repdetail- " + e.getMessage());
        }
        return employees;
    }
}
