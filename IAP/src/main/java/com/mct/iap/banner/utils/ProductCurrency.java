package com.mct.iap.banner.utils;

import android.content.Context;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.android.billingclient.api.ProductDetails;

public class ProductCurrency {

    @NonNull
    public static ProductCurrency of(float salePercent, @NonNull ProductDetails.PricingPhase pricingPhase) {
        return of(salePercent,
                pricingPhase.getPriceAmountMicros() / 1000000f,
                pricingPhase.getPriceCurrencyCode(),
                pricingPhase.getBillingPeriod());
    }

    @NonNull
    public static ProductCurrency of(float salePercent, float priceAmount, String priceCurrencyCode) {
        return of(salePercent, priceAmount, priceCurrencyCode, "");
    }

    @NonNull
    public static ProductCurrency of(float salePercent, float priceAmount, String priceCurrencyCode, String billingPeriod) {
        return of(salePercent, priceAmount, priceCurrencyCode, BillingPeriod.findPeriod(billingPeriod));
    }

    @NonNull
    public static ProductCurrency of(float salePercent, float priceAmount, String priceCurrencyCode, BillingPeriod billingPeriod) {
        return new ProductCurrency(salePercent, priceAmount, priceCurrencyCode, billingPeriod);
    }

    public final float discountPercent;
    public final float priceAmount;
    public final String priceCurrencyCode;
    public final BillingPeriod billingPeriod;

    ProductCurrency(float discountPercent, float priceAmount, String priceCurrencyCode, @NonNull BillingPeriod billingPeriod) {
        this.discountPercent = discountPercent;
        this.priceAmount = priceAmount;
        this.priceCurrencyCode = priceCurrencyCode;
        this.billingPeriod = billingPeriod;
    }

    public String getRealPrice() {
        return getFormattedPrice(priceAmount);
    }

    public String getFakePrice() {
        return getFakePrice(priceAmount, discountPercent);
    }

    public String getFakePrice(@FloatRange(from = 0f, to = 100f) float salePercent) {
        return getFakePrice(priceAmount, salePercent);
    }

    public String getFakePrice(float priceAmount, @FloatRange(from = 0f, to = 100f) float salePercent) {
        return getFormattedPrice(priceAmount / (100 - salePercent) * 100);
    }

    /**
     * Get avg period and avg price
     * <br/>return
     * <br/>- Pair.first = averagePeriod => {@link BillingPeriod#MONTH} || {@link BillingPeriod#DAY}
     * <br/>- Pair.second = averagePrice => price has been calculated
     */
    public Pair<BillingPeriod, String> getAveragePrice() {
        BillingPeriod averagePeriod;
        int averageTotalDay;
        if (billingPeriod.atLeast(BillingPeriod.QUARTER)) {
            averagePeriod = BillingPeriod.MONTH;
            averageTotalDay = billingPeriod.getTotalDay() / 30;
        } else {
            averagePeriod = BillingPeriod.DAY;
            averageTotalDay = billingPeriod.getTotalDay();
        }
        return new Pair<>(averagePeriod, getAveragePrice(averageTotalDay));
    }

    public String getAveragePrice(int average) {
        return getFormattedPrice(priceAmount / average);
    }

    public String getFormattedPrice(float price) {
        return FormatUtils.formatMoney(price, priceCurrencyCode);
    }

    public String getPeriodTitle(@NonNull Context context) {
        return getPeriodTitle(context, billingPeriod);
    }

    public String getPeriodTitle(@NonNull Context context, @NonNull BillingPeriod period) {
        return context.getResources().getString(period.getTitleRes());
    }
}
