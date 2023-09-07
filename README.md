[![](https://jitpack.io/v/MCT-LIB/Iap.svg)](https://jitpack.io/#MCT-LIB/Iap)

# IAP Banner Library

The **IAP Banner Library** is a versatile and user-friendly library for quickly setting up in-app purchase (IAP) banners in your Android applications. It simplifies the process of creating IAP banners with a variety of components to enhance your app's user experience. This library offers components like `TextComponent`, `TimeComponent`, `LazyTextComponent`, `ClaimComponent`, `DismissComponent`, `StatusBarComponent`, `NavigationBarComponent` and helper dialog `IapBannerDialog`.

## # Installation

To integrate the IAP Banner Library into your Android project, simply add the library as a dependency in your app's build.gradle file:

<pre>
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.MCT-LIB:Iap:$TAG'
}
</pre>

## I. Banner
### I.I Component

This library includes the following utility classes:

- `TextComponent`: A class that facilitates text-related operations, including text formatting, styling, and highlighting.

- `TimeComponent`: An extension of `TextComponent` specifically designed for displaying countdown timers.

- `LazyTextComponent`: An extension of `TextComponent` specifically designed for product price when it loaded.

- `ClaimComponent`: A class for managing payments and transactions within your app.

- `DismissComponent`: Allows you to dismiss banner.

- `SystemBarComponent`, `StatusBarComponent`, and `NavigationBarComponent`: Classes that enable customization of the Android system status bar and navigation bar on Android devices.

- `Countdown`: A utility class for managing and displaying countdown timers in your app.

With these utility classes, you can optimize your Android app development process and minimize repetitive work.

### I.II Usage

Each utility class in the library comes with descriptions, usage examples, and specific integration instructions. Refer to each section for descriptions and examples of how to integrate them into your project.

Usage example:

<pre>
ProductConfiguration monthConfiguration = ProductConfiguration.of(SUB_MONTH).build();
ProductConfiguration yearConfiguration = ProductConfiguration.of(SUB_YEAR)
        .withDiscountPercent(80)
        .withOfferIndex(0)
        .build();

new IapBannerBuilder(context, R.layout.your_layout)
        .bindView(R.id.view).and()
        .bindView(R.id.view1, v -> v.setClickListener(listener))
        .bindViewGroup(R.id.view_group, vg -> {
            vg.bindView(id);
            vg.bindText(id);
        })
        .bindText(R.id.text, t -> t.text("").highlightText("").bold().strikeThru().underline().highlight(color))
        .setCountDownTime(60 * 60 * 1000)
        .bindTime(R.id.hour, t -> t.hour().bold().underline())
        .bindTime(R.id.minute, t -> t.minute())
        .bindTime(R.id.second, t -> t.second())
        .bindTime(R.id.millisecond, t -> t.millisecond())
        .bindDismiss(R.id.close_btn).and()
        .bindBilling(activity, billing -> billing
                .addConsumable(consumableConfigurations)
                .addNonConsumable(nonConsumableConfigurations)
                .addSubscription(subscriptionConfigurations)
                .addSubscription(monthConfiguration)
                .addSubscription(yearConfiguration)
                .autoConsume()
                .autoAcknowledge()
                .addBillingEventListener(new BillingEventListeners() {
                    // override func here
                }))
        .bindClaim(R.id.claim_month_btn, monthConfiguration).and()
        .bindClaim(R.id.claim_year_btn, yearConfiguration).and()
        .bindLazyText(R.id.text_lazy, monthConfiguration, lzt -> lzt
                .bold()
                .strikeThru()
                .lazyText((productInfo, productPriceInfo) -> {
                    String realPrice = productPriceInfo.getRealPrice();
                    String fakePrice = productPriceInfo.getFakePrice();
                    Pair&lt;BillingPeriod, String&gt; averagePrice = productPriceInfo.getAveragePrice();
                    return realPrice;
                })
                .lazyHighlightText((productInfo, productPriceInfo) -> {
                    String realPrice = productPriceInfo.getRealPrice();
                    String fakePrice = productPriceInfo.getFakePrice();
                    Pair&lt;BillingPeriod, String&gt; averagePrice = productPriceInfo.getAveragePrice();
                    return realPrice;
                }))
        .bindStatusBar(status -> status
                .background(Color.TRANSPARENT)
                .lightAppearance()
                .show())
        .bindNavigationBar(nav -> nav
                .background(Color.TRANSPARENT)
                .darkAppearance())
        .show( /*fullScreen*/ true);
</pre>

### I.III Component Inheritance

- Create a custom your Component
  
<pre>
public class CustomComponent extends BaseComponentAdapter {

    @Override
    public void init(@NonNull IapBanner banner, View root) {
        super.init(banner, root);
    }

    @Override
    public void release(@NonNull IapBanner banner, View root) {
        super.release(banner, root);
    }
    
    // custom your logic,...
}
</pre>
  
- Usage CustomComponent?
<pre>
new IapBannerBuilder(context, R.layout.your_layout)
      // ...
      .bindComponent(R.id.component_id, new CustomComponent())
      // ...
      .show(true);
</pre>

## II. Billing helper
- You can use `BillingConnector` to handle billing, scenarios that we don't have yet.
- Or refer at https://github.com/moisoni97/google-inapp-billing

# License
This library is distributed under the MIT License. See the LICENSE file for more details.
# Contributing
Feel free to contribute to this repository by creating issues or pull requests. If you have any suggestions, improvements, or bug reports, please let us know!
