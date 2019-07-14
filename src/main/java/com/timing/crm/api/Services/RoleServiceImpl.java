package com.timing.crm.api.Services;

import com.timing.crm.api.Repository.RoleRepository;
import com.timing.crm.api.View.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final Logger logger = LoggerFactory.getLogger("RoleServiceImpl");

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getListRole(){
        logger.info("Starting getAllRoles()");
        return roleRepository.getAllRoles();
    }
}
