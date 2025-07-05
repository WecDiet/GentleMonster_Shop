package com.gentlemonster.GentleMonsterBE.Services.Token;

import com.gentlemonster.GentleMonsterBE.Entities.AuthToken;
import com.gentlemonster.GentleMonsterBE.Entities.User;

public interface ITokenService {
    AuthToken saveToken(String token, User userId, String refreshToken, String tokenType, String deviceToken, String deviceName);
}
