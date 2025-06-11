package com.gentlemonster.GentleMonsterBE.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "warehouse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<WarehouseProduct> warehouseProducts;

    @Column(name = "warehouse_name", length = 200, nullable = false)
    private String warehouseName; // Tên kho hàng

    @Column(name = "warehouse_location", length = 2000, nullable = false)
    private String warehouseLocation; // Vị trí trong kho

    @Column(name = "street", length = 200)
    private String street; // Đường

    @Column(name = "ward", length = 150)
    private String ward; // Phường

    @Column(name = "district", length = 100)
    private String district; // Quận

    @Column(name = "city", length = 100)
    private String city; // Thành phố

    @Column(name = "country", length = 100)
    private String country; // Quốc gia

//    @Column(name = "status_product")
//    private boolean statusProduct; // Trạng thái sản phẩm

    @Column(name = "total_capacity")
    private int totalCapacity; // Dung lượng tối đa của kho

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "modified_date")
    private Date modifiedDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Người quản lý kho hàng này (User)
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "warehouse_media",
            joinColumns = @JoinColumn(name = "warehouse_id"),
            inverseJoinColumns = @JoinColumn(name = "meida_id")
    )
    private List<Media> medias = new ArrayList<>();

}
