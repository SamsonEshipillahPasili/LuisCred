package com.credit.reports.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author eshipillah
 */
public class PaymentHistory {

    private final List<String> orderedMonthsList;
    private final List<String> orderedYearList;
    private final List<String> orderedPaymentList;

    public PaymentHistory() {
        orderedMonthsList = new ArrayList<>();
        orderedYearList = new ArrayList<>();
        orderedPaymentList = new ArrayList<>();
    }

    public List<String> getOrderedMonthsList() {
        return orderedMonthsList;
    }

    public List<String> getOrderedYearList() {
        return orderedYearList;
    }

    public List<String> getOrderedPaymentList() {
        return orderedPaymentList;
    }

    @Override
    public String toString() {
        return "PaymentHistory{" + "orderedMonthsList=" + orderedMonthsList + ", orderedYearList=" + orderedYearList + ", orderedPaymentList=" + orderedPaymentList + '}';
    }

    public int getLatePayments() {
        int count = 0;
        for (String payment : this.orderedPaymentList) {
            try {
                payment = payment.trim();
                Integer.parseInt(payment);
                count++;
            } catch (NumberFormatException e) {
                // value not a number 
            }
        }
        return count;
    }
}
