package dev.spoocy.adapter.message;

import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@FunctionalInterface
public interface LocalizedComponent {

    LocalizedComponent EMPTY = l -> Component.empty();

    @NotNull
    static LocalizedComponent of(@NotNull Component component) {
        Args.notNull(component, "component");
        return l -> component;
    }

    @NotNull
    static LocalizedComponent wrap(@NotNull Function<Locale, Component> function) {
        Args.notNull(function, "function");
        return function::apply;
    }

    @NotNull
    Component cmp(@NotNull Locale locale);

    /**
     * Used for lores on items as there is no support for {@link Component#newline() lines}.
     */
    @ApiStatus.OverrideOnly
    default List<Component> cmpLines(@NotNull Locale locale) {
        return List.of(cmp(locale));
    }
}
