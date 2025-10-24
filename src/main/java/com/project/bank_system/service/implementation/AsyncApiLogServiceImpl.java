package com.project.bank_system.service.implementation;

import com.project.bank_system.entity.ApiLog;
import com.project.bank_system.repository.ApiLogRepository;
import com.project.bank_system.service.interfaces.AsyncApiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncApiLogServiceImpl implements AsyncApiLogService {

    @Autowired
    private ApiLogRepository apiLogRepository;
    @Override
    public void saveLog(ApiLog apiLog) {
        apiLogRepository.save(apiLog);
    }
}
