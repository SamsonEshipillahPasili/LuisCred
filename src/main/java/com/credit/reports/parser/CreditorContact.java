package com.credit.reports.parser;

public class CreditorContact {
    private String creditorName;
    private String address;
    private String phoneNumber;

    public String getCreditorName() {
        return this.creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String toString() {
        return "CreditorContact{creditorName=" + this.creditorName + ", address=" + this.address + ", phoneNumber=" + this.phoneNumber + '}';
    }
}
