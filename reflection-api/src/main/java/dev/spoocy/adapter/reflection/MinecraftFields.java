package dev.spoocy.adapter.reflection;

import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.accessor.FieldAccessor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class MinecraftFields {

    public static Object getFieldValue(@NotNull Object nmsPlayer, @NotNull Class<?> fieldClazz) {
        FieldAccessor field = Reflection
                .builder()
                .forClass(nmsPlayer.getClass())
                .inheritedMembers()
                .buildAccess()
                .field(Reflection.field().type(fieldClazz).build());

        return field == null ? null : field.get(nmsPlayer);
    }

    public static Object getPlayerConnection(@NotNull Player player) {
        Object nmsplayer = BukkitUnwrapper.INSTANCE.unwrapObject(player);
        if (nmsplayer == null) {
            throw new IllegalArgumentException("Player is not an instance of EntityPlayer.");
        }
        return getPlayerConnection(nmsplayer);
    }

    private static volatile FieldAccessor CONNECTION;
    public static Object getPlayerConnection(@NotNull Object nmsPlayer) {
        if (CONNECTION == null) {
            Class<?> connectionClass = MinecraftReflection.getPlayerConnectionClass();

            CONNECTION = Reflection
                .builder()
                .forClass(nmsPlayer.getClass())
                .inheritedMembers()
                .buildAccess()
                .field(Reflection.field().type(connectionClass).build());
        }

        return CONNECTION.get(nmsPlayer);
    }

}
