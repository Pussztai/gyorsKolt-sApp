//package com.example.asd2.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.List;
//
//@AllArgsConstructor
//@Getter
//@Setter
//public class SpendingResponse {
//    public Long totalSpending;
//    private Long dailySpending;
//    private Long weeklySpending;
//    private Long monthlySpending;
//    private List<CategorySummary> allSpending;
//
//    public SpendingResponse() {
//
//    }
//}


package com.example.asd2.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SpendingResponse {
    // Setters
    // Getters
    private Long totalSpending;
    private Long dailySpending;
    private Long weeklySpending;
    private Long monthlySpending;
    private List<CategorySummary> summary;

    public SpendingResponse() {
    }

}