package com.credit.reports.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class AccountInformationFactory {
    private final Document document;
    private final List<AccountInformation> transUnionAccountInformationList = new ArrayList<>();
    private final List<AccountInformation> experianAccountInformationList = new ArrayList<>();
    private final List<AccountInformation> equifaxAccountInformationList = new ArrayList<>();

    public AccountInformationFactory(Document document) {
        this.document = document;
        this.init();
    }

    private void init() {
        Elements accountHistoryTitles = this.document.select(".sub_header.ng-binding.ng-scope");
        accountHistoryTitles.forEach((accountHistoryTitle) -> {
            AccountInformation transUnion = new AccountInformation();
            AccountInformation experian = new AccountInformation();
            AccountInformation equifax = new AccountInformation();
            transUnion.setAccountName(accountHistoryTitle.text());
            experian.setAccountName(accountHistoryTitle.text());
            equifax.setAccountName(accountHistoryTitle.text());
            Element accountHistoryTable = accountHistoryTitle.nextElementSibling();
            Elements rows = accountHistoryTable.child(0).children();

            for(int count = 1; count < rows.size(); ++count) {
                switch(count) {
                    case 1:
                        transUnion.setAccountNumer(rows.get(count).child(1).text());
                        experian.setAccountNumer(rows.get(count).child(2).text());
                        equifax.setAccountNumer(rows.get(count).child(3).text());
                        break;
                    case 2:
                        transUnion.setAccountType(rows.get(count).child(1).text());
                        experian.setAccountType(rows.get(count).child(2).text());
                        equifax.setAccountType(rows.get(count).child(3).text());
                        break;
                    case 3:
                        transUnion.setDetailedAccountType(rows.get(count).child(1).text());
                        experian.setDetailedAccountType(rows.get(count).child(2).text());
                        equifax.setDetailedAccountType(rows.get(count).child(3).text());
                        break;
                    case 4:
                        transUnion.setBeaureuCode(rows.get(count).child(1).text());
                        experian.setBeaureuCode(rows.get(count).child(2).text());
                        equifax.setBeaureuCode(rows.get(count).child(3).text());
                        break;
                    case 5:
                        transUnion.setAccountStatus(rows.get(count).child(1).text());
                        experian.setAccountStatus(rows.get(count).child(2).text());
                        equifax.setAccountStatus(rows.get(count).child(3).text());
                        break;
                    case 6:
                        transUnion.setMonthlyPayment(rows.get(count).child(1).text());
                        experian.setMonthlyPayment(rows.get(count).child(2).text());
                        equifax.setMonthlyPayment(rows.get(count).child(3).text());
                        break;
                    case 7:
                        transUnion.setDateOpened(rows.get(count).child(1).text());
                        experian.setDateOpened(rows.get(count).child(2).text());
                        equifax.setDateOpened(rows.get(count).child(3).text());
                        break;
                    case 8:
                        transUnion.setBalance(rows.get(count).child(1).text());
                        experian.setBalance(rows.get(count).child(2).text());
                        equifax.setBalance(rows.get(count).child(3).text());
                        break;
                    case 9:
                        transUnion.setNoOfMonths(rows.get(count).child(1).text());
                        experian.setNoOfMonths(rows.get(count).child(2).text());
                        equifax.setNoOfMonths(rows.get(count).child(3).text());
                        break;
                    case 10:
                        transUnion.setHighCredit(rows.get(count).child(1).text());
                        experian.setHighCredit(rows.get(count).child(2).text());
                        equifax.setHighCredit(rows.get(count).child(3).text());
                        break;
                    case 11:
                        transUnion.setCreditLimit(rows.get(count).child(1).text());
                        experian.setCreditLimit(rows.get(count).child(2).text());
                        equifax.setCreditLimit(rows.get(count).child(3).text());
                        break;
                    case 12:
                        transUnion.setPastDue(rows.get(count).child(1).text());
                        experian.setPastDue(rows.get(count).child(2).text());
                        equifax.setPastDue(rows.get(count).child(3).text());
                        break;
                    case 13:
                        transUnion.setPaymentStatus(rows.get(count).child(1).text());
                        experian.setPaymentStatus(rows.get(count).child(2).text());
                        equifax.setPaymentStatus(rows.get(count).child(3).text());
                        break;
                    case 14:
                        transUnion.setLastReported(rows.get(count).child(1).text());
                        experian.setLastReported(rows.get(count).child(2).text());
                        equifax.setLastReported(rows.get(count).child(3).text());
                        break;
                    case 15:
                        transUnion.setComments(rows.get(count).child(1).text());
                        experian.setComments(rows.get(count).child(2).text());
                        equifax.setComments(rows.get(count).child(3).text());
                        break;
                    case 16:
                        transUnion.setDateLastActive(rows.get(count).child(1).text());
                        experian.setDateLastActive(rows.get(count).child(2).text());
                        equifax.setDateLastActive(rows.get(count).child(3).text());
                        break;
                    case 17:
                        transUnion.setDateOfLastPayment(rows.get(count).child(1).text());
                        experian.setDateOfLastPayment(rows.get(count).child(2).text());
                        equifax.setDateOfLastPayment(rows.get(count).child(3).text());
                }
            }

            PaymentHistoryFactory paymentHistoryFactory = new PaymentHistoryFactory(accountHistoryTable.parent().nextElementSibling().nextElementSibling());
            transUnion.setPaymentHistory(paymentHistoryFactory.transUnion());
            experian.setPaymentHistory(paymentHistoryFactory.experian());
            equifax.setPaymentHistory(paymentHistoryFactory.equifax());
            this.equifaxAccountInformationList.add(equifax);
            this.experianAccountInformationList.add(experian);
            this.transUnionAccountInformationList.add(transUnion);
        });
    }

    public List<AccountInformation> transUnion() {
        return this.transUnionAccountInformationList;
    }

    public List<AccountInformation> experian() {
        return this.experianAccountInformationList;
    }

    public List<AccountInformation> equifax() {
        return this.equifaxAccountInformationList;
    }
}
