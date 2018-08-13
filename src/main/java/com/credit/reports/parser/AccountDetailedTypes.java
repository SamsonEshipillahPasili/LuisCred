package com.credit.reports.parser;

import com.credit.reports.pdf.ReportData;
import java.util.List;
import java.util.function.Predicate;

public class AccountDetailedTypes {
    private final List<TradeLine> tradeLines;

    public AccountDetailedTypes(ReportData reportData) {
        this.tradeLines = reportData.getTradeLines();
    }

    public long getRealEstate() {
        return this.tradeLines.stream().filter((tradeLine) -> {
            return tradeLine.getNearestPaymentStatus().toLowerCase().contains("current");
        }).filter((tradeLine) -> {
            return tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("real estate") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("real estate") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("real estate") || tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("mortgage") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("mortgage") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("mortgage");
        }).count();
    }

    public long getCreditCardAndOthers() {
        return this.tradeLines.stream().filter((tradeLine) -> {
            return tradeLine.getNearestPaymentStatus().toLowerCase().contains("current");
        }).filter((tradeLine) -> {
            return tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("credit") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("credit") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("credit") || tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("charge") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("charge") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("charge") || tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("home") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("home") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("home") || tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("home improvement") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("home improvement") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("home improvement") || tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("hiloc") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("hiloc") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("hiloc") || tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("flexible spending") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("flexible spending") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("flexible spending");
        }).count();
    }

    public long getAutoLoans() {
        return this.tradeLines.stream().filter((tradeLine) -> {
            return tradeLine.getNearestPaymentStatus().toLowerCase().contains("current");
        }).filter((tradeLine) -> {
            return tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("auto") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("auto") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("auto");
        }).count();
    }

    public long getEducational() {
        return this.tradeLines.stream().filter((tradeLine) -> {
            return tradeLine.getNearestPaymentStatus().toLowerCase().contains("current");
        }).filter((tradeLine) -> {
            return tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("educational") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("educational") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("educational");
        }).count();
    }

    public long getOtherInstallmentLoans() {
        return this.tradeLines.stream().filter((tradeLine) -> {
            return tradeLine.getNearestPaymentStatus().toLowerCase().contains("current");
        }).filter((tradeLine) -> {
            return tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("other") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("other") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("other") || tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("other installment loans") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("other installment loans") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("other installment loans") || tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("timeshare") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("timeshare") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("timeshare") || tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("term loans") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("term loans") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("term loans") || tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("family") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("family") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("family") || tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("finance companies") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("finance companies") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("finance companies");
        }).count();
    }

    public String getAccountDetailedTypesRating() {
        long total = this.getAutoLoans() + this.getCreditCardAndOthers() + this.getEducational() + this.getOtherInstallmentLoans() + this.getRealEstate();
        if (total >= 0L && total <= 1L) {
            return "Bad";
        } else if (total == 2L) {
            return "Poor";
        } else if (total == 3L) {
            return "Fair";
        } else if (total == 4L) {
            return "Good";
        } else {
            return total > 4L ? "Excellent" : "Out of Range";
        }
    }
}
