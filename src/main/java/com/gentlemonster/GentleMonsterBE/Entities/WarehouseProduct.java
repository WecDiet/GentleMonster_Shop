package com.gentlemonster.GentleMonsterBE.Entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "warehouse_product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product products;

    @Column(name = "product_name", length = 500, nullable = false)
    private String productName; // Thêm trường này

    @Column(name = "import_date")
    private LocalDateTime importDate; // Ngày nhập hàng

    @Column(name = "quantity", nullable = false)
    private BigInteger quantity; // Số lượng ProductType trong kho

    @Column(name = "public_product")
    private boolean publicProduct; // Kiểu Sản phẩm công khai =  true: công khai, false: không công khai

    @Column(name = "import_price")
    private BigInteger importPrice; // Giá nhập hàng

    @Column(name = "product_type")
    private String productType; // Loại sản phẩm

    @Column(name = "total_import_price")
    private BigInteger totalImportPrice; // Tổng giá nhập hàng

    // Nhà sản xuất
    @Column(name = "manufacturer", length = 200)
    private String manufacturer;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "warehouse_product_media",
            joinColumns = @JoinColumn(name = "warehouse_product_id"),
            inverseJoinColumns = @JoinColumn(name = "meida_id")
    )
    private List<Media> images = new ArrayList<>();
}
