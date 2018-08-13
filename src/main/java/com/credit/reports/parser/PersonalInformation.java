package com.credit.reports.parser;

public class PersonalInformation {
    private String creditReportDate = "";
    private String name = "";
    private String alias = "";
    private String former = "";
    private String dateOfBirth = "";
    private String currentAddresses = "";
    private String previousAddresses = "";
    private String employers = "";

    public PersonalInformation() {
    }

    public String getCreditReportDate() {
        return this.creditReportDate.replace("-", "");
    }

    public void setCreditReportDate(String creditReportDate) {
        this.creditReportDate = creditReportDate;
    }

    public String getName() {
        return this.name.replace("-", "");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return this.alias.replace("-", "");
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFormer() {
        return this.former.replace("-", "");
    }

    public void setFormer(String former) {
        this.former = former;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth.replace("-", "");
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCurrentAddresses() {
        return this.currentAddresses.replace("-", "");
    }

    public void setCurrentAddresses(String currentAddresses) {
        this.currentAddresses = currentAddresses;
    }

    public String getPreviousAddresses() {
        return this.previousAddresses.replace("-", "");
    }

    public void setPreviousAddresses(String previousAddresses) {
        this.previousAddresses = previousAddresses;
    }

    public String getEmployers() {
        return this.employers.replace("-", "");
    }

    public void setEmployers(String employers) {
        this.employers = employers;
    }

    public String toString() {
        return "PersonalInformation{creditReportDate=" + this.creditReportDate + ", name=" + this.name + ", alias=" + this.alias + ", former=" + this.former + ", dateOfBirth=" + this.dateOfBirth + ", currentAddresses=" + this.currentAddresses + ", previousAddresses=" + this.previousAddresses + ", employers=" + this.employers + '}';
    }
}
