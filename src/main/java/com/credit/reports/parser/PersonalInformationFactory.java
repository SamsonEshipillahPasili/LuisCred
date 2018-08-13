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
        this.piMap = new HashMap();

        Elements rows;
        try {
            Element tableBody = ((Element)document.getElementsMatchingOwnText("Personal Information").get(0)).nextElementSibling().nextElementSibling().child(0);
            rows = tableBody.children();
        } catch (Exception var10) {
            rows = new Elements();
        }

        PersonalInformation transUnion = new PersonalInformation();
        PersonalInformation equifax = new PersonalInformation();
        PersonalInformation experian = new PersonalInformation();

        for(int count = 1; count < rows.size(); ++count) {
            Element currentRow = (Element)rows.get(count);
            Elements currentRowColumns = currentRow.children();
            switch(count) {
                case 1:
                    transUnion.setCreditReportDate(((Element)currentRowColumns.get(1)).text());
                    experian.setCreditReportDate(((Element)currentRowColumns.get(2)).text());
                    equifax.setCreditReportDate(((Element)currentRowColumns.get(3)).text());
                    break;
                case 2:
                    transUnion.setName(((Element)currentRowColumns.get(1)).text());
                    experian.setName(((Element)currentRowColumns.get(2)).text());
                    equifax.setName(((Element)currentRowColumns.get(3)).text());
                    break;
                case 3:
                    transUnion.setAlias(((Element)currentRowColumns.get(1)).text());
                    experian.setAlias(((Element)currentRowColumns.get(2)).text());
                    equifax.setAlias(((Element)currentRowColumns.get(3)).text());
                    break;
                case 4:
                    transUnion.setFormer(((Element)currentRowColumns.get(1)).text());
                    experian.setFormer(((Element)currentRowColumns.get(2)).text());
                    equifax.setFormer(((Element)currentRowColumns.get(3)).text());
                    break;
                case 5:
                    transUnion.setDateOfBirth(((Element)currentRowColumns.get(1)).text());
                    experian.setDateOfBirth(((Element)currentRowColumns.get(2)).text());
                    equifax.setDateOfBirth(((Element)currentRowColumns.get(3)).text());
                    break;
                case 6:
                    transUnion.setCurrentAddresses(((Element)currentRowColumns.get(1)).text());
                    experian.setCurrentAddresses(((Element)currentRowColumns.get(2)).text());
                    equifax.setCurrentAddresses(((Element)currentRowColumns.get(3)).text());
                    break;
                case 7:
                    transUnion.setPreviousAddresses(((Element)currentRowColumns.get(1)).text());
                    experian.setPreviousAddresses(((Element)currentRowColumns.get(2)).text());
                    equifax.setPreviousAddresses(((Element)currentRowColumns.get(3)).text());
                    break;
                case 8:
                    transUnion.setEmployers(((Element)currentRowColumns.get(1)).text());
                    experian.setEmployers(((Element)currentRowColumns.get(2)).text());
                    equifax.setEmployers(((Element)currentRowColumns.get(3)).text());
            }
        }

        this.piMap.put("transUnion", transUnion);
        this.piMap.put("equifax", equifax);
        this.piMap.put("experian", experian);
    }

    public PersonalInformation experian() {
        return (PersonalInformation)this.piMap.get("experian");
    }

    public PersonalInformation transUnion() {
        return (PersonalInformation)this.piMap.get("transUnion");
    }

    public PersonalInformation equifax() {
        return (PersonalInformation)this.piMap.get("equifax");
    }
}
