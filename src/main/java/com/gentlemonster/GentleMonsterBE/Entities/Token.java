package com.gentlemonster.GentleMonsterBE.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token", length = 255) // token được tạo ra khi user đăng nhập vào hệ thống
    private String token;

    @Column(name = "refresh_token", length = 255) // refresh token khi token hết hạn sử dụng (refresh token)
    private String refreshToken;

    @Column(name = "token_type", length = 50)
    private String tokenType; // kiểu token: Bearer, JWT, ...

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate; // thời gian hết hạn của token

    @Column(name = "refresh_expiration_date")
    private LocalDateTime refreshExpirationDate;

    @Column(name = "is_mobile")
    private boolean isMobile;

    private boolean revoked;
    private boolean expired;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;
}
