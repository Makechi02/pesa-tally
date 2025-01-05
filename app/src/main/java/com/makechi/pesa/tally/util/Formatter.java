package com.makechi.pesa.tally.util;

import java.util.Locale;

public class Formatter {

    private static final String CURRENCY = "Ksh. ";
    private static final String PERCENT_SIGN = "%";

    public static String formatMoneyWithCurrency(double amount) {
        String amountText = formatMoney(amount);
        return CURRENCY + amountText;
    }

    public static String formatMoney(double amount) {
        return String.format(Locale.getDefault(), "%.2f", amount);
    }

    public static String formatDepositWithCurrency(double amount) {
        String amountText = formatMoney(amount);
        return "+ " + CURRENCY + amountText;
    }

    public static String formatWithdrawalWithCurrency(double amount) {
        String amountText = formatMoney(amount);
        return "- " + CURRENCY + amountText;
    }

    public static String formatProgress(double progress) {
        double percentageProgress = (double) Math.round(progress * 100) / 100;
        return percentageProgress + PERCENT_SIGN;
    }
}
