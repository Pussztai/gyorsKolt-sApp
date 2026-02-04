//package com.example.asd2.dto;
//
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//
//@AllArgsConstructor
//@Getter
//@Setter
//public class SpendingRequest {
//    private String Category;
//    private Integer amount;
//    private String pymentMethod;
//    private String comment;
//}


package com.example.asd2.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SpendingRequest {
    // Setters
    // Getters
    private String category;
    private Integer amount;
    private String paymentMethod;
    private String comment;

    // Konstruktorok
    public SpendingRequest() {
    }

//    public SpendingRequest(String category, Integer amount, String paymentMethod, String comment) {
//        this.category = category;
//        this.amount = amount;
//        this.paymentMethod = paymentMethod;
//        this.comment = comment;
//    }

}