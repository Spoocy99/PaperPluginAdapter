package dev.spoocy.adapter.reflection;

import dev.spoocy.utils.common.cache.Cache;
import dev.spoocy.utils.common.cache.Caches;
import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.accessor.ClassAccess;
import dev.spoocy.utils.reflection.accessor.MethodAccessor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public final class MinecraftMethods {

    private MinecraftMethods() { }

    private volatile static MethodAccessor sendPacketMethod;

    public static MethodAccessor getSendPacketMethod() {
        if (sendPacketMethod == null) {

            final ClassAccess playerConnectionClass = Reflection
                    .builder()
                    .forClass(MinecraftReflection.getPlayerConnectionClass())
                    .privateMembers()
                    .buildAccess();

            try {
                sendPacketMethod = playerConnectionClass.method(
                        Reflection.method()
                                .parameterCount(1)
                                .parameterType(0, MinecraftReflection.getPacketClass())
                                .returnTypeVoid()
                                .build()
                );

            } catch (Exception e) {
                sendPacketMethod = playerConnectionClass.method(
                        Reflection.method()
                                .name("sendPacket.*")
                                .parameterCount(1)
                                .returnTypeVoid()
                                .build()
                );
            }
        }

        return sendPacketMethod;
    }

    private volatile static MethodAccessor disconnectMethod;

    public static MethodAccessor getDisconnectMethod(@NotNull Class<?> playerConnection) {
        if (disconnectMethod == null) {

            ClassAccess playerConnectionAccess = Reflection
                    .builder()
                    .forClass(playerConnection)
                    .privateMembers()
                    .buildAccess();


            try {
                disconnectMethod = playerConnectionAccess
                        .method(
                                Reflection.method()
                                        .name("disconnect.*")
                                        .parameterCount(1)
                                        .returnTypeVoid()
                                        .build()
                        );
            } catch (Exception e) {
                disconnectMethod = playerConnectionAccess
                        .method(
                                Reflection.method()
                                        .parameterCount(1)
                                        .parameterType(0, String.class)
                                        .returnTypeVoid()
                                        .build()
                        );
            }
        }

        return disconnectMethod;
    }

    private static final Cache<Class<?>, MethodAccessor> CACHED_HANDLE_METHODS = Caches.createCache();

    public static MethodAccessor getHandleMethod(@NotNull Class<?> craftClass) {
        if (CACHED_HANDLE_METHODS.contains(craftClass)) {
            return CACHED_HANDLE_METHODS.get(craftClass);
        }

        ClassAccess craftClassAccess = Reflection
                .builder()
                .forClass(craftClass)
                .privateMembers()
                .buildAccess();

        MethodAccessor getHandleMethod;

        try {
            getHandleMethod = craftClassAccess.method(
                    Reflection.method()
                            .name("getHandle")
                            .parameterCount(0)
                            .build()
            );
        } catch (Exception e) {
            getHandleMethod = craftClassAccess
                    .method(
                            Reflection.method()
                                    .name("getHandle")
                                    .build()
                    );
        }

        if (getHandleMethod == null) {
            throw new IllegalArgumentException("Class '" + craftClass.getCanonicalName() + "' does not have a 'getHandle' method.");
        }

        CACHED_HANDLE_METHODS.add(craftClass, getHandleMethod);
        return getHandleMethod;
    }



}
