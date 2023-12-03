package com.example.bank.borrowbank.infrastructure;

import com.example.bank.borrowbank.core.LoanPaymentPlanService;
import com.example.bank.borrowbank.infrastructure.dto.LoanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generate-plan")
public class LoanResource {
    @Autowired
    private LoanPaymentPlanService loanPaymentPlanService;

    @PostMapping
    public ResponseEntity<?> generatePlan(@RequestBody LoanRequest loanRequest) {
       return  ResponseEntity.ok(loanPaymentPlanService.calculateRePaymentPlan(loanRequest));
    }
}
