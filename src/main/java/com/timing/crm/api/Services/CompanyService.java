package com.timing.crm.api.Services;
import com.timing.crm.api.View.Company;

import java.util.List;

public interface CompanyService {

    public Company createCompany(Company company);

    public Company modifyCompany(Company company);

    public Company getCompanyById(Integer id);

    public List<Company> getAllCompany(String name, String email);
}

