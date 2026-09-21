package dev.spoocy.adapter.gui.items;

import dev.spoocy.adapter.gui.click.Click;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.utils.TriConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface AwareItem extends Item {

    /**
     * Gets the {@link Gui} this item is part of
     *
     * @return the gui
     *
     * @throws IllegalStateException if the gui is not set
     */
    @NotNull
    Gui getGui();

    /*
     * Sets the gui this item is part of
     */
    @ApiStatus.Internal
    void setGui(@NotNull Gui gui);

    /**
     * Builder for {@link AwareItem}
     *
     * @param <G> the {@link Gui gui type}
     */
    interface Builder<G extends Gui> extends Item.Builder<Builder<G>> {

        @NotNull
        Builder<G> item(@NotNull AwareItemProvider<G> provider);

        @Override
        @NonNull
        default @NotNull Builder<G> item(@NotNull ItemProvider provider) {
            return item((gui, locale) -> provider.provide(locale));
        }

        @NotNull
        Builder<G> onClick(@NotNull TriConsumer<AwareItem, Click, G> action);

        @Override
        default @NotNull Builder<G> onClick(@NotNull BiConsumer<Item, Click> action) {
            return onClick((item, click, gui) -> action.accept(item, click));
        }

        @Override
        @NotNull AwareItem build();
    }

}
