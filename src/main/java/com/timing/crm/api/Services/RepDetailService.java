package com.timing.crm.api.Services;


import com.timing.crm.api.View.CharactAppRepDetail;
import com.timing.crm.api.View.RepDetail;

import java.util.List;
import java.util.UUID;

public interface RepDetailService {

    public RepDetail createRepDetail(RepDetail repDetail);

    public RepDetail modifyRepDetail(RepDetail repDetail);

    public RepDetail getRepDetailbyId(UUID token, Integer repDetailId);

    public List<CharactAppRepDetail> getListCharactAppRepDetail(Integer repDetailId);

    public List<RepDetail> getListRepDetail(String phone, String email, UUID token, String status);

}
