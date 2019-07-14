package com.timing.crm.api.Services;

import com.timing.crm.api.Repository.ProspectCategoryRepository;
import com.timing.crm.api.View.ProspectCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProspectCategoryServiceImpl implements ProspectCategoryService{
    private final Logger logger = LoggerFactory.getLogger("ProspecCategoryServiceImpl");

    @Autowired
    private ProspectCategoryRepository prospectCategoryRepository;

    public List<ProspectCategory> getListProspectCategory(){
        logger.info("Starting getListProspectCategory()");
        return prospectCategoryRepository.getAllApproachType();
    }
}
