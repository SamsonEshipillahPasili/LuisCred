package com.credit.reports.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

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
        List<String> processedBalances = (List)rawBalances.stream().map((rawBalance) -> {
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

        List<Date> dateOpenedList = new ArrayList();
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

        List<Date> dateLastActiveList = new ArrayList();
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

    public boolean isConsistent(int constant) {
        Constants constants = new Constants();
        constants.getClass();
        if (constant == 0) {
            return this.checkEquality(this.transUnion.getAccountNumer(), this.experian.getAccountNumer(), this.equifax.getAccountNumer());
        } else {
            constants.getClass();
            if (constant == 2) {
                return this.checkEquality(this.transUnion.getDetailedAccountType(), this.experian.getDetailedAccountType(), this.equifax.getDetailedAccountType());
            } else {
                constants.getClass();
                if (constant == 4) {
                    return this.checkEquality(this.transUnion.getAccountStatus(), this.experian.getAccountStatus(), this.equifax.getAccountStatus());
                } else {
                    constants.getClass();
                    if (constant == 1) {
                        return this.checkEquality(this.transUnion.getAccountType(), this.experian.getAccountType(), this.equifax.getAccountType());
                    } else {
                        constants.getClass();
                        if (constant == 7) {
                            return this.checkMonetaryConsistency(this.transUnion.getRawBalance(), this.experian.getRawBalance(), this.equifax.getRawBalance());
                        } else {
                            constants.getClass();
                            if (constant == 3) {
                                return this.checkEquality(this.transUnion.getBeaureuCode(), this.experian.getBeaureuCode(), this.equifax.getBeaureuCode());
                            } else {
                                constants.getClass();
                                if (constant == 10) {
                                    return this.checkMonetaryConsistency(this.transUnion.getRawCreditLimit(), this.experian.getRawCreditLimit(), this.equifax.getRawCreditLimit());
                                } else {
                                    constants.getClass();
                                    if (constant == 15) {
                                        return this.checkDateConsistency(this.transUnion.getDateLastActive(), this.experian.getDateLastActive(), this.equifax.getDateLastActive());
                                    } else {
                                        constants.getClass();
                                        if (constant == 16) {
                                            return this.checkDateConsistency(this.transUnion.getDateOfLastPayment(), this.experian.getDateOfLastPayment(), this.equifax.getDateOfLastPayment());
                                        } else {
                                            constants.getClass();
                                            if (constant == 6) {
                                                return this.checkDateConsistency(this.transUnion.getDateOpened(), this.experian.getDateOpened(), this.equifax.getDateOpened());
                                            } else {
                                                constants.getClass();
                                                if (constant == 9) {
                                                    return this.checkMonetaryConsistency(this.transUnion.getHighCredit(), this.experian.getHighCredit(), this.equifax.getHighCredit());
                                                } else {
                                                    constants.getClass();
                                                    if (constant == 13) {
                                                        return this.checkDateConsistency(this.transUnion.getLastReported(), this.experian.getLastReported(), this.equifax.getLastReported());
                                                    } else {
                                                        constants.getClass();
                                                        if (constant == 5) {
                                                            return this.checkMonetaryConsistency(this.transUnion.getMonthlyPayment(), this.experian.getMonthlyPayment(), this.equifax.getMonthlyPayment());
                                                        } else {
                                                            constants.getClass();
                                                            if (constant == 11) {
                                                                return this.checkMonetaryConsistency(this.transUnion.getPastDue(), this.experian.getPastDue(), this.equifax.getPastDue());
                                                            } else {
                                                                constants.getClass();
                                                                if (constant == 12) {
                                                                    return this.checkEquality(this.transUnion.getPaymentStatus(), this.experian.getPaymentStatus(), this.equifax.getPaymentStatus());
                                                                } else {
                                                                    constants.getClass();
                                                                    if (constant == 17) {
                                                                        return this.checkEquality(this.transUnion.getAccountName(), this.experian.getAccountName(), this.equifax.getAccountName());
                                                                    } else {
                                                                        constants.getClass();
                                                                        return constant == 8 ? this.checkEquality(this.transUnion.getNoOfMonths(), this.experian.getNoOfMonths(), this.equifax.getNoOfMonths()) : false;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean checkEquality(String transUnion, String experian, String equifax) {
        List<String> rawValues = Arrays.asList(transUnion, experian, equifax);
        List<String> processedValues = (List)rawValues.stream().map(String::trim).filter((value) -> {
            return !value.isEmpty();
        }).collect(Collectors.toList());
        if (!processedValues.isEmpty()) {
            String firstVal = (String)processedValues.get(0);
            return processedValues.stream().allMatch((val) -> {
                return val.equalsIgnoreCase(firstVal);
            });
        } else {
            return true;
        }
    }

    private boolean checkMonetaryConsistency(String money1, String money2, String money3) {
        List<String> monies = Arrays.asList(money1, money2, money3);
        List<String> processedMonies = (List)monies.stream().map(String::trim).filter((money) -> {
            return !money.equals("$0.00") && !money.isEmpty();
        }).collect(Collectors.toList());
        if (!processedMonies.isEmpty()) {
            String firstMoney = (String)processedMonies.get(0);
            return processedMonies.stream().allMatch((p) -> {
                return p.equals(firstMoney);
            });
        } else {
            return true;
        }
    }

    private boolean checkDateConsistency(String date1, String date2, String date3) {
        List<String> dates = Arrays.asList(date1, date2, date3);
        List<String> processedDates = (List)dates.stream().map(String::trim).filter((date) -> {
            return !date.isEmpty();
        }).map((date) -> {
            return date.substring(0, 3) + date.substring(5);
        }).collect(Collectors.toList());
        if (!processedDates.isEmpty()) {
            String firstValue = (String)processedDates.get(0);
            return processedDates.stream().allMatch((date) -> {
                return date.equals(firstValue);
            });
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
        return !this.transUnion.getAccountStatus().contains("Derogatory") && !this.experian.getAccountStatus().contains("Derogatory") && !this.equifax.getAccountStatus().contains("Derogatory") ? "background-color: white" : "background-color: #ffb7b3";
    }
}
