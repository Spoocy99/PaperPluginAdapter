package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.messages.Localization;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class TranslatableMessage extends Editable {

    private final String key;

    public TranslatableMessage(@NotNull String key) {
        super(Message.EMPTY_STYLE);
        this.key = key;
    }

    public TranslatableMessage(@NotNull String key, @Nullable TextColor color, @Nullable TextDecoration... decorations) {
        super(
                Message.style()
                        .color(color)
                        .decorateIfNotNull(decorations)
                        .build()
        );
        this.key = key;
    }

    @Override
    protected Component cmp0(@NotNull Localization locale, @NotNull TagResolver[] placeholders, boolean upperCase) {
        String value = upperCase ? firstUpperCase(locale.msg(key)) : locale.msg(key);
        return Message.PROCESSOR.miniMessageSerializer().deserialize(value, placeholders);
    }

    @Override
    public String toString() {
        return "TranslatableMessage{" +
                "key='" + key + '\'' +
                '}';
    }
}
