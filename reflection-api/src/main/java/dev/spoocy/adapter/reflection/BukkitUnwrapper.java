package dev.spoocy.adapter.reflection;

import com.google.gson.internal.Primitives;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.utils.common.cache.Cache;
import dev.spoocy.utils.common.cache.Caches;
import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.accessor.FieldAccessor;
import dev.spoocy.utils.reflection.accessor.MethodAccessor;
import dev.spoocy.utils.reflection.unwrapper.Unwrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class BukkitUnwrapper implements Unwrapper {

    public static BukkitUnwrapper INSTANCE = new BukkitUnwrapper();

    private static final Cache<Class<?>, Unwrapper> unwrappers = Caches.createCache();

    private BukkitUnwrapper() { }

    @Nullable
    @Override
    public Object unwrapObject(@NotNull Object wrappedObject) {
        Class<?> currentClass = getClassOf(wrappedObject);

        if (currentClass.isPrimitive() || currentClass.equals(String.class)) {
            return null;
        }

        if (wrappedObject instanceof Collection) {
            BukkitLogger.debug("Object of class '{}' is Collection. Handling collection...", wrappedObject.getClass());
            return handleCollection((Collection<Object>) wrappedObject);
        }

        if (Primitives.isWrapperType(currentClass) || wrappedObject instanceof String) {
            BukkitLogger.debug("Object of class '{}' is Primitive.", wrappedObject.getClass());
            return null;
        }

        Unwrapper unwrapper = getUnwrapper(currentClass);

        if (unwrapper == null) {
            BukkitLogger.debug("No Unwrapper for Object '{}' found.", wrappedObject.getClass());
            return null;
        }

        return unwrapper.unwrapObject(wrappedObject);
    }

    private Object handleCollection(Collection<Object> wrappedObject) {
        Collection<Object> copy = new ArrayList<>();

        try {
            for (Object element : wrappedObject) {
            copy.add(unwrapObject(element));
        }
        } catch (Exception e) {
            BukkitLogger.error("Failed to unwrap collection.", e);
        }

        return copy;
    }

    private Unwrapper getUnwrapper(final Class<?> clazz) {

        if (unwrappers.contains(clazz)) {
            return unwrappers.get(clazz);
        }

        try {
            final Method handle = Reflection
                    .builder()
                    .forClass(clazz)
                    .privateMembers()
                    .build()
                    .method(Reflection.method().name("getHandle").build());

            if (handle == null) {
                throw new NoSuchMethodException("Cannot find method 'getHandle' in class '" + clazz.getName() + "'.");
            }

            Unwrapper unwrapper = wrappedObject -> {

                try {

                    if (wrappedObject instanceof Class) {
                        return getIfExpected((Class<?>) wrappedObject, clazz, handle.getReturnType());
                    }

                    return handle.invoke(wrappedObject);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    BukkitLogger.error("Method 'getHandle' in class {} is unaccessable.", clazz.getName(), e);
                    return null;
                }

            };

            unwrappers.add(clazz, unwrapper);
            return unwrapper;

        } catch (NoSuchMethodException e) {

            Unwrapper proxyUnwrapper = getProxyUnwrapper(clazz);
            if (proxyUnwrapper != null) {
                return proxyUnwrapper;
            }

            Unwrapper fieldUnwrapper = getFieldUnwrapper(clazz);
            if (fieldUnwrapper != null) {
                return fieldUnwrapper;
            }

        } catch (Throwable ignored) { }

        BukkitLogger.trace("No Unwrapper found. Class '{}' cannot be unwrapped.", clazz.getName());
        return null;
    }

    @Nullable
    private Unwrapper getProxyUnwrapper(final Class<?> clazz) {
        try {

            if (Player.class.isAssignableFrom(clazz)) {
                MethodAccessor getHandle = Reflection
                        .builder()
                        .forClass(MinecraftReflection.getCraftPlayerClass())
                        .publicMembers()
                        .buildAccess()
                        .method(Reflection.method().name("getHandle").build());

                if (getHandle == null) {
                    BukkitLogger.trace("Cannot find method 'getHandle' in CraftPlayer.");
                    return null;
                }

                Unwrapper unwrapper = wrappedObject -> {

                    try {

                        return getHandle.invoke(((Player) wrappedObject).getPlayer());
                    } catch (Exception e) {

                        return getHandle.invoke(Bukkit.getPlayer(((Player) wrappedObject).getUniqueId()));
                    }
                };

                unwrappers.add(clazz, unwrapper);
                return unwrapper;
            }

        } catch (Throwable e) {
            BukkitLogger.trace("Failed to get Proxy Unwrapper for class '{}'.", clazz.getName(), e);
        }

        return null;
    }

    @Nullable
    private Unwrapper getFieldUnwrapper(@NotNull final Class<?> clazz) {
        FieldAccessor accessor = Reflection
                .builder()
                .forClass(clazz)
                .privateMembers()
                .buildAccess()
                .field(Reflection.field().name("handle").build());

        if(accessor == null) {
            BukkitLogger.trace("Class '{}' does not have Field 'handle'.", clazz.getName());
            return null;
        }

        Unwrapper unwrapper = wrappedObject -> {

            try {

                if (wrappedObject instanceof Class) {
                    return getIfExpected((Class<?>) wrappedObject, clazz, accessor.getField().getType());
                }

                return accessor.get(wrappedObject);

            } catch (Exception e) {
                BukkitLogger.error("Failed to unwrap field for class {}.", clazz.getName(), e);
                return null;
            }

        };

        unwrappers.add(clazz, unwrapper);
        return unwrapper;
    }

}
