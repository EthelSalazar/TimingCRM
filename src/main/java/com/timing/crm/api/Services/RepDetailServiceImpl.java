package com.timing.crm.api.Services;

import com.timing.crm.api.Controller.Exception.ForbiddenException;
import com.timing.crm.api.Controller.Exception.NoContentException;
import com.timing.crm.api.Repository.ApprTypeRepDetailRepository;
import com.timing.crm.api.Repository.CharactAppRepDetailRepository;
import com.timing.crm.api.Repository.RepDetailRepository;
import com.timing.crm.api.Repository.ScheduleRepDetailRepository;
import com.timing.crm.api.Repository.UserRepository;
import com.timing.crm.api.View.ApproachType;
import com.timing.crm.api.View.CharactAppRepDetail;
import com.timing.crm.api.View.RepDetail;
import com.timing.crm.api.View.UserAndCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.timing.crm.api.Utils.Constants.EMPTY_ID;

@Service
public class RepDetailServiceImpl implements RepDetailService{
    private final Logger logger = LoggerFactory.getLogger("RepDetailServiceImpl");


    @Autowired
    private RepDetailRepository repDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApprTypeRepDetailRepository apprTypeRepDetailRepository;
    @Autowired
    private CharactAppRepDetailRepository charactAppRepDetailRepository;
    @Autowired
    private ScheduleRepDetailRepository scheduleRepDetailRepository;

    public RepDetail createRepDetail(RepDetail repDetail){
        RepDetail repDetailResult;
        logger.info("Starting createRepDetail with name: {}", repDetail.getName());
        repDetailResult = repDetailRepository.createRepDetail(repDetail);

        if (repDetail.getApproachTypeList().size()>0){
            logger.info("Starting createApproachTypesByRep with size: {}", repDetail.getApproachTypeList().size());
            repDetailResult.setApproachTypeList(apprTypeRepDetailRepository.createApproachTypesByRep(repDetail.getApproachTypeList(), repDetailResult.getUsersId()));
        }
        if (repDetail.getCharacteristicsAppList().size()>0){
            logger.info("Starting createCharactAppByRep with size: {}", repDetail.getCharacteristicsAppList().size());
            repDetailResult.setCharacteristicsAppList(charactAppRepDetailRepository.createCharactAppByRep(repDetail.getCharacteristicsAppList(), repDetailResult.getUsersId()));
        }
        if (repDetail.getScheduleList().size()>0){
            logger.info("Starting createScheduleByRep with size: {}", repDetail.getScheduleList().size());
            repDetailResult.setScheduleList(scheduleRepDetailRepository.createScheduleByRep(repDetail.getScheduleList(), repDetailResult.getUsersId()));
        }
        return repDetailResult;
    }

    public RepDetail modifyRepDetail(RepDetail repDetail) {
        RepDetail repDetailResult;
        logger.info("Starting modifyRepDetail with id: {}", repDetail.getId());
        repDetailResult = repDetailRepository.modifyRepDetail(repDetail);
        /*if (repDetail.getApproachTypeList().size()>0){
            logger.info("Starting createApproachTypesByRep with size: {}", repDetail.getApproachTypeList().size());
            repDetailResult.setApproachTypeList(apprTypeRepDetailRepository.createApproachTypesByRep(repDetail.getApproachTypeList(), repDetailResult.getUsersId()));
        }
        if (repDetail.getCharacteristicsAppList().size()>0){
            logger.info("Starting createCharactAppByRep with size: {}", repDetail.getCharacteristicsAppList().size());
            repDetailResult.setCharacteristicsAppList(charactAppRepDetailRepository.createCharactAppByRep(repDetail.getCharacteristicsAppList(), repDetailResult.getUsersId()));
        }*/
        if (repDetail.getScheduleList().size()>0){
            logger.info("Starting createScheduleByRep with size: {}", repDetail.getScheduleList().size());
            repDetailResult.setScheduleList(scheduleRepDetailRepository.modifyScheduleByRep(repDetail.getScheduleList()));
        }
        return repDetailResult;
    }
    public RepDetail getRepDetailbyId(UUID token, Integer repDetailId){
        logger.info("Starting getRepDetailbyId with token: {}", token);
        RepDetail repDetail;
        Integer userRepDetailId;
        Integer companyId;
        Integer roleId;
        UserAndCompany userAndCompany;

        //Getting employeeId and companyId based on the security token
        userAndCompany = userRepository.getUserByToken(token);
        if (repDetailId == null){
            userRepDetailId = userAndCompany.getUserId();
        }else{
            userRepDetailId = repDetailId;
        }
        repDetail = repDetailRepository.getRepDetailByUserId(userRepDetailId);
        if (repDetail.getId()==EMPTY_ID) {
            throw new NoContentException("representative does not exist");
        }else{
            repDetail.setApproachTypeList(apprTypeRepDetailRepository.getApproachTypesByRep(repDetail.getUsersId()));
            repDetail.setScheduleList(scheduleRepDetailRepository.getScheduleByRep(repDetail.getUsersId()));
            if (repDetail.getScheduleList().isEmpty()){
                logger.info("calling createScheduleEmptyByRepDetail ");
                repDetail.setScheduleList(scheduleRepDetailRepository.createScheduleEmptyByRepDetail(repDetail.getUsersId()));
            }
            repDetail.setCharacteristicsAppList(charactAppRepDetailRepository.getAllCharactAppRepDetail(repDetail.getUsersId()));
        }

        logger.info("Completing getRepDetailbyId");
        return repDetail;
    }

    public List<CharactAppRepDetail> getListCharactAppRepDetail(Integer repDetailId){
        logger.info("Starting getListCharactAppRepDetail with repDetailId: {}", repDetailId);
        List<CharactAppRepDetail> charactAppRepDetails;
        charactAppRepDetails = charactAppRepDetailRepository.getAllCharactAppRepDetail(repDetailId);
        logger.info("Completing getListCharactAppRepDetail");
        return charactAppRepDetails;
    }

    public List<RepDetail> getListRepDetail(String phone, String email, UUID token, String status){
        logger.info("Starting getListRepDetail with phone: {}, email: {}, token: {}, status: {}", phone, email, token, status);
        List<RepDetail> repDetails;
        UserAndCompany userAndCompany = userRepository.getUserByToken(token);
        if (userAndCompany==null) {
            logger.error("Invalid credentials - UserAndCompany is null");
            throw new ForbiddenException("Invalid credentials");
        }
        if (phone != null && !phone.equals("")){
            if (email != null && !email.equals("")){
                if (userAndCompany.getCompanyId() != null){
                    repDetails = repDetailRepository.getRepDetailByPhoneEmailAndCompany(phone, email, userAndCompany.getCompanyId());
                }else{
                    repDetails = repDetailRepository.getRepDetailByPhoneAndEmail(phone, email);
                }
            }else{
                if (userAndCompany.getCompanyId() != null){
                    repDetails = repDetailRepository.getRepDetailByPhoneAndCompany(phone, userAndCompany.getCompanyId());
                }else{
                    repDetails = repDetailRepository.getRepDetailByPhone(phone);
                }
            }
        }else {
            if (email != null && !email.equals("")){
                if (userAndCompany.getCompanyId() != null){
                    repDetails = repDetailRepository.getRepDetailByEmailAndCompany(email, userAndCompany.getCompanyId());
                }else{
                    repDetails = repDetailRepository.getRepDetailByEmail(email);
                }
            }else{
                if (userAndCompany.getCompanyId() != null){
                    if (status!= null && !status.equals("") ){
                        repDetails = repDetailRepository.getRepDetailByCompanyIdAndStatus(userAndCompany.getCompanyId(), status);
                    }else {
                        repDetails = repDetailRepository.getRepDetailByCompanyId(userAndCompany.getCompanyId());
                    }
                }else{
                    repDetails = repDetailRepository.getAllRepDetail();
                }
            }
        }
        logger.info("Completing getListRepDetail");
        return repDetails;
    }

}
