package com.credit.reports.parser;

import java.util.HashMap;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CreditScoreFactory {
    private final Document document;
    private final Map<String, CreditScore> csMap;

    public CreditScoreFactory(Document document) {
        this.document = document;
        this.csMap = new HashMap<>();
        CreditScore transUnion = new CreditScore();
        CreditScore experian = new CreditScore();
        CreditScore equifax = new CreditScore();

        Elements rows;
        try {
            Element tableBody = this.document.select("#CreditScore").get(0).nextElementSibling().nextElementSibling().child(0);
            rows = tableBody.children();
        } catch (Exception var9) {
            rows = new Elements();
        }

        for(int count = 1; count < rows.size(); ++count) {
            Element currentRow = rows.get(count);
            Elements currentColumns = currentRow.children();
            switch(count) {
                case 1:
                    transUnion.setCreditScoreValue(currentColumns.get(1).text());
                    experian.setCreditScoreValue(currentColumns.get(2).text());
                    equifax.setCreditScoreValue(currentColumns.get(3).text());
                    break;
                case 2:
                    transUnion.setLenderRank(currentColumns.get(1).text());
                    experian.setLenderRank(currentColumns.get(2).text());
                    equifax.setLenderRank(currentColumns.get(3).text());
                    break;
                case 3:
                    transUnion.setScoreScale(currentColumns.get(1).text());
                    experian.setScoreScale(currentColumns.get(2).text());
                    equifax.setScoreScale(currentColumns.get(3).text());
            }
        }

        this.csMap.put("experian", experian);
        this.csMap.put("transUnion", transUnion);
        this.csMap.put("equifax", equifax);
    }

    public CreditScore transUnion() {
        return this.csMap.get("transUnion");
    }

    public CreditScore experian() {
        return this.csMap.get("experian");
    }

    public CreditScore equifax() {
        return this.csMap.get("equifax");
    }
}
