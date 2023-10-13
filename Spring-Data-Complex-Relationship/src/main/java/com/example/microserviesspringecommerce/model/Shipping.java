package com.example.microserviesspringecommerce.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "shipping")
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date shipment_date;
    private String address;
    private String city;
    private String country;
    private Integer postal_code;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
    @OneToOne(mappedBy = "shipping")
    private Order order;
    
}
