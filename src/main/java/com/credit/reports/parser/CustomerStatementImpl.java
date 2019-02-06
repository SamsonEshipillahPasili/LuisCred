package com.credit.reports.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CustomerStatementImpl implements CustomerStatement {
    private final Document document;

    public CustomerStatementImpl(Document document) {
        this.document = document;
    }

    public String transUnion() {
        Element target = this.document.select("[ng-show='crdStmt.length > 0']").get(0);
        target = target.child(0).child(1).child(0).child(0).child(1).child(0);
        return target.text();
    }

    public String experian() {
        Element target = this.document.select("[ng-show='crdStmt.length > 0']").get(0);
        target = target.child(0).child(1).child(0).child(1).child(1).child(0);
        return target.text();
    }

    public String equifax() {
        Element target = this.document.select("[ng-show='crdStmt.length > 0']").get(0);
        target = target.child(0).child(1).child(0).child(2).child(1).child(0);
        return target.text();
    }
}
