package com.example.bank.borrowbank.core;

import com.example.bank.borrowbank.infrastructure.dto.LoanRequest;
import com.example.bank.borrowbank.infrastructure.dto.RepaymentDetails;
import com.example.bank.borrowbank.infrastructure.dto.RepaymentPlan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoanPaymentPlanServiceTest {
    @Mock
    private LoanRequest loanRequest;

    @InjectMocks
    private LoanPaymentPlanService loanPaymentPlanService;


    @Test
    void testCalculateRepaymentPlan() {
        // given
        Mockito.when(loanRequest.loanAmount()).thenReturn(new BigDecimal("10000"));
        Mockito.when(loanRequest.nominalRate()).thenReturn(new BigDecimal("5"));
        Mockito.when(loanRequest.duration()).thenReturn(12);
        Mockito.when(loanRequest.startDate()).thenReturn(LocalDate.of(2023, 1, 1));

        // when
        RepaymentPlan repaymentPlan = loanPaymentPlanService.calculateRePaymentPlan(loanRequest);

        // then
        List<RepaymentDetails> repaymentDetailsList = repaymentPlan.borrowerPayments();
        assertEquals(12, repaymentDetailsList.size());

        RepaymentDetails firstRepayment = repaymentDetailsList.get(0);
        assertEquals(new BigDecimal("856.09"), firstRepayment.borrowerPaymentAmount());
        assertEquals(LocalDate.of(2023, 1, 1), firstRepayment.date());
        assertEquals(new BigDecimal("10000"), firstRepayment.initialOutstandingPrincipal());
        assertEquals(new BigDecimal("42.47"), firstRepayment.interest());
        assertEquals(new BigDecimal("813.62"), firstRepayment.principal());
        assertEquals(new BigDecimal("9186.38"), firstRepayment.remainingOutstandingPrincipal());


    }
}