package dev.spoocy.adapter.scheduler;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.utils.common.scheduler.Scheduler;
import dev.spoocy.utils.common.scheduler.task.CompletableTask;
import dev.spoocy.utils.common.scheduler.task.Task;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class BukkitScheduler implements Scheduler {

    private final PluginAdapter plugin;

    public BukkitScheduler(@NotNull PluginAdapter plugin) {
        this.plugin = plugin;
    }

    @Override
    public void executeSync(@NotNull Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    @Override
    public void executeAsync(@NotNull Runnable runnable) {
        Bukkit.getScheduler()
                .runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public <V> Task<V> runSync(@NotNull Runnable runnable) {
        return runSyncCallable(() -> {
            runnable.run();
            return null;
        });
    }

    @Override
    public <V> Task<V> runAsync(@NotNull Runnable runnable) {
        return runAsyncCallable(() -> {
            runnable.run();
            return null;
        });
    }

    @Override
    public <V> Task<V> runDelayed(@NotNull Runnable runnable, long delay, @NotNull TimeUnit unit) {
        return runDelayedCallable(() -> {
            runnable.run();
            return null;
        }, delay, unit);
    }

    @Override
    public <V> Task<V> runSyncCallable(@NotNull Callable<V> callable) {
        return callSync(callable);
    }

    @Override
    public <V> Task<V> runAsyncCallable(@NotNull Callable<V> callable) {
        return callAsync(callable);
    }

    @Override
    public <V> Task<V> runDelayedCallable(@NotNull Callable<V> callable, long delay, @NotNull TimeUnit unit) {
        return callAsyncDelayed(callable, delay, unit);
    }

    @Override
    public <V> Task<V> runSyncSupplier(@NotNull Supplier<V> supplier) {
        return callSync(supplier::get);
    }

    @Override
    public <V> Task<V> runAsyncSupplier(@NotNull Supplier<V> supplier) {
        return callAsync(supplier::get);
    }

    @Override
    public <V> Task<V> runDelayedSupplier(@NotNull Supplier<V> supplier, long delay, @NotNull TimeUnit unit) {
        return callAsyncDelayed(supplier::get, delay, unit);
    }

    private <V> Task<V> callSync(@NotNull Callable<V> callable) {
        CompletableTask<V> task = CompletableTask.empty();

        Bukkit.getScheduler().runTask(plugin, () -> {
                    try {
                        task.complete(callable.call());
                    } catch (Throwable ex) {
                        task.fail(ex);
                    }
                });

        return task;
    }

    private <V> Task<V> callAsync(@NotNull Callable<V> callable) {
        CompletableTask<V> task = CompletableTask.empty();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {
                        task.complete(callable.call());
                    } catch (Throwable ex) {
                        task.fail(ex);
                    }
                });

        return task;
    }

    private <V> Task<V> callAsyncDelayed(@NotNull Callable<V> callable, long delay, TimeUnit unit) {
        CompletableTask<V> task = CompletableTask.empty();

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                    try {
                        task.complete(callable.call());
                    } catch (Throwable ex) {
                        task.fail(ex);
                    }
                }, toTicks(delay, unit));

        return task;
    }

    private long toTicks(long delay, TimeUnit unit) {

        switch (unit) {
            case NANOSECONDS:
                return delay / 1000000;
            case MICROSECONDS:
                return delay / 1000;
            case MILLISECONDS:
                return delay;
            case SECONDS:
                return delay * 20;
            case MINUTES:
                return delay * 20 * 60;
            case HOURS:
                return delay * 20 * 60 * 60;
            case DAYS:
                return delay * 20 * 60 * 60 * 24;
            default:
                throw new IllegalArgumentException("Unknown time unit " + unit);
        }
    }

}
