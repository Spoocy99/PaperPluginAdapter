package dev.spoocy.adapter.gui.implementations;

import dev.spoocy.adapter.gui.animation.Animation;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.content.GuiItemProvider;
import dev.spoocy.adapter.gui.layout.Coordinate;
import dev.spoocy.adapter.gui.layout.builder.CharLayout;
import dev.spoocy.adapter.gui.layout.builder.Layout;
import dev.spoocy.adapter.gui.types.PageGui;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class PageGuiImpl extends ContentItemGui implements PageGui {

    private final boolean infinitePages;
    private int currentPage;
    private int cachedTotalItems, cachedMaxPages;

    protected PageGuiImpl(int width, int height, @NotNull GuiItemProvider contentProvider, boolean infinitePages) {
        super(width, height, contentProvider);
        this.infinitePages = infinitePages;
        this.currentPage = 1;
        this.cachedTotalItems = -1;
        this.cachedMaxPages = -1;
    }

    public int getMaxPages() {
        if(infinitePages) {
            return Integer.MAX_VALUE;
        }

        int totalItems = this.contentProvider.getTotalItems();
        if (totalItems != cachedTotalItems) {
            cachedTotalItems = totalItems;
            cachedMaxPages = (int) Math.ceil((double) totalItems / this.contentSlots.length);
        }
        return cachedMaxPages;
    }

    @Override
    public boolean hasInfinitePages() {
        return this.infinitePages;
    }

    @Override
    @Nonnegative
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public int getMaxPage() {
        return this.getMaxPages();
    }

    @Override
    public boolean canGoForward() {
        return this.currentPage < this.getMaxPages();
    }

    @Override
    public boolean canGoBackward() {
        return this.currentPage > 1;
    }

    @Override
    public void goForward(@Nonnegative int pages) {
        this.currentPage = Math.min(this.currentPage + pages, this.getMaxPages());
        this.updateContent();
    }

    @Override
    public void goBackward(@Nonnegative int pages) {
        this.currentPage = Math.max(this.currentPage - pages, 1);
        this.updateContent();
    }

    @Override
    public void playAnimation(@NotNull Animation<?, PageGui> animation) {
        this.playAnimationInternally(animation);
    }

    @Override
    public void reset() {
        this.currentPage = 1;
        this.updateContent();
    }

    @Override
    protected void onContentSlotUpdate(@NotNull Coordinate[] slots) {
        this.cachedTotalItems = -1; // Reset cached total items
        this.cachedMaxPages = -1; // Reset cached max pages
    }

    @Override
    protected List<Item> retrieveContent(@NotNull GuiItemProvider contentProvider, @NotNull Coordinate[] contentSlots) {
        int offset = (this.currentPage - 1) * contentSlots.length;
        return contentProvider.getItemList(offset, offset + contentSlots.length);
    }

        @Override
    public String toString() {
        return "PageGuiImpl{" +
                "height=" + height +
                ", width=" + width +
                ", infinitePages=" + infinitePages +
                ", currentPage=" + currentPage +
                '}';
    }

    public static class Builder extends ContentItemGui.Builder<PageGui, Builder> {

        private boolean infinitePages = false;

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

        public Builder infinitePages(boolean infinitePages) {
            this.infinitePages = infinitePages;
            return instance();
        }

        @Override
        protected @NotNull Builder instance() {
            return this;
        }

        @Override
        protected PageGui build0() {
            return new PageGuiImpl(this.width, this.height, this.contentProvider, this.infinitePages);
        }
    }


}
