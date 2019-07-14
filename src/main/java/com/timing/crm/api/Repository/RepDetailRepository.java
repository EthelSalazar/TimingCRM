package com.timing.crm.api.Repository;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.View.RepDetail;
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

import static com.timing.crm.api.Utils.Constants.ACTIVO;
import static com.timing.crm.api.Utils.Constants.EMPTY_ID;
import static com.timing.crm.api.Utils.Constants.INACTIVO;

@Repository
public class RepDetailRepository {
    private final Logger logger = LoggerFactory.getLogger("RepDetailRepository");


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer getRepDetailSequence(){
        Integer repDetailId = new Integer(-1);

        final String sql = "SELECT nextval('Rep_Detail_id_seq')";
        try {
            repDetailId = jdbcTemplate.queryForObject(sql, Integer.class);
            logger.debug("SQL: {} - with result: {}", sql, repDetailId );
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting getRepDetailSequence - " + e.getMessage());

        }
        return repDetailId;
    }

    public RepDetail createRepDetail(RepDetail repDetail) {
        int result = 0;
        Integer repDetailId = getRepDetailSequence();
        Object[] args = new Object[]{repDetailId, repDetail.getName(), repDetail.getEmail(),repDetail.getOptionalEmail(), repDetail.getPhone(),
                repDetail.getOptionalPhone(),repDetail.getZip(), repDetail.getCity(), repDetail.getCountry(), repDetail.getAddress(),
                repDetail.getRepDetailPredecessor(), repDetail.getCompanyId(), repDetail.getUsersId(),
                repDetail.getAreaTrabajo(), repDetail.getDataFrequency(), repDetail.getDataQtAprox(), repDetail.getCooking(),
                repDetail.getAddressCooking(), repDetail.getReportShared(),repDetail.getReportCall(), repDetail.getReportMessage(),
        repDetail.getCityAppointments(), repDetail.getCityAuthAppointments()};
        String sql = "INSERT INTO rep_detail (id,name,email,optional_email,phone,optional_phone,zip, city, country, address, " +
                "rep_detail_predecessor,company_id, users_id,area_trabajo,data_frequency,data_qt_aprox,cooking,address_cooking," +
                "report_shared,report_call,report_message, city_appointments, city_auth_appointments,created_on, updated_on) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?, now(),now());";
        logger.debug("SQL: {} - with name: {}", sql, repDetail.getName());
        try {
            result = jdbcTemplate.update(sql, args);
            logger.info("createRepDetail - Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Creating Representative - " + e.getMessage());
            throw new BadRequestException("Representative Email duplicated");
        }
        repDetail.setId(repDetailId);
        repDetail.setStatus(ACTIVO);
        return repDetail;
    }

    public RepDetail modifyRepDetail(RepDetail repDetail) {
        int result;
        Object[] args = new Object[]{repDetail.getName(), repDetail.getEmail(),repDetail.getOptionalEmail(), repDetail.getPhone(),
                repDetail.getOptionalPhone(), repDetail.getZip(), repDetail.getCity(), repDetail.getCountry(), repDetail.getAddress(),
                repDetail.getRepDetailPredecessor(), repDetail.getCompanyId(), repDetail.getUsersId(),
                repDetail.getAreaTrabajo(), repDetail.getDataFrequency(), repDetail.getDataQtAprox(), repDetail.getCooking(), repDetail.getAddressCooking(),
                repDetail.getReportShared(),repDetail.getReportCall(), repDetail.getReportMessage(), repDetail.getCityAppointments(),
                repDetail.getCityAuthAppointments(), repDetail.getId()};
        String sql = "UPDATE rep_detail SET name = ? , email = ?, optional_email = ?, phone = ?, optional_phone = ? , zip = ?, city = ?," +
                "country = ?, address = ?, rep_detail_predecessor = ?, " +
                "company_id = ?, users_id = ?, area_trabajo = ?, data_frequency = ?, data_qt_aprox = ?, cooking = ?, " +
                "address_cooking = ?, report_shared = ?, report_call = ? , report_message = ?, city_appointments = ?, " +
                "city_auth_appointments = ?, updated_on = now()" +
                " WHERE id = ? ;";
        logger.debug("SQL: {} - with name: {}", sql, repDetail.getName());
        System.out.println(sql);
        try {
            result = jdbcTemplate.update(sql, args);
            logger.info("modifyRepDetail - Result: {}", result);
        } catch (DataAccessException e) {
            logger.error("Error while Modifying Representative - " + e.getMessage());
            throw new BadRequestException("Representative Email duplicated");
        }
        System.out.println("result repository: " + result);
        return repDetail;
    }

    public RepDetail getRepDetailbyId(Integer id) {
        Object[] args = new Object[]{id};
        RepDetail repDetail = new RepDetail();
        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo," +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on " +
                "FROM rep_detail WHERE id = ?";
        System.out.println(sql);
        logger.debug("SQL: {} - with id: {}", sql, id);
        try {
            repDetail = (RepDetail) jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (EmptyResultDataAccessException e){
            repDetail.setId(EMPTY_ID);
            logger.info("Representative with id: {} does not exist", id);
        } catch (DataAccessException e) {
            logger.error("Error while getting Representative - " + e.getMessage());
        }
        return repDetail;
    }

    public RepDetail getRepDetailByUserId(Integer usersId){
        Object[] args = new Object[]{usersId};
        RepDetail repDetail = new RepDetail();
        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo," +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on " +
                "FROM rep_detail WHERE users_id = ?";
        System.out.println(sql);
        logger.debug("SQL: {} - with usersId: {}", sql, usersId);
        try {
            repDetail = (RepDetail) jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (EmptyResultDataAccessException e){
            repDetail.setId(EMPTY_ID);
            logger.info("Representative with usersId: {} does not exist", usersId);
        } catch (DataAccessException e) {
            logger.error("Error while getting Representative - " + e.getMessage());
        }
        return repDetail;
    }

    public List<RepDetail> getAllRepDetail() {
        List<RepDetail> repDetails = new ArrayList<>();

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo, " +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on  " +
                "FROM rep_detail ";
        System.out.println(sql);
        logger.debug("SQL: {}", sql);
        try {
            repDetails = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RepDetail.class));
            logger.info("Result size: {}", repDetails.size());
        } catch (DataAccessException e) {
            logger.error("Error while getting Representatives - " + e.getMessage());
        }
        return repDetails;
    }

    public List<RepDetail> getRepDetailByName(String name) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{"%" + name.toUpperCase() + "%"};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo," +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on " +
                "FROM rep_detail WHERE upper(name) LIKE ?";
        logger.debug("SQL: {} - with name: {} ", sql, name.toUpperCase());
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting Representative by name - " + e.getMessage());
        }
        return repDetails;
    }

    public List<RepDetail> getRepDetailByPredecessor(Integer repDetailPredecessor) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{repDetailPredecessor};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo," +
                " rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on  " +
                "FROM rep_detail WHERE rep_detail_predecessor = ?";
        logger.debug("SQL: {} - with repDetailPredecessor: {} ", sql, repDetailPredecessor);
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting Representative by repDetailProdecessor - " + e.getMessage());
        }
            return repDetails;
    }

    public List<RepDetail> getRepDetailByPhone(String phone) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{phone};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo," +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on " +
                "FROM rep_detail WHERE phone = ?";
        logger.debug("SQL: {} - with phone: {} ", sql, phone);
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting Representative by phone - " + e.getMessage());
        }
        return repDetails;
    }

    public List<RepDetail> getRepDetailByEmail(String email) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{email.toUpperCase()};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo," +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on  " +
                "FROM rep_detail WHERE upper(email) = ?";
        logger.debug("SQL: {} - with phone: {} ", sql, email);
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting Representative by email - " + e.getMessage());
        }
        return repDetails;
    }

    public List<RepDetail> getRepDetailByCompanyId(Integer companyId) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{companyId};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo," +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on  " +
                "FROM rep_detail WHERE company_id = ?";
        logger.debug("SQL: {} - with companyId: {} ", sql, companyId);
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting Representative by companyId - " + e.getMessage());
        }
        return repDetails;
    }

    public List<RepDetail> getRepDetailByCompanyIdAndStatus(Integer companyId, String status) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{companyId, status.toUpperCase()};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo," +
                " rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on " +
                "FROM rep_detail WHERE company_id = ? " +
                " AND users_id IN (SELECT id FROM users WHERE UPPER (status) = ? )";
        logger.debug("SQL: {} - with status: {} ", sql, status);
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting Representative by company and status - " + e.getMessage());
        }
        return repDetails;
    }

    public List<RepDetail> getRepDetailByAreaTrabajo(String areaTrabajo) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{areaTrabajo};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo," +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on  " +
                "FROM rep_detail WHERE area_trabajo = ?";
        logger.debug("SQL: {} - with areaTrabajo: {} ", sql, areaTrabajo);
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting Representative by areaTrabajo - " + e.getMessage());

        }
        return repDetails;
    }


    public List<RepDetail> getRepDetailByReportShared(Boolean reportShared) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{reportShared};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo," +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on  " +
                "FROM rep_detail WHERE report_shared = ?";
        logger.debug("SQL: {} - with reportShared: {} ", sql, reportShared);
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting Representative by reportShared - " + e.getMessage());
        }
        return repDetails;
    }

    public List<RepDetail> getRepDetailByReportCall(Boolean reportCall) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{reportCall};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo, " +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on " +
                " FROM rep_detail WHERE report_call = ?";
        logger.debug("SQL: {} - with reportCall: {} ", sql, reportCall);
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting Representative by reportCall - " + e.getMessage());
        }
        return repDetails;
    }

    public List<RepDetail> getRepDetailByReportMessage(Boolean reportMessage) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{reportMessage};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo, " +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on " +
                "FROM rep_detail WHERE report_message = ?";
        logger.debug("SQL: {} - with reportMessage: {} ", sql, reportMessage);
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
            logger.error("Error while getting Representative by reportMessage - " + e.getMessage());
        }
        return repDetails;
    }

    public List<RepDetail> getRepDetailByPhoneAndEmail(String phone, String email) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{phone, email.toUpperCase()};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo, " +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on " +
                "FROM rep_detail WHERE phone = ? AND upper(email) = ?";
        logger.debug("SQL: {} - with phone: {}, email: {}", sql, phone, email.toUpperCase());
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting Representative by phone and email - " + e.getMessage());
        }
        return repDetails;
    }
    public List<RepDetail> getRepDetailByPhoneEmailAndCompany(String phone, String email, Integer companyId) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{phone, email.toUpperCase(), companyId};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo, " +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on " +
                "FROM rep_detail WHERE phone = ? AND upper(email) = ?" +
                "AND company_id = ?";
        logger.debug("SQL: {} - with phone: {}, email: {}", sql, phone, email.toUpperCase());
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting Representative by phone, email and companyId - " + e.getMessage());
        }
        return repDetails;
    }

    public List<RepDetail> getRepDetailByPhoneAndCompany(String phone, Integer companyId) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{phone, companyId};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo, " +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on " +
                "FROM rep_detail WHERE phone = ? " +
                "AND company_id = ?";
        logger.debug("SQL: {} - with phone: {}, companyId: {}", sql, phone, companyId);
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting Representative by phone and companyId - " + e.getMessage());
        }
        return repDetails;
    }
    public List<RepDetail> getRepDetailByEmailAndCompany(String email, Integer companyId) {
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{email.toUpperCase(), companyId};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo, " +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on " +
                "FROM rep_detail WHERE upper(email) = ?" +
                "AND company_id = ?";
        logger.debug("SQL: {} - with email: {}, companyId: {}", sql, email.toUpperCase(), companyId);
        System.out.println(sql);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
        } catch (DataAccessException e) {
            logger.error("Error while getting Representative by email and companyId - " + e.getMessage());
        }
        return repDetails;
    }


    public List<RepDetail> getrepDetailsByCompanyStatusAndNoInEmplRepDetail(Integer companyId, String status){
        List<RepDetail> repDetails = new ArrayList<>();
        Object[] args = new Object[]{companyId, status.toUpperCase(), companyId};

        String sql = "SELECT id, name, phone, optional_phone, zip, city, country, address, email, optional_email, area_trabajo, " +
                "rep_detail_predecessor, company_id, users_id, data_frequency, data_qt_aprox, cooking, address_cooking, " +
                "city_appointments, city_auth_appointments, report_shared, report_call, report_message, created_on, updated_on " +
                "FROM rep_detail rp WHERE company_id = ? " +
                "AND (SELECT UPPER(STATUS) FROM USERS WHERE ID = rp.USERS_ID) = ? " +
                "AND USERS_ID NOT IN (SELECT USER_REPDETAIL_ID FROM EMPLOYEE_REPDETAIL WHERE COMPANY_ID = ?)";
        logger.info("SQL: {} - with companyId {} ", sql, companyId);
        logger.debug("SQL: {} - with companyId {} ", sql, companyId);
        try {
            repDetails = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper(RepDetail.class));
            logger.info("repDetails.size: {}", repDetails.size());
            logger.debug("SQL: {} - with  companyId: {}", sql, companyId);
        } catch (DataAccessException e) {
            logger.error("Error while getting REPDETAILS by companyId and status but not in employee_repdetail- " + e.getMessage());
        }
        if(repDetails.size()>0){
            for (int indice = 0; indice<repDetails.size(); indice++)
            {
                logger.info("indice: {}, getId(): {}, getName(): {}", indice, repDetails.get(indice).getId(), repDetails.get(indice).getName());
            }
        }
        return repDetails;
    }
}
