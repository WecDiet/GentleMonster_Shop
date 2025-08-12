//package com.gentlemonster.Entities;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@Getter
//@Setter
//@Entity
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "shippings")
//public class Shipping {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Column(name = "id")
//    private UUID id;
//
//    @Column(name = "fullName", nullable = false)
//    private String fullName;
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
//    @Column(name = "city", length = 100)
//    private String city;
//
//    @Column(name = "country", length = 100)
//    private String country;
//
//    @Column(name = "phone", nullable = false)
//    private String phone;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "shipping")
//    private List<Order> orderList;
//}
