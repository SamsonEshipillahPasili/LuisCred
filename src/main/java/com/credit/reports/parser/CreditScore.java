package com.credit.reports.parser;

public class CreditScore {
    private String creditScoreValue = "-1";
    private String lenderRank;
    private String scoreScale;

    public String getCreditScoreValue() {
        return this.creditScoreValue;
    }

    public String getCreditScoreRating() {
        int creditScoreValueInt = Integer.parseInt(this.creditScoreValue.trim());
        if (creditScoreValueInt >= 300 && creditScoreValueInt <= 549) {
            return "Deficient";
        } else if (creditScoreValueInt >= 550 && creditScoreValueInt <= 649) {
            return "Poor";
        } else if (creditScoreValueInt >= 650 && creditScoreValueInt <= 699) {
            return "Fair";
        } else if (creditScoreValueInt >= 700 && creditScoreValueInt <= 749) {
            return "Good";
        } else {
            return creditScoreValueInt >= 750 && creditScoreValueInt <= 850 ? "Excellent" : "Unavailable";
        }
    }

    public void setCreditScoreValue(String creditScoreValue) {
        this.creditScoreValue = creditScoreValue;
    }

    public String getLenderRank() {
        return this.lenderRank;
    }

    public void setLenderRank(String lenderRank) {
        this.lenderRank = lenderRank;
    }

    public String getScoreScale() {
        return this.scoreScale;
    }

    public void setScoreScale(String scoreScale) {
        this.scoreScale = scoreScale;
    }

    public String toString() {
        return "CreditScore{creditScoreValue=" + this.creditScoreValue + ", lenderRank=" + this.lenderRank + ", scoreScale=" + this.scoreScale + '}';
    }
}
