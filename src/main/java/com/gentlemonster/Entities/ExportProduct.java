package com.gentlemonster.Entities;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.google.auto.value.AutoValue.Builder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "export_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // anotaion này giúp tạo ra 1 builder để tạo ra 1 object mà không cần phải khởi
         // tạo giá trị cho tất cả các trường
public class ExportProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "name", length = 200)
    private String name;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "status", length = 100)
    private String status;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "modified_date")
    private Date modifiedDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_product_id")
    private WarehouseProduct warehouseProduct;

}
