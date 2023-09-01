package com.mct.iap.banner;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.mct.iap.banner.component.BaseComponent;
import com.mct.iap.banner.component.BaseComponentAdapter;
import com.mct.iap.banner.component.normal.ClaimComponent;
import com.mct.iap.banner.component.normal.Component;
import com.mct.iap.banner.component.normal.CompositeComponent;
import com.mct.iap.banner.component.normal.DismissComponent;
import com.mct.iap.banner.component.normal.TextComponent;
import com.mct.iap.banner.component.normal.TimeComponent;
import com.mct.iap.banner.component.system.NavigationBarComponent;
import com.mct.iap.banner.component.system.StatusBarComponent;
import com.mct.iap.banner.countdown.Countdown;
import com.mct.iap.banner.listener.OnBannerDismissListener;
import com.mct.iap.banner.listener.OnBannerShowListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Class builder for {@link IapBanner}<br/>
 * This class provides a convenient way to create and configure {@link IapBanner} instances
 * with different components such as TextComponent, TimeComponent, DismissComponent, ClaimComponent,
 * StatusBarComponent, and NavigationBarComponent.
 * <p>
 * Usage example:
 * <code>
 * <pre>
 *     new IapBannerBuilder(this, R.layout.your_layout)
 *          .bindViewGroup(R.id.card_year, cc -> cc
 *                  .bindView(R.id.card_radio_button, Customizer.withDefaults())
 *                  .bindText(R.id.card_title, tv -> tv.text("Yearly"))
 *                  .bindText(R.id.card_price, tv -> tv.text(priceYear).underline().highlight(Color.RED))
 *                  .setClickListener(v -> {}))
 *          .bindText(R.id.tv_money_real, component -> component
 *                  .text(realMoney)
 *                  .bold())
 *          .bindText(R.id.tv_money_fake, component -> component
 *                  .text(fakeMoney)
 *                  .strikeThru())
 *          .bindText(R.id.tv_money_avg).text(avgMoney).and()
 *          .bindTime(R.id.tv_hour, TimeComponent::hour)
 *          .bindTime(R.id.tv_minute, TimeComponent::minute)
 *          .bindTime(R.id.tv_second).second().highlight(Color.RED).and()
 *          .bindTime(R.id.tv_millisecond, component -> component
 *                  .millisecond()
 *                  .underline()
 *                  .bold())
 *          .bindDismiss(R.id.btn_close, Customizer.withDefaults())
 *          .bindClaim(R.id.btn_claim).setOnClaimListener((view, params) -> {})
 *          .bindStatusBar(bar-> bar
 *                  .background(Color.parseColor("#000000"))
 *                  .lightAppearance())
 *          .bindNavigationBar(SystemBarComponent::hide)
 *          .setCountDown(3 * 60 * 60 * 1000)
 *          .setOnBannerShowListener((banner, dialog) -> {})
 *          .setOnBannerDismissListener((banner, dialog) -> {})
 *          .show(true);
 * </pre>
 * </code>
 */
public class IapBannerBuilder {

    final Context context;
    final int layout;
    final Map<Integer, BaseComponent> components;
    final Countdown countDown = Countdown.newInstance();
    OnBannerShowListener onBannerShowListener;
    OnBannerDismissListener onBannerDismissListener;

    /**
     * Constructor for IapBannerBuilder.
     *
     * @param context The Android application context.
     * @param layout  The layout resource ID for the banner.
     */
    public IapBannerBuilder(@NonNull Context context, @LayoutRes int layout) {
        this.context = context;
        this.layout = layout;
        this.components = new HashMap<>();
        this.bindStatusBar();
        this.bindNavigationBar();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Component with Customizer
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Bind a Composite component and apply customizations using a Customizer.
     *
     * @param id         The resource ID of the ViewGroup.
     * @param customizer The Customizer for configuring the component.
     * @return The builder instance for method chaining.
     */
    public IapBannerBuilder bindViewGroup(@IdRes int id, @NonNull Customizer<CompositeComponent<?>> customizer) {
        customizer.customize(bindViewGroup(id));
        return this;
    }

    /**
     * Bind a View component and apply customizations using a Customizer.
     *
     * @param id         The resource ID of the View.
     * @param customizer The Customizer for configuring the component.
     * @return The builder instance for method chaining.
     */
    public IapBannerBuilder bindView(@IdRes int id, @NonNull Customizer<Component<?>> customizer) {
        customizer.customize(bindView(id));
        return this;
    }

    /**
     * Bind a Text component and apply customizations using a Customizer.
     *
     * @param id         The resource ID of the TextView.
     * @param customizer The Customizer for configuring the component.
     * @return The builder instance for method chaining.
     */
    public IapBannerBuilder bindText(@IdRes int id, @NonNull Customizer<TextComponent<?>> customizer) {
        customizer.customize(bindText(id));
        return this;
    }

    /**
     * Bind a Time component and apply customizations using a Customizer.
     *
     * @param id         The resource ID of the TextView.
     * @param customizer The Customizer for configuring the component.
     * @return The builder instance for method chaining.
     */
    public IapBannerBuilder bindTime(@IdRes int id, @NonNull Customizer<TimeComponent<?>> customizer) {
        customizer.customize(bindTime(id));
        return this;
    }

    /**
     * Bind a Dismiss component and apply customizations using a Customizer.
     *
     * @param id         The resource ID of the View.
     * @param customizer The Customizer for configuring the component.
     * @return The builder instance for method chaining.
     */
    public IapBannerBuilder bindDismiss(@IdRes int id, @NonNull Customizer<DismissComponent<?>> customizer) {
        customizer.customize(bindDismiss(id));
        return this;
    }

    /**
     * Bind a Claim component and apply customizations using a Customizer.
     *
     * @param id         The resource ID of the View.
     * @param customizer The Customizer for configuring the component.
     * @return The builder instance for method chaining.
     */
    public IapBannerBuilder bindClaim(@IdRes int id, @NonNull Customizer<ClaimComponent<?>> customizer) {
        customizer.customize(bindClaim(id));
        return this;
    }

    /**
     * Bind a StatusBar component and apply customizations using a Customizer.
     *
     * @param customizer The Customizer for configuring the component.
     * @return The builder instance for method chaining.
     */
    public IapBannerBuilder bindStatusBar(@NonNull Customizer<StatusBarComponent> customizer) {
        customizer.customize(bindStatusBar());
        return this;
    }

    /**
     * Bind a NavigationBar component and apply customizations using a Customizer.
     *
     * @param customizer The Customizer for configuring the component.
     * @return The builder instance for method chaining.
     */
    public IapBannerBuilder bindNavigationBar(@NonNull Customizer<NavigationBarComponent> customizer) {
        customizer.customize(bindNavigationBar());
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Component without Customizer
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Bind a Composite component.
     *
     * @param id The resource ID of the ViewGroup.
     * @return The builder instance for method chaining.
     */
    public CompositeComponent<?> bindViewGroup(@IdRes int id) {
        return getOrCreate(id, new CompositeComponent<>(id));
    }

    /**
     * Bind a View component.
     *
     * @param id The resource ID of the View.
     * @return The builder instance for method chaining.
     */
    public Component<?> bindView(@IdRes int id) {
        return getOrCreate(id, new Component<>(id));
    }

    /**
     * Bind a Text component.
     *
     * @param id The resource ID of the TextView.
     * @return The builder instance for method chaining.
     */
    public TextComponent<?> bindText(@IdRes int id) {
        return getOrCreate(id, new TextComponent<>(id));
    }

    /**
     * Bind a Time component.
     *
     * @param id The resource ID of the TextView.
     * @return The builder instance for method chaining.
     */
    public TimeComponent<?> bindTime(@IdRes int id) {
        return getOrCreate(id, new TimeComponent<>(id));
    }

    /**
     * Bind a Dismiss component.
     *
     * @param id The resource ID of the View.
     * @return The builder instance for method chaining.
     */
    public DismissComponent<?> bindDismiss(@IdRes int id) {
        return getOrCreate(id, new DismissComponent<>(id));
    }

    /**
     * Bind a Claim component.
     *
     * @param id The resource ID of the View.
     * @return The builder instance for method chaining.
     */
    public ClaimComponent<?> bindClaim(@IdRes int id) {
        return getOrCreate(id, new ClaimComponent<>(id));
    }

    /**
     * Bind a StatusBar component.
     *
     * @return The builder instance for method chaining.
     */
    public StatusBarComponent bindStatusBar() {
        return getOrCreate(StatusBarComponent.ID, new StatusBarComponent());
    }

    /**
     * Bind a NavigationBar component.
     *
     * @return The builder instance for method chaining.
     */
    public NavigationBarComponent bindNavigationBar() {
        return getOrCreate(NavigationBarComponent.ID, new NavigationBarComponent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other component customization methods...
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Set the countdown timer for the banner.
     *
     * @param time The countdown time in milliseconds.
     * @return The builder instance for method chaining.
     */
    public IapBannerBuilder setCountDownTime(long time) {
        countDown.setTime(time);
        return this;
    }

    /**
     * Set an OnBannerShowListener for the banner.
     *
     * @param listener The listener to be invoked when the banner is shown.
     * @return The builder instance for method chaining.
     */
    public IapBannerBuilder setOnBannerShowListener(OnBannerShowListener listener) {
        this.onBannerShowListener = listener;
        return this;
    }

    /**
     * Set an OnBannerDismissListener for the banner.
     *
     * @param listener The listener to be invoked when the banner is dismissed.
     * @return The builder instance for method chaining.
     */
    public IapBannerBuilder setOnBannerDismissListener(OnBannerDismissListener listener) {
        this.onBannerDismissListener = listener;
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Banner creation and display methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Build an instance of IapBanner with the configured components.
     *
     * @return An instance of IapBanner.
     */
    public IapBanner build() {
        return new IapBanner(this);
    }

    /**
     * Show the banner with the option to make it full-screen.
     *
     * @param fullScreen Set to true to display the banner as full-screen.
     */
    public void show(boolean fullScreen) {
        build().show(fullScreen);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private utility methods...
    ///////////////////////////////////////////////////////////////////////////

    @NonNull
    @SuppressWarnings("unchecked")
    private <O extends BaseComponent> O getOrCreate(int id, BaseComponentAdapter component) {
        if (components.containsKey(id)) {
            BaseComponent c = components.get(id);
            if (c != null && component.getClass().equals(c.getClass())) {
                return (O) c;
            }
        }
        component.setBuilder(this);
        components.put(id, component);
        return (O) component;
    }
}
