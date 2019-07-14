package com.timing.crm.api.Services;

import com.timing.crm.api.View.EmployeeRepDetail;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepDetailService {

    public List<EmployeeRepDetail> adminRepToEmplSelect(UUID token);

    public List<EmployeeRepDetail> modifyEmployeeRepDetail(List<EmployeeRepDetail> employeeRepDetails);
}
