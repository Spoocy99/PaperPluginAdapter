package dev.spoocy.adapter.gui.view.impl;

import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.view.GuiView;
import dev.spoocy.adapter.gui.view.TopInventoryView;
import dev.spoocy.adapter.inventory.CustomInventory;
import dev.spoocy.adapter.messages.Localization;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class DropperViewImpl extends TopInventoryView implements GuiView.DropperView {

    public DropperViewImpl(@NotNull Player viewer, @NotNull Localization locale, @NotNull Function<Localization, Component> title, boolean closeable, @NotNull Gui gui) {
        super(viewer, locale, title, closeable, gui);
        if(gui.getWidth() > 3 || gui.getHeight() > 3) {
            throw new IllegalArgumentException("DropperView can only support a maximum size of 3x3");
        }
    }

    @Override
    protected int getWindowWidth() {
        return 3;
    }

    @Override
    protected CustomInventory createInventory(@NotNull Component title) {
        return CustomInventory.dropper(title);
    }

    @Override
    public String toString() {
        return "DropperViewImpl{" +
                ", title=" + this.title +
                ", viewer=" + this.viewer +
                ", closeable=" + this.closeable +
                ", exitView=" + this.exitView +
                ", inventory=" + this.inventory +
                '}';
    }

    public static class Builder extends TopInventoryView.Builder<DropperBuilder, DropperView> implements GuiView.DropperBuilder{


        @Override
        protected DropperBuilder instance() {
            return this;
        }

        @Override
        protected DropperView createGuiView(@NotNull Player viewer) {
            return new DropperViewImpl(viewer, this.locale, this.title, this.closeable, this.gui);
        }
    }


}
