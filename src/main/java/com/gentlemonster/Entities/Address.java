package com.gentlemonster.Entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // anotaion này giúp tạo ra 1 builder để tạo ra 1 object mà không cần phải khởi tạo giá trị cho tất cả các trường
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 100)
    private String name; // Tên người nhận địa chỉ

    @Column(name = "phone_number", length = 15)
    private String phoneNumber; // Số điện thoại người nhận địa chỉ

    @Column(name = "street", length = 200)
    private String street;

    @Column(name = "ward", length = 150)
    private String ward;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "is_default")
    private boolean isDefault; // Địa chỉ mặc định

    // Loại địa chỉ (ví dụ: "home", "office", "other")
    // true: địa chỉ này là địa chỉ nhà, false: địa chỉ này là địa chỉ văn phòng
    @Column(name = "type", length = 50)
    private boolean type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;
}
