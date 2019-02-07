package com.credit.reports.parser;

public class AccountInformation {
    private String accountName;
    private String accountNumer;
    private String accountType;
    private String detailedAccountType;
    private String beaureuCode;
    private String accountStatus;
    private String monthlyPayment;
    private String dateOpened;
    private String balance;
    private String noOfMonths;
    private String highCredit;
    private String creditLimit;
    private String pastDue;
    private String paymentStatus;
    private String lastReported;
    private String comments;
    private String dateLastActive;
    private String dateOfLastPayment;
    private PaymentHistory paymentHistory;


    public String getAccountName() {
        return this.accountName.replace("-", "");
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumer() {
        return this.accountNumer.replace("-", "");
    }

    public void setAccountNumer(String accountNumer) {
        this.accountNumer = accountNumer;
    }

    public String getAccountType() {
        return this.accountType.replace("-", "");
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getDetailedAccountType() {
        return this.detailedAccountType.replace("-", "");
    }

    public void setDetailedAccountType(String detailedAccountType) {
        this.detailedAccountType = detailedAccountType;
    }

    public String getBeaureuCode() {
        return this.beaureuCode.replace("-", "");
    }

    public void setBeaureuCode(String beaureuCode) {
        this.beaureuCode = beaureuCode;
    }

    public String getAccountStatus() {
        return this.accountStatus.replace("-", "");
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getMonthlyPayment() {
        return this.monthlyPayment.replace("-", "");
    }

    public void setMonthlyPayment(String monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public String getDateOpened() {
        return this.dateOpened.replace("-", "");
    }

    public void setDateOpened(String dateOpened) {
        this.dateOpened = dateOpened;
    }

    public String getBalance() {
        return this.balance.trim().isEmpty() ? "0" : this.balance.replace("-", "");
    }

    public String getRawCreditLimit() {
        return this.creditLimit.replace("-", "");
    }

    public String getRawBalance() {
        return this.balance.replace("-", "");
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getNoOfMonths() {
        return this.noOfMonths.replace("-", "");
    }

    public void setNoOfMonths(String noOfMonths) {
        this.noOfMonths = noOfMonths;
    }

    public String getHighCredit() {
        return this.highCredit.replace("-", "");
    }

    public void setHighCredit(String highCredit) {
        this.highCredit = highCredit;
    }

    public String getCreditLimit() {
        return this.creditLimit.trim().isEmpty() ? "0" : this.creditLimit.trim().replace("-", "");
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getPastDue() {
        return this.pastDue.replace("-", "");
    }

    public void setPastDue(String pastDue) {
        this.pastDue = pastDue;
    }

    public String getPaymentStatus() {
        return this.paymentStatus.replace("-", "");
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getLastReported() {
        return this.lastReported.replace("-", "");
    }

    public void setLastReported(String lastReported) {
        this.lastReported = lastReported;
    }

    public String getComments() {
        return this.comments.replace("-", "");
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDateLastActive() {
        return this.dateLastActive.replace("-", "");
    }

    public void setDateLastActive(String dateLastActive) {
        this.dateLastActive = dateLastActive;
    }

    public String getDateOfLastPayment() {
        return this.dateOfLastPayment.replace("-", "");
    }

    public void setDateOfLastPayment(String dateOfLastPayment) {
        this.dateOfLastPayment = dateOfLastPayment;
    }

    public PaymentHistory getPaymentHistory() {
        return this.paymentHistory;
    }

    public void setPaymentHistory(PaymentHistory paymentHistory) {
        this.paymentHistory = paymentHistory;
    }

    public String toString() {
        return "AccountInformation{accountName=" + this.accountName + ", accountNumer=" + this.accountNumer + ", accountType=" + this.accountType + ", detailedAccountType=" + this.detailedAccountType + ", beaureuCode=" + this.beaureuCode + ", accountStatus=" + this.accountStatus + ", monthlyPayment=" + this.monthlyPayment + ", dateOpened=" + this.dateOpened + ", balance=" + this.balance + ", noOfMonths=" + this.noOfMonths + ", highCredit=" + this.highCredit + ", creditLimit=" + this.creditLimit + ", pastDue=" + this.pastDue + ", paymentStatus=" + this.paymentStatus + ", lastReported=" + this.lastReported + ", comments=" + this.comments + ", dateLastActive=" + this.dateLastActive + ", dateOfLastPayment=" + this.dateOfLastPayment + ", paymentHistory=" + this.paymentHistory + '}';
    }

    public String getDerogatoryStyle(){
        if(this.accountStatus.toLowerCase().contains("derogatory")){
            return "vertical-align: middle;background-color: #ffb7b3";
        }

        return "vertical-align: middle;background-color: white";
    }


    public String getLateStyle(){
        if(getPaymentHistory().getLatePayments() > 0){
            return "vertical-align: middle;background-color: #ffb7b3";
        }

        return "vertical-align: middle;background-color: white";
    }
}
