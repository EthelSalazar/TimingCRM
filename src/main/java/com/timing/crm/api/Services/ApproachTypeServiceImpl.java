package com.timing.crm.api.Services;

import com.timing.crm.api.Repository.ApproachTypeRepository;
import com.timing.crm.api.View.ApproachType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApproachTypeServiceImpl implements ApproachTypeService{
    private final Logger logger = LoggerFactory.getLogger("ApproachTypeServiceImpl");

    @Autowired
    private ApproachTypeRepository approachTypeRepository;

    public List<ApproachType> getListApproachType(){
        logger.info("Starting getListApproachType()");
        return approachTypeRepository.getAllApproachType();
    }
}
