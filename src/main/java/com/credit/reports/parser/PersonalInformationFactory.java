package com.credit.reports.parser;

import java.util.HashMap;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PersonalInformationFactory {
    private final Document document;
    private final Map<String, PersonalInformation> piMap;

    public PersonalInformationFactory(Document document) {
        this.document = document;
        this.piMap = new HashMap<>();

        Elements rows;
        try {
            Element tableBody = document.getElementsMatchingOwnText("Personal Information").get(0).nextElementSibling().nextElementSibling().child(0);
            rows = tableBody.children();
        } catch (Exception var9) {
            rows = new Elements();
        }

        PersonalInformation transUnion = new PersonalInformation();
        PersonalInformation equifax = new PersonalInformation();
        PersonalInformation experian = new PersonalInformation();

        for(int count = 1; count < rows.size(); ++count) {
            Element currentRow = rows.get(count);
            Elements currentRowColumns = currentRow.children();
            switch(count) {
                case 1:
                    transUnion.setCreditReportDate(currentRowColumns.get(1).text());
                    experian.setCreditReportDate(currentRowColumns.get(2).text());
                    equifax.setCreditReportDate(currentRowColumns.get(3).text());
                    break;
                case 2:
                    transUnion.setName(currentRowColumns.get(1).text());
                    experian.setName(currentRowColumns.get(2).text());
                    equifax.setName(currentRowColumns.get(3).text());
                    break;
                case 3:
                    transUnion.setAlias(currentRowColumns.get(1).text());
                    experian.setAlias(currentRowColumns.get(2).text());
                    equifax.setAlias(currentRowColumns.get(3).text());
                    break;
                case 4:
                    transUnion.setFormer(currentRowColumns.get(1).text());
                    experian.setFormer(currentRowColumns.get(2).text());
                    equifax.setFormer(currentRowColumns.get(3).text());
                    break;
                case 5:
                    transUnion.setDateOfBirth(currentRowColumns.get(1).text());
                    experian.setDateOfBirth(currentRowColumns.get(2).text());
                    equifax.setDateOfBirth(currentRowColumns.get(3).text());
                    break;
                case 6:
                    transUnion.setCurrentAddresses(currentRowColumns.get(1).text());
                    experian.setCurrentAddresses(currentRowColumns.get(2).text());
                    equifax.setCurrentAddresses(currentRowColumns.get(3).text());
                    break;
                case 7:
                    transUnion.setPreviousAddresses(currentRowColumns.get(1).text());
                    experian.setPreviousAddresses(currentRowColumns.get(2).text());
                    equifax.setPreviousAddresses(currentRowColumns.get(3).text());
                    break;
                case 8:
                    transUnion.setEmployers(currentRowColumns.get(1).text());
                    experian.setEmployers(currentRowColumns.get(2).text());
                    equifax.setEmployers(currentRowColumns.get(3).text());
            }
        }

        this.piMap.put("transUnion", transUnion);
        this.piMap.put("equifax", equifax);
        this.piMap.put("experian", experian);
    }

    public PersonalInformation experian() {
        return this.piMap.get("experian");
    }

    public PersonalInformation transUnion() {
        return this.piMap.get("transUnion");
    }

    public PersonalInformation equifax() {
        return this.piMap.get("equifax");
    }
}
