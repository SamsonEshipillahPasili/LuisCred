package com.credit.reports.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AccountInformationFactory {
    private final Document document;
    private final List<AccountInformation> transUnionAccountInformationList = new ArrayList();
    private final List<AccountInformation> experianAccountInformationList = new ArrayList();
    private final List<AccountInformation> equifaxAccountInformationList = new ArrayList();

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
                        transUnion.setAccountNumer(((Element)rows.get(count)).child(1).text());
                        experian.setAccountNumer(((Element)rows.get(count)).child(2).text());
                        equifax.setAccountNumer(((Element)rows.get(count)).child(3).text());
                        break;
                    case 2:
                        transUnion.setAccountType(((Element)rows.get(count)).child(1).text());
                        experian.setAccountType(((Element)rows.get(count)).child(2).text());
                        equifax.setAccountType(((Element)rows.get(count)).child(3).text());
                        break;
                    case 3:
                        transUnion.setDetailedAccountType(((Element)rows.get(count)).child(1).text());
                        experian.setDetailedAccountType(((Element)rows.get(count)).child(2).text());
                        equifax.setDetailedAccountType(((Element)rows.get(count)).child(3).text());
                        break;
                    case 4:
                        transUnion.setBeaureuCode(((Element)rows.get(count)).child(1).text());
                        experian.setBeaureuCode(((Element)rows.get(count)).child(2).text());
                        equifax.setBeaureuCode(((Element)rows.get(count)).child(3).text());
                        break;
                    case 5:
                        transUnion.setAccountStatus(((Element)rows.get(count)).child(1).text());
                        experian.setAccountStatus(((Element)rows.get(count)).child(2).text());
                        equifax.setAccountStatus(((Element)rows.get(count)).child(3).text());
                        break;
                    case 6:
                        transUnion.setMonthlyPayment(((Element)rows.get(count)).child(1).text());
                        experian.setMonthlyPayment(((Element)rows.get(count)).child(2).text());
                        equifax.setMonthlyPayment(((Element)rows.get(count)).child(3).text());
                        break;
                    case 7:
                        transUnion.setDateOpened(((Element)rows.get(count)).child(1).text());
                        experian.setDateOpened(((Element)rows.get(count)).child(2).text());
                        equifax.setDateOpened(((Element)rows.get(count)).child(3).text());
                        break;
                    case 8:
                        transUnion.setBalance(((Element)rows.get(count)).child(1).text());
                        experian.setBalance(((Element)rows.get(count)).child(2).text());
                        equifax.setBalance(((Element)rows.get(count)).child(3).text());
                        break;
                    case 9:
                        transUnion.setNoOfMonths(((Element)rows.get(count)).child(1).text());
                        experian.setNoOfMonths(((Element)rows.get(count)).child(2).text());
                        equifax.setNoOfMonths(((Element)rows.get(count)).child(3).text());
                        break;
                    case 10:
                        transUnion.setHighCredit(((Element)rows.get(count)).child(1).text());
                        experian.setHighCredit(((Element)rows.get(count)).child(2).text());
                        equifax.setHighCredit(((Element)rows.get(count)).child(3).text());
                        break;
                    case 11:
                        transUnion.setCreditLimit(((Element)rows.get(count)).child(1).text());
                        experian.setCreditLimit(((Element)rows.get(count)).child(2).text());
                        equifax.setCreditLimit(((Element)rows.get(count)).child(3).text());
                        break;
                    case 12:
                        transUnion.setPastDue(((Element)rows.get(count)).child(1).text());
                        experian.setPastDue(((Element)rows.get(count)).child(2).text());
                        equifax.setPastDue(((Element)rows.get(count)).child(3).text());
                        break;
                    case 13:
                        transUnion.setPaymentStatus(((Element)rows.get(count)).child(1).text());
                        experian.setPaymentStatus(((Element)rows.get(count)).child(2).text());
                        equifax.setPaymentStatus(((Element)rows.get(count)).child(3).text());
                        break;
                    case 14:
                        transUnion.setLastReported(((Element)rows.get(count)).child(1).text());
                        experian.setLastReported(((Element)rows.get(count)).child(2).text());
                        equifax.setLastReported(((Element)rows.get(count)).child(3).text());
                        break;
                    case 15:
                        transUnion.setComments(((Element)rows.get(count)).child(1).text());
                        experian.setComments(((Element)rows.get(count)).child(2).text());
                        equifax.setComments(((Element)rows.get(count)).child(3).text());
                        break;
                    case 16:
                        transUnion.setDateLastActive(((Element)rows.get(count)).child(1).text());
                        experian.setDateLastActive(((Element)rows.get(count)).child(2).text());
                        equifax.setDateLastActive(((Element)rows.get(count)).child(3).text());
                        break;
                    case 17:
                        transUnion.setDateOfLastPayment(((Element)rows.get(count)).child(1).text());
                        experian.setDateOfLastPayment(((Element)rows.get(count)).child(2).text());
                        equifax.setDateOfLastPayment(((Element)rows.get(count)).child(3).text());
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
