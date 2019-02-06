package com.credit.reports.parser;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CreditorContactFactory {
    private final Document document;
    private final List<CreditorContact> creditorContacts = new ArrayList<>();

    public CreditorContactFactory(Document document) {
        this.document = document;
        this.init();
    }

    private void init() {
        Elements rows;
        try {
            Elements elements = this.document.select("[ng-if='subscribers.length > 0']");
            rows = elements.get(0).child(0).children();
        } catch (Exception var4) {
            rows = new Elements();
        }

        for(int count = 1; count < rows.size(); ++count) {
            CreditorContact creditorContact = new CreditorContact();
            creditorContact.setCreditorName(rows.get(count).child(0).text());
            creditorContact.setAddress(rows.get(count).child(1).text());
            creditorContact.setPhoneNumber(rows.get(count).child(2).text());
            this.creditorContacts.add(creditorContact);
        }

    }

    public List<CreditorContact> getCreditorContacts() {
        return this.creditorContacts;
    }
}
