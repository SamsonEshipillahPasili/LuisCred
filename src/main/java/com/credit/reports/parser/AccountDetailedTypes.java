package com.credit.reports.parser;

import com.credit.reports.pdf.ReportData;

import java.util.List;
import java.util.stream.Stream;

public class AccountDetailedTypes {
    private final List<TradeLine> tradeLines;

    public AccountDetailedTypes(ReportData reportData) {
        this.tradeLines = reportData.getTradeLines();
    }

    public long getRealEstate() {
        return this.tradeLines.stream().filter((tradeLine) -> !Stream.of(tradeLine.getEquifax().getPaymentStatus(), tradeLine.getTransUnion().getPaymentStatus(), tradeLine.getExperian().getPaymentStatus()).map(String::toLowerCase).anyMatch((it) -> {
            return it.contains("collection") || it.contains("charge");
        })).filter((tradeLine) -> {
            return Stream.of(tradeLine.getTransUnion().getDetailedAccountType(), tradeLine.getEquifax().getDetailedAccountType(), tradeLine.getExperian().getDetailedAccountType()).map(String::toLowerCase).map(String::trim).anyMatch((it) -> {
                return it.contains("real estate") || it.contains("mortgage");
            });
        }).count();
    }

    public long getCreditCardAndOthers() {
        return this.tradeLines.stream().filter((tradeLine) -> {
            return !Stream.of(tradeLine.getEquifax().getPaymentStatus(), tradeLine.getTransUnion().getPaymentStatus(), tradeLine.getExperian().getPaymentStatus()).map(String::toLowerCase).anyMatch((it) -> {
                return it.contains("collection") || it.contains("charge");
            });
        }).filter((tradeLine) -> {
            return Stream.of(tradeLine.getTransUnion().getDetailedAccountType(), tradeLine.getEquifax().getDetailedAccountType(), tradeLine.getExperian().getDetailedAccountType()).map(String::toLowerCase).map(String::trim).anyMatch((it) -> {
                return it.contains("credit") || it.contains("charge") || it.contains("home") || it.contains("hiloc") || it.contains("flexible spending");
            });
        }).count();
    }

    public long getAutoLoans() {
        return this.tradeLines.stream().filter((tradeLine) -> {
            return !Stream.of(tradeLine.getEquifax().getPaymentStatus(), tradeLine.getTransUnion().getPaymentStatus(), tradeLine.getExperian().getPaymentStatus()).map(String::toLowerCase).anyMatch((it) -> {
                return it.contains("collection") || it.contains("charge");
            });
        }).filter((tradeLine) -> {
            return tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("auto") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("auto") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("auto");
        }).count();
    }

    public long getEducational() {
        return this.tradeLines.stream().filter((tradeLine) -> {
            return !Stream.of(tradeLine.getEquifax().getPaymentStatus(), tradeLine.getTransUnion().getPaymentStatus(), tradeLine.getExperian().getPaymentStatus()).map(String::toLowerCase).anyMatch((it) -> {
                return it.contains("collection") || it.contains("charge");
            });
        }).filter((tradeLine) -> {
            return tradeLine.getTransUnion().getDetailedAccountType().toLowerCase().contains("educational") || tradeLine.getExperian().getDetailedAccountType().toLowerCase().contains("educational") || tradeLine.getEquifax().getDetailedAccountType().toLowerCase().contains("educational");
        }).count();
    }

    public long getOtherInstallmentLoans() {
        return this.tradeLines.stream().filter((tradeLine) -> {
            return !Stream.of(tradeLine.getEquifax().getPaymentStatus(), tradeLine.getTransUnion().getPaymentStatus(), tradeLine.getExperian().getPaymentStatus()).map(String::toLowerCase).anyMatch((it) -> {
                return it.contains("collection") || it.contains("charge");
            });
        }).filter((tradeLine) -> {
            return Stream.of(tradeLine.getTransUnion().getDetailedAccountType(), tradeLine.getExperian().getDetailedAccountType(), tradeLine.getEquifax().getDetailedAccountType()).map(String::trim).map(String::toLowerCase).anyMatch((it) -> {
                return it.contains("other") || it.contains("other installment loans") || it.contains("timeshare") || it.contains("term loans") || it.contains("family") || it.contains("finance companies");
            });
        }).count();
    }

    public String getAccountDetailedTypesRating() {
        long total = Stream.of(this.getAutoLoans(), this.getCreditCardAndOthers(), this.getEducational(), this.getOtherInstallmentLoans(), this.getRealEstate()).filter((value) -> {
            return value > 0L;
        }).count();
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
