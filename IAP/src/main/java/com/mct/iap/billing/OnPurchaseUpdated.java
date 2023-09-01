package com.mct.iap.billing;

import com.android.billingclient.api.Purchase;

import java.util.List;

public interface OnPurchaseUpdated {

    void onUpdate(List<Purchase> purchases);

}
