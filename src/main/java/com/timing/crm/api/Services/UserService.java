package com.timing.crm.api.Services;

import com.timing.crm.api.View.Users;

import java.util.List;

public interface UserService {

    public Users createUser(Users users);

    public String modifyUser(Users users);

    public Users getUserbyId(Integer id);

    public List<Users> getListUsers (String login, Integer companyId, Integer role, String status);
}
