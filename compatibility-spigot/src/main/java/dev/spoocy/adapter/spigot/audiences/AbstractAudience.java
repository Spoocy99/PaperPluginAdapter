package dev.spoocy.adapter.spigot.audiences;

import dev.spoocy.adapter.spigot.SpigotCompatibilityProvider;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractAudience implements Audience {

    @NotNull
    protected static String renderText(@NotNull ComponentLike message) {
        return SpigotCompatibilityProvider.BUNGEE_TEXT_SERIALIZER.serialize(message.asComponent());
    }

    @NotNull
    protected static BaseComponent[] renderBungee(@NotNull ComponentLike message) {
        Component component = message.asComponent();
        return SpigotCompatibilityProvider.BUNGEE_COMPONENT_SERIALIZER.serialize(message.asComponent());
    }

}
