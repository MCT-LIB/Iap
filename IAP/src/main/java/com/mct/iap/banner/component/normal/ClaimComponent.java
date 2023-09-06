package com.mct.iap.banner.component.normal;

import android.view.View;

import androidx.annotation.NonNull;

import com.mct.iap.banner.IapBanner;
import com.mct.iap.banner.component.billing.BillingComponent;

/**
 * ClaimComponent - A component for enabling users to claim an offer within an IapBanner.
 * <p>
 * The {@link ClaimComponent} class is designed to create a clickable component within an `IapBanner`
 * that allows users to claim offers associated with specific products. It provides a convenient
 * way to handle offer claiming interactions within your app's user interface.
 * <p>
 * Usage example:
 * <code>
 * <pre>
 * // Create a ClaimComponent instance with a unique ID.
 * ClaimComponent claimComponent = new ClaimComponent(R.id.claim_component)
 *     .setProductId("your_product_id")
 *     .setSelectedOfferIndex(0);
 * </pre>
 * </code>
 *
 * @param <C> - The type of the {@link ClaimComponent} for method chaining.
 */
public class ClaimComponent<C extends ClaimComponent<C>> extends Component<C> {

    private String productId;
    private int selectedOfferIndex;

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
     * Sets up an `OnClickListener` for the {@link ClaimComponent} to handle the claim action.
     *
     * @param banner - The parent `IapBanner` to which the component belongs.
     * @param root   - The root View of the layout.
     */
    @Override
    protected void setupOnClickListener(@NonNull IapBanner banner, View root) {
        if (view != null) {
            view.setOnClickListener(v -> {
                BillingComponent component = banner.findComponentById(BillingComponent.ID);
                if (component != null) {
                    component.subscribe(productId, selectedOfferIndex);
                }
            });
        }
    }

    /**
     * Sets the product ID for which the offer will be claimed.
     *
     * @param productId - The product ID to set.
     * @return The {@link ClaimComponent} instance for method chaining.
     */
    @SuppressWarnings("unchecked")
    public C setProductId(String productId) {
        this.productId = productId;
        return (C) this;
    }

    /**
     * Sets the selected offer index to specify which offer to claim (for products with multiple offers).
     *
     * @param selectedOfferIndex - The selected offer index to set.
     * @return The {@link ClaimComponent} instance for method chaining.
     */
    @SuppressWarnings("unchecked")
    public C setSelectedOfferIndex(int selectedOfferIndex) {
        this.selectedOfferIndex = selectedOfferIndex;
        return (C) this;
    }
}
