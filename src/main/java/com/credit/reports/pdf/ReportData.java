package com.credit.reports.pdf;

import com.credit.reports.parser.AccountDetailedTypes;
import com.credit.reports.parser.AccountInformation;
import com.credit.reports.parser.Constants;
import com.credit.reports.parser.CreditProfile;
import com.credit.reports.parser.HtmlCreditReport;
import com.credit.reports.parser.Inquiry;
import com.credit.reports.parser.TradeLine;
import com.credit.reports.parser.TradeLineFactory;
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
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
        } catch (Exception var2) {
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
        } catch (ParseException var2) {
            Logger.getLogger(ReportData.class.getName()).log(Level.SEVERE, (String)null, var2);
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
                long maximumDifference = df.parse("01/01/2018").getTime() - df.parse("01/01/2017").getTime();
                long difference = currentDate.getTime() - date.getTime();
                if (difference <= maximumDifference) {
                    atomicInteger.incrementAndGet();
                }
            } catch (ParseException var10) {
                Logger.getLogger(ReportData.class.getName()).log(Level.SEVERE, (String)null, var10);
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
        } else if (hardInquiries >= 6 && hardInquiries <= 7) {
            return "Poor";
        } else {
            return hardInquiries >= 8 ? "Bad" : "Unavailable";
        }
    }

    public CreditProfile getCreditProfile() {
        return new CreditProfile(this);
    }

    public List<TradeLine> getNegativeTradeLinesImproved() {
        List<TradeLine> allTradeLines = this.getTradeLines();
        List<TradeLine> negativeTradeLines = new ArrayList();
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
        return (List)allTradeLines.stream().filter((tradeLine) ->
                !this.isNegativeAccount(tradeLine.getTransUnion()) && !this.isNegativeAccount(tradeLine.getExperian()) && !this.isNegativeAccount(tradeLine.getEquifax())).collect(Collectors.toList());
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
        List<TradeLine> revolvingTradeLines = new ArrayList();
        tradeLines.forEach((tradeLine) -> {
            if (tradeLine.getTransUnion().getAccountType().contains("Revolving") || tradeLine.getExperian().getAccountType().contains("Revolving") || tradeLine.getEquifax().getAccountType().contains("Revolving")) {
                revolvingTradeLines.add(tradeLine);
            }

        });
        return revolvingTradeLines;
    }

    public String getRevolvingDCradtiosRating() {
        double avgDcRatio = Double.parseDouble(this.getAverageRevolvingDCratio().replace("%", ""));
        if (avgDcRatio > 0.0D && avgDcRatio <= 6.0D) {
            return "Excellent";
        } else if (avgDcRatio > 6.0D && avgDcRatio <= 10.0D) {
            return "Good";
        } else if (avgDcRatio > 10.0D && avgDcRatio <= 30.0D) {
            return "Fair";
        } else if (avgDcRatio > 30.0D && avgDcRatio <= 60.0D) {
            return "Poor";
        } else {
            return avgDcRatio > 60.0D ? "Bad" : "Value Out of Range";
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
