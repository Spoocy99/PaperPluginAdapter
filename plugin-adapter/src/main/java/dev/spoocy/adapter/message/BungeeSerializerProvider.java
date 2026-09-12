package dev.spoocy.adapter.message;

import dev.spoocy.adapter.serializers.BungeeComponentSerializer;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class BungeeSerializerProvider {

    private static final BungeeComponentSerializer SERIALIZER = BungeeComponentSerializer.get();

    @NotNull
    public static BaseComponent[] serialize(@NotNull Component component) {
        return SERIALIZER.serialize(component);
    }

    public static Component serialize(@NotNull BaseComponent[] components) {
        return SERIALIZER.deserialize(components);
    }

}
