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
        this.cssMap = new HashMap();
        CreditStatusSummary transUnion = new CreditStatusSummary();
        CreditStatusSummary experian = new CreditStatusSummary();
        CreditStatusSummary equifax = new CreditStatusSummary();

        Elements rows;
        try {
            Element tableBody = ((Element)this.document.select("#Summary").get(0)).nextElementSibling().nextElementSibling().child(0);
            rows = tableBody.children();
        } catch (Exception var10) {
            rows = new Elements();
        }

        for(int count = 1; count < rows.size(); ++count) {
            Element currentRow = (Element)rows.get(count);
            Elements currentRowColumns = currentRow.children();
            switch(count) {
                case 1:
                    transUnion.setTotalAccounts(((Element)currentRowColumns.get(1)).text());
                    experian.setTotalAccounts(((Element)currentRowColumns.get(2)).text());
                    equifax.setTotalAccounts(((Element)currentRowColumns.get(3)).text());
                    break;
                case 2:
                    transUnion.setOpenAccounts(((Element)currentRowColumns.get(1)).text());
                    experian.setOpenAccounts(((Element)currentRowColumns.get(2)).text());
                    equifax.setOpenAccounts(((Element)currentRowColumns.get(3)).text());
                    break;
                case 3:
                    transUnion.setClosedAccounts(((Element)currentRowColumns.get(1)).text());
                    experian.setClosedAccounts(((Element)currentRowColumns.get(2)).text());
                    equifax.setClosedAccounts(((Element)currentRowColumns.get(3)).text());
                    break;
                case 4:
                    transUnion.setDelinquent(((Element)currentRowColumns.get(1)).text());
                    experian.setDelinquent(((Element)currentRowColumns.get(2)).text());
                    equifax.setDelinquent(((Element)currentRowColumns.get(3)).text());
                    break;
                case 5:
                    transUnion.setDerogatory(((Element)currentRowColumns.get(1)).text());
                    experian.setDerogatory(((Element)currentRowColumns.get(2)).text());
                    equifax.setDerogatory(((Element)currentRowColumns.get(3)).text());
                    break;
                case 6:
                    transUnion.setCollection(((Element)currentRowColumns.get(1)).text());
                    experian.setCollection(((Element)currentRowColumns.get(2)).text());
                    equifax.setCollection(((Element)currentRowColumns.get(3)).text());
                    break;
                case 7:
                    transUnion.setBalances(((Element)currentRowColumns.get(1)).text());
                    experian.setBalances(((Element)currentRowColumns.get(2)).text());
                    equifax.setBalances(((Element)currentRowColumns.get(3)).text());
                    break;
                case 8:
                    transUnion.setPayments(((Element)currentRowColumns.get(1)).text());
                    experian.setPayments(((Element)currentRowColumns.get(2)).text());
                    equifax.setPayments(((Element)currentRowColumns.get(3)).text());
                    break;
                case 9:
                    transUnion.setPublicRecords(((Element)currentRowColumns.get(1)).text());
                    experian.setPublicRecords(((Element)currentRowColumns.get(2)).text());
                    equifax.setPublicRecords(((Element)currentRowColumns.get(3)).text());
                    break;
                case 10:
                    transUnion.setInquiries(((Element)currentRowColumns.get(1)).text());
                    experian.setInquiries(((Element)currentRowColumns.get(2)).text());
                    equifax.setInquiries(((Element)currentRowColumns.get(3)).text());
            }
        }

        this.cssMap.put("transUnion", transUnion);
        this.cssMap.put("experian", experian);
        this.cssMap.put("equifax", equifax);
    }

    public CreditStatusSummary transUnion() {
        return (CreditStatusSummary)this.cssMap.get("transUnion");
    }

    public CreditStatusSummary experian() {
        return (CreditStatusSummary)this.cssMap.get("experian");
    }

    public CreditStatusSummary equifax() {
        return (CreditStatusSummary)this.cssMap.get("equifax");
    }
}
