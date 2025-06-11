package com.gentlemonster.GentleMonsterBE.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "banners")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "link", length = 100, nullable = false)
    private String link;

    @Column(name = "status", nullable = false)
    private boolean status;  // Trạng thái hiển thị banner

    @Column(name = "seq", nullable = false)
    private int seq;  // Số thứ tự của banner từ 1 -> 4

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "banner_media",
            joinColumns = @JoinColumn(name = "banner_id"),
            inverseJoinColumns = @JoinColumn(name = "meida_id")
    )
    private List<Media> medias = new ArrayList<>();
}
