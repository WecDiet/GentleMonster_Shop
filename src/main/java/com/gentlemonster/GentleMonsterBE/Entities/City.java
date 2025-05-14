package com.gentlemonster.GentleMonsterBE.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 500, nullable = false)
    private String name;

    @Column(name = "slug", length = 500, nullable = false)
    private String slug;

    @Column(name = "thumbnail", length = 300)
    private String thumbnail;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "country", length = 200)
    private String country; // Tên quốc gia của thành phố

    @Column(name = "country_slug", length = 200)
    private String countrySlug;

    @CreationTimestamp
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime  updatedAt;

}
