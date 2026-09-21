package dev.spoocy.adapter.gui.items.types;

import dev.spoocy.adapter.gui.items.AwareItem;
import dev.spoocy.adapter.gui.types.Gui;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractAwareItem<G extends Gui> extends GuiItem implements AwareItem {

    private final Class<? extends Gui> guiClass;
    private Gui gui;

    public AbstractAwareItem(@NotNull Class<? extends Gui> guiClass) {
        this.guiClass = guiClass;
    }

    @NotNull
    public G getGui() {
        if (this.gui == null) {
            throw new IllegalStateException("Tried to retrieve GUI before available in any.");
        }
        return (G) gui;
    }

    public void setGui(@NotNull Gui gui) {
        if (this.gui != null) {
            throw new IllegalStateException("Tried to overwrite gui of " + this.getClass().getSimpleName() + " item, with gui already linked.");
        }

        if (!this.guiClass.isAssignableFrom(gui.getClass())) {
            throw new IllegalArgumentException("This item can only be bound to a " + guiClass.getSimpleName() + ".");
        }

        this.gui = gui;
    }
}
