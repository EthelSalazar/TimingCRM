package com.timing.crm.api.Services;

import com.timing.crm.api.Controller.Exception.NoContentException;
import com.timing.crm.api.Repository.AuthenticationRepository;
import com.timing.crm.api.Repository.UserRepository;
import com.timing.crm.api.View.Authentication;
import com.timing.crm.api.View.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static com.timing.crm.api.Utils.Constants.EMPTY_ID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final Logger logger = LoggerFactory.getLogger("AuthenticationServiceImpl");

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private UserRepository userRepository;

    public Users validateUser(String login, String password){
        Users user;
        UUID token;
        logger.info("Starting ValidateUser with login: {}, password: {}", login, password);
        user = userRepository.getUserByExactLogin(login);

        if (user.getId()==EMPTY_ID){
            logger.info("Completing validateUser throwing NoContentException: Invalid Login / Password");
            throw new NoContentException("Login does not exist");
        }else{
            logger.info("Login exists with id: "+ user.getId());
            if(user.getPassword().equals(password)){
                token = UUID.randomUUID();
                user.setToken(token);
                logger.info("Creating Authentication with token: {}, login: {}, password: {}", token, login, password);
                authenticationRepository.createAuthentication(user.getId(), token);
            } else { //password diferentes
                logger.info("Completing validateUser throwing NoContentException: Invalid Login / Password");
                throw new NoContentException("Invalid Password");
            }
        }
        logger.info("Completing validateUser");
        return user;
    }

    public Boolean loginExist(String login){
        Users user;
        Boolean exist;
        logger.info("Starting loginExist with login: {}", login);
        user = userRepository.getUserByExactLogin(login);
        if (user.getId()==EMPTY_ID){
            System.out.println("is empty");
            exist = false;
        }else{
            exist = true;
        }
        logger.info("Completing loginExist");
        return exist;
    }

    @Override
    public Boolean isAuthenticated(UUID token) {
        Boolean valid;
        logger.info("Checking token: {}", token);
        valid = authenticationRepository.isAuthenticated(token);
        logger.info("Checking token: {} with result: {}", token, valid);
        return valid;
    }
}
