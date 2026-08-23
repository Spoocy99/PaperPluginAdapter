package dev.spoocy.adapter.gui.view;

import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.Coordinate;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.types.Resetable;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.messages.Localization;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class TopInventoryView extends AbstractInventoryView implements GuiView.SingleGuiView {

    private Gui gui;

    public TopInventoryView(
            @NotNull Player viewer,
            @NotNull Localization locale,
            @NotNull Function<Localization, Component> title,
            boolean closeable,
            @NotNull Gui gui
    ) {
        super(viewer, locale, title, closeable);

        validateGui(gui);
        this.applyGui(gui);
        this.buildInventory();
        this.redraw();
    }

    @Override
    public @NotNull Gui getDisplayedGui() {
        return this.gui;
    }

    @Override
    public void setGui(@NotNull Gui gui) {
        if(this.gui == gui) return;
        validateGui(gui);

        if(this.gui != null) {
            this.gui.unsubscribe(this);
            this.gui.retrieveAllPresentItems().forEach(item -> item.unsubscribe(this));
        }

        applyGui(gui);

        // current gui is big enough?
        if(this.inventory.getSize() != gui.getHeight() * 9) {
            this.buildInventory();
        }

        this.redraw();
    }

    private void applyGui(@NotNull Gui gui) {
        //BukkitLogger.debug("Applying new GUI to TopInventoryView: {}", gui);
        this.gui = gui;
        this.gui.subscribe(this);
        this.gui.retrieveAllPresentItems().forEach(item -> item.subscribe(this));
    }

    private void validateGui(@NotNull Gui gui) {
        if(gui.getHeight() < 1 || gui.getHeight() > 6 || gui.getWidth() > 9 || gui.getWidth() < 1) {
            throw new IllegalArgumentException("Invalid GUI dimensions for Normal View: " + gui.getWidth() + "x" + gui.getHeight());
        }
    }

    @Override
    public void resetDisplayedGui() {
        if (this.gui instanceof Resetable) {
            ((Resetable) this.gui).reset();
        }
    }

    @Override
    public void redraw() {
        Item background = this.gui.getBackground();

        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            if (!isInDisplayedGui(slotToCoordinate(slot))) {
                this.inventory.setItem(slot, background != null ? background.getItemStack(this) : null);
            }
        }

        for (int y = 0; y < gui.getHeight(); y++) {
            for (int x = 0; x < gui.getWidth(); x++) {
                redraw(x, y);
            }
        }
    }

    @Override
    public void redraw(@NotNull Item item) {
        Coordinate[] c = gui.retrieveCoordinates(item);

        if(c.length == 0) {
            BukkitLogger.warn("Tried to redraw item that is not present in the current GUI: {}", item);
            return;
        }

        for (Coordinate coordinate : c) {
            int slot = coordinateToSlot(coordinate.getX(), coordinate.getY());
            this.inventory.setItem(slot, item.getItemStack(this));
        }
    }

    @Override
    public void redraw(int x, int y) {
        BukkitLogger.trace("Redrawing slot at X: {} Y: {}", x, y);
        Item item = gui.getItem(x, y);
        this.inventory.setItem(
                coordinateToSlot(x, y),
                item != null ? item.getItemStack(this) : null
        );
    }

    @Override
    protected void handleOpen(@NotNull InventoryOpenEvent event) { }

    @Override
    protected void handleClose(@NotNull InventoryCloseEvent event) { }

    @Override
    protected void handleClick(@NotNull InventoryClickEvent event) {
        Coordinate coords = slotToCoordinate(event.getSlot());
        BukkitLogger.trace("Clicked slot {} >> X: {} Y: {}", event.getSlot(), coords.getX(), coords.getY());

        if (!isInDisplayedGui(coords)) {
            event.setCancelled(true);
            return;
        }

        if (!this.gui.isInteractionAllowed(coords.getX(), coords.getY(), event.getClick(), event.getAction())) {
            event.setCancelled(true);
        }

        this.gui.handleClick(
                coords.getX(),
                coords.getY(),
                event,
                this

        );
    }

    // x and y at top-left corner (0,0)
    protected int coordinateToSlot(int x, int y) {
        int guiWidth = getDisplayedGui().getWidth();
        int displayedWidth = getWindowWidth();

        int offsetToSides = (displayedWidth - guiWidth) / 2;
        return (y * displayedWidth) + (x + offsetToSides);
    }

    // x and y at top-left corner (0,0)
    protected Coordinate slotToCoordinate(int slot) {
        int guiWidth = getDisplayedGui().getWidth();
        int displayedWidth = getWindowWidth();

        int offsetToSides = (displayedWidth - guiWidth) / 2;
        int x = (slot % displayedWidth) - offsetToSides;
        int y = slot / displayedWidth;
        return Coordinate.of(x, y);
    }

    protected boolean isInDisplayedGui(@NotNull Coordinate coordinate) {
        return coordinate.getX() >= 0
                && coordinate.getX() < this.gui.getWidth()
                && coordinate.getY() >= 0
                && coordinate.getY() < this.gui.getHeight();
    }

    protected abstract int getWindowWidth();

    public abstract static class Builder<B extends GuiView.SingleGuiBuilder<B, G>, G extends SingleGuiView> extends AbstractGuiView.Builder<B, G> implements SingleGuiBuilder<B, G> {

        protected Gui gui;

        @Override
        public B gui(@NotNull Gui gui) {
            this.gui = gui;
            return instance();
        }

        @Override
        protected void validate() {
            Objects.requireNonNull(this.gui, "GUI cannot be null");
            super.validate();
        }

    }

}
