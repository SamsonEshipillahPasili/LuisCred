package com.credit.reports.parser;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class InquiryFactory {
    private final List<Inquiry> inquiries = new ArrayList();
    private final Document document;

    public InquiryFactory(Document document) {
        this.document = document;
        this.init();
    }

    private void init() {
        Elements rows;
        try {
            Elements elements = this.document.select("[ng-if='inquiryPartition.length>0']");
            rows = ((Element)elements.get(0)).child(0).children();
        } catch (Exception var5) {
            rows = new Elements();
        }

        for(int count = 1; count < rows.size(); ++count) {
            Inquiry inquiry = new Inquiry();
            inquiry.setCreditorName(((Element)rows.get(count)).child(0).text());
            inquiry.setTypeOfBusiness(((Element)rows.get(count)).child(1).text());
            inquiry.setDateOfInquiry(((Element)rows.get(count)).child(2).text());
            inquiry.setCreditBureau(((Element)rows.get(count)).child(3).text());
            this.inquiries.add(inquiry);
        }

    }

    public List<Inquiry> getInquiries() {
        return this.inquiries;
    }
}
