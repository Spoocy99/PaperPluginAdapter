package dev.spoocy.adapter.gui.items.types;

import dev.spoocy.adapter.gui.types.Gui;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class GuiControlItem<G extends Gui> extends GuiButton {

    private G gui;

    public void setGui(@NotNull G gui) {
        if (this.gui != null) {
            throw new IllegalStateException("Tried to overwrite gui of " + this.getClass().getSimpleName() + " item, with gui already linked.");
        }
        this.gui = gui;
    }

    @NotNull
    public G getGui() {
        if (this.gui == null) {
            throw new IllegalStateException("Tried to retrieve GUI before available in any.");
        }
        return gui;
    }
}
