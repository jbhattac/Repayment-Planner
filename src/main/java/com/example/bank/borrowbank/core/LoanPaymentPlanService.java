package com.example.bank.borrowbank.core;

import com.example.bank.borrowbank.infrastructure.dto.LoanRequest;
import com.example.bank.borrowbank.infrastructure.dto.RepaymentDetails;
import com.example.bank.borrowbank.infrastructure.dto.RepaymentPlan;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanPaymentPlanService {

    public RepaymentPlan calculateRePaymentPlan(LoanRequest loanRequest){

        List<RepaymentDetails> repaymentDetailsList = new ArrayList<>();
        BigDecimal remainingOutstandingPrincipal = loanRequest.loanAmount();
        BigDecimal annuity = calculateAnnuity(loanRequest.loanAmount(), loanRequest.nominalRate(), loanRequest.duration());
        BigDecimal initialOutstandingPrincipal = loanRequest.loanAmount();
        for (int i = 0; i < loanRequest.duration(); i++) {
            BigDecimal interest = calculateInterest(loanRequest.nominalRate(), remainingOutstandingPrincipal, loanRequest.startDate());
            BigDecimal principal = calculatePrincipal(annuity, interest, remainingOutstandingPrincipal);

            if (principal.compareTo(remainingOutstandingPrincipal) > 0) {
                principal = remainingOutstandingPrincipal;
            }

            remainingOutstandingPrincipal = remainingOutstandingPrincipal.subtract(principal);

            RepaymentDetails repaymentDetails = new RepaymentDetails(
                    annuity, loanRequest.startDate().plusMonths(i), initialOutstandingPrincipal,
                    interest, principal, remainingOutstandingPrincipal
            );
            initialOutstandingPrincipal = remainingOutstandingPrincipal;

            repaymentDetailsList.add(repaymentDetails);
        }

        return new RepaymentPlan(repaymentDetailsList);
    }


    private BigDecimal calculateAnnuity(BigDecimal loanAmount, BigDecimal nominalRate, int duration) {

        if (loanAmount.compareTo(BigDecimal.ZERO) <= 0 || nominalRate.compareTo(BigDecimal.ZERO) < 0 || duration <= 0) {
            throw new IllegalArgumentException("Invalid input parameters. Loan amount, nominal rate, and duration must be positive values.");
        }

        BigDecimal monthlyRate = nominalRate.divide(BigDecimal.valueOf(12).multiply(BigDecimal.valueOf(100)), 5, RoundingMode.HALF_EVEN);

        BigDecimal powerTerm = BigDecimal.ONE.add(monthlyRate).pow(duration);

        BigDecimal annuity = loanAmount.multiply(monthlyRate).multiply(powerTerm)
                .divide(powerTerm.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_EVEN);

        return roundToTwoDecimalPlaces(annuity);
    }



    private BigDecimal calculateInterest(BigDecimal nominalRate, BigDecimal remainingOutstandingPrincipal, LocalDate stLocalDate) {

        if (nominalRate.compareTo(BigDecimal.ZERO) < 0 || remainingOutstandingPrincipal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid input parameters. Nominal rate and remaining outstanding principal must be non-negative.");
        }

        int daysInMonth = stLocalDate.getMonth().length(true);
        int daysInYear = 365;
        BigDecimal rate = nominalRate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
        BigDecimal interest = rate.multiply(BigDecimal.valueOf(daysInMonth)).multiply(remainingOutstandingPrincipal)
                .divide(BigDecimal.valueOf(daysInYear), 2, RoundingMode.HALF_EVEN);

        return roundToTwoDecimalPlaces(interest);
    }
    private BigDecimal calculatePrincipal(BigDecimal annuity, BigDecimal interest, BigDecimal remainingOutstandingPrincipal) {
        // Validate input parameters
        if (annuity.compareTo(BigDecimal.ZERO) < 0 || interest.compareTo(BigDecimal.ZERO) < 0 || remainingOutstandingPrincipal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid input parameters. Annuity, interest, and remaining outstanding principal must be non-negative.");
        }

        BigDecimal principal = annuity.subtract(interest);

        if (principal.compareTo(remainingOutstandingPrincipal) > 0) {
            principal = remainingOutstandingPrincipal;
        }

        return roundToTwoDecimalPlaces(principal);
    }

    private BigDecimal roundToTwoDecimalPlaces(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_EVEN);
    }
}
