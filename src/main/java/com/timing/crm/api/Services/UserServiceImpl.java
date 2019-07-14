package com.timing.crm.api.Services;

import com.timing.crm.api.Repository.UserRepository;
import com.timing.crm.api.View.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService{

    private final Logger logger = LoggerFactory.getLogger("userServiceImpl");

    @Autowired
    private UserRepository userRepository;

    public Users createUser(Users users){
        logger.info("Starting createUser with login: {}, password: {}", users.getLogin(), users.getPassword());
        return userRepository.createUser(users);
    }


    public String modifyUser(Users users){
        logger.info("Starting modifyUser with id: {}", users.getId());
        return userRepository.modifyUser(users);
    }


    public Users getUserbyId(Integer id){
        return userRepository.getUserbyId(id);
    }


    public List<Users> getListUsers (String login,Integer companyId, Integer role,String status){
        logger.info("Starting getListUsers with login: {}, companyId: {}, role: {}, status: {}", login, companyId, role, status);
        List<Users> users;
        if (login != null && !login.equals("")){
            if (companyId != null && !companyId.toString().equals("")){
                if (role != null && !role.equals("")){
                    if (status != null && !status.equals("")){
                        users = userRepository.getUserByLoginCompanyIdRoleAndStatus(login,companyId,role,status);
                    }else{
                        users = userRepository.getUserByLoginCompanyIdAndRole(login,companyId,role);
                    }
                }else{
                    if (status != null && !status.equals("")){
                        users = userRepository.getUserByLoginCompanyIdAndStatus(login,companyId,status);
                    }else{
                        users = userRepository.getUserByLoginAndCompanyId(login,companyId);
                    }
                }
            }else{
                if (role != null && !role.equals("")){
                    if (status != null && !status.equals("")){
                        users = userRepository.getUserByLoginRoleAndStatus(login,role,status);
                    }else{
                        users = userRepository.getUserByLoginAndRole(login,role);
                    }
                }else{
                    if (status != null && !status.equals("")){
                        users = userRepository.getUserByLoginAndStatus(login,status);
                    }else{
                        users = userRepository.getUserByLogin(login);
                    }
                }
            }
        }else{
            if (companyId != null && !companyId.toString().equals("")){
                if (role != null && !role.equals("")){
                    if (status != null && !status.equals("")){
                        users = userRepository.getUserByCompanyIdRoleAndStatus(companyId,role,status);
                    }else{
                        users = userRepository.getUserByCompanyIdAndRole(companyId,role);
                    }
                }else{
                    if (status != null && !status.equals("")){
                        users = userRepository.getUserByCompanyIdAndStatus(companyId,status);
                    }else{
                        users = userRepository.getUserByCompanyId(companyId);
                    }
                }
            }else {
                if (role != null && !role.equals("")) {
                    if (status != null && !status.equals("")) {
                        users = userRepository.getUserByRoleAndStatus(role, status);
                    } else {
                        users = userRepository.getUserByRole(role);
                    }
                } else {
                    if (status != null && !status.equals("")) {
                        users = userRepository.getUserByStatus(status);
                    } else {
                        users = userRepository.getAllUsers();
                    }
                }
            }
        }
        logger.info("Completing getListUsers");
        return users;
    }
}
