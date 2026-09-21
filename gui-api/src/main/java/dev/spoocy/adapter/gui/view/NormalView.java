package dev.spoocy.adapter.gui.view;

import dev.spoocy.adapter.gui.types.Gui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface NormalView extends GuiView {

    @Nullable
    Gui getDisplayedGui();

    void displayGui(@NotNull Gui gui);


    interface Builder extends GuiView.Builder<Builder, NormalView> {

        Builder gui(@NotNull Gui gui);

    }

}
