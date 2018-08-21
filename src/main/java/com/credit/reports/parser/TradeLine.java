package com.credit.reports.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TradeLine {
    private AccountInformation transUnion;
    private AccountInformation experian;
    private AccountInformation equifax;

    public TradeLine() {
    }

    public AccountInformation getTransUnion() {
        return this.transUnion;
    }

    public void setTransUnion(AccountInformation transUnion) {
        this.transUnion = transUnion;
    }

    public AccountInformation getExperian() {
        return this.experian;
    }

    public void setExperian(AccountInformation experian) {
        this.experian = experian;
    }

    public AccountInformation getEquifax() {
        return this.equifax;
    }

    public void setEquifax(AccountInformation equifax) {
        this.equifax = equifax;
    }

    public String getDebtCreditRatio() {
        double averageBalance = Double.parseDouble(this.getAverageBalance().replace("$", ""));
        double averageCreditLimit = Double.parseDouble(this.getAverageCreditLimit().replace("$", ""));
        double debitCreditRatio = averageBalance * 100.0D / averageCreditLimit;
        return Math.round(debitCreditRatio) + "%";
    }

    public String getAverageBalance() {
        List<String> rawBalances = Arrays.asList(this.transUnion.getBalance(), this.experian.getBalance(), this.equifax.getBalance());
        List<String> processedBalances = (List<String>)rawBalances.stream().map((rawBalance) -> {
            return rawBalance.replace("$", "").replace("-", "").replace(",", "").trim();
        }).collect(Collectors.toList());
        Double[] average = new Double[]{0.0D};
        processedBalances.stream().mapToDouble(Double::parseDouble).filter((b) -> {
            return b != 0.0D;
        }).average().ifPresent((averageBalance) -> {
            average[0] = averageBalance;
        });
        return String.valueOf(Math.round(average[0].doubleValue()));
    }

    public String getAverageCreditLimit() {
        List<String> creditLimits = Arrays.asList(this.transUnion.getCreditLimit().replace("$", "").replace("-", "").replace(",", "").trim(), this.equifax.getCreditLimit().replace("$", "").replace("-", "").replace(",", "").trim(), this.experian.getCreditLimit().replace("$", "").replace("-", "").replace(",", "").trim());
        int totalCount = 0;
        double totalCreditLimits = 0.0D;

        for(int count = 0; count < creditLimits.size(); ++count) {
            if (!((String)creditLimits.get(count)).isEmpty()) {
                totalCreditLimits += Double.parseDouble((String)creditLimits.get(count));
                ++totalCount;
            }
        }

        double averageCreditLimit = totalCreditLimits / (double)totalCount;
        return "$" + String.valueOf(Math.round(averageCreditLimit));
    }

    public String getOldestDateOpened() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        Date tuDate;
        try {
            tuDate = df.parse(this.transUnion.getDateOpened());
        } catch (ParseException var8) {
            tuDate = null;
        }

        Date exDate;
        try {
            exDate = df.parse(this.experian.getDateOpened());
        } catch (ParseException var7) {
            exDate = null;
        }

        Date eqDate;
        try {
            eqDate = df.parse(this.equifax.getDateOpened());
        } catch (ParseException var6) {
            eqDate = null;
        }

        List<Date> dateOpenedList = new ArrayList<>();
        if (null != tuDate) {
            dateOpenedList.add(tuDate);
        }

        if (null != exDate) {
            dateOpenedList.add(exDate);
        }

        if (null != eqDate) {
            dateOpenedList.add(eqDate);
        }

        dateOpenedList.sort((a, b) -> {
            return a.before(b) ? 1 : -1;
        });
        return dateOpenedList.isEmpty() ? "" : df.format((Date)dateOpenedList.get(dateOpenedList.size() - 1));
    }

    public String getOldestDateLastActive() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        Date tuDate;
        try {
            tuDate = df.parse(this.transUnion.getDateLastActive());
        } catch (ParseException var8) {
            tuDate = null;
        }

        Date exDate;
        try {
            exDate = df.parse(this.experian.getDateLastActive());
        } catch (ParseException var7) {
            exDate = null;
        }

        Date eqDate;
        try {
            eqDate = df.parse(this.equifax.getDateLastActive());
        } catch (ParseException var6) {
            eqDate = null;
        }

        List<Date> dateLastActiveList = new ArrayList<>();
        if (null != tuDate) {
            dateLastActiveList.add(tuDate);
        }

        if (null != exDate) {
            dateLastActiveList.add(exDate);
        }

        if (null != eqDate) {
            dateLastActiveList.add(eqDate);
        }

        dateLastActiveList.sort((a, b) -> {
            return a.before(b) ? 1 : -1;
        });
        return dateLastActiveList.isEmpty() ? "" : df.format((Date)dateLastActiveList.get(dateLastActiveList.size() - 1));
    }

    public String getNearestPaymentStatus() {
        String transUnionDetailedAccountType = this.transUnion.getDetailedAccountType().toLowerCase();
        String experianDetailedAccountType = this.experian.getDetailedAccountType().toLowerCase();
        String equifaxDetailedAccountType = this.equifax.getDetailedAccountType().toLowerCase();
        if (!transUnionDetailedAccountType.contains("collection") && !experianDetailedAccountType.contains("collection") && !equifaxDetailedAccountType.contains("collection")) {
            if (!this.transUnion.getPaymentStatus().replace("-", "").isEmpty()) {
                return this.transUnion.getPaymentStatus();
            } else if (!this.equifax.getPaymentStatus().replace("-", "").isEmpty()) {
                return this.equifax.getPaymentStatus();
            } else {
                return !this.experian.getPaymentStatus().replace("-", "").isEmpty() ? this.experian.getPaymentStatus() : "";
            }
        } else {
            return "Collection/Charge-off";
        }
    }

    public String getNearestDetailedAccountType() {
        if (!this.transUnion.getDetailedAccountType().replace("-", "").isEmpty()) {
            return this.transUnion.getDetailedAccountType();
        } else if (!this.equifax.getDetailedAccountType().replace("-", "").isEmpty()) {
            return this.equifax.getDetailedAccountType();
        } else {
            return !this.experian.getDetailedAccountType().replace("-", "").isEmpty() ? this.experian.getDetailedAccountType() : "";
        }
    }

    public String getNearestAccountName() {
        if (!this.transUnion.getAccountName().replace("-", "").isEmpty()) {
            return this.transUnion.getAccountName();
        } else if (!this.equifax.getAccountName().replace("-", "").isEmpty()) {
            return this.equifax.getAccountName();
        } else {
            return !this.experian.getAccountName().replace("-", "").isEmpty() ? this.experian.getAccountName() : "";
        }
    }


    public boolean isConsistent(int constant){
        Constants constants = new Constants();
        if(constant == constants.ACCOUNT_NUMBER){
            return this.checkEquality(this.transUnion.getAccountNumer(), this.experian.getAccountNumer(), this.equifax.getAccountNumer());
        }else if(constant == constants.ACCOUNT_DETAILED_TYPE){
            return this.checkEquality(this.transUnion.getDetailedAccountType(), this.experian.getDetailedAccountType(), this.equifax.getDetailedAccountType());
        }else if(constant == constants.ACCOUNT_STATUS){
            return this.checkEquality(this.transUnion.getAccountStatus(), this.experian.getAccountStatus(), this.equifax.getAccountStatus());
        }else if(constant == constants.ACCOUNT_TYPE){
            return this.checkEquality(this.transUnion.getAccountType(), this.experian.getAccountType(), this.equifax.getAccountType());
        }else if(constant == constants.BALANCE){
            return this.checkMonetaryConsistency(this.transUnion.getRawBalance(), this.experian.getRawBalance(), this.equifax.getRawBalance());
        }else if(constant == constants.BUREAU_CODE){
            return this.checkEquality(this.transUnion.getBeaureuCode(), this.experian.getBeaureuCode(), this.equifax.getBeaureuCode());
        }else if(constant == constants.CREDIT_LIMIT){
            return this.checkMonetaryConsistency(this.transUnion.getRawCreditLimit(), this.experian.getRawCreditLimit(), this.equifax.getRawCreditLimit());
        }else if(constant == constants.DATE_LAST_ACTIVE){
            return this.checkDateConsistency(this.transUnion.getDateLastActive(), this.experian.getDateLastActive(), this.equifax.getDateLastActive());
        }else if(constant == constants.DATE_OF_LAST_PAYMENT) {
            return this.checkDateConsistency(this.transUnion.getDateOfLastPayment(), this.experian.getDateOfLastPayment(), this.equifax.getDateOfLastPayment());
        }else if(constant == constants.DATE_OPENED){
            return this.checkDateConsistency(this.transUnion.getDateOpened(), this.experian.getDateOpened(), this.equifax.getDateOpened());
        }else if(constant == constants.HIGH_CREDIT){
            return this.checkMonetaryConsistency(this.transUnion.getHighCredit(), this.experian.getHighCredit(), this.equifax.getHighCredit());
        }else if(constant == constants.LAST_REPORTED){
            return this.checkDateConsistency(this.transUnion.getLastReported(), this.experian.getLastReported(), this.equifax.getLastReported());
        }else if(constant == constants.MONTHLY_PAYMENT){
            return this.checkMonetaryConsistency(this.transUnion.getMonthlyPayment(), this.experian.getMonthlyPayment(), this.equifax.getMonthlyPayment());
        }else if(constant == constants.PAST_DUE){
            return this.checkMonetaryConsistency(this.transUnion.getPastDue(), this.experian.getPastDue(), this.equifax.getPastDue());
        }else if(constant == constants.PAYMENT_STATUS){
            return this.checkEquality(this.transUnion.getPaymentStatus(), this.experian.getPaymentStatus(), this.equifax.getPaymentStatus());
        }else if(constant == constants.ACCOUNT_NAME){
            return this.checkEquality(this.transUnion.getAccountName(), this.experian.getAccountName(), this.equifax.getAccountName());
        }else if(constant == constants.NUMBER_OF_MONTHS){
            return this.checkEquality(this.transUnion.getNoOfMonths(), this.experian.getNoOfMonths(), this.equifax.getNoOfMonths());
        }else if(constant == constants.COMMENTS){
            return this.checkEquality(this.transUnion.getComments(), this.experian.getComments(), this.equifax.getComments());
        }
        else{
            return false;
        }
    }


    private boolean checkEquality(String transUnion, String experian, String equifax) {
        // if the account number is empty, then do not check for the corresponding equality consistency.
        if(this.transUnion.getAccountNumer().isEmpty()) transUnion = null;
        if(this.experian.getAccountNumer().isEmpty()) experian = null;
        if(this.equifax.getAccountNumer().isEmpty()) equifax = null;

        List<String> rawValues = Arrays.asList(transUnion, experian, equifax);
        List<String> processedValues = rawValues
                .stream()
                .filter(Objects::nonNull)
                .map(String::trim).collect(Collectors.toList());
        if (!processedValues.isEmpty()) {
            String firstVal = processedValues.get(0);
            return processedValues.stream().allMatch((val) -> val.equalsIgnoreCase(firstVal));
        } else {
            return true;
        }
    }

    private boolean checkMonetaryConsistency(String transUnion, String experian, String equifax) {
        // if the account number is empty, then do not check for the corresponding monetary consistency.
        if(this.transUnion.getAccountNumer().isEmpty()) transUnion = null;
        if(this.experian.getAccountNumer().isEmpty()) experian = null;
        if(this.equifax.getAccountNumer().isEmpty()) equifax = null;

        List<String> monies = Arrays.asList(transUnion, experian, equifax);
        List<String> processedMonies = monies.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .collect(Collectors.toList());
        if (!processedMonies.isEmpty()) {
            String firstMoney = processedMonies.get(0);
            return processedMonies.stream().allMatch((p) -> p.equals(firstMoney));
        } else {
            return true;
        }
    }

    private boolean checkDateConsistency(String transUnion, String experian, String equifax) {
        // if the account number is empty, then do not check for the corresponding monetary consistency.
        if(this.transUnion.getAccountNumer().isEmpty()) transUnion = null;
        if(this.experian.getAccountNumer().isEmpty()) experian = null;
        if(this.equifax.getAccountNumer().isEmpty()) equifax = null;

        List<String> dates = Arrays.asList(transUnion, experian, equifax);
        List<String> processedDates = dates
                .stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .map((date) -> {
                    if(date.isEmpty()){
                        return date;
                    }
                    return date.substring(0, 3) + date.substring(5);
        }).collect(Collectors.toList());
        if (!processedDates.isEmpty()) {
            String firstValue = (String)processedDates.get(0);
            return processedDates.stream().allMatch((date) -> date.equals(firstValue));
        } else {
            return true;
        }
    }

    public String getRowStyle(boolean isConsistent) {
        return isConsistent ? "background-color: white;" : "background-color: #ffff99";
    }

    public String getLateStyle() {
        if (this.transUnion.getPaymentHistory().getLatePayments() > 0) {
            return "background-color: #ffb7b3";
        } else if (this.experian.getPaymentHistory().getLatePayments() > 0) {
            return "background-color: #ffb7b3";
        } else {
            return this.equifax.getPaymentHistory().getLatePayments() > 0 ? "background-color: #ffb7b3" : "background-color: white";
        }
    }

    public String getDerogatoryStyle() {
        String returnStyle = !this.transUnion.getAccountStatus().contains("Derogatory")
                && !this.experian.getAccountStatus().contains("Derogatory") &&
                !this.equifax.getAccountStatus().contains("Derogatory") ? "background-color: white" : "background-color: #ffb7b3";

        // so, there is no account with the status "Derogatory", so check for consistency
        if(returnStyle.equals("background-color: white")){
            boolean allMatch = Stream.of(this.transUnion.getAccountStatus(), this.experian.getAccountStatus(), this.equifax.getAccountStatus())
                    .map(status -> status.trim().toLowerCase())
                    .filter(status -> !status.isEmpty())
                    .allMatch(status -> status.equals(this.transUnion.getAccountStatus().trim().toLowerCase()));
            if(!allMatch){
                returnStyle = "background-color: #ffff99";
            }
        }

        return returnStyle;

    }
}
