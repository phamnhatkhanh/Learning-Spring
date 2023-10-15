package com.example.microserviesspringecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "image"}))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition="TEXT")
    private String description;
    private Integer current_quantity;
    private Double costPrice;

    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product")
    private List<WhishList> whishLists;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "catefory_id",referencedColumnName = "id"))
    private  List<Category> categories;
}
