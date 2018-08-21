package com.credit.reports.entities;

import com.credit.reports.dto.ReportDataProxy;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table
public class Report implements Serializable {
    private String dateGenerated;
    @Id
    @Column
    private String id;
    @Column
    @Lob
    private ReportDataProxy reportDataProxy;
    @Column
    @Lob
    private byte[] documentBytes;

    public Report(String id, ReportDataProxy reportDataProxy) {
        this.id = id;
        this.reportDataProxy = reportDataProxy;
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        this.dateGenerated = dateFormat.format(new Date());
    }

    public Report(){} // needed for JPA to work.

    public String getClientName() {
        return this.reportDataProxy.getClientName();
    }

    public String documentPath() {
        return "/report/pdf/" + this.id;

    }

    public String getDateGenerated() {
        return this.dateGenerated;
    }

    public String getId() {
        return this.id;
    }

    public ReportDataProxy getReportDataProxy() {
        return this.reportDataProxy;
    }

    public byte[] getDocumentBytes() {
        return this.documentBytes;
    }

    public void setDateGenerated(String dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReportDataProxy(ReportDataProxy reportDataProxy) {
        this.reportDataProxy = reportDataProxy;
    }

    public void setDocumentBytes(byte[] documentBytes) {
        this.documentBytes = documentBytes;
    }


    public String toString() {
        return "Report(dateGenerated=" + this.getDateGenerated() + ", id=" + this.getId() + ", reportDataProxy=" + this.getReportDataProxy() + ", documentBytes=" + Arrays.toString(this.getDocumentBytes()) + ")";
    }
}