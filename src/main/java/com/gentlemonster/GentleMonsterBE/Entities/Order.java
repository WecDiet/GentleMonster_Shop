package com.gentlemonster.GentleMonsterBE.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "paymentType_id", nullable = false)
    private Paymenttype paymentMethod;

    // Phương thức giao hàng
    // 1. Giao hàng tận nơi , 2. Giao hàng tại cửa hàng, 3. Giao hàng qua bưu điện, 4. Giao hàng qua Grab,
    @ManyToOne
    @JoinColumn(name = "deliveryType_id", nullable = false)
    private DeliveryType deliveryMethod;

    @Column(name = "itemsPrice")
    private Long itemsPrice;

    @Column(name = "shippingPrice", nullable = false)
    private BigDecimal shippingPrice;

    @Column(name = "totalPrice", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "note", length = 2000)
    private String note;

    @Column(name = "quantity")
    private int quantity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // trạng thái thanh toán
    @Column(name = "isPaid", nullable = false)
    private boolean isPaid = false;

    // trạng thái giao hàng
    @Column(name = "isDelivered", nullable = false)
    private String isDelivered ; // "PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"

    // Ngày thanh toán
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "paidAt")
    private Date paidAt;

    // Ngày giao hàng
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "deliveryAt")
    private Date deliveryAt;

    @Column(name = "status")
    private boolean status;

}
