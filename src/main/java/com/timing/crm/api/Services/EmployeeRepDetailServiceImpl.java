package com.timing.crm.api.Services;

import com.timing.crm.api.Repository.EmployeeRepDetailRepository;
import com.timing.crm.api.Repository.EmployeeRepository;
import com.timing.crm.api.Repository.RepDetailRepository;
import com.timing.crm.api.Repository.UserRepository;
import com.timing.crm.api.Utils.Constants;
import com.timing.crm.api.View.Employee;
import com.timing.crm.api.View.EmployeeRepDetail;
import com.timing.crm.api.View.RepDetail;
import com.timing.crm.api.View.UserAndCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.timing.crm.api.Utils.Constants.COORDINATOR;
import static com.timing.crm.api.Utils.Constants.TELEMARKETER;

@Service
public class EmployeeRepDetailServiceImpl implements EmployeeRepDetailService{
    private final Logger logger = LoggerFactory.getLogger("EmployeeRepDetailServiceImpl");


    @Autowired
    private EmployeeRepDetailRepository employeeRepDetailRepository;
    @Autowired
    private RepDetailRepository repDetailRepository;
    @Autowired
    private UserRepository userRepository;

    public List<EmployeeRepDetail> adminRepToEmplSelect(UUID token){
        Integer userEmployeeId;
        Integer companyId;
        Integer roleId;
        UserAndCompany userAndCompany;

        //Getting employeeId and companyId based on the security token
        userAndCompany = userRepository.getUserByToken(token);
        userEmployeeId = userAndCompany.getUserId();
        companyId = userAndCompany.getCompanyId();
        roleId = userRepository.getUserbyId(userEmployeeId).getRoleId();
        logger.info("Starting adminRepToEmplSelect with companyId: {}, roleId: {}, userEmployeeId: {}", companyId, roleId, userEmployeeId);
        List<EmployeeRepDetail> employeeRepDetails = new ArrayList<>();
        List<RepDetail> repDetails;
        EmployeeRepDetail employeeRepDetail;
        repDetails = repDetailRepository.getrepDetailsByCompanyStatusAndNoInEmplRepDetail(companyId, Constants.ACTIVO);
        logger.info("RepDetails Without Employee", repDetails.size());
        if(repDetails.size()>0){
            for (RepDetail repDetail : repDetails)
            {
                employeeRepDetail = new EmployeeRepDetail();
                employeeRepDetail.setUserRepdetailId(repDetail.getUsersId());
                employeeRepDetail.setNameRepDetail(repDetail.getName());
                employeeRepDetail.setCompanyId(companyId);
                employeeRepDetails.add(employeeRepDetail);
            }

            employeeRepDetailRepository.createEmployeeRepDetail(employeeRepDetails);
        }
        if (roleId == Constants.COORDINATOR){
            logger.info("Employee by Sup:{}", userEmployeeId);
            employeeRepDetails = employeeRepDetailRepository.employeeRepDetByCompanyAndUserSupId(companyId, userEmployeeId);
        }else {
            employeeRepDetails = employeeRepDetailRepository.employeeRepDetailByCompany(companyId);
        }
        logger.info("Completing adminRepToEmplSelect");
        return employeeRepDetails;
    }

    public List<EmployeeRepDetail> modifyEmployeeRepDetail(List<EmployeeRepDetail> employeeRepDetails){
        logger.info("Starting modifyEmployeeRepDetail with employeeRepDetails.size: {}", employeeRepDetails.size());
        employeeRepDetailRepository.modifyEmployeeRepDetail(employeeRepDetails);
        logger.info("Completing modifyEmployeeRepDetail");
        return employeeRepDetails;
    }
}
