package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.object.ObjectContents;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ObjectContentMessage extends NonEditable {

    private final Function<Localization, ObjectContents> content;

    public ObjectContentMessage(@NotNull ObjectContents content) {
        Args.notNull(content, "content");
        this.content = locale -> content;
    }

    public ObjectContentMessage(@NotNull Function<Localization, ObjectContents> content) {
        Args.notNull(content, "content");
        this.content = content;
    }

    @Override
    public List<Component> cmpList(@NotNull Localization locale, boolean reset) {
        return List.of(cmp(locale, reset));
    }

    @Override
    public Component cmp(@NotNull Localization locale, boolean reset) {
        return Component.object(this.content.apply(locale));
    }

}
