package com.example.microserviesspringecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;

    @ManyToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Customer customer;

    @ManyToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Product product;
}
