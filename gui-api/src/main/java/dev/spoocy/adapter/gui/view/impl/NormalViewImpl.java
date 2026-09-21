package dev.spoocy.adapter.gui.view.impl;

import dev.spoocy.adapter.gui.exceptions.IncompatibleGuiException;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.view.NormalView;
import dev.spoocy.adapter.inventory.CustomInventory;
import dev.spoocy.adapter.message.LocalizedComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class NormalViewImpl extends SingleInventoryView implements NormalView {

    public NormalViewImpl(
            @NotNull Player viewer,
            @NotNull Locale locale,
            @NotNull LocalizedComponent title,
            boolean closeable,
            @NotNull Gui gui
    ) {
        super(viewer, locale, title, closeable, gui);

    }

    @Override
    public @Nullable Gui getDisplayedGui() {
        return super.gui;
    }

    @Override
    public void displayGui(@NotNull Gui gui) {
        this.setGui0(gui);
    }

    @Override
    protected void validateGui(@NotNull Gui gui) throws IncompatibleGuiException {
        if(gui.getWidth() != 9 || gui.getHeight() < 1 || gui.getHeight() > 6) {
            throw new IncompatibleGuiException("This View only accepts [1-6]x9 guis.");
        }
    }

    @Override
    protected CustomInventory createInventory(@NotNull Component title, @NotNull Gui gui) {
        return CustomInventory.chest(title, gui.getHeight());
    }

    @Override
    public String toString() {
        return "NormalView{viewer=" + super.viewer + '}';
    }

    public static class Builder extends AbstractBuilder<NormalView.Builder, NormalView> implements NormalView.Builder {

        private Gui gui;

        public Builder() {

        }

        @Override
        public NormalView.Builder gui(@NotNull Gui gui) {
            this.gui = gui;
            return this;
        }

        @Override
        protected NormalView.Builder instance() {
            return this;
        }

        @Override
        protected NormalView createGuiView(@NotNull Player viewer) {
            return new NormalViewImpl(viewer, this.locale, this.title, this.closeable, this.gui);
        }
    }
}
