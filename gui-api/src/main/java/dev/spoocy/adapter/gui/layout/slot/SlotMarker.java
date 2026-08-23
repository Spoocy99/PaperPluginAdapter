package dev.spoocy.adapter.gui.layout.slot;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface SlotMarker {

    SlotMarker EMPTY = new Empty();
    SlotMarker CONTENT_HORIZONTAL = new Content(ContentDirection.HORIZONTAL);
    SlotMarker CONTENT_VERTICAL = new Content(ContentDirection.VERTICAL);
    SlotMarker CONTENT_PAIR_HORIZONTAL = new ContentPair(ContentDirection.HORIZONTAL);
    SlotMarker CONTENT_PAIR_VERTICAL = new ContentPair(ContentDirection.VERTICAL);

    static SlotMarker item(@NotNull dev.spoocy.adapter.gui.items.Item item) {
        return new Item(item);
    }

    default boolean isItem() {
        return this instanceof Item;
    }

    default boolean isContent() {
        return this instanceof Content;
    }

    default boolean isContentPair() {
        return this instanceof ContentPair;
    }

    default boolean isEmpty() {
        return this instanceof Empty;
    }

    class Item implements SlotMarker {
        private final dev.spoocy.adapter.gui.items.Item item;

        private Item(@NotNull dev.spoocy.adapter.gui.items.Item item) {
            this.item = item;
        }

        @NotNull
        public dev.spoocy.adapter.gui.items.Item getItem() {
            return this.item;
        }

    }

    class Content implements SlotMarker {
        private final ContentDirection direction;

        private Content(@NotNull ContentDirection direction) {
            this.direction = direction;
        }

        @NotNull
        public ContentDirection getDirection() {
            return this.direction;
        }
    }

    class ContentPair implements SlotMarker {
        private final ContentDirection direction;

        private ContentPair(@NotNull ContentDirection direction) {
            this.direction = direction;
        }

        @NotNull
        public ContentDirection getDirection() {
            return this.direction;
            }
    }

    class Empty implements SlotMarker { }
}
