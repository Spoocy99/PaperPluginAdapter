package dev.spoocy.adapter.gui.items.builder;

import dev.spoocy.adapter.gui.click.Click;
import dev.spoocy.adapter.gui.icon.Icon;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.items.types.GuiButton;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.adapter.sound.PSound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Control Button to be put into the gui.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public class BasicButton extends GuiButton {

    public static GuiItemBuilder.ButtonBuilder builder() {
        return new Builder();
    }

    @Nullable
    private final PSound sound, errorSound;
    private final Icon icon;
    private final Function<Click, Boolean> action;

    public BasicButton(
            @NotNull Icon icon,
            @Nullable PSound sound,
            @Nullable PSound errorSound,
            @NotNull Function<Click, Boolean> runnable
    ) {
        this.icon = icon;
        this.sound = sound;
        this.errorSound = errorSound;
        this.action = runnable;
    }

    @Override
    public ItemStack getItemStack(@NotNull Localization locale) {
        return this.icon.decode(locale);
    }

    @Override
    public void executeClick(@NotNull Click click) {
        boolean action = this.action.apply(click);

        if(action && this.sound != null) {
            this.sound.play(click.getPlayer());

        } else if(!action && this.errorSound != null) {
            this.errorSound.play(click.getPlayer());
        }
    }

    @Override
    public String toString() {
        return "BasicButton{" +
                "action=" + action +
                ", sound=" + sound +
                ", errorSound=" + errorSound +
                ", icon=" + icon +
                '}';
    }

    private static class Builder implements GuiItemBuilder.ButtonBuilder {

        private Icon icon;
        private PSound sound, errorSound;
        private Function<Click, Boolean> action;

        public Builder() {
            super();
            this.icon = Icon.EMPTY;
            this.action = click -> true;
        }

        @Override
        public ButtonBuilder icon(@NotNull Icon icon) {
            this.icon = icon;
            return this;
        }

        @Override
        public ButtonBuilder sound(@NotNull PSound sound) {
            this.sound = sound;
            return this;
        }

        @Override
        public ButtonBuilder errorSound(@NotNull PSound sound) {
            this.errorSound = sound;
            return this;
        }

        @Override
        public ButtonBuilder run(@NotNull Function<Click, Boolean> runnable) {
            this.action = runnable;
            return this;
        }

        @Override
        public Item build() {
            return new BasicButton(
                    this.icon,
                    this.sound,
                    this.errorSound,
                    this.action
            );
        }
    }

}
