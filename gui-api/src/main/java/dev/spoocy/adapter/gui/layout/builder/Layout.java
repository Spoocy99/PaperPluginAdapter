package dev.spoocy.adapter.gui.layout.builder;

import dev.spoocy.adapter.gui.layout.Coordinate;
import dev.spoocy.adapter.gui.layout.slot.ContentDirection;
import dev.spoocy.adapter.gui.layout.slot.SlotMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class Layout implements Cloneable {

    public static CharLayout empty(int width, int height, char emptyChar) {
        return new CharLayout(width, height, String.valueOf(emptyChar).repeat(width * height));
    }

    public static CharLayout builder(@NotNull String structure, @NotNull String... additional) {
        return new CharLayout(structure, additional);
    }

    public static CharLayout builder(int width, int height, @NotNull String structure) {
        return new CharLayout(width, height, structure);
    }

    private final int width, height;
    private final Coordinate[] contentSlots;
    private final Coordinate[] pairSlots;
    private final SlotMarker[][] slotMap;

    protected Layout(int width,
                  int height,
                  @NotNull SlotMarker[][] slotMap
    ) {
        this.width = width;
        this.height = height;
        this.slotMap = slotMap;

        this.contentSlots = Stream.concat(
                walkSlotsHorizontal(
                        marker -> marker.isContent() && ((SlotMarker.Content) marker).getDirection() == ContentDirection.HORIZONTAL
                ).stream(),
                walkSlotsVertical(
                        marker -> marker.isContent() && ((SlotMarker.Content) marker).getDirection() == ContentDirection.VERTICAL
                ).stream()
        ).toArray(Coordinate[]::new);

        this.pairSlots = Stream.concat(
                walkSlotsHorizontal(
                        marker -> marker.isContentPair() && ((SlotMarker.ContentPair) marker).getDirection() == ContentDirection.HORIZONTAL
                ).stream(),
                walkSlotsVertical(
                        marker -> marker.isContentPair() && ((SlotMarker.ContentPair) marker).getDirection() == ContentDirection.VERTICAL
                ).stream()
        ).toArray(Coordinate[]::new);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Nullable
    public SlotMarker getMarker(int x, int y) {
        return this.slotMap[x][y];
    }

    public Coordinate[] getOrderedContentSlots() {
        return this.contentSlots;
    }

    public Coordinate[] getOrderedContentPairSlots() {
        return this.pairSlots;
    }

    private List<Coordinate> walkSlotsHorizontal(@NotNull Predicate<SlotMarker> filter) {
        List<Coordinate> slots = new ArrayList<>(this.width * this.height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                SlotMarker marker = slotMap[x][y];
                if (marker != null && filter.test(marker)) {
                    slots.add(Coordinate.of(x, y));
                }
            }
        }
        return slots;
    }

    private List<Coordinate> walkSlotsVertical(@NotNull Predicate<SlotMarker> filter) {
        List<Coordinate> slots = new ArrayList<>(this.width * this.height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                SlotMarker marker = slotMap[x][y];
                if (marker != null && filter.test(marker)) {
                    slots.add(Coordinate.of(x, y));
                }
            }
        }
        return slots;
    }

    @Override
    public Layout clone() {
        return new Layout(
                this.width,
                this.height,
                this.slotMap.clone()
                );
    }
}
