package com.mct.iap.billing;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillingClientHelper implements
        PurchasesUpdatedListener,
        PurchasesResponseListener,
        BillingClientStateListener,
        ProductDetailsResponseListener {

    private static final String TAG = "BillingLifecycle";

    private final Context context;
    private final List<QueryProductDetailsParams.Product> listOfProducts;

    private OnPurchaseUpdated onPurchaseUpdated;
    private OnProductDetailsResponse onProductDetailsResponse;

    private BillingClient billingClient;

    public BillingClientHelper(Context context) {
        this.context = context;
        this.listOfProducts = new ArrayList<>();
    }

    public void setProducts(QueryProductDetailsParams.Product... products) {
        listOfProducts.clear();
        Collections.addAll(listOfProducts, products);
    }

    public void setProducts(String... products) {
        listOfProducts.clear();
        if (products != null) {
            for (String id : products) {
                listOfProducts.add(QueryProductDetailsParams.Product.newBuilder()
                        .setProductType(BillingClient.ProductType.SUBS)
                        .setProductId(id)
                        .build());
            }
        }
    }

    public void setOnPurchaseUpdated(OnPurchaseUpdated onPurchaseUpdated) {
        this.onPurchaseUpdated = onPurchaseUpdated;
    }

    public void setOnProductDetailsResponse(OnProductDetailsResponse onProductDetailsResponse) {
        this.onProductDetailsResponse = onProductDetailsResponse;
    }

    public void startConnection() {
        Log.d(TAG, "startConnection");
        // Create a new BillingClient.
        // Since the BillingClient can only be used once, we need to create a new instance
        // after ending the previous connection to the Google Play Store in onDestroy().
        billingClient = BillingClient.newBuilder(context)
                .setListener(this)
                .enablePendingPurchases() // Not used for subscriptions.
                .build();
        if (!billingClient.isReady()) {
            Log.d(TAG, "BillingClient: Start connection...");
            billingClient.startConnection(this);
        }
    }

    public void endConnection() {
        Log.d(TAG, "endConnection");
        if (billingClient.isReady()) {
            Log.d(TAG, "BillingClient can only be used once -- closing connection");
            // BillingClient can only be used once.
            // After calling endConnection(), we must create a new BillingClient.
            billingClient.endConnection();
        }
    }

    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
        int responseCode = billingResult.getResponseCode();
        String debugMessage = billingResult.getDebugMessage();
        Log.d(TAG, "onBillingSetupFinished: " + responseCode + " " + debugMessage);
        if (responseCode == BillingClient.BillingResponseCode.OK) {
            // The billing client is ready. You can query purchases here.
            querySubscriptions();
            queryPurchases();
        }
    }

    @Override
    public void onBillingServiceDisconnected() {
        Log.d(TAG, "onBillingServiceDisconnected");
        // TODO: Try connecting again with exponential backoff.
    }

    @Override
    public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> productDetailsList) {
        int responseCode = billingResult.getResponseCode();
        String debugMessage = billingResult.getDebugMessage();
        switch (responseCode) {
            case BillingClient.BillingResponseCode.OK:
                Log.i(TAG, "onProductDetailsResponse: " + responseCode + " " + debugMessage);
                Map<String, ProductDetails> newProductDetailList = new HashMap<>();
                for (ProductDetails productDetails : productDetailsList) {
                    newProductDetailList.put(productDetails.getProductId(), productDetails);
                }
                onProductDetailsResponse.onResponse(newProductDetailList);
                int productDetailCount = newProductDetailList.size();
                int expectedProductDetailsCount = listOfProducts.size();
                if (productDetailCount == expectedProductDetailsCount) {
                    Log.i(TAG, "onProductDetailsResponse: Found " + productDetailCount + " ProductDetails");
                } else {
                    Log.e(TAG, "onProductDetailsResponse: " +
                            "Expected " + expectedProductDetailsCount + ", " +
                            "Found " + productDetailCount + " ProductDetails. " +
                            "Check to see if the Products you requested are correctly published " +
                            "in the Google Play Console.");
                }
                break;
            case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
            case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
            case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
            case BillingClient.BillingResponseCode.ERROR:
                Log.e(TAG, "onProductDetailsResponse: " + responseCode + " " + debugMessage);
                break;
            case BillingClient.BillingResponseCode.USER_CANCELED:
                Log.i(TAG, "onProductDetailsResponse: " + responseCode + " " + debugMessage);
                break;
            // These response codes are not expected.
            case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
            case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
            default:
                Log.wtf(TAG, "onProductDetailsResponse: " + responseCode + " " + debugMessage);
        }
    }

    /**
     * Query Google Play Billing for existing purchases.
     * <p>
     * New purchases will be provided to the PurchasesUpdatedListener.
     * You still need to check the Google Play Billing API to know when purchase tokens are removed.
     */
    public void queryPurchases() {
        if (!billingClient.isReady()) {
            Log.e(TAG, "queryPurchases: BillingClient is not ready");
        }
        Log.d(TAG, "queryPurchases: SUBS");
        billingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build(), this);
    }

    /**
     * Callback from the billing library when queryPurchasesAsync is called.
     */
    @Override
    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
        int responseCode = billingResult.getResponseCode();
        String debugMessage = billingResult.getDebugMessage();
        Log.d(TAG, String.format("onQueryPurchasesResponse: %s %s", responseCode, debugMessage));
        if (responseCode == BillingClient.BillingResponseCode.OK) {
            processPurchases(list);
        }
    }

    /**
     * Called by the Billing Library when new purchases are detected.
     */
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, List<Purchase> purchases) {
        int responseCode = billingResult.getResponseCode();
        String debugMessage = billingResult.getDebugMessage();
        Log.d(TAG, String.format("onPurchasesUpdated: %s %s", responseCode, debugMessage));
        switch (responseCode) {
            case BillingClient.BillingResponseCode.OK:
                if (purchases == null) {
                    Log.d(TAG, "onPurchasesUpdated: null purchase list");
                    processPurchases(null);
                } else {
                    processPurchases(purchases);
                }
                break;
            case BillingClient.BillingResponseCode.USER_CANCELED:
                Log.i(TAG, "onPurchasesUpdated: User canceled the purchase");
                break;
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                Log.i(TAG, "onPurchasesUpdated: The user already owns this item");
                break;
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                Log.e(TAG, "onPurchasesUpdated: Developer error means that Google Play " +
                        "does not recognize the configuration. If you are just getting started, " +
                        "make sure you have configured the application correctly in the " +
                        "Google Play Console. The product ID must match and the APK you " +
                        "are using must be signed with release keys."
                );
                break;
        }
    }

    /**
     * Send purchase SingleLiveEvent and update purchases LiveData.
     * <p>
     * The SingleLiveEvent will trigger network call to verify the subscriptions on the sever.
     * The LiveData will allow Google Play settings UI to update based on the latest purchase data.
     */
    private void processPurchases(List<Purchase> purchases) {
        if (purchases != null) {
            Log.d(TAG, "processPurchases: " + purchases.size() + " purchase(s)");
        } else {
            Log.d(TAG, "processPurchases: with no purchases");
        }
        if (isUnchangedPurchaseList(purchases)) {
            Log.d(TAG, "processPurchases: Purchase list has not changed");
            return;
        }
        onPurchaseUpdated.onUpdate(purchases);
        if (purchases != null) {
            logAcknowledgementStatus(purchases);
        }
    }

    /**
     * Log the number of purchases that are acknowledge and not acknowledged.
     * <p>
     * https://developer.android.com/google/play/billing/billing_library_releases_notes#2_0_acknowledge
     * <p>
     * When the purchase is first received, it will not be acknowledge.
     * This application sends the purchase token to the server for registration. After the
     * purchase token is registered to an account, the Android app acknowledges the purchase token.
     * The next time the purchase list is updated, it will contain acknowledged purchases.
     */
    private void logAcknowledgementStatus(@NonNull List<Purchase> purchasesList) {
        int ack_yes = 0;
        int ack_no = 0;
        for (Purchase purchase : purchasesList) {
            if (purchase.isAcknowledged()) {
                ack_yes++;
            } else {
                ack_no++;
            }
        }
        Log.d(TAG, "logAcknowledgementStatus: acknowledged=" + ack_yes + " unacknowledged=" + ack_no);
    }

    /**
     * Check whether the purchases have changed before posting changes.
     */
    private boolean isUnchangedPurchaseList(List<Purchase> purchasesList) {
        // TODO: Optimize to avoid updates with identical data.
        return false;
    }

    private void querySubscriptions() {
        Log.d(TAG, "querySubscriptions");

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(listOfProducts)
                .build();

        billingClient.queryProductDetailsAsync(params, this);
    }

    /**
     * Launching the billing flow.
     * <p>
     * Launching the UI to make a purchase requires a reference to the Activity.
     */
    public int launchBillingFlow(Activity activity, BillingFlowParams params) {
        if (!billingClient.isReady()) {
            Log.e(TAG, "launchBillingFlow: BillingClient is not ready");
        }
        BillingResult billingResult = billingClient.launchBillingFlow(activity, params);
        int responseCode = billingResult.getResponseCode();
        String debugMessage = billingResult.getDebugMessage();
        Log.d(TAG, "launchBillingFlow: BillingResponse " + responseCode + " " + debugMessage);
        return responseCode;
    }

    /**
     * Acknowledge a purchase.
     * <p>
     * https://developer.android.com/google/play/billing/billing_library_releases_notes#2_0_acknowledge
     * <p>
     * Apps should acknowledge the purchase after confirming that the purchase token
     * has been associated with a user. This app only acknowledges purchases after
     * successfully receiving the subscription data back from the server.
     * <p>
     * Developers can choose to acknowledge purchases from a server using the
     * Google Play Developer API. The server has direct access to the user database,
     * so using the Google Play Developer API for acknowledgement might be more reliable.
     * TODO(134506821): Acknowledge purchases on the server.
     * <p>
     * If the purchase token is not acknowledged within 3 days,
     * then Google Play will automatically refund and revoke the purchase.
     * This behavior helps ensure that users are not charged for subscriptions unless the
     * user has successfully received access to the content.
     * This eliminates a category of issues where users complain to developers
     * that they paid for something that the app is not giving to them.
     */
    public void acknowledgePurchase(String purchaseToken, AcknowledgePurchaseResponseListener listener) {
        Log.d(TAG, "acknowledgePurchase");
        AcknowledgePurchaseParams params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build();
        billingClient.acknowledgePurchase(params, billingResult -> {
            int responseCode = billingResult.getResponseCode();
            String debugMessage = billingResult.getDebugMessage();
            Log.d(TAG, "acknowledgePurchase: " + responseCode + " " + debugMessage);
            if (listener != null) {
                listener.onAcknowledgePurchaseResponse(billingResult);
            }
        });
    }

}