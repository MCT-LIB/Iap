package com.mct.iap.banner.component.billing;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.mct.iap.BuildConfig;
import com.mct.iap.R;
import com.mct.iap.banner.IapBanner;
import com.mct.iap.banner.component.BaseComponentAdapter;
import com.mct.iap.billing.BillingConnector;
import com.mct.iap.billing.BillingEventListener;
import com.mct.iap.billing.enums.ProductType;
import com.mct.iap.billing.enums.PurchasedResult;
import com.mct.iap.billing.models.BillingResponse;
import com.mct.iap.billing.models.ProductInfo;
import com.mct.iap.billing.models.PurchaseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * BillingComponent manages in-app purchase functionality for fetching product
 * listings and purchase information. It handles connectivity to the billing
 * library and notifies listeners of purchase events.
 * <p>
 * The BillingComponent is designed to facilitate in-app purchases within your Android
 * application. It connects to the billing library, fetches product details, handles
 * purchases, and provides callbacks for purchase-related events.
 * <p>
 * Usage:
 * To use the BillingComponent, create an instance of it with an Activity and an optional
 * base64 key. You can add consumable, non-consumable, and subscription product IDs to
 * fetch listings for. You can also configure auto-acknowledgment and auto-consumption
 * of purchases, enable logging, and add listeners for purchase events.
 * <p>
 * Example Usage:
 * <code>
 * <pre>
 * BillingComponent billingComponent = new BillingComponent(activity, "YOUR_BASE64_KEY")
 *     .addConsumableIds("consumable_product_id")
 *     .addNonConsumableIds("non_consumable_product_id")
 *     .addSubscriptionIds("subscription_product_id")
 *     .autoAcknowledge()
 *     .autoConsume()
 *     .enableLogging()
 *     .addBillingEventListener(billingEventListener);
 * </pre>
 * </code>
 */
public class BillingComponent extends BaseComponentAdapter {

    /**
     * The ID for the billing component.
     */
    public static final int ID = R.id.iap_component_billing;
    public static final boolean LOG_ENABLE = BuildConfig.DEBUG;

    private final Activity activity;
    private final BillingConnector connector;
    private final List<BillingEventListener> listeners;

    private final List<String> consumableIds = new ArrayList<>();
    private final List<String> nonConsumableIds = new ArrayList<>();
    private final List<String> subscriptionIds = new ArrayList<>();

    /**
     * BillingComponent public constructor
     *
     * @param activity - is the activity
     */
    public BillingComponent(Activity activity) {
        this(activity, null);
    }

    /**
     * BillingComponent public constructor
     *
     * @param activity  - is the activity
     * @param base64Key - The public developer key from Play Console (optional).
     */
    public BillingComponent(Activity activity, String base64Key) {
        this.activity = activity;
        this.connector = new BillingConnector(activity, base64Key);
        this.listeners = new ArrayList<>();
        if (LOG_ENABLE) {
            enableLogging();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(@NonNull IapBanner banner, View root) {
        super.init(banner, root);
        connector.setConsumableIds(consumableIds);
        connector.setNonConsumableIds(nonConsumableIds);
        connector.setSubscriptionIds(subscriptionIds);
        connector.connect();
        connector.setBillingEventListener(new BillingEventListener() {
            @Override
            public void onProductsFetched(@NonNull List<ProductInfo> productDetails) {
                for (BillingEventListener listener : listeners) {
                    listener.onProductsFetched(productDetails);
                }
            }

            @Override
            public void onPurchasedProductsFetched(@NonNull ProductType productType, @NonNull List<PurchaseInfo> purchases) {
                for (BillingEventListener listener : listeners) {
                    listener.onPurchasedProductsFetched(productType, purchases);
                }
            }

            @Override
            public void onProductsPurchased(@NonNull List<PurchaseInfo> purchases) {
                for (BillingEventListener listener : listeners) {
                    listener.onProductsPurchased(purchases);
                }
            }

            @Override
            public void onPurchaseAcknowledged(@NonNull PurchaseInfo purchase) {
                for (BillingEventListener listener : listeners) {
                    listener.onPurchaseAcknowledged(purchase);
                }
            }

            @Override
            public void onPurchaseConsumed(@NonNull PurchaseInfo purchase) {
                for (BillingEventListener listener : listeners) {
                    listener.onPurchaseConsumed(purchase);
                }
            }

            @Override
            public void onBillingError(@NonNull BillingConnector billingConnector, @NonNull BillingResponse response) {
                for (BillingEventListener listener : listeners) {
                    listener.onBillingError(billingConnector, response);
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(@NonNull IapBanner banner, View root) {
        super.release(banner, root);
        connector.release();
        listeners.clear();
    }

    /**
     * Adds a consumable product ID to fetch listings for.
     *
     * @param consumableId - The consumable product ID to be added.
     * @return The {@link BillingComponent} instance for method chaining.
     */
    public BillingComponent addConsumableIds(String consumableId) {
        add(consumableIds, consumableId);
        return this;
    }

    /**
     * Adds a non-consumable product ID to fetch listings for.
     *
     * @param nonConsumableId - The non-consumable product ID to be added.
     * @return The {@link BillingComponent} instance for method chaining.
     */
    public BillingComponent addNonConsumableIds(String nonConsumableId) {
        add(nonConsumableIds, nonConsumableId);
        return this;
    }

    /**
     * Adds a subscription product ID to fetch listings for.
     *
     * @param subscriptionId - The subscription product ID to be added.
     * @return The {@link BillingComponent} instance for method chaining.
     */
    public BillingComponent addSubscriptionIds(String subscriptionId) {
        add(subscriptionIds, subscriptionId);
        return this;
    }

    /**
     * Set consumable products ID to fetch listings for.
     *
     * @param consumableIds - A list of consumable product IDs to be set.
     * @return The {@link BillingComponent} instance for method chaining.
     */
    public BillingComponent setConsumableIds(List<String> consumableIds) {
        set(this.consumableIds, consumableIds);
        return this;
    }

    /**
     * Set non-consumable products ID to fetch listings for.
     *
     * @param nonConsumableIds - A list of non-consumable product IDs to be set.
     * @return The {@link BillingComponent} instance for method chaining.
     */
    public BillingComponent setNonConsumableIds(List<String> nonConsumableIds) {
        set(this.nonConsumableIds, nonConsumableIds);
        return this;
    }

    /**
     * Set subscription products ID to fetch listings for.
     *
     * @param subscriptionIds - A list of subscription product IDs to be set.
     * @return The {@link BillingComponent} instance for method chaining.
     */
    public BillingComponent setSubscriptionIds(List<String> subscriptionIds) {
        set(this.subscriptionIds, subscriptionIds);
        return this;
    }

    /**
     * Configures auto-acknowledgment of purchases. When enabled, purchases are automatically acknowledged.
     *
     * @return The {@link BillingComponent} instance for method chaining.
     */
    public BillingComponent autoAcknowledge() {
        connector.autoAcknowledge();
        return this;
    }

    /**
     * Configures auto-consumption of consumable purchases. When enabled, consumable purchases are automatically consumed.
     *
     * @return The {@link BillingComponent} instance for method chaining.
     */
    public BillingComponent autoConsume() {
        connector.autoConsume();
        return this;
    }

    /**
     * Enables logging from the billing library.
     *
     * @return The {@link BillingComponent} instance for method chaining.
     */
    public BillingComponent enableLogging() {
        connector.enableLogging();
        return this;
    }

    /**
     * Adds a listener for purchase events.
     *
     * @param listener - The BillingEventListener to be added.
     * @return The {@link BillingComponent} instance for method chaining.
     */
    public BillingComponent addBillingEventListener(BillingEventListener listener) {
        if (listener == null) {
            return this;
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        return this;
    }

    /**
     * Removes a purchase event listener.
     *
     * @param listener - The BillingEventListener to be removed.
     * @return The {@link BillingComponent} instance for method chaining.
     */
    public BillingComponent removeBillingEventListener(BillingEventListener listener) {
        if (listener == null) {
            return this;
        }
        listeners.remove(listener);
        return this;
    }

    /**
     * Subscribes to a subscription product.
     *
     * @param productId - The ID of the subscription product to subscribe to.
     */
    public final void subscribe(String productId) {
        subscribe(productId, 0);
    }

    /**
     * Subscribes to a subscription product with a selected offer index.
     *
     * @param productId          - The ID of the subscription product to subscribe to.
     * @param selectedOfferIndex - The index of the selected offer for the subscription.
     */
    public final void subscribe(String productId, int selectedOfferIndex) {
        connector.subscribe(activity, productId, selectedOfferIndex);
    }

    /**
     * Consumes a purchased product.
     *
     * @param purchaseInfo - The PurchaseInfo object representing the purchased product to be consumed.
     */
    public final void consumePurchase(PurchaseInfo purchaseInfo) {
        connector.consumePurchase(purchaseInfo);
    }

    /**
     * Acknowledges a purchased product.
     *
     * @param purchaseInfo - The PurchaseInfo object representing the purchased product to be acknowledged.
     */
    public final void acknowledgePurchase(PurchaseInfo purchaseInfo) {
        connector.acknowledgePurchase(purchaseInfo);
    }

    /**
     * Unsubscribes from a subscription product.
     *
     * @param productId - The ID of the subscription product to unsubscribe from.
     */
    public final void unsubscribe(Activity activity, String productId) {
        connector.unsubscribe(activity, productId);
    }

    /**
     * Checks if a product has been purchased.
     *
     * @param productInfo - The ProductInfo object representing the product to check.
     * @return A {@link PurchasedResult} indicating whether the product has been purchased.
     */
    public final PurchasedResult isPurchased(@NonNull ProductInfo productInfo) {
        return connector.isPurchased(productInfo);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Helper methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Adds a String data to a List.
     *
     * @param list - The List to which the data is added.
     * @param data - The String data to be added.
     */
    private void add(@NonNull List<String> list, String data) {
        list.add(data);
    }

    /**
     * Sets a List of String data.
     *
     * @param list - The List to be set.
     * @param data - The List of String data to set.
     */
    private void set(@NonNull List<String> list, List<String> data) {
        list.clear();
        list.addAll(data);
    }
}
