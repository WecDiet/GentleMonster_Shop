package com.gentlemonster.GentleMonsterBE.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "sliders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slider {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "image", length = 300, nullable = false)
    private String image;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "title_banner", length = 200)
    private String titleBanner;

    @Column(name = "slider_banner", length = 500)
    private String sliderBanner; // Ảnh banner slider

    @Column(name = "is_highlighted", nullable = false)
    private boolean highlighted;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "slug", length=100, nullable = false)
    private String slug;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

//    @OneToMany(mappedBy = "slider")
//    @JsonManagedReference
//    @JsonIgnoreProperties("slider")
//    private Set<ProductType> productType = new HashSet<>();

}
