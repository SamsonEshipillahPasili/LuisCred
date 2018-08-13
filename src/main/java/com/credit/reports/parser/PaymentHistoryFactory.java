package com.credit.reports.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author eshipillah
 */
public class PaymentHistoryFactory {

    private final Element paymentHistoryTable;
    private final PaymentHistory transUnion;
    private final PaymentHistory experian;
    private final PaymentHistory equifax;

    public PaymentHistoryFactory(Element paymentHistoryTable) {
        this.paymentHistoryTable = paymentHistoryTable;
        transUnion = new PaymentHistory();
        experian = new PaymentHistory();
        equifax = new PaymentHistory();
        this.init();
    }

    private void init() {
        Elements rows = paymentHistoryTable.child(0).children();
        for (int count = 0; count < rows.size(); count++) {
            switch (count) {
                case 0: // we are on row 0 ...
                    // deal with months
                    Elements months = rows.get(count).children();
                    for (int i = 1; i < months.size(); i++) {
                        transUnion.getOrderedMonthsList().add(months.get(i).text());
                        experian.getOrderedMonthsList().add(months.get(i).text());
                        equifax.getOrderedMonthsList().add(months.get(i).text());
                    }
                    break;
                case 1:
                    // deal with years
                    Elements years = rows.get(count).children();
                    for (int i = 1; i < years.size(); i++) {
                        transUnion.getOrderedYearList().add(years.get(i).text());
                        experian.getOrderedYearList().add(years.get(i).text());
                        equifax.getOrderedYearList().add(years.get(i).text());
                    }
                    break;
                case 2:
                    // deal with transUnion
                    Elements tPaymentStati = rows.get(count).children();
                    for (int i = 1; i < tPaymentStati.size(); i++) {
                        transUnion.getOrderedPaymentList().add(tPaymentStati.get(i).text());
                    }
                    break;
                case 3:
                    // deal with experian
                    Elements exPaymentStati = rows.get(count).children();
                    for (int i = 1; i < exPaymentStati.size(); i++) {
                        experian.getOrderedPaymentList().add(exPaymentStati.get(i).text());
                    }
                    break;
                case 4:
                    // deal with equifax
                    Elements eqPaymentStati = rows.get(count).children();
                    for (int i = 1; i < eqPaymentStati.size(); i++) {
                        equifax.getOrderedPaymentList().add(eqPaymentStati.get(i).text());
                    }
                    break;

            }
        }
    }

    public PaymentHistory transUnion() {
        return this.transUnion;
    }

    public PaymentHistory experian() {
        return this.experian;
    }

    public PaymentHistory equifax() {
        return this.equifax;
    }

}