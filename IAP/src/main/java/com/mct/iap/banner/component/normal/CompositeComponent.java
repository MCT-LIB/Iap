package com.mct.iap.banner.component.normal;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mct.iap.banner.Customizer;
import com.mct.iap.banner.IapBanner;
import com.mct.iap.banner.component.BaseComponent;
import com.mct.iap.banner.component.BaseComponentAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * CompositeComponent - A component that can contain multiple sub-components within an IapBanner.
 * <p>
 * This class extends the Component class and allows the creation of composite components that can
 * contain multiple sub-components, such as buttons, text, or other custom components. Composite
 * components facilitate the organization and customization of multiple components as a single unit
 * within an IapBanner.
 */
public class CompositeComponent<C extends CompositeComponent<C>> extends Component<C> {

    // Map to store sub-components associated with this composite component
    private final Map<Integer, BaseComponent> components = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    public CompositeComponent(int id) {
        super(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(@NonNull IapBanner banner, View root) {
        super.init(banner, root);
        if (view != null) {
            for (Map.Entry<Integer, BaseComponent> component : components.entrySet()) {
                component.getValue().init(banner, view);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(@NonNull IapBanner banner, View root) {
        super.release(banner, root);
        if (view != null) {
            for (Map.Entry<Integer, BaseComponent> component : components.entrySet()) {
                component.getValue().release(banner, view);
            }
        }
    }

    /**
     * Get a reference to the sub-component's resource ID.
     *
     * @param id  The resource ID of the sub-component's view.
     * @param <T> The type of the sub-component's view.
     * @return The sub-component's view or null if not found.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends View> T findViewById(@IdRes int id) {
        if (view == null) {
            return null;
        }
        return (T) view.findViewById(id);
    }

    /**
     * Get a reference to a sub-component by its resource ID.
     *
     * @param id  The resource ID of the sub-component.
     * @param <T> The type of the sub-component.
     * @return The sub-component instance or null if not found.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends BaseComponent> T findComponentById(@IdRes int id) {
        return (T) components.get(id);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Component with Customizer
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Bind and customize a sub-component using a customizer.
     *
     * @param id         The resource ID of the sub-component's view.
     * @param customizer The customizer for the sub-component.
     * @return The composite component instance to enable method chaining.
     */
    @SuppressWarnings("unchecked")
    public C bindView(@IdRes int id, @NonNull Customizer<Component<?>> customizer) {
        customizer.customize(bindView(id));
        return (C) this;
    }

    /**
     * Bind and customize a TextComponent sub-component using a customizer.
     *
     * @param id         The resource ID of the sub-component's view.
     * @param customizer The customizer for the sub-component.
     * @return The composite component instance to enable method chaining.
     */
    @SuppressWarnings("unchecked")
    public C bindText(@IdRes int id, @NonNull Customizer<TextComponent<?>> customizer) {
        customizer.customize(bindText(id));
        return (C) this;
    }

    /**
     * Bind and customize a TimeComponent sub-component using a customizer.
     *
     * @param id         The resource ID of the sub-component's view.
     * @param customizer The customizer for the sub-component.
     * @return The composite component instance to enable method chaining.
     */
    @SuppressWarnings("unchecked")
    public C bindTime(@IdRes int id, @NonNull Customizer<TimeComponent<?>> customizer) {
        customizer.customize(bindTime(id));
        return (C) this;
    }

    /**
     * Bind and customize a DismissComponent sub-component using a customizer.
     *
     * @param id         The resource ID of the sub-component's view.
     * @param customizer The customizer for the sub-component.
     * @return The composite component instance to enable method chaining.
     */
    @SuppressWarnings("unchecked")
    public C bindDismiss(@IdRes int id, @NonNull Customizer<DismissComponent<?>> customizer) {
        customizer.customize(bindDismiss(id));
        return (C) this;
    }

    /**
     * Bind and customize a ClaimComponent sub-component using a customizer.
     *
     * @param id         The resource ID of the sub-component's view.
     * @param customizer The customizer for the sub-component.
     * @return The composite component instance to enable method chaining.
     */
    @SuppressWarnings("unchecked")
    public C bindClaim(@IdRes int id, @NonNull Customizer<ClaimComponent<?>> customizer) {
        customizer.customize(bindClaim(id));
        return (C) this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Component without Customizer
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Bind a sub-component using its resource ID.
     *
     * @param id The resource ID of the sub-component's view.
     * @return The sub-component instance.
     */
    public Component<?> bindView(@IdRes int id) {
        return getOrCreate(id, new Component<>(id));
    }

    /**
     * Bind a TextComponent sub-component using its resource ID.
     *
     * @param id The resource ID of the sub-component's view.
     * @return The TextComponent sub-component instance.
     */
    public TextComponent<?> bindText(@IdRes int id) {
        return getOrCreate(id, new TextComponent<>(id));
    }

    /**
     * Bind a TimeComponent sub-component using its resource ID.
     *
     * @param id The resource ID of the sub-component's view.
     * @return The TimeComponent sub-component instance.
     */
    public TimeComponent<?> bindTime(@IdRes int id) {
        return getOrCreate(id, new TimeComponent<>(id));
    }

    /**
     * Bind a DismissComponent sub-component using its resource ID.
     *
     * @param id The resource ID of the sub-component's view.
     * @return The DismissComponent sub-component instance.
     */
    public DismissComponent<?> bindDismiss(@IdRes int id) {
        return getOrCreate(id, new DismissComponent<>(id));
    }

    /**
     * Bind a ClaimComponent sub-component using its resource ID.
     *
     * @param id The resource ID of the sub-component's view.
     * @return The ClaimComponent sub-component instance.
     */
    public ClaimComponent<?> bindClaim(@IdRes int id) {
        return getOrCreate(id, new ClaimComponent<>(id));
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
        component.setBuilder(getBuilder());
        components.put(id, component);
        return (O) component;
    }
}
