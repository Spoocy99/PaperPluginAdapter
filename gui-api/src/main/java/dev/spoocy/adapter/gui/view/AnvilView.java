package dev.spoocy.adapter.gui.view;

import dev.spoocy.adapter.gui.types.Gui;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface AnvilView extends GuiView {

    @NotNull
    Gui getDisplayedGui();

    void displayGui(@NotNull Gui gui);

    void setRenameHandler(@NotNull RenameHandler handler);

    interface Builder extends GuiView.Builder<AnvilView.Builder, AnvilView> {

        Builder gui(@NotNull Gui gui);

        Builder renameHandler(@NotNull RenameHandler handler);

    }

    @FunctionalInterface
    interface RenameHandler {
        void onTyping(@NotNull String input);
    }

}
