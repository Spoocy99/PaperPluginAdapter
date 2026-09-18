package dev.spoocy.adapter.dependencies.service;

import dev.spoocy.adapter.dependencies.Dependency;
import dev.spoocy.utils.common.misc.Args;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class DefaultLoadedDependency<T> implements Dependency<T> {

    private final Class<T> type;
    private final T instance;

    public DefaultLoadedDependency(@NotNull Class<T> type, @NonNull T instance) {
        this.type = Args.notNull(type, "type");
        this.instance = Args.notNull(instance, "instance");
    }


    @Override
    public boolean is(@NotNull Class<?> type) {
        return type.isAssignableFrom(this.type);
    }

    @Override
    public @NotNull Class<T> type() {
        return this.type;
    }

    @Override
    public @NonNull T dependency() {
        return this.instance;
    }
}
