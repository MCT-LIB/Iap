package com.mct.iap.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.mct.iap.banner.IapBanner;
import com.mct.iap.banner.IapBannerBuilder;
import com.mct.iap.banner.component.billing.BillingEventListeners;
import com.mct.iap.banner.component.billing.BillingPeriod;
import com.mct.iap.banner.component.billing.ProductConfiguration;
import com.mct.iap.banner.component.billing.ProductPriceInfo;
import com.mct.iap.billing.models.PurchaseInfo;

public class MainActivity extends AppCompatActivity {

    public static final String SUB_MONTH = "sub_month_no_trial";
    public static final String SUB_YEAR = "sub_yearly_no_trial";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.banner_countdown).setOnClickListener(v -> showBannerCountdown());
        findViewById(R.id.banner_medium).setOnClickListener(v -> showBannerMedium());
        findViewById(R.id.banner_large).setOnClickListener(v -> showBannerLarge());
        findViewById(R.id.pay_wall).setOnClickListener(v -> showPaywall());

        /*/
        getSupportFragmentManager().beginTransaction()
                .add(Window.ID_ANDROID_CONTENT, new MainFragment(), null)
                .addToBackStack(null)
                .commit();
        /*/
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
        ProductPriceInfo product = ProductPriceInfo.fromPriceAmount(80, 750_000, "VND", "P1Y");

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
                .bindDismiss(R.id.btn_close).and()
                .show(false);
    }

    private void showBannerLarge() {
        ProductConfiguration monthConfiguration = ProductConfiguration.of(SUB_MONTH).build();
        ProductConfiguration yearConfiguration = ProductConfiguration.of(SUB_YEAR).withDiscountPercent(80).build();
        new IapBannerBuilder(this, R.layout.iap_banner_large)
                .bindBilling(this, billingComponent -> billingComponent
                        .addSubscription(monthConfiguration)
                        .addSubscription(yearConfiguration)
                        //.autoConsume()
                        .autoAcknowledge()
                        .addBillingEventListener(new BillingEventListeners() {
                            @Override
                            public void onPurchaseAcknowledged(@NonNull IapBanner banner, @NonNull PurchaseInfo purchase) {

                            }

                        }))
                .bindLazyText(R.id.tv_money_avg, yearConfiguration, lazyTextComponent -> lazyTextComponent
                        .lazyText((productInfo, productPriceInfo) -> {
                            Pair<BillingPeriod, String> avgPrice = productPriceInfo.getAveragePrice();
                            return String.format("Only %s/%s", avgPrice.second, getString(avgPrice.first.getTitleRes()));
                        }))
                .bindLazyText(R.id.tv_money_real, yearConfiguration, lazyTextComponent -> lazyTextComponent
                        .lazyText((productInfo, productPriceInfo) -> {
                            String realPrice = productPriceInfo.getRealPrice();
                            String periodTitle = productPriceInfo.getPeriodTitle(this);
                            return String.format("Total %s/%s", realPrice, periodTitle);
                        }))
                .bindLazyText(R.id.tv_money_fake, yearConfiguration, lazyTextComponent -> lazyTextComponent
                        .strikeThru()
                        .lazyText((productInfo, productPriceInfo) -> String.format("(was %s)", productPriceInfo.getFakePrice()))
                        .lazyHighlightText((productInfo, productPriceInfo) -> productPriceInfo.getFakePrice()))
                .bindView(R.id.tv_privacy_policy).setClickListener(v -> showToast("click tv_privacy_policy")).and()
                .bindView(R.id.tv_restore_purchase).setClickListener(v -> showToast("click tv_restore_purchase")).and()
                .bindView(R.id.tv_terms_of_use).setClickListener(v -> showToast("click tv_terms_of_use")).and()
                .bindDismiss(R.id.btn_close).and()
                .bindClaim(R.id.btn_claim, yearConfiguration)
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