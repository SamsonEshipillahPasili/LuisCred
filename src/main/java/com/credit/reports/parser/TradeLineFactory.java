package com.credit.reports.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TradeLineFactory {
    private final AccountInformationFactory accountInformationFactory;

    public TradeLineFactory(AccountInformationFactory accountInformationFactory) {
        this.accountInformationFactory = accountInformationFactory;
    }

    public List<TradeLine> tradeLines() {
        List<TradeLine> tradeLines = new ArrayList<>();
        List<AccountInformation> transUnionAccountInformation = this.accountInformationFactory.transUnion();
        List<AccountInformation> equifaxAccountInformation = this.accountInformationFactory.equifax();
        List<AccountInformation> experianAccountInformation = this.accountInformationFactory.experian();

        for(int count = 0; count < transUnionAccountInformation.size(); ++count) {
            TradeLine tradeLine = new TradeLine();
            tradeLine.setTransUnion(transUnionAccountInformation.get(count));
            tradeLine.setExperian(experianAccountInformation.get(count));
            tradeLine.setEquifax(equifaxAccountInformation.get(count));
            tradeLines.add(tradeLine);
        }

        return tradeLines;
    }

    public String averageAccountAge() throws ParseException {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        List<TradeLine> tradeLines = this.tradeLines();
        List<Long> ages = new ArrayList<>();
        AtomicLong atomicLong = new AtomicLong();
        tradeLines.forEach((e) -> {
            if (!e.getTransUnion().getDateOpened().trim().isEmpty()) {
                try {
                    ages.add((new Date()).getTime() - df.parse(e.getTransUnion().getDateOpened()).getTime());
                } catch (ParseException var6) {
                    Logger.getLogger(TradeLineFactory.class.getName()).log(Level.SEVERE, null, var6);
                }
            }

            if (!e.getExperian().getDateOpened().trim().isEmpty()) {
                try {
                    ages.add((new Date()).getTime() - df.parse(e.getExperian().getDateOpened()).getTime());
                } catch (ParseException var5) {
                    Logger.getLogger(TradeLineFactory.class.getName()).log(Level.SEVERE, null, var5);
                }
            }

            if (!e.getEquifax().getDateOpened().trim().isEmpty()) {
                try {
                    ages.add((new Date()).getTime() - df.parse(e.getEquifax().getDateOpened()).getTime());
                } catch (ParseException var4) {
                    Logger.getLogger(TradeLineFactory.class.getName()).log(Level.SEVERE, null, var4);
                }
            }

        });
        long yearTime = df.parse("01/01/2018").getTime() - df.parse("01/01/2017").getTime();
        ages.forEach(atomicLong::addAndGet);
        double result = (double)atomicLong.get() / (double)ages.size();
        String resultString = String.valueOf(result / (double)yearTime);
        return resultString.length() < 5 ? resultString : resultString.substring(0, 4);
    }

    public String averageAgeRating() {
        try {
            double averageAgeRating = Double.parseDouble(this.averageAccountAge());
            if (averageAgeRating > 20.0D) {
                return "Excellent";
            } else if (averageAgeRating > 15.0D && averageAgeRating <= 20.0D) {
                return "Good";
            } else if (averageAgeRating > 10.0D && averageAgeRating <= 15.0D) {
                return "Average";
            } else {
                return averageAgeRating > 6.0D && averageAgeRating <= 10.0D ? "Poor" : "Bad";
            }
        } catch (ParseException var3) {
            return "Unavailable";
        }
    }
}
