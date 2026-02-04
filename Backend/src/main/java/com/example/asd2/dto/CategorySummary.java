//package com.example.asd2.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//
//@AllArgsConstructor
//@Getter
//@Setter
//public class CategorySummary {
//    private String category;
//    private Long total;
//    private Double percentage;
//
//
//}


package com.example.asd2.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategorySummary {
    // Setters
    // Getters
    private String category;
    private Long total;
    private Double percentage;

    public CategorySummary() {
    }

    public CategorySummary(String category, Long total) {
        this.category = category;
        this.total = total;
    }

    public CategorySummary(String s, long l, double v) {
    }

}