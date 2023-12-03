package com.example.bank.borrowbank.infrastructure.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanRequest (@Min(1) BigDecimal loanAmount, @Min(1) BigDecimal nominalRate, @Min(1) Integer duration,@NotEmpty @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
LocalDate startDate) {}

