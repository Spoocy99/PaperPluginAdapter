package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.message.MessageLike;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.TemporalAccessor;
import java.util.function.Supplier;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Arguments<P extends Arguments<P>> {

    @CheckReturnValue
    @Contract("_ -> new")
    @NotNull
    P arg(@NotNull TagResolver tagResolver);

    @CheckReturnValue
    @Contract("_, _ -> new")
    @NotNull
    P arg(@TagPattern @NotNull String key, @NotNull MessageLike message);

    @CheckReturnValue
    @Contract("_, _ -> new")
    @NotNull
    P arg(@TagPattern @NotNull String key, @NotNull String minimessage);

    @CheckReturnValue
    @Contract("_, _ -> new")
    @NotNull
    P arg(@TagPattern @NotNull String key, @NotNull Supplier<String> arg);

    @CheckReturnValue
    @Contract("_, _ -> new")
    @NotNull
    P arg(@TagPattern @NotNull String key, @NotNull ComponentLike component);

    @CheckReturnValue
    @Contract("_, _ -> new")
    @NotNull
    P argText(@TagPattern @NotNull String key, @NotNull String text);

    @CheckReturnValue
    @Contract("_, _, _, _ -> new")
    @NotNull
    P argText(@TagPattern @NotNull String key, @NotNull String text, @NotNull TextColor color, @NotNull TextDecoration... decorations);

    @CheckReturnValue
    @Contract("_, _ -> new")
    @NotNull
    P argNum(@TagPattern @NotNull String key, @NotNull Number number);

    @CheckReturnValue
    @Contract("_, _, _, _ -> new")
    @NotNull
    P argNum(@TagPattern @NotNull String key, @NotNull Number number, @NotNull TextColor color, @NotNull TextDecoration... decorations);

    @CheckReturnValue
    @Contract("_, _ -> new")
    @NotNull
    P argDate(@TagPattern @NotNull String key, @NotNull TemporalAccessor value);

    @CheckReturnValue
    @Contract("_, _ -> new")
    @NotNull
    P boolChoice(@TagPattern @NotNull String key, boolean value);

    @CheckReturnValue
    @Contract("_, _ -> new")
    @NotNull
    P choice(@TagPattern @NotNull String key, @NotNull Number value);

    @CheckReturnValue
    @Contract("_, _ -> new")
    @NotNull
    P joining(@TagPattern @NotNull String key, @NotNull ComponentLike... values);

    @CheckReturnValue
    @Contract("_, _ -> new")
    @NotNull
    default P translatableArg(@TagPattern @NotNull String key, @NotNull String translationKey) {
        return arg(key, Message.translatable(translationKey));
    }

    @CheckReturnValue
    @Contract("_, _, _, _ -> new")
    @NotNull
    default P translatableArg(@TagPattern @NotNull String key, @NotNull String translationKey, @NotNull TextColor color, @NotNull TextDecoration... decorations) {
        return arg(key, Message.translatable(translationKey, color, decorations));
    }

    /**
     * Adds an argument that displays the player's name or
     * the translation "general.self" to the player itself.
     *
     * @param key The argument key
     * @param player the player
     *
     * @return a new instance.
     */
    @CheckReturnValue
    @Contract("_, _ -> new")
    @NotNull
    P argPlayer(@TagPattern @NotNull String key, @NotNull OfflinePlayer player);


}
