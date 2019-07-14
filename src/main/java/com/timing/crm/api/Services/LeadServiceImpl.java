package com.timing.crm.api.Services;

import com.timing.crm.api.Controller.Exception.BadRequestException;
import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Controller.Exception.NoContentException;
import com.timing.crm.api.Repository.EmployeeRepDetailRepository;
import com.timing.crm.api.Repository.LeadRepository;
import com.timing.crm.api.Repository.UserRepository;
import com.timing.crm.api.Utils.Constants;
import com.timing.crm.api.View.Lead;
import com.timing.crm.api.View.UserAndCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.timing.crm.api.Helper.ValidatorHelper.cleanPhoneNumber;
import static com.timing.crm.api.Utils.Constants.EMPTY_ID;

@Service
public class LeadServiceImpl implements LeadService{
    private final Logger logger = LoggerFactory.getLogger("LeadServiceImpl");


    @Autowired
    private LeadRepository leadRepository;
    @Autowired
    private EmployeeRepDetailRepository employeeRepDetailRepository;
    @Autowired
    private UserRepository userRepository;

    public Lead getLeadById(Integer id){
        logger.info("Starting getleadbyId with id: {}", id);
        Lead lead;
        lead = leadRepository.getLeadById(id);
        if (lead.getId()==EMPTY_ID) throw new NoContentException("lead does not exist");
        logger.info("Completing getleadbyId");
        return lead;
    }

    public Lead modifyLead(Lead lead){
        logger.info("Starting getleadbyId with id: {}", lead.getId());
        lead = leadRepository.modifyLead(lead);
        if (lead.getId()==EMPTY_ID) throw new NoContentException("lead does not exist");
        logger.info("Completing modifyLead");
        return lead;
    }

    public List<Lead> getListLeads(Integer status, UUID token, Integer hostId, Integer userRepDetailId, String startDate,
            String endDate, String phone, String name) {
        logger.info("Starting getListLeads with status: {}, hostId: {}, userRepDetailId: {}, startDate:{}, " +
                         "endDate:{}, phone: {}", status, hostId, userRepDetailId, startDate, endDate, phone);
        List<Lead> leads = new ArrayList<>();
        UserAndCompany userAndCompany = userRepository.getUserByToken(token);
        /*
        if (!StringUtils.isEmpty(status)) {
            // TODO: este metodo de repositorio deberia filtrar por token - rol superadmin
            leads = leadRepository.getLeadsByStatus(status);
        } else {
            // TODO: este metodo de repositorio deberia filtrar por token - rol superadmin
            leads = leadRepository.getAllLeads();
        }
        */
        if (!StringUtils.isEmpty(status)) {
            if (userAndCompany.getRoleId() == Constants.TELEMARKETER) {
                logger.info("Role: TELEMARKETER");
                leads = leadRepository.getLeadsByStatusCompanyAndRepDetail(status, userAndCompany.getCompanyId(), userAndCompany.getUserId());
            } else if (userAndCompany.getRoleId() == Constants.COORDINATOR) {
                logger.info("Role: COORDINATOR");
                leads = leadRepository.getLeadsByStatusCompanyAndCoord(status, userAndCompany.getCompanyId(), userAndCompany.getUserId());
            } else if (userAndCompany.getRoleId() == Constants.MANAGER) {
                logger.info("Role: MANAGER");
                leads = leadRepository.getLeadsByStatusAndCompany(status, userAndCompany.getCompanyId());
            } else {
                logger.error("getListLeads() - invalid role");
            }
            // TODO: hacer la validación de cuando viene status y hostId con valores para consultas futuras
            // TODO: hacer la validación de cuando viene status, Dates y userRepDetailId en todas sus combinaciones con valores para consultas futuras
        } else if (!StringUtils.isEmpty(userRepDetailId)) {
            logger.info("userRepDetailId not empty");
            leads = leadRepository.getLeadByRepPhoneDatesOrName(userRepDetailId, phone, startDate, endDate, name);
        }else if (!StringUtils.isEmpty(hostId)) {
            logger.info("hostId");
            leads = leadRepository.getLeadsByHostId(hostId);
        } else {
            logger.error("getListLeads() - invalid status");
        }
        logger.info("Completing getListLeads");
        return leads;
    }

    public List<Lead> getListLeadsByStatusAndUserEmployeeId(Integer status, UUID token) {
        logger.info("Starting getListLeadsByStatusAndUserEmployeeId with status: {}", status);
        List<Lead> leads;
        UserAndCompany userAndCompany = userRepository.getUserByToken(token);
        if (userAndCompany.getRoleId() != Constants.MANAGER && userAndCompany.getRoleId() != Constants.COORDINATOR) {
            logger.info("Select Telemarketer or Representative");
            leads = leadRepository.getLeadsByStatusCompanyAndRepDetail(status, userAndCompany.getCompanyId(), userAndCompany.getUserId());
        } else {
            logger.info("Select Manager or Coordinator");
            leads = leadRepository.getLeadsByStatusCompanyAndCoordOfTele(status, userAndCompany.getCompanyId(), userAndCompany.getUserId());
        }
        logger.info("Completing getListLeadsByStatusAndUserEmployeeId with leads size: {}", leads.size());
        return leads;
    }

    @Override
    public Lead createLead(Lead lead, UUID token) {
        logger.info("Starting Create Lead with name: {}", lead.getName());
        UserAndCompany userAndCompany = userRepository.getUserByToken(token);
        if (userAndCompany==null) {
            logger.error("Invalid credentials - UserAndCompany is null");
            throw new ForbiddenException("Invalid credentials");
        }
        lead = leadRepository.insertSingleLead(lead, lead.getUserRepdetailId(), userAndCompany.getCompanyId(),
                userAndCompany.getUserId());
        logger.info("Completing Create Lead with name: {}", lead.getName());
        return lead;
    }

}
