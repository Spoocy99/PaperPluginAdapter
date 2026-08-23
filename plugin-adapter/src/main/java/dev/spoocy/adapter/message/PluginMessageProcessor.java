package dev.spoocy.adapter.message;

import dev.spoocy.adapter.message.color.Color;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class PluginMessageProcessor {

    public final PlainTextComponentSerializer PLAIN_SERIALIZER = PlainTextComponentSerializer
            .plainText();

    public final LegacyComponentSerializer BUNGEE_TEXT_SERIALIZER = LegacyComponentSerializer
            .builder()
            .character(LegacyComponentSerializer.SECTION_CHAR)
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    private MiniMessage MINIMESSAGE_SERIALIZER;

    private final List<TagResolver> customPlaceholders = new ArrayList<>();

    public PluginMessageProcessor() {
        registerPlaceholder(
                Placeholder.styling("h", Color.HIGHLIGHT),
                Placeholder.styling("e", Color.ERROR)
        );
        refreshSerializer();
    }

    public void registerPlaceholder(@NotNull TagResolver placeholder, @NotNull TagResolver... placeholders) {
        this.customPlaceholders.add(placeholder);
        Collections.addAll(this.customPlaceholders, placeholders);
        refreshSerializer();
    }

    private void refreshSerializer() {
        this.MINIMESSAGE_SERIALIZER = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolvers(StandardTags.defaults())
                        .resolvers(customPlaceholders)
                        .build()
                )
                .build();
    }

    public MiniMessage miniMessageSerializer() {
        return this.MINIMESSAGE_SERIALIZER;
    }

    public PlainTextComponentSerializer plainSerializer() {
        return this.PLAIN_SERIALIZER;
    }

    public LegacyComponentSerializer bungeeSerializer() {
        return this.BUNGEE_TEXT_SERIALIZER;
    }

}
