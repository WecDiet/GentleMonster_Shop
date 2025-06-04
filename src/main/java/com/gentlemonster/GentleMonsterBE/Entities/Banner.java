package com.gentlemonster.GentleMonsterBE.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
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

    @OneToMany
    @JoinColumn(name = "banner_id")
    private List<Media> mediaBanner;  // Ảnh hoặc video của banner, nếu serial = 0 thì là ảnh, nếu serial = 1 thì là video
}
