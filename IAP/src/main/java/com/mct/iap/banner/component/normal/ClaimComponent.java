package com.mct.iap.banner.component.normal;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ProductDetails;
import com.mct.iap.banner.IapBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * ClaimComponent - A component for enabling users to claim an offer within an IapBanner.
 * <p>
 * The ClaimComponent class allows you to create a clickable component within an IapBanner that
 * triggers the claiming of an offer when clicked. You can set an `OnClaimListener` to handle the
 * claim action.
 * <p>
 * Usage example:
 * <code>
 * <pre>
 *
 * // Create a ClaimComponent with the associated view ID
 * ClaimComponent<?> claimComponent = new ClaimComponent<>(R.id.claim_component_id)
 *         .setOfferToken(token)
 *         .setProductDetails(productDetails)
 *         .setOnClaimListener((view, billingFlowParams) -> {
 *             // Handle the claim action here
 *             if (billingFlowParams != null) {
 *                 // Start the billing flow to claim the offer
 *                 BillingClient billingClient = BillingClient.newBuilder(context).build();
 *                 billingClient.launchBillingFlow(activity, billingFlowParams);
 *             }
 *         })
 * </pre>
 * </code>
 */
public class ClaimComponent<C extends ClaimComponent<C>> extends Component<C> {

    private ClaimListener claimListener;
    private String offerToken;
    private ProductDetails productDetails;


    /**
     * {@inheritDoc}
     */
    public ClaimComponent(int id) {
        super(id);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void init(@NonNull IapBanner banner, View root) {
        super.init(banner, root);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void release(@NonNull IapBanner banner, View root) {
        super.release(banner, root);
    }


    /**
     * Setup an OnClickListener for the ClaimComponent's view, triggering the claim action.
     *
     * @param banner The IapBanner instance to which the component belongs.
     * @param root   The root View of the IapBanner layout.
     */
    @Override
    protected void setupOnClickListener(@NonNull IapBanner banner, View root) {
        if (view != null && (clickListener != null || claimListener != null)) {
            view.setOnClickListener(v -> {
                if (claimListener != null) {
                    claimListener.onClaim(v, createBillingFlowParams());
                }
                if (clickListener != null) {
                    clickListener.onClick(v);
                }
            });
        }
    }

    /**
     * Set an `OnClaimListener` to handle the claim action when the component is clicked.
     *
     * @param claimListener The listener to handle the claim action.
     * @return The ClaimComponent instance to enable method chaining.
     */
    @SuppressWarnings("unchecked")
    public C setOnClaimListener(@Nullable ClaimListener claimListener) {
        this.claimListener = claimListener;
        return (C) this;
    }

    /**
     * Set the offer token required for claiming an offer.
     *
     * @param offerToken The offer token associated with the offer.
     * @return The ClaimComponent instance to enable method chaining.
     * @see ProductDetails.SubscriptionOfferDetails#getOfferToken()
     * @see ClaimComponent#createBillingFlowParams()
     */
    @SuppressWarnings("unchecked")
    public C setOfferToken(String offerToken) {
        this.offerToken = offerToken;
        return (C) this;
    }

    /**
     * Set the product details for the offer.
     *
     * @param productDetails The product details of the offer.
     * @return The ClaimComponent instance to enable method chaining.
     * @see ClaimComponent#createBillingFlowParams()
     */
    @SuppressWarnings("unchecked")
    public C setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
        return (C) this;
    }

    /**
     * Create the BillingFlowParams required for initiating the claim process.
     *
     * @return The BillingFlowParams for claiming the offer, or null if the required parameters
     * are not set.
     */
    @Nullable
    private BillingFlowParams createBillingFlowParams() {
        if (productDetails == null || offerToken == null) {
            return null;
        }
        List<BillingFlowParams.ProductDetailsParams> productList = new ArrayList<>();
        productList.add(BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(offerToken)
                .build());
        return BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productList)
                .build();
    }

    /**
     * Interface for handling the claim action when the component is clicked.
     */
    public interface ClaimListener {
        /**
         * Called when the claim action is triggered.
         *
         * @param view   The view that was clicked.
         * @param params The BillingFlowParams required for claiming the offer.
         */
        void onClaim(@NonNull View view, @Nullable BillingFlowParams params);
    }

}
