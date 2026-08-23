package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.utils.common.collections.Collector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ListTranslatableMessage extends Editable {

    private final String key;

    public ListTranslatableMessage(@NotNull String key) {
        super(Message.EMPTY_STYLE);
        this.key = key;
    }

    @Override
    protected Component cmp0(@NotNull Localization locale, @NotNull TagResolver[] placeholders, boolean upperCase) {
        List<String> messages = locale.msgList(this.key);
        if(messages.isEmpty()) {
            return Component.empty();
        }

        String firstValue = upperCase ? firstUpperCase(messages.get(0)) : messages.get(0);
        Component first = Message.PROCESSOR.miniMessageSerializer().deserialize(firstValue, placeholders);

        for(int i = 1; i < messages.size(); i++) {
            String value = messages.get(i);
            Component part = Message.PROCESSOR.miniMessageSerializer().deserialize(value, placeholders);
            first = first.append(Component.newline()).append(part);
        }

        return first;
    }

    @Override
    protected List<Component> cmpList0(@NotNull Localization locale, @NotNull TagResolver[] placeholders, boolean upperCase, boolean reset) {
        List<String> messages = locale.msgList(this.key);
        if(messages.isEmpty()) {
            return List.of(Component.empty());
        }

        return Collector.of(messages)
                .map(value -> {
                    String mess = upperCase ? firstUpperCase(value) : value;
                    Component cmp = Message.PROCESSOR.miniMessageSerializer().deserialize(mess, placeholders);
                    return applyTo(cmp, locale, reset);
                })
                .asList();
    }

    @Override
    public String toString() {
        return "TranslatableMessage{" +
                "key='" + key + '\'' +
                '}';
    }
}
