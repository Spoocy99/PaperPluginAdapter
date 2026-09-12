package dev.spoocy.adapter.message.types.impl;

import dev.spoocy.adapter.message.TextMessage;
import dev.spoocy.adapter.message.style.MessageStyle;
import dev.spoocy.adapter.message.types.AbstractArgumentsMessage;
import dev.spoocy.adapter.message.types.Arguments;
import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ContentMessage extends AbstractArgumentsMessage<TextMessage> implements TextMessage, Arguments<TextMessage> {

    private final boolean isMiniMessageFormat;

    @NotNull
    private final String content;

    @Unmodifiable
    @NotNull
    private final List<TagResolver> tagResolvers;

    public ContentMessage(boolean isMiniMessageFormat, @NotNull String content, @NotNull MessageStyle style) {
        this(isMiniMessageFormat, content, List.of(), style);
    }

    private ContentMessage(
            boolean isMiniMessageFormat,
            @NotNull String content,
            @NotNull List<TagResolver> tagResolvers,
            @NotNull MessageStyle style
    ) {
        super(style);
        this.isMiniMessageFormat = isMiniMessageFormat;
        this.content = Args.notNull(content, "content");
        this.tagResolvers = List.copyOf(Args.notNull(tagResolvers, "tagResolvers"));
    }

    @Override
    protected @NotNull TextMessage newInstance(@NotNull MessageStyle style) {
        return new ContentMessage(this.isMiniMessageFormat, this.content, this.tagResolvers, style);
    }

    @Override
    public @NotNull TextMessage withContent(@NotNull String content) {
        return new ContentMessage(this.isMiniMessageFormat, content, this.tagResolvers, this.style());
    }

    @Override
    public boolean isMiniMessageFormat() {
        return this.isMiniMessageFormat;
    }

    @Override
    public @NotNull String getContent() {
        return this.content;
    }

    @Unmodifiable
    @Override
    public @NotNull List<TagResolver> getTagResolvers() {
        return this.tagResolvers;
    }


    @Override
    protected TextMessage withTagResolver(@NotNull TagResolver resolver) {
        List<TagResolver> tagResolvers = new ArrayList<>(this.tagResolvers);
        tagResolvers.add(resolver);
        return new ContentMessage(this.isMiniMessageFormat, this.content, tagResolvers, this.style());
    }

    @Override
    public String toString() {
        return "ContentMessage{" +
                "content='" + content + '\'' +
                '}';
    }
}
