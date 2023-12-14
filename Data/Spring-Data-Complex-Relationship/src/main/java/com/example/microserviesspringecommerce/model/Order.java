package com.example.microserviesspringecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double total_price;
    private String order_status;
    private Date order_date;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;

    @OneToOne(fetch = FetchType.LAZY)
    private Shipping shipping;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
    



}
