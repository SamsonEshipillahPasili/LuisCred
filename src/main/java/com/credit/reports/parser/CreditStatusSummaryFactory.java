package com.credit.reports.parser;

import java.util.HashMap;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CreditStatusSummaryFactory {
    private final Document document;
    private final Map<String, CreditStatusSummary> cssMap;

    public CreditStatusSummaryFactory(Document document) {
        this.document = document;
        this.cssMap = new HashMap<>();
        CreditStatusSummary transUnion = new CreditStatusSummary();
        CreditStatusSummary experian = new CreditStatusSummary();
        CreditStatusSummary equifax = new CreditStatusSummary();

        Elements rows;
        try {
            Element tableBody = this.document.select("#Summary").get(0).nextElementSibling().nextElementSibling().child(0);
            rows = tableBody.children();
        } catch (Exception var9) {
            rows = new Elements();
        }

        for(int count = 1; count < rows.size(); ++count) {
            Element currentRow = rows.get(count);
            Elements currentRowColumns = currentRow.children();
            switch(count) {
                case 1:
                    transUnion.setTotalAccounts(currentRowColumns.get(1).text());
                    experian.setTotalAccounts(currentRowColumns.get(2).text());
                    equifax.setTotalAccounts(currentRowColumns.get(3).text());
                    break;
                case 2:
                    transUnion.setOpenAccounts(currentRowColumns.get(1).text());
                    experian.setOpenAccounts(currentRowColumns.get(2).text());
                    equifax.setOpenAccounts(currentRowColumns.get(3).text());
                    break;
                case 3:
                    transUnion.setClosedAccounts(currentRowColumns.get(1).text());
                    experian.setClosedAccounts(currentRowColumns.get(2).text());
                    equifax.setClosedAccounts(currentRowColumns.get(3).text());
                    break;
                case 4:
                    transUnion.setDelinquent(currentRowColumns.get(1).text());
                    experian.setDelinquent(currentRowColumns.get(2).text());
                    equifax.setDelinquent(currentRowColumns.get(3).text());
                    break;
                case 5:
                    transUnion.setDerogatory(currentRowColumns.get(1).text());
                    experian.setDerogatory(currentRowColumns.get(2).text());
                    equifax.setDerogatory(currentRowColumns.get(3).text());
                    break;
                case 6:
                    transUnion.setCollection(currentRowColumns.get(1).text());
                    experian.setCollection(currentRowColumns.get(2).text());
                    equifax.setCollection(currentRowColumns.get(3).text());
                    break;
                case 7:
                    transUnion.setBalances(currentRowColumns.get(1).text());
                    experian.setBalances(currentRowColumns.get(2).text());
                    equifax.setBalances(currentRowColumns.get(3).text());
                    break;
                case 8:
                    transUnion.setPayments(currentRowColumns.get(1).text());
                    experian.setPayments(currentRowColumns.get(2).text());
                    equifax.setPayments(currentRowColumns.get(3).text());
                    break;
                case 9:
                    transUnion.setPublicRecords(currentRowColumns.get(1).text());
                    experian.setPublicRecords(currentRowColumns.get(2).text());
                    equifax.setPublicRecords(currentRowColumns.get(3).text());
                    break;
                case 10:
                    transUnion.setInquiries(currentRowColumns.get(1).text());
                    experian.setInquiries(currentRowColumns.get(2).text());
                    equifax.setInquiries(currentRowColumns.get(3).text());
            }
        }

        this.cssMap.put("transUnion", transUnion);
        this.cssMap.put("experian", experian);
        this.cssMap.put("equifax", equifax);
    }

    public CreditStatusSummary transUnion() {
        return this.cssMap.get("transUnion");
    }

    public CreditStatusSummary experian() {
        return this.cssMap.get("experian");
    }

    public CreditStatusSummary equifax() {
        return this.cssMap.get("equifax");
    }
}
