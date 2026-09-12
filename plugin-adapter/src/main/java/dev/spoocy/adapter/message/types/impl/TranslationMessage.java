package dev.spoocy.adapter.message.types.impl;

import dev.spoocy.adapter.message.TranslatableMessage;
import dev.spoocy.adapter.message.serialization.RenderContext;
import dev.spoocy.adapter.message.style.MessageStyle;
import dev.spoocy.adapter.message.types.AbstractArgumentsMessage;
import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class TranslationMessage extends AbstractArgumentsMessage<TranslatableMessage> implements TranslatableMessage {

    @NotNull
    private final String key;

    @Nullable
    private final String fallback;

    @Unmodifiable
    @NotNull
    private final List<ComponentLike> args;

    public TranslationMessage(@NotNull String key, @Nullable String fallback, @NotNull MessageStyle style) {
        this(key, fallback, List.of(), style);
    }

    private TranslationMessage(@NotNull String key, @Nullable String fallback, @NotNull List<ComponentLike> args, @NotNull MessageStyle style) {
        super(style);
        this.key = Args.notNull(key, "key");
        this.fallback = fallback;
        this.args = List.copyOf(args);
    }

    @Override
    protected @NonNull TranslatableMessage newInstance(@NotNull MessageStyle style) {
        return new TranslationMessage(this.key, this.fallback, this.args, style);
    }

    @Override
    public @NotNull String getTranslationKey() {
        return this.key;
    }

    @Override
    public @NotNull TranslatableComponent asTranslatableComponent(@Nullable RenderContext context) {
        List<ComponentLike> args = new LinkedList<>();

        if (context != null) {
            // add context if possible so this reaches underlying
            // translatable components in arguments
            args.add(Argument.target(context));
        }

        // copy rest of arguments to new list
        args.addAll(this.args);

        return Component.translatable(this.key, this.fallback, args);
    }

    @Override
    public @NotNull TranslatableMessage args(@NotNull ComponentLike... arguments) {
        if(arguments == null ||arguments.length == 0) {
            return this;
        }

        List<ComponentLike> args = new ArrayList<>(this.args);
        args.addAll(List.of(arguments));
        return new TranslationMessage(this.key, this.fallback, args, this.style());
    }

    @Override
    protected TranslatableMessage withTagResolver(@NotNull TagResolver resolver) {
        return args(Argument.tagResolver(resolver));
    }

    @Override
    public String toString() {
        return "TranslatableMessage{" +
                "key='" + key + '\'' +
                '}';
    }
}
