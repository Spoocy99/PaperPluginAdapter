package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.adapter.messages.PluginMessage;
import dev.spoocy.utils.common.text.StringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class TextMessage extends Editable {

    public final Function<Localization, String> text;

    public TextMessage(@NotNull Function<Localization, String> text) {
        super(Message.EMPTY_STYLE);
        this.text = text;
    }

    public TextMessage(@NotNull Function<Localization, String> text, @Nullable TextColor color, @Nullable TextDecoration... decorations) {
        super(
                Message.style()
                        .color(color)
                        .decorateIfNotNull(decorations)
                        .build()
        );

        this.text = text;
    }

    @Override
    protected Component cmp0(@NotNull Localization locale, @NotNull TagResolver[] placeholders, boolean upperCase) {
        String value = text.apply(locale);
        if(StringUtils.isNullOrEmpty(value)) {
            return Component.empty();
        }


        return Component.text(upperCase ? firstUpperCase(value) : value);
    }
}
