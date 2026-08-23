package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.message.color.StyleImpl;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.adapter.messages.PluginMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class WrappedMessage extends Editable {

    private final Function<Localization, Component> component;

    public WrappedMessage(@NotNull Component component) {
        super(Message.EMPTY_STYLE);
        this.component = locale -> component;
    }

    public WrappedMessage(@NotNull Function<Localization, Component> component) {
        super(Message.EMPTY_STYLE);
        this.component = component;
    }

    @Override
    public List<Component> cmpList(@NotNull Localization locale, boolean reset) {
        return List.of(this.component.apply(locale));
    }

    @Override
    public Component cmp(@NotNull Localization locale, boolean reset) {
        return this.component.apply(locale);
    }

    @Override
    protected Component cmp0(@NotNull Localization locale, @NotNull TagResolver[] placeholders, boolean upperCase) {
        return this.component.apply(locale);
    }

    @Override
    public PluginMessage upperCase() {
        throw new UnsupportedOperationException("WrappedMessage cannot be upperCased");
    }
}
