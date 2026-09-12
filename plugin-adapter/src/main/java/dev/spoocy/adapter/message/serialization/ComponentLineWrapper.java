package dev.spoocy.adapter.message.serialization;

import dev.spoocy.adapter.message.font.DefaultFont;
import dev.spoocy.adapter.message.font.Font;
import dev.spoocy.adapter.message.font.FontRegistry;
import dev.spoocy.adapter.message.style.DecorationMap;
import dev.spoocy.utils.common.tuple.Pair;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ComponentLineWrapper {

    private final FontRegistry fontRegistry;

    public ComponentLineWrapper(@NotNull FontRegistry fontRegistry) {
        this.fontRegistry = fontRegistry;
    }

    /**
     * Wraps a Component into multiple lines if it exceeds the specified line width.
     *
     * @param component    the component
     * @param maxLineWidth the maximum width of a line in pixels
     *
     * @return a list of Components, each representing a line
     */
    public List<Component> splitWidth(@NotNull Component component, int maxLineWidth) {
        return splitWidth(component, Component::empty, maxLineWidth);
    }

    /**
     * Wraps a Component into multiple lines if it exceeds the specified line width.
     *
     * @param component    the component
     * @param linePrefix   the prefix of each line
     * @param maxLineWidth the maximum width of a line in pixels
     *
     * @return a list of Components, each representing a line
     */
    public List<Component> splitWidth(
            @NotNull Component component,
            @NotNull Supplier<Component> linePrefix,
            int maxLineWidth
    ) {
        List<Component> newLineWrapped = ComponentSplitter.splitByNewLine(component);

        return newLineWrapped.stream()
                .map(line -> wrap(line, linePrefix, maxLineWidth))
                .flatMap(Collection::stream)
                .map(Component::compact)
                .map(Component::compact)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @NotNull
    public List<Component> wrap(@NotNull Component component, @NotNull Supplier<Component> linePrefix, int maxLineWidth) {
        List<Pair<Component, Integer>> wordList = ComponentSplitter.splitByLeadingSpace(component)
                .stream()
                .map(word -> Pair.of(word, width(word)))
                .collect(Collectors.toCollection(LinkedList::new));

        List<Component> lines = new ArrayList<>();
        ComponentBuilder line = new ComponentBuilder();
        int currentLineWidth = 0;

        for (Pair<Component, Integer> entry : wordList) {

            Component word = entry.first();
            int wordWidth = entry.second();

            // apply prefix if this is a new line
            if (line.isEmpty()) {
                Component prefix = linePrefix.get();
                line.append(prefix);
                currentLineWidth = width(prefix);
            }

            if (currentLineWidth + wordWidth <= maxLineWidth) {
                line.append(word);
                currentLineWidth += wordWidth;
                continue;
            }

            if (wordWidth > maxLineWidth) {
                currentLineWidth = wrapWordExceedingLineLength(lines, line, currentLineWidth, word, maxLineWidth, linePrefix);
                continue;
            }

            // Word didn't fit, start a new line
            lines.add(line.buildAndReset());

            Component prefix = linePrefix.get();
            line.append(prefix);
            currentLineWidth = width(prefix);

            Component wordWithoutSpace = getComponentWithoutLeadingSpace(word);

            line.append(wordWithoutSpace);
            currentLineWidth += width(wordWithoutSpace);
        }
        
        if (!line.isEmpty()) {
            lines.add(line.buildAndReset());
        }
        return lines;
    }

    private int wrapWordExceedingLineLength(
            @NotNull List<Component> lines,
            @NotNull ComponentBuilder line,
            int currentLineWidth,
            @NotNull Component word,
            int maxLineWidth,
            @NotNull Supplier<Component> linePrefix
    ) {
        List<Component> characters = ComponentSplitter.splitByCharacter(word);

        for (Component character : characters) {

            if (line.isEmpty()) {
                Component prefix = linePrefix.get();
                line.append(prefix);
                currentLineWidth = width(prefix);
            }

            int characterWidth = width(character);

            if (currentLineWidth + characterWidth <= maxLineWidth) {
                line.append(character);
                currentLineWidth += characterWidth;
                continue;
            }

            lines.add(line.buildAndReset());
            final Component characterWithoutSpace = getComponentWithoutLeadingSpace(character);

            Component prefix = linePrefix.get();
            line.append(prefix);
            currentLineWidth = width(prefix);

            line.append(characterWithoutSpace);
            currentLineWidth += width(characterWithoutSpace);
        }
        
        return currentLineWidth;
    }

    /**
     * Calculates the width of a Component in pixels.
     *
     * @param component the Component to calculate the width for
     *
     * @return the width of the Component in pixels
     */
    public int width(@NotNull Component component) {
        return width(component, DecorationMap.fromMap(component.decorations()));
    }

    private int width(@NotNull Component component, @NotNull DecorationMap decorations) {
        int width = 0;

        if (component instanceof TextComponent) {
            TextComponent textComponent = (TextComponent) component;
            Key fontKey = textComponent.font();
            
            // if font is null, adventure implies default font 
            if (fontKey == null) {
                fontKey = DefaultFont.KEY;
            }
            
            Font font = fontRegistry.getFontOrDefault(fontKey);
            width += getTextWidth(font, textComponent.content(), decorations);
        }

        for (Component child : component.children()) {
            width += width(child, decorations.getChild(child));
        }

        return width;
    }

    private static int getTextWidth(
            @NotNull Font font,
            @NotNull String text,
            @NotNull DecorationMap decorations
    ) {
        final int decorationFix = decorations.get(TextDecoration.BOLD) == TextDecoration.State.TRUE ? 1 : 0;

        return text.codePoints()
                .map(elem -> font.getWidth(elem) + (Character.isWhitespace(elem) ? 0 : 1) + decorationFix)
                .sum();
    }

    @Contract(pure = true)
    private @NotNull Component getComponentWithoutLeadingSpace(@NotNull Component component) {
        return component.replaceText(builder -> builder
                .match("^ ")
                .replacement("")
                .times(1)
        );
    }

    private static final class ComponentBuilder {

        @Nullable
        private Component current;

        private ComponentBuilder() {
            this.current = null;
        }

        /**
         * Checks if the builder is empty.
         *
         * @return {@code true} if the builder is empty, {@code false} otherwise
         */
        public boolean isEmpty() {
            return this.current == null;
        }

        /**
         * Appends a Component.
         *
         * @param component the component
         */
        public void append(@NotNull Component component) {
            if (this.current == null) {
                this.current = component;
            } else {
                this.current = current.append(component);
            }
        }

        /**
         * Returns the current component and resets the builder.
         *
         * @return the current component
         */
        public Component buildAndReset() {
            if (current == null) {
                return Component.empty();
            }

            Component component = current;
            current = null;

            return component;
        }
    }
}