package com.example.asd2.Controllers;


import com.example.asd2.Service.SpendingService;
import com.example.asd2.dto.SpendingRequest;
import com.example.asd2.dto.SpendingResponse;
import com.example.asd2.model.Spending;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")


public class SpendingController {
    @Autowired
    private SpendingService spendigService;

    @PostMapping("/spending")
    public ResponseEntity<SpendingResponse> addSpending(@RequestBody SpendingRequest request){
        SpendingResponse response = spendigService.addSpending(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics")
    public ResponseEntity<SpendingResponse> getStatictics(){
        SpendingResponse response = spendigService.getStatistics();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Spending>> getTransactions(){
        List<Spending> transactions = spendigService.getTransactions();
        return ResponseEntity.ok(transactions);

    }
}
