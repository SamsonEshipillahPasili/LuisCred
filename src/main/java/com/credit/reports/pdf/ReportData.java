package com.credit.reports.pdf;

import com.credit.reports.parser.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReportData implements Serializable {
    private final HtmlCreditReport htmlCreditReport;
    private String phoneNumber;
    private String email;
    private String clientName;
    private String clientAddress;
    private final Constants constants;

    public ReportData(InputStream inputStream) throws Exception {
        Document document = Jsoup.parse(inputStream, "UTF-8", "");
        this.htmlCreditReport = new HtmlCreditReport(document);
        this.constants = new Constants();
    }

    public String getRefNumber() {
        return this.htmlCreditReport.metaData().getReferenceNumber();
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(new Date());
    }

    public String getName() {
        try {
            return this.htmlCreditReport.personalInformationFactory().transUnion().getName().replace("-", "");
        } catch (Exception e) {
            return "";
        }
    }

    public String getClientFirstName() {
        String fullName = this.getClientName().toLowerCase();
        if (fullName.isEmpty()) {
            return fullName.trim();
        } else {
            String firstName = fullName.split("\\s")[0].trim();
            firstName = firstName.replace("\\s", "");
            System.out.println(firstName);
            System.out.println(firstName.length());
            String firstLetter = String.valueOf(firstName.toCharArray()[0]).toUpperCase();
            firstName = firstLetter.trim() + firstName.substring(1).trim();
            return firstName.trim();
        }
    }

    public String getFirstName() {
        String fullName = this.getName().trim().toLowerCase();
        if (fullName.isEmpty()) {
            return fullName.trim();
        } else {
            String firstName = fullName.split("\\s")[0].trim();
            firstName = firstName.replace("\\s", "");
            System.out.println(firstName);
            System.out.println(firstName.length());
            String firstLetter = String.valueOf(firstName.toCharArray()[0]).toUpperCase();
            firstName = firstLetter.trim() + firstName.substring(1).trim();
            return firstName.trim();
        }
    }

    public String getCurrentAddress() {
        try {
            return this.htmlCreditReport.personalInformationFactory().transUnion().getCurrentAddresses().replace("-", "");
        } catch (Exception var2) {
            return "";
        }
    }

    public HtmlCreditReport getHtmlCreditReport() {
        return this.htmlCreditReport;
    }

    public List<TradeLine> getTradeLines() {
        return (new TradeLineFactory(this.htmlCreditReport.accountInformationFactory())).tradeLines();
    }

    public String getAverageAccountAges() {
        try {
            return (new TradeLineFactory(this.htmlCreditReport.accountInformationFactory())).averageAccountAge();
        } catch (ParseException e) {
            Logger.getLogger(ReportData.class.getName()).log(Level.SEVERE, null, e);
            return "Unavailable";
        }
    }

    public String getAverageAgeRating() {
        return (new TradeLineFactory(this.htmlCreditReport.accountInformationFactory())).averageAgeRating();
    }

    public String getHardInquiries() {
        AtomicInteger atomicInteger = new AtomicInteger();
        List<Inquiry> allInquiries = this.htmlCreditReport.inquiryFactory().getInquiries();
        allInquiries.forEach((e) -> {
            String dateOfInquiry = e.getDateOfInquiry().trim();
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            try {
                Date date = df.parse(dateOfInquiry);
                Date currentDate = new Date();
                long maximumDifference = df.parse("01/01/2018").getTime() - df.parse("01/01/2016").getTime();
                long difference = currentDate.getTime() - date.getTime();
                if (difference <= maximumDifference) {
                    atomicInteger.incrementAndGet();
                }
            } catch (ParseException pe) {
                Logger.getLogger(ReportData.class.getName()).log(Level.SEVERE, null, pe);
            }

        });
        return String.valueOf(atomicInteger.get());
    }

    public String getInquiryRating() {
        int hardInquiries = Integer.parseInt(this.getHardInquiries());
        if (hardInquiries >= 0 && hardInquiries <= 1) {
            return "Excellent";
        } else if (hardInquiries >= 2 && hardInquiries <= 3) {
            return "Good";
        } else if (hardInquiries >= 4 && hardInquiries <= 5) {
            return "Fair";
        } else {
            return hardInquiries >= 6 && hardInquiries <= 7 ? "Poor" : "Bad";
        }
    }

    public CreditProfile getCreditProfile() {
        return new CreditProfile(this);
    }

    public List<TradeLine> getNegativeTradeLinesImproved() {
        List<TradeLine> allTradeLines = this.getTradeLines();
        List<TradeLine> negativeTradeLines = new ArrayList<>();
        allTradeLines.forEach((tradeLine) -> {
            if (this.isNegativeAccount(tradeLine.getTransUnion())) {
                negativeTradeLines.add(tradeLine);
            } else if (this.isNegativeAccount(tradeLine.getExperian())) {
                negativeTradeLines.add(tradeLine);
            } else if (this.isNegativeAccount(tradeLine.getEquifax())) {
                negativeTradeLines.add(tradeLine);
            }

        });
        return negativeTradeLines;
    }

    public List<TradeLine> getPositiveTradeLines() {
        List<TradeLine> allTradeLines = this.getTradeLines();
        List<TradeLine> list = new ArrayList<>();

        for (TradeLine tradeLine : allTradeLines) {
            if (!this.isNegativeAccount(tradeLine.getTransUnion()) && !this.isNegativeAccount(tradeLine.getExperian()) && !this.isNegativeAccount(tradeLine.getEquifax())) {
                list.add(tradeLine);
            }
        }

        return list;
    }

    public String getPaymentHistoryQuality() {
        int tradeLines = this.getTradeLines().size();
        int positiveTradeLines = this.getPositiveTradeLines().size();
        double percentage = (double)(positiveTradeLines * 100 / tradeLines);
        if (percentage >= 90.0D && percentage <= 100.0D) {
            return "Excellent";
        } else if (percentage >= 80.0D && percentage <= 89.0D) {
            return "Good";
        } else if (percentage >= 70.0D && percentage <= 79.0D) {
            return "Fair";
        } else if (percentage >= 60.0D && percentage <= 69.0D) {
            return "Poor";
        } else {
            return percentage < 60.0D ? "Deficient" : "Out of Range";
        }
    }

    public String getPaymentHistoryPercentage() {
        int numberOfNegativeTradeLines = this.getNegativeTradeLinesImproved().size();
        int totalNumberOfTraderLines = this.getTradeLines().size();
        if (totalNumberOfTraderLines == 0) {
            return "0%";
        } else {
            int percentage = numberOfNegativeTradeLines * 100 / totalNumberOfTraderLines;
            return percentage + "%";
        }
    }

    public String getPaymentHistoryRating() {
        int paymentHistoryPercentage = Integer.parseInt(this.getPaymentHistoryPercentage().trim().replace("%", ""));
        if (paymentHistoryPercentage > 0 && paymentHistoryPercentage <= 30) {
            return "Bad";
        } else if (paymentHistoryPercentage > 30 && paymentHistoryPercentage <= 50) {
            return "Poor";
        } else if (paymentHistoryPercentage > 50 && paymentHistoryPercentage <= 70) {
            return "Fair";
        } else if (paymentHistoryPercentage > 70 && paymentHistoryPercentage <= 90) {
            return "Good";
        } else {
            return paymentHistoryPercentage > 90 && paymentHistoryPercentage <= 100 ? "Excellent" : "Value Out Of Range";
        }
    }

    public List<TradeLine> getRevolvingDebitCreditRatios() {
        List<TradeLine> tradeLines = this.getTradeLines();

        Predicate<TradeLine> p = tradeLine -> {
            boolean accType = Stream.of(tradeLine.getTransUnion().getAccountType(), tradeLine.getExperian().getAccountType(),
                    tradeLine.getEquifax().getAccountType())
                    .anyMatch(it -> it.toLowerCase().contains("revolving"));

            boolean paymentStatus = Stream.of(tradeLine.getTransUnion().getPaymentStatus(), tradeLine.getExperian().getPaymentStatus(),
                    tradeLine.getEquifax().getPaymentStatus())
                    .anyMatch(it -> it.toLowerCase().contains("current"));

            return accType && paymentStatus;
        };

        return tradeLines.stream()
                .filter(p)
                .collect(Collectors.toList());

    }

    public String getRevolvingDCradtiosRating() {
        double avgDcRatio = Double.parseDouble(this.getAverageRevolvingDCratio().replace("%", ""));
        if (avgDcRatio >= 0.0D && avgDcRatio <= 6.0D) {
            return "Excellent";
        } else if (avgDcRatio > 6.0D && avgDcRatio <= 10.0D) {
            return "Good";
        } else if (avgDcRatio > 10.0D && avgDcRatio <= 30.0D) {
            return "Fair";
        } else if (avgDcRatio > 30.0D && avgDcRatio <= 60.0D) {
            return "Poor";
        } else {
            return avgDcRatio > 60.0D ? "Bad" : "";
        }
    }

    public String getAverageRevolvingDCratio() {
        List<TradeLine> tradeLines = this.getRevolvingDebitCreditRatios();
        double sum = 0.0D;

        TradeLine tradeLine;
        for(Iterator var4 = tradeLines.iterator(); var4.hasNext(); sum += Double.parseDouble(tradeLine.getDebtCreditRatio().replace("%", ""))) {
            tradeLine = (TradeLine)var4.next();
        }

        return Math.round(sum / (double)tradeLines.size()) + "%";
    }

    private boolean isNegativeAccount(AccountInformation accountInformation) {
        if (accountInformation.getAccountStatus().toLowerCase().contains("derogatory")) {
            return true;
        } else if (!accountInformation.getPaymentStatus().toLowerCase().contains("late") && !accountInformation.getPaymentStatus().toLowerCase().contains("chargeoff") && !accountInformation.getPaymentStatus().toLowerCase().contains("collection") && !accountInformation.getPaymentStatus().toLowerCase().contains("charge-off")) {
            if (!accountInformation.getAccountType().toLowerCase().contains("repossession") && !accountInformation.getAccountType().toLowerCase().contains("tax") && !accountInformation.getAccountType().toLowerCase().contains("lien") && !accountInformation.getAccountType().toLowerCase().contains("bankrupt") && !accountInformation.getAccountType().toLowerCase().contains("judgement") && !accountInformation.getAccountType().toLowerCase().contains("foreclosure") && !accountInformation.getAccountType().toLowerCase().contains("short") && !accountInformation.getAccountType().toLowerCase().contains("chargeoff") && !accountInformation.getAccountType().toLowerCase().contains("collection") && !accountInformation.getAccountType().toLowerCase().contains("charge-off")) {
                if (!accountInformation.getDetailedAccountType().toLowerCase().contains("repossession") && !accountInformation.getDetailedAccountType().toLowerCase().contains("tax") && !accountInformation.getDetailedAccountType().toLowerCase().contains("lien") && !accountInformation.getDetailedAccountType().toLowerCase().contains("bankrupt") && !accountInformation.getDetailedAccountType().toLowerCase().contains("judgement") && !accountInformation.getDetailedAccountType().toLowerCase().contains("foreclosure") && !accountInformation.getDetailedAccountType().toLowerCase().contains("short") && !accountInformation.getDetailedAccountType().contains("chargeoff") && !accountInformation.getDetailedAccountType().contains("charge-off") && !accountInformation.getDetailedAccountType().contains("collection")) {
                    if (accountInformation.getPaymentHistory().getLatePayments() > 0) {
                        return true;
                    } else {
                        return accountInformation.getComments().toLowerCase().contains("bad debt") || accountInformation.getComments().toLowerCase().contains("profit and loss write-off") || accountInformation.getComments().toLowerCase().contains("unpaid balance reported as a loss") || accountInformation.getComments().toLowerCase().contains("loss");
                    }
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public AccountDetailedTypes getAccountDetailedTypes() {
        return new AccountDetailedTypes(this);
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return this.clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Constants getConstants() {
        return this.constants;
    }
}
