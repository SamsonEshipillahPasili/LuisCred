package com.credit.reports.parser;

import org.jsoup.nodes.Document;

public class MetaDataImpl implements MetaData {
    private final Document document;

    public MetaDataImpl(Document document) {
        this.document = document;
    }

    public String getReferenceNumber() {
        return this.document.select("#reportTop tr td.ng-binding").text();
    }

    public String getReportDate() {
        return this.document.select("#reportTop tr ng").text();
    }
}
