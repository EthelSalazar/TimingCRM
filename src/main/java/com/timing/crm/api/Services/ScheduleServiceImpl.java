package com.timing.crm.api.Services;

import com.timing.crm.api.Repository.ScheduleRepository;
import com.timing.crm.api.View.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService{
    private final Logger logger = LoggerFactory.getLogger("ScheduleServiceImpl");

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> getListSchedule(){
        logger.info("Starting getListSchedule()");
        return scheduleRepository.getAllSchedules();
    }
}
