package dev.spoocy.adapter.gui.implementations;

import dev.spoocy.adapter.gui.animation.Animation;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.Coordinate;
import dev.spoocy.adapter.gui.content.GuiItemProvider;
import dev.spoocy.adapter.gui.layout.builder.CharLayout;
import dev.spoocy.adapter.gui.layout.builder.Layout;
import dev.spoocy.adapter.gui.types.SimpleGui;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SimpleGuiImpl extends ContentItemGui implements SimpleGui {

    protected SimpleGuiImpl(int width, int height, @NotNull GuiItemProvider contentProvider) {
        super(width, height, contentProvider);
    }

    @Override
    protected List<Item> retrieveContent(@NotNull GuiItemProvider contentProvider, @NotNull Coordinate[] contentSlots) {
        return contentProvider.getItemList(0, contentSlots.length);
    }

    @Override
    public void playAnimation(@NotNull Animation<SimpleGui> animation) {
        this.playAnimationInternally(animation);
    }

    @Override
    protected void onContentSlotUpdate(@NotNull Coordinate[] slots) { }

        @Override
    public String toString() {
        return "SimpleGuiImpl{" +
                "height=" + height +
                ", width=" + width +
                '}';
    }

    public static class Builder extends ContentItemGui.Builder<SimpleGui, Builder> {

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
        protected SimpleGui build0() {
            return new SimpleGuiImpl(this.width, this.height, this.contentProvider);
        }
    }

}
