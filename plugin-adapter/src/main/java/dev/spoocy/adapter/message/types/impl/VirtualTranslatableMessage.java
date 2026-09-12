package dev.spoocy.adapter.message.types.impl;

import dev.spoocy.adapter.language.Localization;
import dev.spoocy.adapter.message.TextMessage;
import dev.spoocy.adapter.message.style.MessageStyle;
import dev.spoocy.adapter.message.types.AbstractArgumentsMessage;
import dev.spoocy.adapter.message.types.Arguments;
import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class VirtualTranslatableMessage extends AbstractArgumentsMessage<TextMessage> implements TextMessage, Arguments<TextMessage> {

    @TagPattern
    private static final String TAG_NAME = "virtual_translated_content";

    @NotNull
    private final Function<Locale, String> content;

    @Unmodifiable
    @NotNull
    private final List<TagResolver> tagResolvers;

    public VirtualTranslatableMessage(
            @NotNull Function<Locale, String> content,
            @NotNull MessageStyle style
    ) {
        this(content, List.of(), style);
    }

    private VirtualTranslatableMessage(
            @NotNull Function<Locale, String> content,
            @NotNull List<TagResolver> tagResolvers,
            @NotNull MessageStyle style
    ) {
        super(style);
        this.content = Args.notNull(content, "content");
        this.tagResolvers = List.copyOf(Args.notNull(tagResolvers, "tagResolvers"));
    }

    @Override
    protected @NonNull TextMessage newInstance(@NotNull MessageStyle style) {
        return new VirtualTranslatableMessage(this.content, this.tagResolvers, style);
    }

    @Override
    public @NotNull TextMessage withContent(@NotNull String content) {
        throw new UnsupportedOperationException("Message is virtually translated.");
    }

    @Override
    public boolean isMiniMessageFormat() {
        return true;
    }

    @Override
    public @NotNull String getContent() {
        return "<" + TAG_NAME +">";
    }

    @Unmodifiable
    @Override
    public @NotNull List<TagResolver> getTagResolvers() {
        List<TagResolver> resolvers = new LinkedList<>();

        resolvers.add(TagResolver.resolver(TAG_NAME, (args, context) -> {

            Pointered target = context.target();
            Locale locale;

            if(target == null) {
                locale = Localization.DEFAULT_LOCALE;
            } else {
                locale = target.get(Identity.LOCALE).orElse(Localization.DEFAULT_LOCALE);
            }

            String content = this.content.apply(locale);
            return Tag.preProcessParsed(content);
        }));

        resolvers.addAll(this.tagResolvers);
        return resolvers;
    }

    @Override
    protected TextMessage withTagResolver(@NotNull TagResolver resolver) {
        List<TagResolver> tagResolvers = new ArrayList<>(this.tagResolvers);
        tagResolvers.add(resolver);
        return new VirtualTranslatableMessage(this.content, tagResolvers, this.style());
    }
}
