package com.example.bank.borrowbank.infrastructure;


import com.example.bank.borrowbank.infrastructure.dto.LoanRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.DEFAULT)
class LoanResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void generatePlan() throws Exception {
        LoanRequest loanRequest = new LoanRequest(
                new BigDecimal("10000"),
                new BigDecimal("5"),
                12,
                LocalDate.parse("2023-01-01")
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/generate-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.borrowerPayments").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.borrowerPayments.length()").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.borrowerPayments[0].borrowerPaymentAmount").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.borrowerPayments[0].date").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.borrowerPayments[0].initialOutstandingPrincipal").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.borrowerPayments[0].interest").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.borrowerPayments[0].principal").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.borrowerPayments[0].remainingOutstandingPrincipal").isNumber());
    }
}