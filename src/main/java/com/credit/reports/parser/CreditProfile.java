package com.credit.reports.parser;

import com.credit.reports.pdf.ReportData;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CreditProfile {
    private final ReportData reportData;
    private final List<TradeLine> tradeLines;
    Predicate<TradeLine> latePaymentPredicate = (tradeLine) -> {
        return tradeLine.getTransUnion().getPaymentHistory().getLatePayments() > 0 || tradeLine.getExperian().getPaymentHistory().getLatePayments() > 0 || tradeLine.getEquifax().getPaymentHistory().getLatePayments() > 0 || tradeLine.getTransUnion().getPaymentStatus().toLowerCase().contains("late") || tradeLine.getExperian().getPaymentStatus().toLowerCase().contains("late") || tradeLine.getEquifax().getPaymentStatus().toLowerCase().contains("late");
    };
    Predicate<TradeLine> negativePublicInformationPrediate = (tradeLine) -> {
        return tradeLine.getNearestPaymentStatus().toLowerCase().contains("repossession") || tradeLine.getNearestPaymentStatus().toLowerCase().contains("judgement") || tradeLine.getNearestPaymentStatus().toLowerCase().contains("fore") || tradeLine.getNearestPaymentStatus().toLowerCase().contains("short") || tradeLine.getNearestPaymentStatus().toLowerCase().contains("tax") || tradeLine.getNearestPaymentStatus().toLowerCase().contains("lien") || tradeLine.getNearestPaymentStatus().toLowerCase().contains("bankrupt") || tradeLine.getNearestPaymentStatus().toLowerCase().contains("court") || tradeLine.getNearestPaymentStatus().toLowerCase().contains("record");
    };

    public CreditProfile(ReportData reportData) {
        this.reportData = reportData;
        this.tradeLines = reportData.getTradeLines();
    }

    public long getCurrentPositivePayments() {
        List<TradeLine> positiveTradeLines = this.reportData.getPositiveTradeLines();
        return positiveTradeLines.stream().filter((tradeLine) -> {
            return tradeLine.getNearestPaymentStatus().toLowerCase().contains("current");
        }).count();
    }

    public long getLateCurrentPayments() {
        List<TradeLine> allTradeLines = this.reportData.getTradeLines();
        List<TradeLine> currentTradeLines = (List)allTradeLines.stream().filter((tradeLine) -> {
            return tradeLine.getNearestPaymentStatus().toLowerCase().contains("current");
        }).collect(Collectors.toList());
        return currentTradeLines.stream().filter(this.latePaymentPredicate).count();
    }

    public long getNegativePublicInformationPayments() {
        List<TradeLine> allTradeLines = this.reportData.getTradeLines();
        return allTradeLines.stream().filter(this.negativePublicInformationPrediate).count();
    }

    public long getChargeOffsOrCollections() {
        return this.tradeLines.stream().filter((tradeLine) -> {
            return tradeLine.getNearestPaymentStatus().toLowerCase().contains("collection") || tradeLine.getNearestPaymentStatus().toLowerCase().contains("chargeoff") || tradeLine.getNearestPaymentStatus().toLowerCase().contains("charge-off");
        }).count();
    }

    public String getPaymentHistoryQuality() {
        long total = this.getChargeOffsOrCollections() + this.getCurrentPositivePayments() + this.getLateCurrentPayments() + this.getNegativePublicInformationPayments();
        double percentage = (double)(this.getCurrentPositivePayments() * 100L / total);
        if (percentage >= 90.0D && percentage <= 100.0D) {
            return "Excellent";
        } else if (percentage >= 80.0D && percentage <= 89.0D) {
            return "Good";
        } else if (percentage >= 70.0D && percentage <= 79.0D) {
            return "Fair";
        } else {
            return percentage <= 69.0D ? "Poor" : "Out of Range";
        }
    }

    public String getCurrentPositiveTradeLinesPercentage() {
        long total = this.getChargeOffsOrCollections() + this.getCurrentPositivePayments() + this.getLateCurrentPayments() + this.getNegativePublicInformationPayments();
        DecimalFormat df = new DecimalFormat("##.##");
        double percentage = (double)(this.getCurrentPositivePayments() * 100L / total);
        return df.format(percentage);
    }

    public String getLateCurrentPaymentsPercentage() {
        long total = this.getChargeOffsOrCollections() + this.getCurrentPositivePayments() + this.getLateCurrentPayments() + this.getNegativePublicInformationPayments();
        DecimalFormat df = new DecimalFormat("##.##");
        double percentage = (double)(this.getLateCurrentPayments() * 100L / total);
        return df.format(percentage);
    }

    public String getChargeOffsOrCollectionsPercentage() {
        long total = this.getChargeOffsOrCollections() + this.getCurrentPositivePayments() + this.getLateCurrentPayments() + this.getNegativePublicInformationPayments();
        DecimalFormat df = new DecimalFormat("##.##");
        double percentage = (double)(this.getChargeOffsOrCollections() * 100L / total);
        return df.format(percentage);
    }

    public String getNegativePublicInfoPercentage() {
        long total = this.getChargeOffsOrCollections() + this.getCurrentPositivePayments() + this.getLateCurrentPayments() + this.getNegativePublicInformationPayments();
        DecimalFormat df = new DecimalFormat("##.##");
        double percentage = (double)(this.getNegativePublicInformationPayments() * 100L / total);
        return df.format(percentage);
    }
}
