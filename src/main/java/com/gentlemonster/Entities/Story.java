package com.gentlemonster.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "slug", length = 100, nullable = false, unique = true)
    private String slug;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @OneToOne
    @JoinColumn(name = "story_id")
    private ProductType productType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "story_media",
            joinColumns = @JoinColumn(name = "story_id"),
            inverseJoinColumns = @JoinColumn(name = "meida_id")
    )
    private List<Media> image = new ArrayList<>();

    // @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    // @JoinColumn(name = "thumbnail_media_id") // FK trong bảng Product
    // private Media thumbnailMedia;

    @OneToOne
    @JoinColumn(name = "collaboration_id", nullable = false, unique = true)
    private Collaboration collaboration;
}
