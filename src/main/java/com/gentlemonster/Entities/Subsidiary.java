//package com.gentlemonster.Entities;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//import java.util.UUID;
//
//@Entity
//@Getter
//@Setter
//@Table(name = "Subsidiaries")
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Subsidiary {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    @Column(name = "name", length = 500, nullable = false)
//    private String companyName;
//
//    @Column(name = "street", length = 200)
//    private String street;
//
//    @Column(name = "ward", length = 150)
//    private String ward;
//
//    @Column(name = "district", length = 100)
//    private String district;
//
//    @Column(name = "phone", length = 100)
//    private String hotLine;
//
//    @Column(name = "email", length = 100)
//    private String email;
//
//    @Column(name = "status", nullable = false)
//    private boolean status;
//
//    @Column(name = "slug", length = 500, nullable = false)
//    private String slug;
//
//    @Column(columnDefinition = "TEXT")
//    private String description;
//
//    @CreationTimestamp
//    @Column(name = "createdAt")
//    private LocalDateTime createdAt;
//
//    @CreationTimestamp
//    @Column(name = "updatedAt")
//    private LocalDateTime  updatedAt;
//
//    @ManyToOne
//    @JoinColumn(name = "city_id")
//    private City city;
//
//    @ManyToMany(mappedBy = "subsidiaries")
//    private Set<ProductType> productTypes;
//
//    @OneToMany(mappedBy = "subsidiary",cascade = CascadeType.ALL)
//    private Set<User> users;
//}
