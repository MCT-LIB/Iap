package com.mct.iap.banner.utils;

import androidx.annotation.NonNull;

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
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            numberFormat.setCurrency(Currency.getInstance(priceCurrencyCode));
            numberFormat.setMinimumFractionDigits(value % 1 != 0 ? 2 : 0);
            return numberFormat.format(value);
        } catch (Exception e) {
            return "";
        }
    }

}
