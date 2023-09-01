package com.mct.iap.utils;

import androidx.annotation.NonNull;

import java.text.NumberFormat;
import java.util.Currency;

public class ConvertUtils {

    public static final int ONE_SECOND = 1000;
    public static final int ONE_MINUTE = 60 * ONE_SECOND;
    public static final int ONE_HOUR = 60 * ONE_MINUTE;

    @NonNull
    public static int[] convertTime(long time) {
        int hours = (int) (time / ONE_HOUR);
        int minutes = (int) ((time % ONE_HOUR) / ONE_MINUTE);
        int seconds = (int) (((time % ONE_HOUR) % ONE_MINUTE) / ONE_SECOND);

        return new int[]{hours, minutes, seconds};
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

    @NonNull
    public static String formatTime(int value) {
        return value < 10 ? "0" + value : "" + value;
    }

}
