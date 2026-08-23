package dev.spoocy.adapter.compatibility;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class PAudience {

    public static Audience combine(@NotNull Audience... audiences) {
        return Audience.audience(audiences);
    }

    public static Audience combine(@NotNull Collection<Audience> audiences) {
        return Audience.audience(audiences);
    }

    public static void sendMessage(@NotNull Audience audience, @NotNull Component input) {
        audience.sendMessage(input);
    }

    public static void sendActionbar(@NotNull Audience audience, @NotNull Component input) {
        audience.sendActionBar(input);
    }

    public static void clearActionbar(@NotNull Audience audience) {
        audience.sendActionBar(Component.empty());
    }

    public static void sendTitle(@NotNull Audience audience, @NotNull Component title, @NotNull Component subtitle) {
        audience.showTitle(Title.title(title, subtitle));
    }

    public static void sendTitle(@NotNull Audience audience, @NotNull Component title, @NotNull Component subtitle, @NotNull Duration fadeIn, @NotNull Duration stay, @NotNull Duration fadeOut) {
        audience.showTitle(Title.title(title, subtitle, Title.Times.times(fadeIn, stay, fadeOut)));
    }

    public static void clearTitle(@NotNull Audience audience) {
        audience.clearTitle();
    }

    public static void resetTitle(@NotNull Audience audience) {
        audience.resetTitle();
    }

}
