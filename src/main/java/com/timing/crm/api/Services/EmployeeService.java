package com.timing.crm.api.Services;

import com.timing.crm.api.View.Employee;
import com.timing.crm.api.View.EmployeeRepDetail;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    public Employee createEmployee(Employee employee);

    public Employee modifyEmployee(Employee employee);

    public List<Employee> modifyEmployeeList(List<Employee> employeeList);

    public Employee getEmployeebyDetail(UUID token);

    public List<Employee> getListEmployee(UUID token, List<Integer> roles, String status, Integer supervId, String email, String phone, Boolean filterByRoleUser);
}
