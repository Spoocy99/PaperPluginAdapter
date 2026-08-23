package dev.spoocy.adapter.gui.implementations;

import dev.spoocy.adapter.gui.click.Click;
import dev.spoocy.adapter.gui.click.ClickImpl;
import dev.spoocy.adapter.gui.items.PredicateItem;
import dev.spoocy.adapter.gui.items.types.GuiControlItem;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.Coordinate;
import dev.spoocy.adapter.gui.layout.builder.CharLayout;
import dev.spoocy.adapter.gui.layout.slot.SlotMarker;
import dev.spoocy.adapter.gui.layout.builder.Layout;
import dev.spoocy.adapter.gui.layout.slot.Slot;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.view.GuiView;
import dev.spoocy.adapter.log.BukkitLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class MatrixGui extends AbstractGui {

    protected final Slot[][] slots;
    protected Item backgroundItem;

    protected MatrixGui(int width, int height) {
        super(width, height);

        this.slots = new Slot[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.slots[x][y] = Slot.create(this, x, y);
            }
        }

        this.backgroundItem = null;
        this.frozen = false;
    }

    protected @NotNull Slot getSlot(int x, int y) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds for " + this.width + "x" + this.height + ": (" + x + ", " + y + ")");
        }
        return this.slots[x][y];
    }

    protected @NotNull List<Slot> getSlots(@NotNull Slot.Type type) {
        List<Slot> matchedSlots = new ArrayList<>();

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {

                Slot slot = this.getSlot(x, y);
                if (slot.getType() == type) {
                    matchedSlots.add(slot);
                }

            }
        }
        return matchedSlots;
    }

    @Override
    public boolean containsItem(@NotNull Item item) {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                Item currentItem = this.getItem(x, y);
                if (Objects.equals(currentItem, item)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Item getBackground() {
        return this.backgroundItem;
    }

    @Override
    public @Nullable Item getItem(int x, int y) {
        Slot slot = this.getSlot(x, y);
        return slot.getItem();
    }

    @Override
    public void setItem(int x, int y, @Nullable Item item) {

        Slot slot = this.getSlot(x, y);

        if(item == null) {
            slot.background(this.backgroundItem);
            return;
        }

        slot.handle(item);
    }

    @Override
    public void removeItem(int x, int y) {
        getSlot(x, y).background(this.backgroundItem);
    }

    @Override
    public void applyLayout(@NotNull final Layout layout) {
        if(layout.getWidth() != this.width || layout.getHeight() != this.height) {
            throw new IllegalArgumentException("Layout width and height must match the Gui dimensions.");
        }
        this.applyLayout0(layout);
    }
    protected abstract void applyLayout0(@NotNull final Layout layout);

    @Override
    public void fill(@NotNull Item item, boolean replaceExisting) {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (replaceExisting || this.getItem(x, y) == null) {
                    this.setItem(x, y, item);
                }
            }
        }
    }

    @Override
    public void fillRow(int row, @NotNull Item item, boolean replaceExisting) {
        for (int x = 0; x < this.width; x++) {
            if (replaceExisting || this.getItem(x, row) == null) {
                this.setItem(x, row, item);
            }
        }
    }

    @Override
    public void fillColumn(int column, @NotNull Item item, boolean replaceExisting) {
        for (int y = 0; y < this.height; y++) {
            if (replaceExisting || this.getItem(column, y) == null) {
                this.setItem(column, y, item);
            }
        }
    }

    @Override
    public void fillRectangle(int x, int y, int width, int height, @NotNull Item item, boolean replaceExisting) {
        for (int i = x; i < x + width && i < this.width; i++) {
            for (int j = y; j < y + height && j < this.height; j++) {
                if (replaceExisting || this.getItem(i, j) == null) {
                    this.setItem(i, j, item);
                }
            }
        }
    }

    @Override
    public void setBackground(@Nullable Item backgroundItem) {
        BukkitLogger.trace("Setting background item: {}", backgroundItem);
        this.backgroundItem = backgroundItem;
        updateBackground();
    }

    protected void updateBackground() {
        List<Slot> backgroundSlots = this.getSlots(Slot.Type.BACKGROUND);
        BukkitLogger.trace("Updating background for {} slots.", backgroundSlots.size());
        for (Slot s : backgroundSlots) {
            s.background(this.backgroundItem);
        }
    }

    @Override
    public boolean isInteractionAllowed(int x, int y, @NotNull ClickType clickType, @NotNull InventoryAction action) {
        if(this.isAnimationPlaying()) return false;
        Slot slot = this.getSlot(x, y);
        return slot.isAllowed(clickType) && slot.isAllowed(action);
    }

    @Override
    public boolean handleClick(int x, int y, @NotNull ClickType type, @NotNull InventoryAction action, @NotNull Player player, @Nullable GuiView executor) {

        if(this.frozen || this.isAnimationPlaying()) {
            BukkitLogger.trace("Gui is frozen or animation is playing, ignoring click.");
            return false;
        }

        Item item = this.getItem(x, y);
        if (item == null) {
            BukkitLogger.trace("No item present at clicked slot, ignoring click.");
            return false;
        }

        Click click = new ClickImpl(this, executor, x, y, type, action, player, item);

        if(item instanceof PredicateItem) {
            if(!((PredicateItem) item).test(click)) {
                BukkitLogger.trace("PredicateItem test failed for player {}, ignoring click.", player.getName());
                ((PredicateItem) item).onTestFailed(click);
                return false;
            }
        }

        BukkitLogger.trace("Handling click on item " + item);
        item.executeClick(click);
        return true;
    }

    @Override
    public Coordinate[] retrieveCoordinates(@NotNull Item item) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                Slot slot = this.getSlot(x, y);

                if (Objects.equals(slot.getItem(), item)) {
                    coordinates.add(Coordinate.of(x, y));
                }

            }
        }
        return coordinates.toArray(Coordinate[]::new);
    }

    @Override
    public Collection<Item> retrieveAllPresentItems() {
        Set<Item> items = new HashSet<>();
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                Item item = this.getItem(x, y);
                if (item != null) {
                    items.add(item);
                }
            }
        }
        return items;
    }

    protected void updateControlItemsInViews() {
        this.retrieveAllPresentItems().forEach(item -> {
            if (item instanceof GuiControlItem) {
                item.updateViews();
            }
        });
    }

    public abstract static class Builder<G extends Gui, B extends AbstractGui.Builder<G, B>> extends AbstractGui.Builder<G, B> {

        protected CharLayout builder;
        protected Item backgroundItem = null;

        protected Builder(@NotNull CharLayout builder) {
            super(builder.getWidth(), builder.getHeight());
            this.builder = builder;
        }

        public B background(@NotNull Item backgroundItem) {
            this.backgroundItem = backgroundItem;
            return instance();
        }

        public B decode(int x, int y, @NotNull SlotMarker marker) {
            this.builder.decode(x, y, marker);
            return instance();
        }

        public B decode(int x, int y, @NotNull Item item) {
            this.builder.decode(x, y, item);
            return instance();
        }

        public B decode(char character, @NotNull SlotMarker marker) {
            this.builder.decode(character, marker);
            return instance();
        }

        public B decode(char character, @NotNull Item item) {
            this.builder.decode(character, item);
            return instance();
        }

        @Override
        protected void applyBuilder(G gui) {
            gui.applyLayout(this.builder.build());

            if (this.backgroundItem != null) {
                gui.setBackground(this.backgroundItem);
            }
        }
    }

}
