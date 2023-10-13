package com.javatechie.spring.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Table(name = "PERSONS_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @Column(name = "FISRT_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;


}
