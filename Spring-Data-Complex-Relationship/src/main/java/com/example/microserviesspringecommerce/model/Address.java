package com.example.microserviesspringecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collection;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length=100) 
    private String address_line1;
    @Column(length=100) 
    private String address_line2;
    @Column(length=12) 
    private String phone;
    private String city;
    private String country;
    private Integer postal_code;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "address_customer",
            joinColumns = @JoinColumn(name = "address_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id",referencedColumnName = "id"))
    private Collection<Customer> customers;


}
