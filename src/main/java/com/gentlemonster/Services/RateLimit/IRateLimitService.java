package com.gentlemonster.Services.RateLimit;

import java.util.concurrent.TimeUnit;

import com.gentlemonster.Exception.RateLimitExceededException;

import jakarta.servlet.http.HttpServletRequest;

public interface IRateLimitService {
    public void setValue(String key, Object value, long timeout, TimeUnit unit);
    public Object getValue(String key);
    public Boolean deleteKey(String key);
    public boolean isAllowed(HttpServletRequest request,String action, String type) throws RateLimitExceededException;
}
