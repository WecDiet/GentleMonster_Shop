package com.gentlemonster.GentleMonsterBE.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 500, nullable = false)
    private String name;

    @Column(name = "description", length = 5000)
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "thumbnail", length = 300)
    private String thumbnail;

    @Column(name = "status", nullable = false)
    private boolean status = false;

    @Column(name = "slug", length = 500, nullable = false)
    private String slug;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

//    @ManyToMany(mappedBy = "likeProductList")
//    @JsonIgnore
//    private List<User> userLikedList;

    @JsonIgnore
    @ManyToMany(mappedBy = "viewedProductList")
    private List<User> userViewedList;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    @JsonIgnoreProperties("products")
    private ProductType productType;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Feedback> feedBackList = new ArrayList<>();

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ProductImage> productImages;

    @OneToOne(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private ProductDetail productDetail;

//    @OneToMany(mappedBy = "products",cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY)
//    private List<WarehouseProduct> warehouseProducts;
}
