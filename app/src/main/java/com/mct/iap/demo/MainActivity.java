package com.mct.iap.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.mct.iap.banner.IapBannerBuilder;
import com.mct.iap.billing.BillingPeriod;
import com.mct.iap.billing.ProductHelper;

public class MainActivity extends AppCompatActivity {

    private final ProductHelper product = ProductHelper.of(
            80,
            750_000,
            "VND",
            "P1Y"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.banner_countdown).setOnClickListener(v -> showBannerCountdown());
        findViewById(R.id.banner_medium).setOnClickListener(v -> showBannerMedium());
        findViewById(R.id.banner_large).setOnClickListener(v -> showBannerLarge());
        findViewById(R.id.pay_wall).setOnClickListener(v -> showPaywall());
    }

    private void showBannerCountdown() {

        new IapBannerBuilder(this, R.layout.iap_banner_countdown)
                .setCountDownTime(3 * 60 * 60 * 1000)
                .bindTime(R.id.tv_hour).hour().bold().and()
                .bindTime(R.id.tv_minute).minute().italic().and()
                .bindTime(R.id.tv_second).second().strikeThru().and()
                .bindTime(R.id.tv_millisecond).millisecond().underline().highlight(Color.RED).and()
                .show(false);
    }

    private void showBannerMedium() {

        Pair<BillingPeriod, String> avgPrice = product.getAveragePrice();

        String money_avg = String.format("Only %s/%s", avgPrice.second, getString(avgPrice.first.getTitleRes()));
        String money_real = String.format("Total %s/%s", product.getRealPrice(), product.getPeriodTitle(this));
        String money_fake = String.format("(was %s)", product.getFakePrice());

        new IapBannerBuilder(this, R.layout.iap_banner_medium)
                .bindText(R.id.tv_money_avg, text -> text
                        .text(money_avg))
                .bindText(R.id.tv_money_real, text -> text
                        .text(money_real))
                .bindText(R.id.tv_money_fake, text -> text
                        .text(money_fake)
                        .highlightText(product.getFakePrice())
                        .strikeThru())
                .bindDismiss(R.id.btn_close)
                .and()
                .bindClaim(R.id.btn_claim).setOnClaimListener((view, params) -> showToast("Click claim!"))
                .show(false);
    }

    private void showBannerLarge() {

        Pair<BillingPeriod, String> avgPrice = product.getAveragePrice();

        String money_avg = String.format("Only %s/%s", avgPrice.second, getString(avgPrice.first.getTitleRes()));
        String money_real = String.format("Total %s/%s", product.getRealPrice(), product.getPeriodTitle(this));
        String money_fake = String.format("(was %s)", product.getFakePrice());

        new IapBannerBuilder(this, R.layout.iap_banner_large)
                .bindText(R.id.tv_money_avg, text -> text
                        .text(money_avg))
                .bindText(R.id.tv_money_real, text -> text
                        .text(money_real))
                .bindText(R.id.tv_money_fake, text -> text
                        .text(money_fake)
                        .highlightText(product.getFakePrice())
                        .strikeThru())
                .bindView(R.id.tv_privacy_policy).setClickListener(v -> showToast("click tv_privacy_policy")).and()
                .bindView(R.id.tv_restore_purchase).setClickListener(v -> showToast("click tv_restore_purchase")).and()
                .bindView(R.id.tv_terms_of_use).setClickListener(v -> showToast("click tv_terms_of_use")).and()
                .bindDismiss(R.id.btn_close).and()
                .bindClaim(R.id.btn_claim).setOnClaimListener((view, params) -> showToast("Click claim!"))
                .show(true);
    }

    private void showPaywall() {
        new Paywall(this).show();
    }

    private Toast toast;

    private void showToast(String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}