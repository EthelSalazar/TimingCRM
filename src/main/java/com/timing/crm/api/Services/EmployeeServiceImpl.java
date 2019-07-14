package com.timing.crm.api.Services;

import com.timing.crm.api.Controller.Exception.NoContentException;
import com.timing.crm.api.Repository.EmployeeRepository;
import com.timing.crm.api.Repository.UserRepository;
import com.timing.crm.api.Utils.Constants;
import com.timing.crm.api.View.Employee;
import com.timing.crm.api.View.EmployeeRepDetail;
import com.timing.crm.api.View.UserAndCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.timing.crm.api.Utils.Constants.COORDINATOR;
import static com.timing.crm.api.Utils.Constants.EMPTY_ID;
import static com.timing.crm.api.Utils.Constants.TELEMARKETER;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final Logger logger = LoggerFactory.getLogger("EmployeeServiceImpl");


    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;

    public Employee createEmployee(Employee employee){
        logger.info("Starting createEmployee with name: {}", employee.getName());
        //TODO: validar que no esten repetidos por telefono y por email
        return employeeRepository.createEmployee(employee);
    }
    public Employee modifyEmployee(Employee employee) {
        logger.info("Starting modifyEmployee with id: {}", employee.getId());
        return employeeRepository.modifyEmployee(employee);
    }
    public Employee getEmployeebyDetail(UUID token){
        logger.info("Starting getEmployeebyDetail with token: {}", token);
        Employee employee;
        Integer userEmployeeId;
        Integer companyId;
        Integer roleId;
        UserAndCompany userAndCompany;

        //Getting employeeId and companyId based on the security token
        userAndCompany = userRepository.getUserByToken(token);
        userEmployeeId = userAndCompany.getUserId();
        employee = employeeRepository.getEmployeeByUserId(userEmployeeId);
        if (employee.getId()==EMPTY_ID) throw new NoContentException("employee does not exist");
        logger.info("Completing getEmployeebyDetail");
        return employee;
    }

    public List<Employee> modifyEmployeeList(List<Employee> employeeList){
        logger.info("Starting modifyEmployeeList with employeeList.size: {}", employeeList.size());
        employeeRepository.modifyEmployeeList(employeeList);
        logger.info("Completing modifyEmployeeList");
        return employeeList;
    }

    public List<Employee> getListEmployee(UUID token, List<Integer> roles, String status, Integer supervId, String email, String phone, Boolean filterByRoleUser) {
        List<Employee> employees;
        Integer userEmployeeId;
        Integer companyId;
        Integer roleId;
        UserAndCompany userAndCompany;

        //Getting employeeId and companyId based on the security token
        userAndCompany = userRepository.getUserByToken(token);
        userEmployeeId = userAndCompany.getUserId();
        companyId = userAndCompany.getCompanyId();
        roleId = userRepository.getUserbyId(userEmployeeId).getRoleId();
        logger.info("Starting getListEmployee with companyId: {}, roleId: {}, userEmployeeId: {}", companyId, roleId, userEmployeeId);
        //todo: agregar busqueda por email y phone en esta parte
        if (filterByRoleUser != null && filterByRoleUser == true){
            /// Modulo Admin
            logger.info("filterByRoleUser = true");
            if (roleId == Constants.COORDINATOR) {
                logger.info("coordinator = true");
                if (companyId != null && !companyId.toString().equals("")) {
                    logger.info("companyId: {} ", companyId);
                    if (status != null && !status.toString().equals("")) {//todo: agregar busqueda por email y phone en esta parte donde el status es no null
                        logger.info("status: {} ", status);
                        if (roles.size() > 0) {
                            logger.info("filtro employee, roles and status by coordinador");
                            employees = employeeRepository.getEmployeeByCompanyIdRolesStatusAndSupervId(companyId, roles, status, userEmployeeId);
                        }else{
                            logger.info("roles empty");
                            employees = new ArrayList<>();
                            // todo: falta implemetar employeeRepository.getEmployeeByCompanyIdAndStatusAndSupervId(companyId, status, userEmployeeId);
                        }
                    }else if (roles.size() > 0) {
                        logger.info("status null, roles.size: {} ", roles.size());
                        employees = new ArrayList<>();
                            // todo: falta implemetar employeeRepository.getEmployeeByCompanyIdRolesAndSupervId(companyId, roles, userEmployeeId);
                        } else {
                        logger.info("status null, no roles, companyId: {} ", companyId);
                        employees = new ArrayList<>();
                            // todo: falta implemetar employeeRepository.getEmployeeByCompanyIdAndSupervId(companyId, roles, userEmployeeId);
                        }
                }else {
                    logger.info("companyId null no employees, coordinator an filter true ");
                    employees =  employees = new ArrayList<>();
                    // todo: falta implemetar employeeRepository.getEmployeeBySupervId(userEmployeeId);
                }
            } else if (roleId == Constants.MANAGER)  {
                logger.info("manager  ");
                if (companyId != null && !companyId.toString().equals("")) {
                    logger.info(" manager companyId: {} ", companyId);
                    if (status != null && !status.toString().equals("")) {//todo: agregar busqueda por email y phone en esta parte donde el status es no null
                        logger.info("manager status: {} ", status);
                        if (roles.size() > 0) {
                            logger.info("manager companyId: {}, status{}, roles.size: {} ", companyId, status, roles.size());
                            employees = employeeRepository.getEmployeeByCompanyIdRolesAndStatus(companyId, roles, status);
                        }else{
                            logger.info("manager companyId: {}, status{}, roles null", companyId, status);
                            employees = employeeRepository.getEmployeeByCompanyIdAndStatus(companyId, status);
                        }
                    }else if (roles.size() > 0) {
                        logger.info(" manager status null companyId: {}, roles.size: {} ", companyId, roles.size());
                        employees = employeeRepository.getEmployeeByCompanyIdAndRoles(companyId, roles);
                    } else {
                        logger.info(" manager status null, roles null, companyId: {} ", companyId);
                        employees = employeeRepository.getEmployeeByCompanyId(companyId);
                    }
                } else {
                    logger.info(" manager companyId: null");
                    employees = employeeRepository.getAllEmployee();
                }
            } else {
                logger.info(" no manager no coord");
                employees = new ArrayList<>();//si no es coordinador ni gerente no puede ver listas de empleados
            }
        } else if (companyId != null && !companyId.toString().equals("")) {
            logger.info(" filter null companyId: {} ", companyId);
            if (status != null && !status.toString().equals("")) {//todo: agregar busqueda por email y phone en esta parte donde el status es no null
                logger.info(" filter null status: {} ", status);
                if (roles.size() > 0) {
                    logger.info(" filter null roles.size: {} ", roles.size());
                    if (supervId != null){
                        logger.info(" filter null roles.size: {}, supervId {} ", roles.size(), supervId);
                        employees = employeeRepository.getEmployeeByCompanyIdRolesStatusAndSupervId(companyId, roles, status, supervId);
                    }else {
                        logger.info(" filter null supervId null roles.size: {} ", roles.size());
                        employees = employeeRepository.getEmployeeByCompanyIdRolesAndStatus(companyId, roles, status);
                    }
                } else if (supervId != null){
                    logger.info(" filter null roles.sise null, supervId: {} ", supervId);
                    employees = new ArrayList<>();
                    //todo: falta implementar employeeRepository.getEmployeeByCompanyIdStatusAndSupervId(companyId, status, supervId);
                }else {
                    logger.info(" filter null roles.size null, supervId null");
                    employees = employeeRepository.getEmployeeByCompanyIdAndStatus(companyId, status);
                }
            }else if (roles.size() > 0) {/// todo: agregar busquedas por filtroByRoleUser y SupervId
                logger.info(" filter null status null roles.size: {} ", roles.size());
                if (supervId != null){
                    logger.info(" filter null status null roles.size: {}, supervId {} ", roles.size(), supervId);
                    employees = new ArrayList<>();
                    //todo: falta implementar employeeRepository.getEmployeeByCompanyIdRolesAndSupervId(companyId, roles, supervId);
                }else {
                    logger.info(" filter null status null roles.size: {}, supervId null ", roles.size());
                    employees = employeeRepository.getEmployeeByCompanyIdAndRoles(companyId, roles);
                }
            }else if (supervId != null){
                logger.info(" filter null status null roles.size null, supervId {} ", supervId);
                employees = new ArrayList<>();
                    //todo: falta implementar employeeRepository.getEmployeeByCompanyIdAndSupervId(companyId, supervId);
                }else {
                logger.info(" filter null status null roles.size null, supervId null");
                employees = employeeRepository.getEmployeeByCompanyId(companyId);
                }
            } else {
            logger.info(" filter null status null roles.size null, supervId null companyId null ");
            employees = employeeRepository.getAllEmployee();
        }
        logger.info("Completing getListEmployee");
        return employees;
    }






}
