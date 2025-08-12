package com.gentlemonster.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "social_accounts")
@NoArgsConstructor
@AllArgsConstructor
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "provider", length = 45, nullable = false)
    private String provider; // facebook, google, twitter, github ... (tên của provider mà user đăng nhập vào hệ thống bằng nhà cung cấp nào)

    @Column(name = "provider_id", length = 200, nullable = false)
    private String providerId;

    @Column(name = "email", length = 200, nullable = false)
    private String email;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;
}
