package com.timing.crm.api.Repository;

import com.timing.crm.api.View.Appointment;
import com.timing.crm.api.View.Call;
import com.timing.crm.api.View.CallRegister;
import com.timing.crm.api.View.Lead;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CallRegisterRowMapper implements RowMapper<CallRegister> {

    @Nullable
    @Override
    public CallRegister mapRow(ResultSet rs, int i) throws SQLException {
        // wrapper composite object of a Call and Lead
        CallRegister callRegister = new CallRegister();
        // A call object has an Appointment object (could be null)
        Call call = new Call();
        Appointment appointment = new Appointment();
        Lead lead = new Lead();

        appointment.setId(rs.getInt("appointmentId"));
        appointment.setDateAppoint(rs.getTimestamp("dateAppoint"));
        if (rs.getInt("resultAppId") != 0){
            appointment.setResultAppId(rs.getInt("resultAppId"));
        }else{
            appointment.setResultAppId(null);
        }
        appointment.setCoordComments(rs.getString("coordComments"));
        appointment.setTeleComments(rs.getString("teleComments"));
        appointment.setStatus(rs.getInt("appointStatus"));
        appointment.setUserRepdetailId(rs.getInt("userRepdetailId"));
        appointment.setCompanyId(rs.getInt("companyId"));
        appointment.setUserEmployeeId(rs.getInt("userEmployeeId"));
        appointment.setLeadsId(rs.getInt("leadsId"));
        appointment.setCallId(rs.getInt("callId"));
        appointment.setCommentSummary(rs.getString("commentSummary"));
        appointment.setRepdetailComment(rs.getString("repdetailComment"));
        if (rs.getInt("appointmentParentId") != 0){
            appointment.setAppointmenteParentId(rs.getInt("appointmentParentId"));
        }else{
            appointment.setAppointmenteParentId(null);
        }
        call.setId(appointment.getCallId());
        call.setCallDate(rs.getTimestamp("callDate"));
        call.setResultCallId(rs.getInt("resultCallId"));
        call.setResultCallDescription(rs.getString("resultCallDescription"));
        call.setReasonsNotqualifyId(rs.getInt("reasonsNotqualifyId"));
        call.setReasonsNotqualifyDescription(rs.getString("reasonsNotqualifyDescription"));
        call.setLeadId(appointment.getLeadsId());
        call.setNotes(rs.getString("notes"));
        call.setUserRepdetailId(appointment.getUserRepdetailId());
        call.setUserEmployeeId(appointment.getUserEmployeeId());
        call.setCompanyId(appointment.getCompanyId());
        call.setAppointment(appointment);
        if (rs.getInt("followupAppointmentId") != 0){
            call.setFollowupAppointmentId(rs.getInt("followupAppointmentId"));
        }else{
            call.setFollowupAppointmentId(null);
        }
        lead.setId(rs.getInt("leadsId"));
        lead.setDateData(rs.getString("dateData"));
        lead.setRating(rs.getString("rating"));
        lead.setName(rs.getString("leadName"));
        lead.setMaritalStatus(rs.getString("maritalStatus"));
        lead.setSpouseName(rs.getString("spouseName"));
        lead.setPhone(rs.getString("phone"));
        lead.setZip(rs.getString("zip"));
        lead.setCity(rs.getString("city"));
        lead.setAddress(rs.getString("address"));
        lead.setStatus(rs.getInt("leadStatus"));
        lead.setApproachType(rs.getString("approachType"));
        lead.setProspectCategory(rs.getString("prospectCategory"));
        lead.setApproachTypeDescription(rs.getString("approachTypeDescription"));
        lead.setProspectCategoryDescription(rs.getString("prospectCategoryDescription"));
        lead.setApproachPlace(rs.getString("approachPlace"));
        lead.setApproachNotes(rs.getString("approachNotes"));
        lead.setApproachNotes414(rs.getString("approachNotes414"));
        lead.setHost(rs.getBoolean("host"));
        if (rs.getInt("hostId") != 0){
            lead.setHostId(rs.getInt("hostId"));
        }else{
            lead.setHostId(null);
        }
        lead.setCreatedOn(rs.getString("createdOn"));
        lead.setUpdatedOn(rs.getString("updatedOn"));
        lead.setHostName(rs.getString("hostName"));
        lead.setNameRepDetail(rs.getString("nameRepDetail"));
        lead.setUserRepdetailId(appointment.getUserRepdetailId());
        lead.setUserEmployeeId(appointment.getUserEmployeeId());
        lead.setCompanyId(appointment.getCompanyId());
        callRegister.setCall(call);
        callRegister.setLead(lead);
        return callRegister;
    }
}
