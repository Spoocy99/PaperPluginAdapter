package dev.spoocy.adapter.message.serialization;

import dev.spoocy.adapter.language.Localization;
import dev.spoocy.adapter.language.LocalizedReceiver;
import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.pointer.Pointers;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface RenderContext extends LocalizedReceiver, Pointered {

    @Contract(value = "_ -> new", pure = true)
    static @NotNull RenderContext of(@NotNull Pointers pointers) {
        Args.notNull(pointers, "pointers");

        return new Static(pointers);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull RenderContext locale(@NotNull Locale locale) {
        Args.notNull(locale, "locale");

        return new Static(
                Pointers.builder()
                        .withStatic(Identity.LOCALE, locale)
                        .build()
        );
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull RenderContext target(@NotNull Pointered target) {
        Args.notNull(target, "target");
        return new Target(target);
    }

    @Override
    @NotNull
    Pointers pointers();

    @Override
    @NotNull
    default Locale getLocale() {
        return this.getOrDefault(Identity.LOCALE, Localization.DEFAULT_LOCALE);
    }

    class Static implements RenderContext {

        private final Pointers pointers;

        public Static(@NotNull Pointers pointers) {
            this.pointers = pointers;
        }

        @Override
        public @NotNull Pointers pointers() {
            return this.pointers;
        }
    }

    class Target implements RenderContext {

        private final Pointered target;

        public Target(@NotNull Pointered target) {
            this.target = target;
        }

        @Override
        public @NotNull Pointers pointers() {
            return target.pointers();
        }

        @Override
        public @NotNull Locale getLocale() {
            return target.getOrDefault(Identity.LOCALE, Localization.DEFAULT_LOCALE);
        }
    }

}
