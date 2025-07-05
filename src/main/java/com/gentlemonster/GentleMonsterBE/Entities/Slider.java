package com.gentlemonster.GentleMonsterBE.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    
    @Column(name = "status", nullable = false)
    private boolean status;
    
    @Column(name = "title_slider", length = 200)
    private String titleSlider;

    @Column(name = "is_highlighted")
    private boolean highlighted; // Trạng thái hiển thị slider thêm vào bộ sưu tập

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "slug", length=100)
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

    @OneToMany(mappedBy = "slider", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("slider")
    private List<ProductType> productTypes = new ArrayList<>();

    @OneToMany(mappedBy = "slider", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("slider")
    private List<Collaboration> collaborations = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "slider_media",
            joinColumns = @JoinColumn(name = "slider_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private Media image;

    // @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "thumbnail_media_id") // FK trong bảng Product
    // private Media image;
}
