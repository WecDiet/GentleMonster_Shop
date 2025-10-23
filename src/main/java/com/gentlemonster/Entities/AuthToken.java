package com.gentlemonster.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // @Column(name = "token", length = 255) // token được tạo ra khi user đăng nhập vào hệ thống
    // private String token;

    @Column(name = "refresh_token", length = 255) // refresh token khi token hết hạn sử dụng (refresh token)
    private String refreshToken;

    @Column(name = "token_type", length = 50)
    private String tokenType; // kiểu token: Bearer, JWT, v.v.

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate; // thời gian hết hạn của token

    @Column(name = "refresh_expiration_date")
    private LocalDateTime refreshExpirationDate; // thời gian hết hạn của refresh token

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "device_name", length = 100)
    private String deviceName;

    /*
     * Các trường này dùng để kiểm tra trạng thái của token
     * - revoked: token đã bị thu hồi hay không 
     * * Nếu revoked là true, token sẽ không còn hợp lệ và không thể sử dụng để truy cập vào hệ thống.
     * * Ví dụ: khi người dùng đăng xuất, token sẽ được đánh dấu là revoked.
     * * Khi token hết hạn, hệ thống sẽ không cho phép người dùng sử dụng token đó nữa.
     * * Các trường này giúp quản lý và bảo mật token trong hệ thống.
     * * - revoked: token đã bị thu hồi hay không, Khi phát hiện thiết bị/phiên đăng nhập bất thường → thu hồi token.
     * * Ví dụ: khi người dùng đăng xuất, token sẽ được đánh dấu là revoked.
     * * Khi token hết hạn, hệ thống sẽ không cho phép người dùng sử dụng token đó nữa.
    */
    private boolean revoked;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;
}
