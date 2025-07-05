package com.gentlemonster.GentleMonsterBE.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Khung
    @Column(name = "frame", length = 100)
    private String frame;

    // Loại lens
    @Column(name = "lens", length = 100)
    private String lens;

    // Hình dàng
    @Column(name = "shape", length = 100)
    private String shape;

    // Chất liệu
    @Column(name = "material", length = 100)
    private String material;

    // Chiều rộng của lens
    @Column(name = "lens_width")
    private double lens_Width;

    // Chiều cao của lens
    @Column(name = "lens_height")
    private double lens_Height;

    // Vòng cầu của kính
    @Column(name = "bridge")
    private double bridge;

    // Nước sản xuất
    @Column(name = "country", length = 100)
    private String country;

    // Nhà sản xuất
    @Column(name = "manufacturer", length = 200)
    private String manufacturer;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "product_gallery",
            joinColumns = @JoinColumn(name = "product_detail_id"),
            inverseJoinColumns = @JoinColumn(name = "meida_id")
    )
    private List<Media> image = new ArrayList<>();
}
