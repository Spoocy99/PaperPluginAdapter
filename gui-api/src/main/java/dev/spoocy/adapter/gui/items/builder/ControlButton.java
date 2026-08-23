package dev.spoocy.adapter.gui.items.builder;

import dev.spoocy.adapter.gui.click.Click;
import dev.spoocy.adapter.gui.icon.Icon;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.items.types.GuiControlItem;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.adapter.sound.PSound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.*;

/**
 * Basic Button to be put into the gui.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public class ControlButton<G extends Gui> extends GuiControlItem<G> {

    public static <T extends Gui> GuiItemBuilder.ControlButtonBuilder<T> builder() {
        return new Builder<>();
    }


    private final Function<G, Icon> item;
    private final PSound sound, errorSound;
    private final BiFunction<G, Click, Boolean> runnable;

    public ControlButton(
            @NotNull Function<G, Icon> icon,
            @Nullable PSound sound,
            @Nullable PSound errorSound,
            @NotNull BiFunction<G, Click, Boolean> action
    ) {
        this.item = icon;
        this.sound = sound;
        this.errorSound = errorSound;
        this.runnable = action;
    }

    @Override
    public ItemStack getItemStack(@NotNull Localization locale) {
        return this.item.apply(getGui()).decode(locale);
    }

    @Override
    public void executeClick(@NotNull Click click) {
        boolean action = this.runnable.apply(getGui(), click);

        if(action && this.sound != null) {
            this.sound.play(click.getPlayer());

        } else if(!action && this.errorSound != null) {
            this.errorSound.play(click.getPlayer());
        }
    }

    @Override
    public String toString() {
        return "ControlButton{" +
                "errorSound=" + errorSound +
                ", item=" + item +
                ", sound=" + sound +
                ", runnable=" + runnable +
                '}';
    }

    private static class Builder<G extends Gui> implements GuiItemBuilder.ControlButtonBuilder<G> {

        private Function<G, Icon> icon;
        private PSound sound, errorSound;
        private BiFunction<G, Click, Boolean> action;

        public Builder() {
            super();
            this.icon = g -> Icon.EMPTY;
        }

        @Override
        public ControlButtonBuilder<G> icon(@NotNull Function<G, Icon> provider) {
            this.icon = provider;
            return this;
        }

        @Override
        public ControlButtonBuilder<G> sound(@NotNull PSound sound) {
            this.sound = sound;
            return this;
        }

        @Override
        public ControlButtonBuilder<G> errorSound(@NotNull PSound sound) {
            this.errorSound = sound;
            return this;
        }

        @Override
        public ControlButtonBuilder<G> run(@NotNull BiFunction<G, Click, Boolean> action) {
            this.action = action;
            return this;
        }


        @Override
        public Item build() {
            return new ControlButton<>(
                    this.icon,
                    this.sound,
                    this.errorSound,
                    this.action
            );
        }
    }

}
