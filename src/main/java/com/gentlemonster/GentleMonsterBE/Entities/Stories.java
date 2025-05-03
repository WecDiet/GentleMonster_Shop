package com.gentlemonster.GentleMonsterBE.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "stories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// anotaion này giúp tạo ra 1 builder để tạo ra 1 object mà không cần phải khởi tạo giá trị cho tất cả các trường
public class Stories {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime  updatedAt;

    @ManyToOne
    @JoinColumn(name = "slider_id")
    private Slider slider;

}
