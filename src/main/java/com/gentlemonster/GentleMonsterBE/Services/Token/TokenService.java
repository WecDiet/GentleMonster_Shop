package com.gentlemonster.GentleMonsterBE.Services.Token;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gentlemonster.GentleMonsterBE.Entities.AuthToken;
import com.gentlemonster.GentleMonsterBE.Entities.User;
import com.gentlemonster.GentleMonsterBE.Repositories.ITokenRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.Specification.TokenSpecification;
import com.gentlemonster.GentleMonsterBE.Utils.JwtTokenUtils;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {

    @Autowired
    private ITokenRepository authTokenRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Override
    public AuthToken saveToken(String token, User user, String refreshToken, String tokenType, String deviceToken,
            String deviceName) {
            AuthToken authToken = AuthToken.builder()
                .user(user)
                .token(token)
                .refreshToken(refreshToken)
                .tokenType(tokenType)
                .expirationDate(LocalDateTime.now().plusHours(jwtTokenUtils.getExpiration()))
                .refreshExpirationDate(LocalDateTime.now().plusHours(jwtTokenUtils.getJwtRefreshExpiration()))
                .deviceToken(deviceToken)
                .deviceName(deviceName != null ? deviceName : "Unknown")
                .revoked(false)
                .build();
        return authTokenRepository.save(authToken);
    }
    @Override
    public Optional<AuthToken> findByToken(User user, String tokenType, String deviceToken,
            String deviceName) {
        if (user == null || tokenType == null || deviceToken == null) {
            return Optional.empty();
        }
        Specification<AuthToken> spec = TokenSpecification.tokenSpec(user, tokenType, deviceToken, deviceName);
        return authTokenRepository.findOne(spec);
    }


    
}
