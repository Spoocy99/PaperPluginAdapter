package dev.spoocy.adapter.message;

import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.language.LocalizedReceiver;
import dev.spoocy.adapter.message.serialization.RenderContext;
import dev.spoocy.adapter.message.sprites.Sprites;
import dev.spoocy.adapter.message.style.DecorationMap;
import dev.spoocy.adapter.message.style.MessageStyle;
import dev.spoocy.adapter.message.types.Styleable;
import dev.spoocy.adapter.message.types.impl.*;
import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import net.kyori.adventure.util.ARGBLike;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.*;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */


public interface Message extends Styleable<Message>, MessageLike, LocalizedComponent {

    MessageStyle DEFAULT_STYLE = MessageStyle.empty();

    /**
     * Empty {@link Component}
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull Component emptyCmp() {
        return Component.empty();
    }

    /**
     * Empty {@link ComponentMessage}
     */
    @Contract(" -> new")
    static @NotNull ComponentMessage emptyMess() {
        return EmptyMessage.INSTANCE;
    }

    /**
     * Creates a {@link ComponentMessage} with the specified {@link Component}.
     *
     * @param component The component to wrap.
     *
     * @return the new message
     */
    @Contract("_ -> new")
    static @NotNull ComponentMessage wrap(@NotNull Component component) {
        return new WrappedMessage(component, MessageStyle.empty()); // empty style because component is already styled
    }

    /**
     * Creates a {@link ComponentMessage} with the specified {@link ObjectContents}.
     *
     * @param contents The object contents.
     *
     * @return the new message
     */
    @Contract("_ -> new")
    static @NotNull ComponentMessage objectContent(@NotNull ObjectContents contents) {
        return new ObjectContentMessage(contents, MessageStyle.empty()); // empty style
    }

    /**
     * Creates a {@link ComponentMessage} with the specified {@link SpriteObjectContents}.
     *
     * @param sprite The sprite object.
     *
     * @return the new message.
     */
    @Contract("_ -> new")
    static @NotNull ComponentMessage sprite(@NotNull SpriteObjectContents sprite) {
        return objectContent(sprite);
    }

    /**
     * Creates a {@link ComponentMessage} with the specified sprite.
     *
     * @param atlas  The atlas that contains the sprite.
     * @param sprite The sprite.
     *
     * @return the new message.
     */
    @Contract("_, _ -> new")
    static @NotNull ComponentMessage sprite(@NotNull Key atlas, @NotNull Key sprite) {
        return objectContent(Sprites.get(atlas, sprite));
    }

    /**
     * Creates a {@link ComponentMessage} with the specified text.
     * <p>
     * <b>This message supports minimessage formatting.</b>
     *
     * @param message The text.
     *
     * @return the new message.
     */
    @Contract("_ -> new")
    static @NotNull TextMessage msg(@NotNull String message) {
        return new ContentMessage(true, message, DEFAULT_STYLE);
    }

    /**
     * Creates a {@link ComponentMessage} with the specified lines.
     * <p>
     * <b>This message supports minimessage formatting.</b>
     *
     * @param messages The lines of text.
     *
     * @return the new message.
     */
    @Contract("_ -> new")
    static @NotNull TextMessage msg(@NotNull Collection<String> messages) {
        StringBuilder builder = new StringBuilder();
        for (String message : messages) {
            builder.append(message).append("<newline>");
        }
        return msg(builder.toString());
    }

    /**
     * Creates a {@link TextMessage} with the specified text.
     * <p>
     * <b>This message does not support minimessage formatting.</b>
     *
     * @param text The text.
     *
     * @return the new message.
     */
    @Contract("_ -> new")
    static @NotNull TextMessage text(@NotNull String text) {
        return new ContentMessage(false, text, DEFAULT_STYLE);
    }

    /**
     * Creates a {@link TextMessage} with the specified text and {@link MessageStyle}.
     * <p>
     * <b>This message does not support minimessage formatting.</b>
     *
     * @param text  The text.
     * @param style The style.
     *
     * @return the new message.
     */
    @Contract("_, _ -> new")
    static @NotNull TextMessage text(@NotNull String text, @NotNull MessageStyle style) {
        return new ContentMessage(false, text, style);
    }

    /**
     * Creates a {@link TextMessage} with the specified text and color.
     * <p>
     * <b>This message does not support minimessage formatting.</b>
     *
     * @param text        The text.
     * @param color       The color.
     * @param decorations The decorations.
     *
     * @return the new message.
     */
    @Contract("_, _, _ -> new")
    static @NotNull TextMessage text(
            @NotNull String text,
            @NotNull TextColor color,
            @NotNull TextDecoration... decorations
    ) {
        return text(
                text, MessageStyle.builder()
                        .color(color)
                        .decorate(decorations)
                        .build()
        );
    }

    /**
     * Creates a {@link TranslatableMessage} with the specified key.
     *
     * @param key The translation key.
     *
     * @return the new message.
     */
    @Contract("_ -> new")
    static @NotNull TranslatableMessage translatable(@NotNull String key) {
        return new TranslationMessage(key, key, DEFAULT_STYLE);
    }

    /**
     * Creates a {@link TranslatableMessage} with the specified key, color, and decorations.
     *
     * @param key         The translation key.
     * @param color       The color.
     * @param decorations The text decorations.
     *
     * @return the new message.
     */
    @Contract("_, _, _ -> new")
    static @NotNull TranslatableMessage translatable(
            @NotNull String key,
            @NotNull TextColor color,
            @NotNull TextDecoration... decorations
    ) {
        return new TranslationMessage(
                key, key, MessageStyle.builder()
                .color(color)
                .decorate(decorations)
                .build()
        );
    }

    /**
     * Creates a {@link TextMessage} whose content is determined by a {@link Function}
     * that is called with the current {@link Locale}.
     *
     * @param translator the translator function
     *
     * @return the new message.
     */
    @Contract("_ -> new")
    static @NotNull TextMessage translatable(@NotNull Function<Locale, String> translator) {
        return new VirtualTranslatableMessage(translator, DEFAULT_STYLE);
    }

    /**
     * Creates a {@link Component} from the specified minimessage message.
     *
     * @param message The minimessage message to convert.
     *
     * @return the new component.
     */
    @NotNull
    static Component cmp(@NotNull String message) {
        return PluginConfig.globalTranslation().miniMessageSerializer().deserialize(message);
    }

    /**
     * Creates a {@link Component} with the specified text and color.
     *
     * @param text        The text to display.
     * @param color       The color of the text.
     * @param decorations The decorations to apply to the text.
     *
     * @return the new component.
     */
    @NotNull
    static Component cmp(
            @NotNull String text,
            @NotNull TextColor color,
            @Nullable TextDecoration... decorations
    ) {
        return Component.text(text).style(b -> {
            b.color(color);

            if(decorations != null) {
                b.decorations(getDecorationsMap(Set.of(decorations)));
            }
        });
    }

    @NotNull
    static RenderContext asContext(@NotNull Pointered target) {
        Args.notNull(target, "target");

        if(target instanceof RenderContext) {
            return (RenderContext) target;
        }

        return RenderContext.target(target);
    }

    /**
     * Creates a {@link DecorationMap}
     */
    @NotNull
    static DecorationMap getDecorationsMap(@NotNull Set<TextDecoration> decorations) {
        final Map<TextDecoration, TextDecoration.State> map = new EnumMap<>(TextDecoration.class);
        for (TextDecoration decoration : TextDecoration.values()) {
            map.put(
                    decoration, decorations.contains(decoration)
                            ? TextDecoration.State.TRUE : TextDecoration.State.FALSE
            );
        }

        return DecorationMap.fromMap(map);
    }

    /**
     * Creates a {@link DecorationMap}
     */
    @NotNull
    static DecorationMap decorationsMap(@NotNull TextDecoration.State state) {
        final Map<TextDecoration, TextDecoration.State> map = new EnumMap<>(TextDecoration.class);
        for (TextDecoration decoration : TextDecoration.values()) {
            map.put(decoration, state);
        }

        return DecorationMap.fromMap(map);
    }

    /**
     * Converts a {@link Component} to bungeecord's legacy format.
     */
    @NotNull
    static String toLegacy(@NotNull Component component) {
        return PluginConfig.globalTranslation().legacySerializer().serialize(component);
    }

    /**
     * Converts a {@link Component} to bungeecord components.
     */
    @NotNull
    static BaseComponent[] toBungee(@NotNull Component component) {
        checkBungeeSerializer();
        return BungeeSerializerProvider.serialize(component);
    }

    private static void checkBungeeSerializer() {
        try {
            Class.forName("dev.spoocy.adapter.serializers.BungeeComponentSerializer");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("dev.spoocy.adapter:bungee-serializer not found in classpath.");
        }
    }

    /**
     * Converts a {@link Component} to plain text.
     */
    @NotNull
    static String toPlainText(@NotNull Component component) {
        return PluginConfig.globalTranslation().plainSerializer().serialize(component);
    }

    /**
     * Converts a {@link Component} to a MiniMessage-formatted string.
     */
    @NotNull
    static String toMiniMessageFormat(@NotNull Component component) {
        return PluginConfig.globalTranslation().miniMessageSerializer().serialize(component);
    }

    int DEFAULT_MAX_PIXELS_PER_LINE = 200;

    @NotNull
    MessageStyle style();

    @NotNull
    Message style(@NotNull MessageStyle style);

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message color(@Nullable TextColor color) {
        return style(this.style().color(color));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message colorIfAbsent(@Nullable TextColor color) {
        return style(this.style().colorIfAbsent(color));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message shadowColor(@Nullable ARGBLike argb) {
        return style(this.style().shadowColor(argb));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message shadowColorIfAbsent(@Nullable ARGBLike argb) {
        return style(this.style().shadowColorIfAbsent(argb));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message decorate(@NotNull TextDecoration decoration) {
        return style(this.style().decorate(decoration));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message decorate(@NotNull TextDecoration... decorations) {
        return style(this.style().decorate(decorations));
    }

    @CheckReturnValue
    @Contract("_, _  -> new")
    @Override
    @NotNull
    default Message decoration(@NotNull TextDecoration decoration, boolean flag) {
        return style(this.style().decoration(decoration, flag));
    }

    @CheckReturnValue
    @Contract("_, _  -> new")
    @Override
    @NotNull
    default Message decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
        return style(this.style().decoration(decoration, state));
    }

    @CheckReturnValue
    @Contract("_, _  -> new")
    @Override
    @NotNull
    default Message decorationIfAbsent(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
        return style(this.style().decorationIfAbsent(decoration, state));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message decorations(@Nullable Map<TextDecoration, TextDecoration.State> decorations) {
        return style(this.style().decorations(decorations));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message click(@Nullable ClickEvent<?> event) {
        return style(this.style().click(event));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message clickIfAbsent(@Nullable ClickEvent<?> event) {
        return style(this.style().clickIfAbsent(event));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @NotNull
    default Message openUrl(@NotNull String url) {
        return click(ClickEvent.openUrl(url));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @NotNull
    default Message suggestCommand(@NotNull String command) {
        return click(ClickEvent.suggestCommand(command));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    default Message runCommand(@NotNull String command) {
        return click(ClickEvent.runCommand(command));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message hover(@Nullable HoverEvent<?> event) {
        return style(this.style().hover(event));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message hoverIfAbsent(@Nullable HoverEvent<?> event) {
        return style(this.style().hoverIfAbsent(event));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @NotNull
    default Message hover(@NotNull ComponentLike message) {
        return hover(HoverEvent.showText(message));
    }

    /**
     * Not tests yet, probably does not receive/preserve context
     */
    @ApiStatus.Experimental
    @CheckReturnValue
    @Contract("_ -> new")
    @NotNull
    default Message hover(@NotNull MessageLike message) {
        // TODO: ensure context is preserved and applied
        return hover(HoverEvent.showText(message.asMessage().cmp()));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message font(@Nullable Key font) {
        return style(this.style().font(font));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message prefix(@Nullable MessageLike message) {
        return style(this.style().prefix(message));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message append(@Nullable MessageLike message) {
        return style(this.style().append(message));
    }

    @CheckReturnValue
    @Contract(" -> new")
    @NotNull
    default Message multipleLines() {
        return multipleLines(DEFAULT_MAX_PIXELS_PER_LINE, null);
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @NotNull
    default Message multipleLines(int maxPixelsPerLine) {
        return multipleLines(maxPixelsPerLine, null);
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @NotNull
    default Message multipleLines(@Nullable ComponentLike linePrefix) {
        return multipleLines(DEFAULT_MAX_PIXELS_PER_LINE, linePrefix);
    }

    @CheckReturnValue
    @Contract("_, _ -> new")
    @Override
    @NotNull
    default Message multipleLines(int maxPixelsPerLine, @Nullable ComponentLike linePrefix) {
        return style(this.style().multipleLines(maxPixelsPerLine, linePrefix));
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message upperCase(boolean upperCase) {
        return style(this.style().upperCase(upperCase));
    }

    @CheckReturnValue
    @Contract(" -> new")
    @NotNull
    default Message upperCase() {
        return upperCase(true);
    }

    @CheckReturnValue
    @Contract("_ -> new")
    @Override
    @NotNull
    default Message inheritStyle(boolean inherit) {
        return style(this.style().inheritStyle(inherit));
    }

    @CheckReturnValue
    @Contract(" -> new")
    @NotNull
    default Message inheritStyle() {
        return inheritStyle(true);
    }

    @CheckReturnValue
    @Contract(" -> new")
    @NotNull
    default Message withDefaultStyle() {
        return inheritStyle(false);
    }

    /*
     * component rendering
     */

    /**
     * Renders the message into a list of components.
     * <br>
     * One component representing each line of the message.
     * <p>
     * <b> This list may contain {@link TranslatableComponent TranslatableComponents}. </b>
     * <p>
     * Some objects like Items and Inventories may not support translatable components,
     * so use {@link #cmpLines(Locale)} for those.
     *
     * @return a list of rendered messages
     */
    @NotNull
    default List<Component> cmpLines() {
        return cmpLines((RenderContext) null, true);
    }

    /**
     * Renders the message into a single component.
     * <br>
     * May contain child {@link Component#newline() NEW_LINE}.
     * <p>
     * <b> This component may be or contain a {@link TranslatableComponent} </b>
     * <p>
     * Some objects like Items and Inventories may not support translatable components,
     * so use {@link #cmp(Locale)} for those.
     *
     * @return the rendered message
     */
    @NotNull
    default Component cmp() {
        return cmp((RenderContext) null, true);
    }

    /**
     * Renders the message into a list of components with a specified {@link RenderContext context}.
     * <br>
     * May contain child {@link Component#newline() NEW_LINE}.
     * <p>
     * <b> This list will not contain any {@link TranslatableComponent TranslatableComponents}. </b>
     *
     * @param context the render context
     * @param freshStyle whether the components should have a fresh style
     *
     * @return the rendered message
     */
    @NotNull
    List<Component> cmpLines(@Nullable RenderContext context, boolean freshStyle);

    /**
     * Renders the message with the {@link RenderContext context} and a fresh style.
     */
    @NotNull
    default List<Component> cmpLines(@Nullable RenderContext context) {
        return this.cmpLines(context, true);
    }

    /**
     * Renders the message into a single component with a specified {@link RenderContext context}.
     * <br>
     * May contain child {@link Component#newline() NEW_LINE}.
     * <p>
     * <b> This component will never be a {@link TranslatableComponent}. </b>
     *
     * @param context the render context
     * @param freshStyle whether this component should have fresh style
     *
     * @return the rendered message
     */
    @NotNull
    Component cmp(@Nullable RenderContext context, boolean freshStyle);

    /**
     * Renders the message with the {@link RenderContext context} and a fresh style.
     */
    @NotNull
    default Component cmp(@Nullable RenderContext context) {
        return this.cmp(context, true);
    }

    /**
     * Renders the message into a list of components with a specified {@link Locale language}.
     * <br>
     * May contain child {@link Component#newline() NEW_LINE}.
     * <p>
     * <b> This list will not contain any {@link TranslatableComponent TranslatableComponents}. </b>
     *
     * @return the rendered message
     */
    @NotNull
    default List<Component> cmpLines(@NotNull Locale locale) {
        Args.notNull(locale, "locale");
        return this.cmpLines(RenderContext.locale(locale));
    }

    /**
     * Renders the message into a single component with a specified {@link Locale language}.
     * <br>
     * May contain child {@link Component#newline() NEW_LINE}.
     * <p>
     * <b> This component will never be a {@link TranslatableComponent}. </b>
     *
     * @return the rendered message
     */
    @NotNull
    default Component cmp(@NotNull Locale locale) {
        Args.notNull(locale, "locale");
        return this.cmp(RenderContext.locale(locale));
    }

    @NotNull
    default List<Component> cmpLines(@NotNull LocalizedReceiver target) {
        Args.notNull(target, "target");
        return this.cmpLines(RenderContext.locale(target.getLocale()));
    }

    @NotNull
    default Component cmp(@NotNull LocalizedReceiver target) {
        Args.notNull(target, "target");
        return this.cmp(RenderContext.locale(target.getLocale()));
    }

    @NotNull
    default List<Component> cmpLines(@NotNull Player target) {
        Args.notNull(target, "target");
        Audience audience = PluginConfig.compatibilityProvider().getAudienceProvider().player(target);
        return this.cmpLines(audience);
    }

    @NotNull
    default Component cmp(@NotNull Player target) {
        Args.notNull(target, "target");
        Audience audience = PluginConfig.compatibilityProvider().getAudienceProvider().player(target);
        return this.cmp(audience);
    }

    @NotNull
    default List<Component> cmpLines(@NotNull Pointered target) {
        return this.cmpLines(asContext(target));
    }

    @NotNull
    default Component cmp(@NotNull Pointered target) {
        return this.cmp(asContext(target));
    }

    default String toMiniMessageFormat(@Nullable Locale locale) {
        Component cmp;

        // we allow null because minimessage can format translatable components
        if(locale == null) {
            cmp = this.cmp((RenderContext) null);
        } else {
            cmp = this.cmp(locale);
        }

        return toMiniMessageFormat(cmp);
    }

    default String toPlainText(@NotNull Locale locale) {
        // cant allow null because of translatables
        return toPlainText(cmp(locale));
    }

    default String toLegacy(@NotNull Locale locale) {
        // cant allow null because of translatables
        return toLegacy(cmp(locale));
    }

    default BaseComponent[] toBungeeComponents(@NotNull Locale locale) {
        return toBungee(cmp(locale));
    }

    /*
     * direct sending for a serialized component
     */

    void send(@NotNull Player player);

    void send(@NotNull CommandSender sender);

    void sendActionbar(@NotNull Player player);

    void sendToConsole();

    void broadcast(@NotNull Audience audience);

    void broadcast(@NotNull ForwardingAudience audience);

    void broadcast();

    void broadcastActionbar();

    @NotNull
    default Component asVirtual() {
        return Component.virtual(RenderContext.class, this::cmp);
    }

    @Override
    default @NonNull Message asMessage() {
        return this;
    }
}
