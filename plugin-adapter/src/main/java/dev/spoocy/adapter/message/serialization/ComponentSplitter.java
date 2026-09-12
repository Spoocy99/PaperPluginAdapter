package dev.spoocy.adapter.message.serialization;

import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ComponentSplitter {

    private ComponentSplitter() {
    }

    private static final Pattern NEW_LINE = Pattern.compile("\n");

    private static final Pattern LEADING_SPACE = Pattern.compile("(?= )");

    private static final Pattern SPACE = Pattern.compile(" ");

    private static final Pattern CHARACTER = Pattern.compile("(?!^)(?=.)");

    /**
     * Splits one {@link Component} into multiple based on {@code new lines}.
     *
     * @param component the component to split
     *
     * @return a list of all split Components
     *
     * @see #NEW_LINE
     */
    @NotNull
    public static LinkedList<Component> splitByNewLine(@NotNull Component component) {
        return split(component, NEW_LINE, true);
    }

    /**
     * Splits one  {@link Component} into multiple based on {@code spaces}.
     *
     * @param component the component to split
     *
     * @return a list of all split Components
     *
     * @see #SPACE
     */
    @NotNull
    public static LinkedList<Component> splitBySpace(@NotNull Component component) {
        return split(component, SPACE, true);
    }

    /**
     * Splits one  {@link Component} into multiple based on {@code characters}.
     *
     * @param component the component to split
     *
     * @return a list of all split Components
     *
     * @see #CHARACTER
     */
    @NotNull
    public static LinkedList<Component> splitByCharacter(@NotNull Component component) {
        return split(component, CHARACTER, true);
    }

    /**
     * Splits one  {@link Component} into multiple based on {@code leading spaces}.
     *
     * @param component the component to split
     *
     * @return a list of all split Components
     *
     * @see #LEADING_SPACE
     */
    @NotNull
    public static LinkedList<Component> splitByLeadingSpace(@NotNull Component component) {
        return split(component, LEADING_SPACE, true);
    }

    /**
     * Splits one {@link Component} into multiple lines based on a {@link Pattern}.
     *
     * @param component the component to split
     * @param pattern   the pattern to split the component by
     * @param compact   if each resulting line component should be compacted
     *
     * @return a list of all split Components
     */
    public static LinkedList<Component> split(
            final Component component,
            final Pattern pattern,
            final boolean compact
    ) {


        LinkedList<Component> split = split(component, pattern);

        if (compact) {
            return split.stream()
                    .map(Component::compact)
                    .map(Component::compact)
                    .collect(Collectors.toCollection(LinkedList::new));
        }

        return split;
    }

    /**
     * Splits one {@link Component} into multiple lines based on a {@link Pattern}.
     *
     * @param component the component to split
     * @param pattern   the pattern to split the component by
     *
     * @return a list of all split Components
     */
    public static LinkedList<Component> split(@NotNull Component component, @NotNull Pattern pattern) {
        Args.notNull(component, "component");
        Args.notNull(pattern, "pattern");

        LinkedList<Component> result = new LinkedList<>();


        if (component instanceof TextComponent) {

            TextComponent textComponent = (TextComponent) component;
            String text = textComponent.content();

            splitString(text, pattern)
                    .forEach(textpart -> {
                        Component part = Component.text(textpart).style(component.style());
                        result.add(part);
                    });

        } else {

            result.add(component.children(Collections.emptyList()));

        }

        // parent so we preserve the style on each line
        TextComponent parent = Component.empty().style(component.style());

        for (Component child : component.children()) {

            Component last = result.removeLast();
            LinkedList<Component> parts = split(child, pattern);
            Component firstPart = parts.removeFirst();

            result.add(isEmpty(firstPart) ? last : last.append(firstPart));

            for (Component part : parts) {
                result.add(parent.append(part));
            }

        }
        return result;
    }

    /**
     * Splits a {@link String} into multiple lines based on a {@link Pattern}.
     *
     * @param text    the string to split
     * @param pattern the pattern to split the text by
     *
     * @return a list of all split text parts
     */
    @NotNull
    private static LinkedList<String> splitString(@NotNull String text, @NotNull Pattern pattern) {
        LinkedList<String> parts = new LinkedList<>();
        Matcher matcher = pattern.matcher(text);

        int last = 0;
        while (matcher.find()) {

            String part = text.substring(last, matcher.start());
            parts.add(part);
            last = matcher.end();

        }

        parts.add(text.substring(last));
        return parts;
    }

    private static boolean isEmpty(final Component component) {
        return component instanceof TextComponent
                && ((TextComponent) component).content().isEmpty()
                && component.children().isEmpty();
    }
}
