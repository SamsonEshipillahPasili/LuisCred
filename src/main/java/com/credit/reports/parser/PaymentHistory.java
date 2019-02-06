package com.credit.reports.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PaymentHistory {
    private final List<String> orderedMonthsList = new ArrayList<>();
    private final List<String> orderedYearList = new ArrayList<>();
    private final List<String> orderedPaymentList = new ArrayList<>();

    public List<String> getOrderedMonthsList() {
        return this.orderedMonthsList;
    }

    public List<String> getOrderedYearList() {
        return this.orderedYearList;
    }

    public List<String> getOrderedPaymentList() {
        return this.orderedPaymentList;
    }

    public String toString() {
        return "PaymentHistory{orderedMonthsList=" + this.orderedMonthsList + ", orderedYearList=" + this.orderedYearList + ", orderedPaymentList=" + this.orderedPaymentList + '}';
    }

    public int getLatePayments() {
        int count = 0;

        for (String payment : this.orderedPaymentList) {
            try {
                payment = payment.trim();
                Integer.parseInt(payment);
                ++count;
            } catch (NumberFormatException var5) {
            }
        }

        return count;
    }
}
