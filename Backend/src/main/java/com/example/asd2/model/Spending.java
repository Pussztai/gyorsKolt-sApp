//package com.example.asd2.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDateTime;
//
//@AllArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "spendings")
//public class Spending {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @Column(nullable = false)
//    private String category;
//
//    @Column(nullable = false)
//    private Integer amount;
//
//    @Column
//    private String paymentMethod;
//
//    @Column
//    private String comment;
//
//    @Column(nullable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//
//    public Spending() {
//
//    }
//}


package com.example.asd2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "spending")
public class Spending {

    // Setters
    // Getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer amount;

    @Column
    private String paymentMethod;

    @Column
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Konstruktorok
    public Spending() {
    }

}