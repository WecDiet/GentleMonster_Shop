package com.gentlemonster.GentleMonsterBE.Services.Token;

import java.util.Optional;

import javax.swing.text.html.Option;

import com.gentlemonster.GentleMonsterBE.Entities.AuthToken;
import com.gentlemonster.GentleMonsterBE.Entities.User;

public interface ITokenService {
    AuthToken saveToken(String token, User userId, String refreshToken, String tokenType, String deviceToken, String deviceName);
    Optional<AuthToken> findByToken(User user, String tokenType, String deviceToken, String deviceName);
}
