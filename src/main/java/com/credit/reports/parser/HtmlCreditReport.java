package com.credit.reports.parser;

import java.io.Serializable;
import org.jsoup.nodes.Document;

public class HtmlCreditReport implements Serializable {
    private final Document document;

    public HtmlCreditReport(Document document) {
        this.document = document;
    }

    public MetaData metaData() {
        return new MetaDataImpl(this.document);
    }

    public CustomerStatement customerStatement() {
        return new CustomerStatementImpl(this.document);
    }

    public PersonalInformationFactory personalInformationFactory() {
        return new PersonalInformationFactory(this.document);
    }

    public CreditScoreFactory creditScoreFactory() {
        return new CreditScoreFactory(this.document);
    }

    public CreditStatusSummaryFactory creditStatusSummaryFactory() {
        return new CreditStatusSummaryFactory(this.document);
    }

    public AccountInformationFactory accountInformationFactory() {
        return new AccountInformationFactory(this.document);
    }

    public InquiryFactory inquiryFactory() {
        return new InquiryFactory(this.document);
    }

    public CreditorContactFactory creditorContactFactory() {
        return new CreditorContactFactory(this.document);
    }
}
