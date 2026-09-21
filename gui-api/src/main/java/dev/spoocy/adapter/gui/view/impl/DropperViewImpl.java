package dev.spoocy.adapter.gui.view.impl;

import dev.spoocy.adapter.gui.exceptions.IncompatibleGuiException;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.view.DropperView;
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

public class DropperViewImpl extends SingleInventoryView implements DropperView {

    public DropperViewImpl(@NotNull Player viewer, @NotNull Locale locale, @NotNull LocalizedComponent title, boolean closeable, @NotNull Gui gui) {
        super(viewer, locale, title, closeable, gui);
    }

    @Override
    protected void validateGui(@NotNull Gui gui) throws IncompatibleGuiException {
        if(gui.getWidth() != 3 || gui.getHeight() != 3) {
            throw new IncompatibleGuiException("This View only accepts 3x3 guis.");
        }
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
    protected CustomInventory createInventory(@NotNull Component title, @NotNull Gui gui) {
        return CustomInventory.dropper(title);
    }

    @Override
    public String toString() {
        return "NormalView{viewer=" + super.viewer + '}';
    }

    public static class BuilderImpl extends AbstractBuilder<DropperView.Builder, DropperView> implements DropperView.Builder {

        protected Gui gui;

        @Override
        public DropperView.Builder gui(@NotNull Gui gui) {
            this.gui = gui;
            return this;
        }

        @Override
        protected BuilderImpl instance() {
            return this;
        }

        @Override
        protected DropperView createGuiView(@NotNull Player viewer) {
            return new DropperViewImpl(viewer, super.locale, super.title, super.closeable, this.gui);
        }
    }


}
