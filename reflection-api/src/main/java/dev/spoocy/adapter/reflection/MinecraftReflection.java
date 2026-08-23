package dev.spoocy.adapter.reflection;

import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.utils.reflection.ClassSource;
import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.ReflectionPackage;
import dev.spoocy.utils.reflection.accessor.MethodAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public final class MinecraftReflection {

    private static final ClassSource CLASS_SOURCE = ClassSource.fromClassLoader();

    private static ReflectionPackage MINECRAFT_PACKAGE;
    private static ReflectionPackage CRAFTBUKKIT_PACKAGE;

    private static ClassSource getClassSource() {
        return CLASS_SOURCE;
    }

    @Nullable
    private static Class<?> getClass(@NotNull String className) {
        return getClassSource().loadClass(className).orElse(null);
    }

    @Nullable
    public static Class<?> getMinecraftClass(@NotNull String className) {
        if (MINECRAFT_PACKAGE == null) {
            MINECRAFT_PACKAGE = new ReflectionPackage(MinecraftPackages.getMinecraftPackageName(), CLASS_SOURCE);
        }

        return MINECRAFT_PACKAGE.getPackageClass(className).orElse(null);
    }

    @Nullable
    public static Class<?> getMinecraftClass(@NotNull String className, @NotNull String... aliases) {
        if (MINECRAFT_PACKAGE == null) {
            MINECRAFT_PACKAGE = new ReflectionPackage(MinecraftPackages.getMinecraftPackageName(), CLASS_SOURCE);
        }

        return MINECRAFT_PACKAGE.getPackageClass(className).orElseGet(() -> {
            Class<?> resolved = null;
            for (String alias : aliases) {
                resolved = MINECRAFT_PACKAGE.getPackageClass(alias).orElse(null);

                if (resolved != null) {
                    break;
                }
            }

            if (resolved != null) {
                MINECRAFT_PACKAGE.setPackageClass(className, resolved);
                return resolved;
            }

            return null;
        });
    }

    private static Class<?> setMinecraftClass(@NotNull String className, @NotNull Class<?> clazz) {
        if (MINECRAFT_PACKAGE == null) {
            MINECRAFT_PACKAGE = new ReflectionPackage(MinecraftPackages.getMinecraftPackageName(), CLASS_SOURCE);
        }
        MINECRAFT_PACKAGE.setPackageClass(className, clazz);
        return clazz;
    }

    private static Class<?> fallbackMethodReturn(@NotNull String nmsClass, @NotNull String craftClass, @NotNull String methodName) {
        Method result = Reflection
                .builder()
                .forClass(getCraftBukkitClass(craftClass))
                .inheritedMembers()
                .build()
                 .method(Reflection.method().name(methodName).build());

        if (result == null) {
            BukkitLogger.error("Cannot fall back to method: {} in craftClass {} for NMS Class {}", methodName, craftClass, nmsClass);
        }

        return setMinecraftClass(nmsClass, result.getReturnType());
    }

    public static Class<?> getCraftBukkitClass(@NotNull String className) {
        if (CRAFTBUKKIT_PACKAGE == null) {
            CRAFTBUKKIT_PACKAGE = new ReflectionPackage(MinecraftPackages.getCraftBukkitPackage(), CLASS_SOURCE);
        }
        return CRAFTBUKKIT_PACKAGE.getPackageClass(className).orElse(null);
    }

    public static boolean is(@Nullable Class<?> clazz, @Nullable Class<?> test) {
        if (clazz == null || test == null) {
            return false;
        }

        return clazz.isAssignableFrom(test);
    }

    @Nullable
    public static Object getCraftObject(Object craftEntity) {
        MethodAccessor getHandleMethod = MinecraftMethods.getHandleMethod(craftEntity.getClass());
        if (getHandleMethod == null) {
            return null;
        }
        return getHandleMethod.invoke(craftEntity);
    }

    public static Class<?> getCraftEntityClass() {
        return getCraftBukkitClass("entity.CraftEntity");
    }

    public static Class<?> getCraftWorldClass() {
        return getCraftBukkitClass("CraftWorld");
    }

    public static Class<?> getCraftPlayerClass() {
        return getCraftBukkitClass("entity.CraftPlayer");
    }

     public static Class<?> getCraftItemStackClass() {
        return getCraftBukkitClass("inventory.CraftItemStack");
    }

    public static Class<?> getCraftMessageClass() {
        return getCraftBukkitClass("util.CraftChatMessage");
    }

    public static Class<?> getCommandMapClass() {
        return getCraftBukkitClass("command.CraftCommandMap");
    }

    public static Class<?> getPacketClass() {
        return getMinecraftClass("network.protocol.Packet", "Packet");
    }

    public static Class<?> getPlayerConnectionClass() {
        return getMinecraftClass("server.network.PlayerConnection", "server.network.ServerGamePacketListenerImpl", "PlayerConnection");
    }

    public static Class<?> getEntityPlayerClass() {
        return getMinecraftClass("server.level.EntityPlayer", "EntityPlayer");
    }

    public static Class<?> getBlockClass() {
        return getMinecraftClass("world.level.block.Block", "Block");
    }

    public static Class<?> getWorldClass() {
        return getMinecraftClass("world.level.World", "World");
    }

    public static Class<?> getWorldServerClass() {
        try {
            return getMinecraftClass("server.level.WorldServer", "server.level.ServerLevel", "WorldServer");
        } catch (RuntimeException e) {
            return fallbackMethodReturn("WorldServer", "CraftWorld", "getHandle");
        }
    }

    public static Class<?> getWorldBorderClass() {
        return getMinecraftClass("world.level.border.WorldBorder", "WorldBorder");
    }

    public static Class<?> getBlockPositionClass() {
        return getMinecraftClass("net.minecraft.core.BlockPosition", "BlockPosition");
    }

    public static Class<?> getGameProfileClass() {
        return getClass("com.mojang.authlib.GameProfile");
    }

    public static Class<?> getPlayerInteractManagerClass() {
        return getMinecraftClass("server.level.PlayerInteractManager", "PlayerInteractManager");
    }

    public static Class<?> getDimensionManagerClass() {
        return getMinecraftClass("server.level.dimension.DimensionManager", "DimensionManager");
    }

    public static Class<?> getEnumGamemodeClass() {
        return getMinecraftClass("world.level.EnumGamemode", "EnumGamemode");
    }

    public static Class<?> getResourceKeyClass() {
        return getMinecraftClass("resources.ResourceKey", "ResourceKey");
    }

    public static Class<?> getHolderClass() {
        return getMinecraftClass("core.Holder", "Holder");
    }

    public static Class<?> getBiomeManagerClass() {
        return getMinecraftClass("server.level.biome.BiomeManager", "BiomeManager");
    }
}
