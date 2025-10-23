package com.gentlemonster.Services.Token;

import java.util.Optional;

import com.gentlemonster.Entities.AuthToken;
import com.gentlemonster.Entities.User;

public interface ITokenService {
    AuthToken saveToken(User userId, String refreshToken, String tokenType, String deviceToken, String deviceName);
    Optional<AuthToken> findByToken(User user, String tokenType, String deviceToken, String deviceName);
    
}
