package dev.spoocy.adapter.gui.items.builder;

import dev.spoocy.adapter.gui.click.Click;
import dev.spoocy.adapter.gui.icon.Icon;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.adapter.sound.PSound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface GuiItemBuilder {

    Item build();

    interface BasicBuilder extends GuiItemBuilder {

        BasicBuilder allowInteraction(@NotNull InventoryAction... actions);

        BasicBuilder allowInteraction(@NotNull ClickType... clickTypes);

        BasicBuilder item(@NotNull Function<Localization, ItemStack> itemStack);

        BasicBuilder run(@NotNull Consumer<Click> runnable);

    }

    interface ButtonBuilder extends GuiItemBuilder {

        ButtonBuilder icon(@NotNull Icon icon);

        ButtonBuilder sound(@NotNull PSound sound);

        ButtonBuilder errorSound(@NotNull PSound sound);

        ButtonBuilder run(@NotNull Function<Click, Boolean> runnable);

    }

    interface ControlButtonBuilder<G extends Gui> extends GuiItemBuilder {

        ControlButtonBuilder<G> icon(@NotNull Function<G, Icon> provider);

        ControlButtonBuilder<G> sound(@NotNull PSound sound);

        ControlButtonBuilder<G> errorSound(@NotNull PSound sound);

        ControlButtonBuilder<G> run(@NotNull BiFunction<G, Click, Boolean> runnable);

    }


}
