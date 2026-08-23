package dev.spoocy.adapter.gui.layout.builder;

import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.slot.SlotMarker;
import dev.spoocy.utils.common.misc.ListUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class CharLayout extends LayoutBuilder {

    private final int width, height, size;
    protected String structure;
    protected final HashMap<Character, SlotMarker> markers = new HashMap<>();

    protected CharLayout(@NotNull String first, @NotNull String... other) {
       // this(clean(first).length(), other.length + 1, String.join("", ListUtils.combineArrays(new String[]{first}, other)));
        this(clean(first).length(), other.length + 1, first + String.join("", other));
    }

    protected CharLayout(int width, int height, @NotNull String structure) {
        this.width = width;
        this.height = height;
        this.size = width * height;
        this.structure = clean(structure);

        if (this.size != this.structure.length()) {
            throw new IllegalArgumentException("Invalid structure for Layout size provided! Expected " + this.size + " but got " + this.structure.length() + "!");
        }
    }

    public CharLayout decode(int x, int y, @NotNull SlotMarker marker) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds for layout!");
        }
        char character = getRandomUnusedCharacter();
        this.replaceCharacterAtPosition(x, y, character);
        this.markers.put(character, marker);
        return this;
    }

    public CharLayout decode(int x, int y, @NotNull Item item) {
        return this.decode(x, y, SlotMarker.item(item));
    }

    public CharLayout decode(char character, @NotNull SlotMarker marker) {
        this.markers.put(character, marker);
        return this;
    }

    public CharLayout decode(char character, @NotNull Item item) {
        return this.decode(character, SlotMarker.item(item));
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public Layout build() {

        SlotMarker[][] map = new SlotMarker[this.width][this.height];
        for (int i = 0; i < this.structure.length(); i++) {
            char c = this.structure.charAt(i);
            int x = i % this.width;
            int y = i / this.width;

            SlotMarker marker = this.markers.getOrDefault(c, null);
            map[x][y] = marker;
        }

        return new Layout(this.width, this.height, map);
    }

    private char getRandomUnusedCharacter() {
        for (char c = 'A'; c <= 'Z'; c++) {
            if (!this.structure.contains(String.valueOf(c))) {
                return c;
            }
        }
        for (char c = 'a'; c <= 'z'; c++) {
            if (!this.structure.contains(String.valueOf(c))) {
                return c;
            }
        }
        for (char c = '0'; c <= '9'; c++) {
            if (!this.structure.contains(String.valueOf(c))) {
                return c;
            }
        }
        throw new IllegalStateException("Keine ungenutzten Zeichen verfügbar!");
    }

    private void replaceCharacterAtPosition(int x, int y, char newCharacter) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds for layout!");
        }

        int position = y * this.width + x;
        StringBuilder updatedStructure = new StringBuilder(this.structure);
        updatedStructure.setCharAt(position, newCharacter);
        this.structure = updatedStructure.toString();
    }

    private static String clean(@NotNull String s) {
        return s
                .replace(" ", "")
                .replace("\n", "");
    }
}
