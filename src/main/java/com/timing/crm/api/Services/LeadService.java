package com.timing.crm.api.Services;

import com.timing.crm.api.View.Lead;

import java.util.List;
import java.util.UUID;

public interface LeadService {

    Lead getLeadById(Integer id);

    Lead modifyLead(Lead lead);

    List<Lead> getListLeads(Integer status, UUID token, Integer hostId, Integer userRepDetailId, String startDate,
            String endDate, String phone, String name);

    Lead createLead(Lead lead, UUID token);
}
