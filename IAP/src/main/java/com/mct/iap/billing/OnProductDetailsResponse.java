package com.mct.iap.billing;

import com.android.billingclient.api.ProductDetails;

import java.util.Map;

public interface OnProductDetailsResponse {

    void onResponse(Map<String, ProductDetails> productDetails);

}
