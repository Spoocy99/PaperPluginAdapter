package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.message.MessageLike;
import dev.spoocy.adapter.message.style.MessageStyle;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.ARGBLike;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ScopedMessage<P extends ScopedMessage<P>> extends Message {

    @NotNull
    MessageStyle style();

    @NotNull
    P style(@NotNull MessageStyle style);

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P color(@Nullable TextColor color) {
        return (P) Message.super.color(color);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P colorIfAbsent(@Nullable TextColor color) {
        return (P) Message.super.colorIfAbsent(color);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P shadowColor(@Nullable ARGBLike argb) {
        return (P) Message.super.shadowColor(argb);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P shadowColorIfAbsent(@Nullable ARGBLike argb) {
        return (P) Message.super.shadowColorIfAbsent(argb);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P decorate(@NotNull TextDecoration decoration) {
        return (P) Message.super.decorate(decoration);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P decorate(@NotNull TextDecoration... decorations) {
        return (P) Message.super.decorate(decorations);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
        return (P) Message.super.decoration(decoration, state);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P decoration(@NotNull TextDecoration decoration, boolean flag) {
        return (P) Message.super.decoration(decoration, flag);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P decorationIfAbsent(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
        return (P) Message.super.decorationIfAbsent(decoration, state);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P decorations(@Nullable Map<TextDecoration, TextDecoration.State> decorations) {
        return (P) Message.super.decorations(decorations);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P click(@Nullable ClickEvent<?> event) {
        return (P) Message.super.click(event);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P clickIfAbsent(@Nullable ClickEvent<?> event) {
        return (P) Message.super.clickIfAbsent(event);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P openUrl(@NotNull String url) {
        return (P) Message.super.openUrl(url);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P suggestCommand(@NotNull String command) {
        return (P) Message.super.suggestCommand(command);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P runCommand(@NotNull String command) {
        return (P) Message.super.runCommand(command);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P hover(@Nullable HoverEvent<?> event) {
        return (P) Message.super.hover(event);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P hoverIfAbsent(@Nullable HoverEvent<?> event) {
        return (P) Message.super.hoverIfAbsent(event);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P hover(@NotNull ComponentLike message) {
        return (P) Message.super.hover(message);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P hover(@NotNull MessageLike message) {
        return (P) Message.super.hover(message);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P font(@Nullable Key font) {
        return (P) Message.super.font(font);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P prefix(@Nullable MessageLike message) {
        return (P) Message.super.prefix(message);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P append(@Nullable MessageLike message) {
        return (P) Message.super.append(message);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P multipleLines() {
        return (P) Message.super.multipleLines();
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P multipleLines(int maxPixelsPerLine) {
        return (P) Message.super.multipleLines(maxPixelsPerLine);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P multipleLines(@Nullable ComponentLike linePrefix) {
        return (P) Message.super.multipleLines(linePrefix);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P multipleLines(int maxPixelsPerLine, @Nullable ComponentLike linePrefix) {
        return (P) Message.super.multipleLines(maxPixelsPerLine, linePrefix);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P upperCase(boolean upperCase) {
        return (P) Message.super.upperCase(upperCase);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P upperCase() {
        return (P) Message.super.upperCase();
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P inheritStyle(boolean inherit) {
        return (P) Message.super.inheritStyle(inherit);
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P inheritStyle() {
        return (P) Message.super.inheritStyle();
    }

    @CheckReturnValue
    @Override
    @SuppressWarnings("unchecked")
    default @NotNull P withDefaultStyle() {
        return (P) Message.super.withDefaultStyle();
    }
}
