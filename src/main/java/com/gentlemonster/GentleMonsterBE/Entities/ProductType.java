package com.gentlemonster.GentleMonsterBE.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "slug", length = 100, nullable = false, unique = true)
    private String slug;

    @Column(name = "description", length = 5000)
    private String description;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "link", length = 500)
    private String linkURL;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;

    @ManyToOne
    @JoinColumn(name = "slider_id", nullable = false)
    private Slider slider;

    @OneToMany(mappedBy = "productType", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("productType")
    private List<Product> products = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties("productTypes")
    private Category category;
}
