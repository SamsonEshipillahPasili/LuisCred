package com.credit.reports.parser;

public class Inquiry {
    private String creditorName;
    private String typeOfBusiness;
    private String dateOfInquiry;
    private String creditBureau;

    public Inquiry() {
    }

    public String getCreditorName() {
        return this.creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getTypeOfBusiness() {
        return this.typeOfBusiness;
    }

    public void setTypeOfBusiness(String typeOfBusiness) {
        this.typeOfBusiness = typeOfBusiness;
    }

    public String getDateOfInquiry() {
        return this.dateOfInquiry;
    }

    public void setDateOfInquiry(String dateOfInquiry) {
        this.dateOfInquiry = dateOfInquiry;
    }

    public String getCreditBureau() {
        return this.creditBureau;
    }

    public void setCreditBureau(String creditBureau) {
        this.creditBureau = creditBureau;
    }

    public String toString() {
        return "Inquiry{creditorName=" + this.creditorName + ", typeOfBusiness=" + this.typeOfBusiness + ", dateOfInquiry=" + this.dateOfInquiry + ", creditBureau=" + this.creditBureau + '}';
    }
}
