[![](https://jitpack.io/v/MCT-LIB/Iap.svg)](https://jitpack.io/#MCT-LIB/Iap)

# IAP Banner Library

The **IAP Banner Library** is a versatile and user-friendly library for quickly setting up in-app purchase (IAP) banners in your Android applications. It simplifies the process of creating IAP banners with a variety of components to enhance your app's user experience. This library offers components like `TextComponent`, `TimeComponent`, `DismissComponent`, `ClaimComponent`, `StatusBarComponent`, `NavigationBarComponent` and helper dialog `IapBannerDialog`.

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

- `TimeComponent`: An extension of `TextComponent` specifically designed for displaying and managing countdown timers.

- `ClaimComponent`: A class for managing payments and transactions within your app.

- `DismissComponent`: Allows you to easily create buttons to dismiss and hide components within an `IapBanner`.

- `SystemBarComponent`, `StatusBarComponent`, and `NavigationBarComponent`: Classes that enable customization of the Android system status bar and navigation bar on Android devices.

- `Countdown`: A utility class for managing and displaying countdown timers in your app.

With these utility classes, you can optimize your Android app development process and minimize repetitive work.

### I.II Usage

Each utility class in the library comes with descriptions, usage examples, and specific integration instructions. Refer to each section for descriptions and examples of how to integrate them into your project.

Usage example:
<pre>
new IapBannerBuilder(this, R.layout.your_layout)
     .bindViewGroup(R.id.card_year, cc -> cc
             .bindView(R.id.card_radio_button, Customizer.withDefaults())
             .bindText(R.id.card_title, tv -> tv.text("Yearly"))
             .bindText(R.id.card_price, tv -> tv.text(priceYear).underline().highlight(Color.RED))
             .setClickListener(v -> {}))
     .bindText(R.id.tv_money_real, component -> component
             .text(realMoney)
             .bold())
     .bindText(R.id.tv_money_fake, component -> component
             .text(fakeMoney)
             .strikeThru())
     .bindText(R.id.tv_money_avg).text(avgMoney).and()
     .bindTime(R.id.tv_hour, TimeComponent::hour)
     .bindTime(R.id.tv_minute, TimeComponent::minute)
     .bindTime(R.id.tv_second).second().highlight(Color.RED).and()
     .bindTime(R.id.tv_millisecond, component -> component
             .millisecond()
             .underline()
             .bold())
     .bindDismiss(R.id.btn_close, Customizer.withDefaults())
     .bindClaim(R.id.btn_claim).setOnClaimListener((view, params) -> {})
     .bindStatusBar(bar-> bar
             .background(Color.parseColor("#000000"))
             .lightAppearance())
     .bindNavigationBar(SystemBarComponent::hide)
     .setCountDown(3 * 60 * 60 * 1000)
     .setOnBannerShowListener((banner, dialog) -> {})
     .setOnBannerDismissListener((banner, dialog) -> {})
     .show(true);
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

- updating...

# License
This library is distributed under the MIT License. See the LICENSE file for more details.
# Contributing
Feel free to contribute to this repository by creating issues or pull requests. If you have any suggestions, improvements, or bug reports, please let us know!
