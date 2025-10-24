package com.project.bank_system.util;

import com.project.bank_system.entity.ApiLog;
import com.project.bank_system.repository.ApiLogRepository;
import com.project.bank_system.service.interfaces.AsyncApiLogService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class ApiLoggingFilter implements Filter {

    @Autowired
    private AsyncApiLogService asyncApiLogService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);

        long startTime = System.currentTimeMillis();

        chain.doFilter(request, responseCopier);

        long duration = System.currentTimeMillis() - startTime;
        byte[] responseCopy = responseCopier.getCopy();
        int responseSize = responseCopy.length;

        ApiLog apiLog = new ApiLog();
        apiLog.setMethod(httpRequest.getMethod());
        apiLog.setEndpoint(httpRequest.getRequestURI());
        apiLog.setStatusCode(responseCopier.getStatus());
        apiLog.setDurationMs(duration);
        apiLog.setResponseSize(responseSize);
        apiLog.setClientIp(httpRequest.getRemoteAddr());
        apiLog.setRequestBody(extractBody(httpRequest)); // optional
        apiLog.setResponseBody(new String(responseCopy)); // optional

        asyncApiLogService.saveLog(apiLog);
    }

    private String extractBody(HttpServletRequest request) {
        // Optional: use ContentCachingRequestWrapper for POST/PUT body
        return null; // you can implement if needed
    }
}
