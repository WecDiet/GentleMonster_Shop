package com.gentlemonster.GentleMonsterBE.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// anotaion này giúp tạo ra 1 builder để tạo ra 1 object mà không cần phải khởi tạo giá trị cho tất cả các trường
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 500, nullable = false)
    private String storeName;

    @Column(name = "street", length = 200)
    private String street;

    @Column(name = "ward", length = 150)
    private String ward;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "phone", length = 100)
    private String hotLine;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime  updatedAt;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> users;

    @ManyToOne
    @JoinColumn(name = "slider_id")
    private Slider slider;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "store_media",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "meida_id")
    )
    private List<Media> image = new ArrayList<>();

    // @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "thumbnail_media_id") // FK trong bảng Product
    // private Media image;
}
