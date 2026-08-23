package dev.spoocy.adapter.gui.implementations;

import com.google.common.base.Preconditions;
import dev.spoocy.adapter.gui.content.ContentUpdateEvent;
import dev.spoocy.adapter.gui.content.FilterableGuiItemProvider;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.items.ItemPairProvider;
import dev.spoocy.adapter.gui.layout.Coordinate;
import dev.spoocy.adapter.gui.content.GuiItemProvider;
import dev.spoocy.adapter.gui.layout.builder.CharLayout;
import dev.spoocy.adapter.gui.layout.builder.Layout;
import dev.spoocy.adapter.gui.layout.slot.Slot;
import dev.spoocy.adapter.gui.layout.slot.SlotMarker;
import dev.spoocy.adapter.gui.types.ContentGui;
import dev.spoocy.adapter.gui.types.Resetable;
import dev.spoocy.adapter.log.BukkitLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class ContentItemGui extends MatrixGui implements ContentGui {

    @NotNull
    protected GuiItemProvider contentProvider;
    @NotNull
    protected Coordinate[] contentSlots;
    @NotNull
    protected Coordinate[] pairSlots;
    @Nullable
    protected Item contentBackgroundItem;
    @Nullable
    private Consumer<ContentUpdateEvent> contentDisplayAction;

    protected boolean contentHidden;

    protected ContentItemGui(int width, int height, @NotNull GuiItemProvider contentProvider) {
        super(width, height);
        this.contentProvider = contentProvider;
        this.contentSlots = new Coordinate[0];
        this.pairSlots = new Coordinate[0];
        this.contentBackgroundItem = null;
        this.contentDisplayAction = null;
        this.contentHidden = false;

        BukkitLogger.trace("Initialized ContentItemGui with content provider: {}", contentProvider);
    }

    @Override
    protected void applyLayout0(@NotNull Layout layout) {
        this.contentSlots = layout.getOrderedContentSlots();
        this.pairSlots = layout.getOrderedContentPairSlots();

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {

                SlotMarker marker = layout.getMarker(x, y);

                if(marker == null) {
                    this.setItem(x, y, null);

                } else if (marker.isContent() || marker.isContentPair()) {
                    this.applyContentItem(x, y, null);

                } else if(marker.isEmpty()) {
                    this.getSlot(x, y).empty();

                } else if (marker.isItem()) {
                    Item item = ((SlotMarker.Item) marker).getItem();
                    this.getSlot(x, y).handle(item);
                }

            }
        }

        this.onContentSlotUpdate(this.contentSlots);
        this.updateContent();
    }

    @Override
    public boolean hasContentSlots() {
        return this.contentSlots.length > 0;
    }

    @Override
    public Coordinate[] getContentSlots() {
        return Arrays.copyOf(this.contentSlots, this.contentSlots.length);
    }

    @Override
    public boolean hasContentPairSlots() {
        return this.pairSlots.length > 0;
    }

    @Override
    public Coordinate[] getContentPairSlots() {
        return Arrays.copyOf(this.pairSlots, this.pairSlots.length);
    }

    @Override
    public boolean hasContent() {
        return !this.contentProvider.isEmpty();
    }

    @Override
    public boolean isContentHidden() {
        return this.contentHidden;
    }

    @Override
    public void setContentHidden(boolean contentHidden) {
        this.contentHidden = contentHidden;
        BukkitLogger.trace("Setting content hidden to {}", contentHidden);
        if(contentHidden) {
            clearContent();
        } else {
            updateContent();
        }
    }

    @Override
    public void updateContent() {
        BukkitLogger.trace("Updating content of {}...", this.getClass().getSimpleName());

        if(hasContentPairSlots()) {
            for (Slot s : getSlots(Slot.Type.CONTENT_PAIR)) {
                applyContentItem(s.getX(), s.getY(), null);
            }
        }

        if(!this.hasContent() || this.isContentHidden()) {
            BukkitLogger.trace("Skipping content retrieve. (hasContent: {}, contentHidden: {})", this.hasContent(), this.isContentHidden());

            if(this.hasContentSlots()) {
                for (Coordinate coord : this.contentSlots) {
                    this.applyContentItem(coord.getX(), coord.getY(), null);
                }
            }

            return;
        }

        List<Item> items = this.retrieveContent(this.contentProvider, contentSlots);
        //BukkitLogger.trace("Retrieved {} content items from provider {}", items.size(), this.contentProvider.getClass().getSimpleName());

        for(int i = 0; i < this.contentSlots.length; i++) {
            Coordinate coords = this.contentSlots[i];

            if (i >= items.size()) {
                int x = coords.getX();
                int y = coords.getY();
                applyContentItem(x, y, null);
                continue;
            }

            Item item = items.get(i);
            int x = coords.getX();
            int y = coords.getY();
            applyContentItem(x, y, item);

        }

        this.updateControlItemsInViews();
    }
    protected abstract List<Item> retrieveContent(@NotNull GuiItemProvider contentProvider, @NotNull Coordinate[] contentSlots);

    @Override
    public @NotNull GuiItemProvider getContentProvider() {
        return this.contentProvider;
    }

    @Override
    public void setContentFilter(@Nullable Pattern filter) {
        if (this.contentProvider instanceof FilterableGuiItemProvider) {
            FilterableGuiItemProvider filterableProvider = (FilterableGuiItemProvider) this.contentProvider;
            filterableProvider.setFilter(filter);

            if(this instanceof Resetable) {
                ((Resetable) this).reset();
            }

            this.updateContent();
        } else {
            throw new UnsupportedOperationException("Content provider does not support filtering.");
        }
    }

    @Override
    public @Nullable Pattern getContentFilter() {
        return this.contentProvider instanceof FilterableGuiItemProvider
                ? ((FilterableGuiItemProvider) this.contentProvider).getFilter()
                : null;
    }

    @Override
    public void setContentBackground(@Nullable Item backgroundItem) {
        this.contentBackgroundItem = backgroundItem;
        this.updateContentBackground();
    }

    protected void updateContentBackground() {
        List<Slot> backgroundSlots = this.getSlots(Slot.Type.CONTENT_BACKGROUND);
        //BukkitLogger.trace("Updating content background for {} slots.", backgroundSlots.size());
        for (Slot s : backgroundSlots) {
            s.contentBackground(this.contentBackgroundItem);
        }
    }

    @Override
    public void onContentChange(@NotNull Consumer<ContentUpdateEvent> action) {
        this.contentDisplayAction = action;
    }

    @Override
    public void setContentItemUntilUpdate(int x, int y, @NotNull Item content) {
        this.applyContentItem(x, y, content);
    }

    private void clearContent() {
        getSlots(Slot.Type.CONTENT).forEach(slot -> {
            int x = slot.getX();
            int y = slot.getY();
            this.applyContentItem(x, y, null);
        });

        if(this.hasContentPairSlots()) {
            for (Slot s : getSlots(Slot.Type.CONTENT_PAIR)) {
                this.applyContentItem(s.getX(), s.getY(), null);
            }
        }
    }

    private Coordinate[] retrievePairSlots(int amount) {
        if(this.pairSlots.length == 0) return new Coordinate[0];

        int from = 0;
        for (Coordinate coord : this.pairSlots) {
            Slot slot = this.getSlot(coord.getX(), coord.getY());
            if (slot.getType() != Slot.Type.CONTENT_BACKGROUND) {
                from++;
                continue;
            }
            break;
        }

        if(from >= this.pairSlots.length) return new Coordinate[0];
        return getNextPairSlots(from, amount);
    }

    private Coordinate[] getNextPairSlots(int from, int count) {
        if(this.pairSlots.length == 0) return new Coordinate[0];

        int to = Math.min(from + count, this.pairSlots.length);
        Coordinate[] slots = new Coordinate[to - from];
        System.arraycopy(this.pairSlots, from, slots, 0, to - from);
        return slots;
    }

    private void applyContentItem(int x, int y, @Nullable Item item) {

        if (item == null) {
            this.getSlot(x, y).contentBackground(this.contentBackgroundItem);
        } else {
            this.getSlot(x, y).content(item);

            if(this.hasContentPairSlots() && item instanceof ItemPairProvider) {
                List<Item> pairItems = ((ItemPairProvider) item).getAdditionalItemsWhenDisplaying();
                Coordinate[] pairSlots = this.retrievePairSlots(pairItems.size());

                for(int j = 0; j < pairSlots.length; j++) {
                    Coordinate pairCoord = pairSlots[j];
                    getSlot(pairCoord.getX(), pairCoord.getY()).contentPair(pairItems.get(j));
                }
            }


        }

        if(this.contentDisplayAction != null) {
                this.contentDisplayAction.accept(new ContentUpdateEvent() {
                    @Override
                    public ContentGui getGui() {
                        return ContentItemGui.this;
                    }

                    @Override
                    public int getX() {
                        return x;
                    }

                    @Override
                    public int getY() {
                        return y;
                    }

                    @Override
                    public Item getNewItem() {
                        return item;
                    }
                });
            }

    }



    protected abstract void onContentSlotUpdate(@NotNull Coordinate[] slots);

    public abstract static class Builder<G extends ContentGui, B extends MatrixGui.Builder<G, B>> extends MatrixGui.Builder<G, B> {

        protected GuiItemProvider contentProvider = GuiItemProvider.EMPTY;
        protected Item contentBackgroundItem = null;
        private Consumer<ContentUpdateEvent> contentDisplayAction;

        protected Builder(@NotNull CharLayout builder) {
            super(builder);
        }

        public B content(@NotNull GuiItemProvider contentProvider) {
            this.contentProvider = contentProvider;
            return instance();
        }

        public B contentBackground(@NotNull Item contentBackgroundItem) {
            this.contentBackgroundItem = contentBackgroundItem;
            return instance();
        }

        public B onContentChange(@NotNull Consumer<ContentUpdateEvent> action) {
            this.contentDisplayAction = action;
            return instance();
        }

        @Override
        public void validate() {
            super.validate();
            Preconditions.checkNotNull(this.contentProvider, "content cannot be null");
        }

        @Override
        protected void applyBuilder(G gui) {
            if (this.contentDisplayAction != null) {
                gui.onContentChange(this.contentDisplayAction);
            }

            super.applyBuilder(gui);

            if (this.contentBackgroundItem != null) {
                gui.setContentBackground(this.contentBackgroundItem);
            }

        }
    }



}
