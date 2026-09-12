package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.message.MessageLike;
import dev.spoocy.adapter.message.serialization.RenderContext;
import dev.spoocy.adapter.message.style.MessageStyle;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.time.temporal.TemporalAccessor;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractArgumentsMessage<P extends Arguments<P> & ScopedMessage<P>> extends AbstractPluginMessage<P> implements Arguments<P> {

    protected AbstractArgumentsMessage(@NotNull MessageStyle style) {
        super(style);
    }

    protected abstract P withTagResolver(@NotNull TagResolver resolver);

    @Override
    public @NonNull P arg(@NotNull TagResolver tagResolver) {
        return withTagResolver(tagResolver);
    }

    @Override
    public @NotNull P arg(@NotNull String key, @NotNull MessageLike message) {
        final Message mess = message.asMessage();

        return arg(TagResolver.resolver(
                key, (args, context) -> {

                    Component component;

                    Pointered target = context.target();

                    // TODO: maybe direct call to renderer?
                    // dont apply a frest style to components as this would fuck with minimessage
                    if(target != null) {
                        // preserve current context
                        RenderContext ctx = Message.asContext(target);
                        component = mess.cmp(ctx, false);
                    } else {
                        component = mess.cmp(null, false);
                    }

                    return Tag.selfClosingInserting(component);
                }
        ));
    }

    @Override
    public @NotNull P arg(@NotNull String key, @NotNull String minimessage) {
        return arg(Placeholder.parsed(key, minimessage));
    }

    @Override
    public @NotNull P arg(@NotNull String key, @NotNull Supplier<String> arg) {
        return arg(TagResolver.resolver(
                key, (args, context) -> {
                    String content = arg.get();
                    return Tag.preProcessParsed(content);
                }
        ));
    }

    @Override
    public @NotNull P arg(@NotNull String key, @NotNull ComponentLike component) {
        return arg(TagResolver.resolver(key, Tag.selfClosingInserting(component)));
    }

    @Override
    public @NotNull P argText(@NotNull String key, @NotNull String text) {
        return arg(key, Component.text(text));
    }

    @Override
    public @NotNull P argText(
            @NotNull String key,
            @NotNull String text,
            @NotNull TextColor color,
            @NotNull TextDecoration... decorations
    ) {
        return arg(key, Component.text(text).color(color).decorate(decorations));
    }

    @Override
    public @NotNull P argNum(@NotNull String key, @NotNull Number number) {
        String content = number.toString();
        return argText(key, content);
    }

    @Override
    public @NotNull P argNum(
            @NotNull String key,
            @NotNull Number number,
            @NotNull TextColor color,
            @NotNull TextDecoration... decorations
    ) {
        String content = number.toString();
        return argText(key, content, color, decorations);
    }

    @Override
    public @NotNull P argDate(@NotNull String key, @NotNull TemporalAccessor value) {
        return arg(Formatter.date(key, value));
    }

    @Override
    public @NotNull P boolChoice(@NotNull String key, boolean value) {
        return arg(Formatter.booleanChoice(key, value));
    }

    @Override
    public @NotNull P choice(@NotNull String key, @NotNull Number value) {
        return arg(Formatter.choice(key, value));
    }

    @Override
    public @NotNull P joining(@NotNull String key, @NotNull ComponentLike... values) {
        return arg(Formatter.joining(key, values));
    }

    @Override
    public @NotNull P argPlayer(
            @NotNull String key,
            @NotNull OfflinePlayer player
    ) {

        return arg(TagResolver.resolver(
                key, (args, context) -> {

                    String content = player.getName();
                    if (content == null) {
                        return Tag.selfClosingInserting(Component.text("Unknown Player name"));
                    }

                    Pointered target = context.target();

                    if (target != null) {
                        UUID render = target.get(Identity.UUID).orElse(null);

                        if (player.getUniqueId().equals(render)) {

                            Component you = Message.translatable("general.self").cmp(
                                    Message.asContext(target),
                                    false
                            );

                            return Tag.selfClosingInserting(you);
                        }
                    }

                    return Tag.preProcessParsed(content);
                }
        ));
    }
}
