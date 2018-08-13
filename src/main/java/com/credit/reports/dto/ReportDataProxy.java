package com.credit.reports.dto;

import com.credit.reports.parser.Constants;
import com.credit.reports.pdf.ReportData;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Arrays;

public class ReportDataProxy implements Serializable {
    private String phoneNumber;
    private String email;
    private String clientName;
    private String clientAddress;
    private final Constants constants = new Constants();
    private byte[] inputHtmlFileBytes;

    public ReportData reportData() throws Exception {
        ReportData reportData = new ReportData(new ByteArrayInputStream(this.inputHtmlFileBytes));
        reportData.setPhoneNumber(this.phoneNumber);
        reportData.setEmail(this.email);
        reportData.setClientName(this.clientName);
        reportData.setClientAddress(this.clientAddress);
        return reportData;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public String getClientName() {
        return this.clientName;
    }

    public String getClientAddress() {
        return this.clientAddress;
    }

    public Constants getConstants() {
        return this.constants;
    }

    public byte[] getInputHtmlFileBytes() {
        return this.inputHtmlFileBytes;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public void setInputHtmlFileBytes(byte[] inputHtmlFileBytes) {
        this.inputHtmlFileBytes = inputHtmlFileBytes;
    }


    public String toString() {
        return "ReportDataProxy(phoneNumber=" + this.getPhoneNumber() + ", email=" + this.getEmail() + ", clientName=" + this.getClientName() + ", clientAddress=" + this.getClientAddress() + ", constants=" + this.getConstants() + ", inputHtmlFileBytes=" + Arrays.toString(this.getInputHtmlFileBytes()) + ")";
    }
}