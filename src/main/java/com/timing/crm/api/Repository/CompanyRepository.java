package com.timing.crm.api.Repository;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.View.Company;

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

import static com.timing.crm.api.Utils.Constants.EMPTY_ID;
import static com.timing.crm.api.Utils.Constants.INACTIVO;

@Repository
public class CompanyRepository {
    public static final String SELECT_COMPANY_SEQ = "SELECT nextval('company_id_seq')";
    private static final String INSERT_COMPANY =
            "INSERT INTO company (id,name,phone,optional_phone,zip,city,country,address,email,optional_email," +
                    "economy_activity,notes,status,created_on,updated_on) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now());";
    private static final String UPDATE_COMPANY =
            "UPDATE company SET name = ?, phone = ?, optional_phone = ?, zip = ?, city = ?,country = ?, "
                    + "email = ?, optional_email = ?, notes = ?, status = ?, updated_on = now() WHERE id = ? ";
    private static final String SELECT_COMPANY_BY_ID =
            "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status "
                    + "FROM company WHERE id = ?";
    private static final String SELECT_COMPANY_NAME =
            "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status "
                    + "FROM company WHERE upper(name) LIKE ?";
    private static final String SELECT_COMPANY_BY_EMAIL =
            "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status "
                    + "FROM company WHERE email = ?";
    private static final String SELECT_COMPANY_BY_NAME_EMAIL =
            "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status "
                    + "FROM company WHERE upper(name) like ? AND upper(email) like ?";
    private static final String SELECT_ALL_COMPANIES =
            "SELECT id, name, phone, optional_phone, zip, city, country, email, optional_email, notes, status "
                    + "FROM company";
    private final Logger logger = LoggerFactory.getLogger("CompanyRepository");

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public Integer getCompanySequence(){
        Integer companyId = -1;

        try {
            companyId = jdbcTemplate.queryForObject(SELECT_COMPANY_SEQ, Integer.class);
        } catch (DataAccessException e) {
            logger.error("Error while getting CompanySequence - " + e.getMessage());
        }
        return companyId;
    }


    public Company createCompany(Company company) {
        int result;
        Integer companyId = getCompanySequence();

        Object[] args = new Object[]{companyId, company.getName(), company.getPhone(), company.getOptional_phone(),
                company.getZip(), company.getCity(), company.getCountry(), company.getEmail(), company.getOptional_email(),
                company.getNotes(), INACTIVO, company.getAddress(), company.getEconomyc_activity()};

        logger.debug("SQL: {} - with name: {}", INSERT_COMPANY, company.getName());
        try {
            result = jdbcTemplate.update(INSERT_COMPANY, args);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Creating Company - " + e.getMessage());
            throw new BadRequestException("Company Email duplicated");
        }
        company.setId(companyId);
        company.setStatus(INACTIVO);
        return company;
    }


    public Company modifyCompany(Company company) {
        int result;
        Object[] args = new Object[]{company.getName(), company.getPhone(), company.getOptional_phone(),
                company.getZip(), company.getCity(), company.getCountry(), company.getEmail(), company.getOptional_email(),
                company.getNotes(), company.getStatus(), company.getAddress(), company.getEconomyc_activity(), company.getId()};

        try {
            logger.debug("SQL: {} - with name: {}", UPDATE_COMPANY, company.getName());
            result = jdbcTemplate.update(UPDATE_COMPANY, args);
            logger.info("Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while updating Company - " + e.getMessage());
        }
        return company;
    }


    public Company getCompanybyId(Integer id) {
        Object[] args = new Object[]{id};
        Company company = new Company();
        logger.debug("SQL: {} - with id: {}", SELECT_COMPANY_BY_ID, id);
        try {
            company = jdbcTemplate.queryForObject(SELECT_COMPANY_BY_ID, args, new BeanPropertyRowMapper<>(Company.class));
        } catch (EmptyResultDataAccessException e){
            company.setId(EMPTY_ID);
            logger.info("Company with id: {} does not exist", id);
        } catch (DataAccessException e) {
            logger.error("Error while getting Company - " + e.getMessage());
        }
        return company;
    }


    public List<Company> getCompanyByName(String name) {
        List<Company> companies = new ArrayList<>();
        Object[] args = new Object[]{"%" + name.toUpperCase() + "%"};

        logger.debug("SQL: {} - with name: {}", SELECT_COMPANY_NAME, name.toUpperCase());
        try {
            companies = jdbcTemplate.query(SELECT_COMPANY_NAME, args, new BeanPropertyRowMapper<>(Company.class));
            logger.info("Result size: {}", companies.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting Company by name - " + e.getMessage());
        }
        return companies;
    }


    public List<Company> getCompanyByEmail(String email) {
        List<Company> companies = new ArrayList<>();
        Object[] args = new Object[]{"%" + email.toUpperCase() + "%"};

        logger.debug("SQL: {} - with email: {}", SELECT_COMPANY_BY_EMAIL, email);

        try {
            companies = jdbcTemplate.query(SELECT_COMPANY_BY_EMAIL, args, new BeanPropertyRowMapper<>(Company.class));
            logger.info("Result size: {}", companies.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting Company by email - " + e.getMessage());
        }
        return companies;
    }


    public List<Company> getCompanyByNameAndEmail(String name, String email) {
        List<Company> companies = new ArrayList<>();
        Object[] args = new Object[]{"%" + name.toUpperCase() + "%", "%" + email.toUpperCase() + "%"};


        logger.debug("SQL: {} - with name: {}, email: {}", SELECT_COMPANY_BY_NAME_EMAIL, name.toUpperCase(), email.toUpperCase());

        try {
            companies = jdbcTemplate.query(SELECT_COMPANY_BY_NAME_EMAIL, args, new BeanPropertyRowMapper<>(Company.class));
            logger.info("Result size: {}", companies.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting Company by name and email - " + e.getMessage());
        }
        return companies;
    }


    public List<Company> getAllCompany() {
        List<Company> companies = new ArrayList<>();

        logger.debug("SQL: {}", SELECT_ALL_COMPANIES);
        try {
            companies = jdbcTemplate.query(SELECT_ALL_COMPANIES, new BeanPropertyRowMapper<>(Company.class));
            logger.info("Result size: {}", companies.size());
        } catch (DataAccessException e) {
            logger.info("Result size: {}", companies.size());
            logger.error("Error while getting all Companies - " + e.getMessage());
        }
        return companies;
    }
}
