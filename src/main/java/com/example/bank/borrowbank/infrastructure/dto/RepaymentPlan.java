package com.example.bank.borrowbank.infrastructure.dto;

import java.util.List;

public record RepaymentPlan(List<RepaymentDetails> borrowerPayments) {
}
