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

public class NormalViewImpl extends TopInventoryView implements GuiView.NormalView {

    public NormalViewImpl(
            @NotNull Player viewer,
            @NotNull Localization locale,
            @NotNull Function<Localization, Component> title,
            boolean closeable,
            @NotNull Gui gui
    ) {
        super(viewer, locale, title, closeable, gui);
    }

    @Override
    protected int getWindowWidth() {
        return 9;
    }

    @Override
    protected CustomInventory createInventory(@NotNull Component title) {
        return CustomInventory.chest(title, getDisplayedGui().getHeight());
    }

    @Override
    public String toString() {
        return "NormalViewImpl{" +
                ", title=" + this.title +
                ", viewer=" + this.viewer +
                ", closeable=" + this.closeable +
                ", exitView=" + this.exitView +
                ", inventory=" + this.inventory +
                '}';
    }

    public static class Builder extends TopInventoryView.Builder<NormalBuilder, NormalView> implements GuiView.NormalBuilder {

        @Override
        protected NormalBuilder instance() {
            return this;
        }

        @Override
        protected NormalView createGuiView(@NotNull Player viewer) {
            return new NormalViewImpl(viewer, this.locale, this.title, this.closeable, this.gui);
        }
    }
}
