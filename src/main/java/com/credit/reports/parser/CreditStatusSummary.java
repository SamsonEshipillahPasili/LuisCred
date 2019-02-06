package com.credit.reports.parser;

public class CreditStatusSummary {
    private String totalAccounts;
    private String openAccounts;
    private String closedAccounts;
    private String delinquent;
    private String derogatory;
    private String collection;
    private String balances;
    private String payments;
    private String publicRecords;
    private String inquiries;


    public String getTotalAccounts() {
        return this.totalAccounts;
    }

    public void setTotalAccounts(String totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    public String getOpenAccounts() {
        return this.openAccounts;
    }

    public void setOpenAccounts(String openAccounts) {
        this.openAccounts = openAccounts;
    }

    public String getClosedAccounts() {
        return this.closedAccounts;
    }

    public void setClosedAccounts(String closedAccounts) {
        this.closedAccounts = closedAccounts;
    }

    public String getDelinquent() {
        return this.delinquent;
    }

    public void setDelinquent(String delinquent) {
        this.delinquent = delinquent;
    }

    public String getDerogatory() {
        return this.derogatory;
    }

    public void setDerogatory(String derogatory) {
        this.derogatory = derogatory;
    }

    public String getCollection() {
        return this.collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getBalances() {
        return this.balances;
    }

    public void setBalances(String balances) {
        this.balances = balances;
    }

    public String getPayments() {
        return this.payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public String getPublicRecords() {
        return this.publicRecords;
    }

    public void setPublicRecords(String publicRecords) {
        this.publicRecords = publicRecords;
    }

    public String getInquiries() {
        return this.inquiries;
    }

    public void setInquiries(String inquiries) {
        this.inquiries = inquiries;
    }

    public String toString() {
        return "CreditStatusSummary{totalAccounts=" + this.totalAccounts + ", openAccounts=" + this.openAccounts + ", closedAccounts=" + this.closedAccounts + ", delinquent=" + this.delinquent + ", derogatory=" + this.derogatory + ", collection=" + this.collection + ", balances=" + this.balances + ", payments=" + this.payments + ", publicRecords=" + this.publicRecords + ", inquiries=" + this.inquiries + '}';
    }
}
