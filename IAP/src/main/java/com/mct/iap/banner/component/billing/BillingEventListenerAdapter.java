package com.mct.iap.banner.component.billing;

import androidx.annotation.NonNull;

import com.mct.iap.billing.BillingConnector;
import com.mct.iap.billing.BillingEventListener;
import com.mct.iap.billing.enums.ProductType;
import com.mct.iap.billing.models.BillingResponse;
import com.mct.iap.billing.models.ProductInfo;
import com.mct.iap.billing.models.PurchaseInfo;

import java.util.List;

public class BillingEventListenerAdapter implements BillingEventListener {

    @Override
    public void onProductsFetched(@NonNull List<ProductInfo> productDetails) {
    }

    @Override
    public void onPurchasedProductsFetched(@NonNull ProductType productType, @NonNull List<PurchaseInfo> purchases) {
    }

    @Override
    public void onProductsPurchased(@NonNull List<PurchaseInfo> purchases) {
    }

    @Override
    public void onPurchaseAcknowledged(@NonNull PurchaseInfo purchase) {
    }

    @Override
    public void onPurchaseConsumed(@NonNull PurchaseInfo purchase) {
    }

    @Override
    public void onBillingError(@NonNull BillingConnector billingConnector, @NonNull BillingResponse response) {
    }
}
