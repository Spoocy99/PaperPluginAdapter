package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.messages.Localization;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class MiniMessage extends Editable {

    private final String message;

    public MiniMessage(@NotNull String message) {
        super(Message.EMPTY_STYLE);
        this.message = message;
    }

    @Override
    protected Component cmp0(@NotNull Localization locale, @NotNull TagResolver[] placeholders, boolean upperCase) {
        String value = upperCase ? firstUpperCase(message) : message;
        return Message.PROCESSOR.miniMessageSerializer().deserialize(value, placeholders);
    }

    @Override
    public String toString() {
        return "MiniMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
