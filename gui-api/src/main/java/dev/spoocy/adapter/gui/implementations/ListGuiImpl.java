package dev.spoocy.adapter.gui.implementations;

import dev.spoocy.adapter.gui.animation.Animation;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.Coordinate;
import dev.spoocy.adapter.gui.content.GuiItemProvider;
import dev.spoocy.adapter.gui.layout.builder.CharLayout;
import dev.spoocy.adapter.gui.layout.builder.Layout;
import dev.spoocy.adapter.gui.types.ListGui;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ListGuiImpl extends ContentItemGui implements ListGui {

    private int lineWidth, currentOffset;
    private int cachedTotalItems, cachedMaxOffset;

    protected ListGuiImpl(int width, int height, @NotNull GuiItemProvider contentProvider) {
        super(width, height, contentProvider);
        this.currentOffset = 0;
        this.lineWidth = 0;
        this.cachedTotalItems = -1;
        this.cachedMaxOffset = -1;
    }

    private int getMaxOffset() {
        if(this.lineWidth == 0) {
            return 0;
        }

        int totalItems = this.contentProvider.getTotalItems();
        if (totalItems != cachedTotalItems) {
            cachedTotalItems = totalItems;
            cachedMaxOffset = (int) Math.ceil((double) (totalItems - this.contentSlots.length) / this.lineWidth);
        }

        return cachedMaxOffset;
    }

    @Override
    @Nonnegative
    public int getOffset() {
        return this.currentOffset;
    }

    @Override
    public boolean canScrollUp() {
        return this.currentOffset > 0;
    }

    @Override
    public boolean canScrollDown() {
        return this.currentOffset < this.getMaxOffset();
    }

    @Override
    public void scrollUp(@Nonnegative int lines) {
        this.currentOffset = Math.max(0, this.currentOffset - lines);
        this.updateContent();
    }

    @Override
    public void scrollDown(@Nonnegative int lines) {
        this.currentOffset = Math.min(this.currentOffset + lines, this.getMaxOffset());
        this.updateContent();
    }

    @Override
    public void playAnimation(@NotNull Animation<ListGui> animation) {
        this.playAnimationInternally(animation);
    }

    @Override
    public void reset() {
        this.currentOffset = 0;
        this.updateContent();
    }

    @Override
    protected void onContentSlotUpdate(@NotNull Coordinate[] slots) {
        int[] lengths = new int[this.height];

        for (Coordinate slot : slots) {
            if (slot.getY() >= 0 && slot.getY() < this.height) {
                lengths[slot.getY()]++;
            }
        }

        int curLen = 0;

        for (int length : lengths) {
            if (length == 0) {
                continue;
            }

            if (curLen == 0) {
                curLen = length;
            }

            if (length != curLen) {
                throw new IllegalArgumentException("List lines must be equal");
            }
        }

        this.lineWidth = curLen;
        this.currentOffset = 0;
        this.cachedTotalItems = -1;
        this.cachedMaxOffset = -1;

        this.updateContent();
    }

    @Override
    protected List<Item> retrieveContent(@NotNull GuiItemProvider contentProvider, @NotNull Coordinate[] contentSlots) {
        int from = this.currentOffset * this.lineWidth;
        int to = from + contentSlots.length;
        return contentProvider.getItemList(from, to);
    }

    @Override
    public String toString() {
        return "ListGuiImpl{" +
                "height=" + height +
                ", width=" + width +
                ", lineOffset=" + currentOffset +
                ", lineWidth=" + lineWidth +
                '}';
    }

    public static class Builder extends ContentItemGui.Builder<ListGui, Builder> {

        public Builder(int width, int height) {
            this(width, height, '#');
        }

        public Builder(int width, int height, char emptyChar) {
            super(Layout.empty(width, height, emptyChar));
        }

        public Builder(@NotNull String structure, @NotNull String... additional) {
            super(Layout.builder(structure, additional));
        }

        public Builder(@NotNull CharLayout builder) {
            super(builder);
        }

        @Override
        protected @NotNull Builder instance() {
            return this;
        }

        @Override
        protected ListGui build0() {
            return new ListGuiImpl(this.width, this.height, this.contentProvider);
        }
    }

}
