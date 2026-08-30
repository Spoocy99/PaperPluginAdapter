package dev.spoocy.adapter.gui.implementations;

import dev.spoocy.adapter.gui.animation.Animation;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.content.GuiItemProvider;
import dev.spoocy.adapter.gui.layout.Coordinate;
import dev.spoocy.adapter.gui.layout.builder.CharLayout;
import dev.spoocy.adapter.gui.layout.builder.Layout;
import dev.spoocy.adapter.gui.types.ScrollGui;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ScrollGuiImpl extends ContentItemGui implements ScrollGui {

    private int offset;

    protected ScrollGuiImpl(int width, int height, @NotNull GuiItemProvider contentProvider) {
        super(width, height, contentProvider);
        this.offset = 0;
    }

    @Override
    @Nonnegative
    public int getOffset() {
        return offset;
    }

    @Override
    public int getItemCount() {
        return this.contentProvider.getTotalItems();
    }

    @Override
    public boolean canScrollForward() {
        return this.offset < (this.contentProvider.getTotalItems() - this.contentSlots.length + 1);
    }

    @Override
    public boolean canScrollBackward() {
        return this.offset > 0;
    }

    @Override
    public void scrollForward(@Nonnegative int value) {
        this.offset = Math.min(this.offset + value, this.contentProvider.getTotalItems() - this.contentSlots.length + 1);
        this.updateContent();
    }

    @Override
    public void scrollBackward(@Nonnegative int value) {
        this.offset = Math.max(this.offset - value, 0);
        this.updateContent();
    }

    @Override
    public void playAnimation(@NotNull Animation<?, ScrollGui> animation) {
        this.playAnimationInternally(animation);
    }

    @Override
    public void reset() {
        this.offset = 0;
        this.updateContent();
    }

    @Override
    protected void onContentSlotUpdate(@NotNull Coordinate[] slots) { }

    @Override
    protected List<Item> retrieveContent(@NotNull GuiItemProvider contentProvider, @NotNull Coordinate[] contentSlots) {
        return contentProvider.getItemList(this.offset, this.offset + contentSlots.length);
    }

        @Override
    public String toString() {
        return "ScrollGuiImpl{" +
                "height=" + height +
                ", width=" + width +
                ", offset=" + offset +
                '}';
    }

    public static class Builder extends ContentItemGui.Builder<ScrollGui, Builder> {

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
        protected ScrollGui build0() {
            return new ScrollGuiImpl(this.width, this.height, this.contentProvider);
        }
    }

}
