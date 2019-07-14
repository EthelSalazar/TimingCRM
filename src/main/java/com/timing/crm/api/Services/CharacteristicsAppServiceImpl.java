package com.timing.crm.api.Services;

import com.timing.crm.api.Repository.CharacteristicsAppRepository;
import com.timing.crm.api.View.CharacteristicsApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacteristicsAppServiceImpl implements CharacteristicsAppService{

    private final Logger logger = LoggerFactory.getLogger("CharacteristicsAppServiceImpl");

    @Autowired
    private CharacteristicsAppRepository characteristicsAppRepository;

    public List<CharacteristicsApp> getListCharacteristicsApp(){
        logger.info("Starting getAllRoles()");
        return characteristicsAppRepository.getAllCharacteristicsApp();
    }
}
