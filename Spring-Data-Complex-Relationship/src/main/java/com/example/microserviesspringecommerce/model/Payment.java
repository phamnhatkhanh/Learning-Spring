package com.example.microserviesspringecommerce.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private String method_name;
    private Double amount;

    @OneToOne(mappedBy = "payment")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Customer customer;

}
