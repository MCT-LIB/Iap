package com.mct.iap.banner.utils;

import androidx.annotation.NonNull;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;

public class FormatUtils {

    @NonNull
    public static String formatTime(int value) {
        return value < 10 ? "0" + value : "" + value;
    }

    @NonNull
    public static String formatMoney(float value, String priceCurrencyCode) {
        if (value < 0) {
            return "";
        }
        try {
            Currency currency = Currency.getInstance(priceCurrencyCode);
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            numberFormat.setRoundingMode(RoundingMode.HALF_EVEN);
            numberFormat.setCurrency(currency);
            adjustDefaultFractionDigits(numberFormat, currency);
            return numberFormat.format(value);
        } catch (Exception e) {
            return "";
        }
    }

    private static void adjustDefaultFractionDigits(@NonNull NumberFormat numberFormat, Currency defaultCurrency) {
        Currency currency = numberFormat.getCurrency() == null ? defaultCurrency : numberFormat.getCurrency();
        if (currency != null) {
            int digits = currency.getDefaultFractionDigits();
            if (digits != -1) {
                int oldMinDigits = numberFormat.getMinimumFractionDigits();
                // Common patterns are "#.##", "#.00", "#".
                // Try to adjust all of them in a reasonable way.
                if (oldMinDigits == numberFormat.getMaximumFractionDigits()) {
                    numberFormat.setMinimumFractionDigits(digits);
                    numberFormat.setMaximumFractionDigits(digits);
                } else {
                    numberFormat.setMinimumFractionDigits(Math.min(digits, oldMinDigits));
                    numberFormat.setMaximumFractionDigits(digits);
                }
            }
        }
    }

}
