package com.gentlemonster.Aspect;



import java.time.LocalDateTime;
import java.util.HashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    private static final String POINT_CUT = "execution(* com.gentlemonster.GentleMonsterBE..*Controller.*(..))";

    @Before(POINT_CUT)
    public void logBefore(JoinPoint joinPoint) {
        // Lấy thông tin request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String method = request.getMethod(); // GET, POST, etc.
        String uri = request.getRequestURI(); // Đường dẫn đầy đủ
        String clientIp = request.getRemoteAddr(); // IP client
        LocalDateTime timestamp = LocalDateTime.now(); // Thời gian

        // Lấy tên phương thức và tham số
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String params = args.length > 0 ? args[0].toString() : "No params";

        // Phân biệt admin/public dựa trên đường dẫn
        String requestType = uri.contains("/manager/") ? "Admin" : "Public";

        // Tạo message log chi tiết
        String logMessage = String.format(
            "%s - [%s] %s %s - IP: %s - Method: %s - Params: %s",
            timestamp.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            requestType,
            method,
            uri,
            clientIp,
            methodName,
            params
        );

        // Ghi log
        log.info(logMessage);
    }
}
