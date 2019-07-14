package com.timing.crm.api.Services;

import com.timing.crm.api.Controller.Exception.NoContentException;
import com.timing.crm.api.Repository.CompanyRepository;
import com.timing.crm.api.View.Company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.timing.crm.api.Utils.Constants.EMPTY_ID;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final Logger logger = LoggerFactory.getLogger("CompanyServiceImpl");

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Company createCompany(Company company) {
        logger.info("Starting createCompany with name: {}", company.getName());
        return companyRepository.createCompany(company);
    }

    public Company modifyCompany(Company company) {
        logger.info("Starting modifyCompany with id: {}", company.getId());
        return companyRepository.modifyCompany(company);
    }

    public Company getCompanyById(Integer id) {
        logger.info("Starting getCompanyById with id: {}", id);
        Company company;
        company = companyRepository.getCompanybyId(id);
        if (company.getId()==EMPTY_ID) throw new NoContentException("Company does not exist");
        logger.info("Completing getCompanyById");
        return company;
    }

    public List<Company> getAllCompany(String name, String email) {
        logger.info("Starting getAllCompany with optional parameters name: {}, email: {}", name, email);
        List<Company> companies;

        if (!StringUtils.isEmpty(name)) {
            if (!StringUtils.isEmpty(email)) {
                companies = companyRepository.getCompanyByNameAndEmail(name, email);
            } else {
                companies = companyRepository.getCompanyByName(name);
            }
        } else {
            if (!StringUtils.isEmpty(email)) {
                companies = companyRepository.getCompanyByEmail(email);
            } else {
                companies = companyRepository.getAllCompany();
            }
        }
        if (companies.isEmpty()) throw new NoContentException("Company does not exist");
        logger.info("Completing getAllCompany");
        return companies;
    }
}
