package com.gentlemonster.GentleMonsterBE.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
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

    @Column(name = "thumbnail", length = 500, nullable = false)
    private String thumbnail;

    @Column(name = "image", length = 500)
    private String image;  // Đường dẫn ảnh banner

    @Column(name = "slug", length = 100, nullable = false)
    private String slug;  // Đường dẫn URL của banner

    @Column(name = "status", nullable = false)
    private boolean status = true;  // Trạng thái hiển thị banner

    @Column(name = "seq", nullable = false)
    private int seq;  // Số thứ tự của banner từ 1 -> 4

    @Column(name = "serial", nullable = false)
    private int serial;  // Serial để phân biệt các banner, 0: image , 1: video

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
