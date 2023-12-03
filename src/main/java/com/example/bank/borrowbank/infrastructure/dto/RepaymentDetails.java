package com.example.bank.borrowbank.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RepaymentDetails(BigDecimal borrowerPaymentAmount, @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate date,
                               BigDecimal initialOutstandingPrincipal,
                               BigDecimal interest, BigDecimal principal, BigDecimal remainingOutstandingPrincipal) {

}
