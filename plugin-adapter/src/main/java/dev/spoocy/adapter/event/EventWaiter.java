package dev.spoocy.adapter.event;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.log.BukkitLogger;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class EventWaiter {

    private final PluginAdapter plugin;

    public EventWaiter(@NotNull PluginAdapter plugin) {
        this.plugin = plugin;
    }

    public <T extends Event> Builder<T> waitFor(@NotNull Class<T> eventType) {
        return new Builder<>(eventType);
    }

    public class Builder<T extends Event> {

        private final Class<T> eventType;
        private Predicate<T> condition = (e) -> true;
        private Consumer<T> action = (e) -> { };
        private Runnable timeoutAction = () -> { };
        private long timeout = -1;

        private Builder(@NotNull Class<T> eventType) {
            this.eventType = eventType;
        }

        public Builder<T> runIf(@NotNull Predicate<T> condition) {
            this.condition = condition;
            return this;
        }

        public Builder<T> run(@NotNull Consumer<T> action) {
            this.action = action;
            return this;
        }

        public Builder<T> timeoutAfter(long ticks) {
            this.timeout = ticks;
            return this;
        }

        public Builder<T> runOnTimeout(@NotNull Runnable action) {
            this.timeoutAction = action;
            return this;
        }

        @SuppressWarnings("unchecked")
        public WaitingEvent<T> build() {
            WaitingEvent<T> event = new WaitingEvent<>(condition, action);
            Bukkit.getPluginManager().registerEvent(eventType, event, EventPriority.LOWEST, (listener, e) -> ((WaitingEvent<T>) listener).attempt((T) e), plugin);

            if (timeout > 0) {

                Bukkit.getScheduler().runTaskLater(plugin, () -> {

                    if (event.wasExecuted()) {
                        return;
                    }

                    HandlerList.unregisterAll(event);

                    try {
                        timeoutAction.run();
                    } catch (Throwable ex) {
                        BukkitLogger.error("Failed to run timeout Action.", ex);
                    }

                }, timeout);

            }

            return event;
        }

    }

    public static final class WaitingEvent<T extends Event> implements Listener {
        private final Predicate<T> condition;
        private final Consumer<T> action;
        private boolean executed = false;

        private WaitingEvent(@NotNull Predicate<T> condition, @NotNull Consumer<T> action) {
            this.condition = condition;
            this.action = action;
        }

        public boolean attempt(@NotNull T event) {
            if (executed) return false;

            if (condition.test(event)) {
                executed = true;
                HandlerList.unregisterAll(this);
                action.accept(event);
                return true;
            }
            return false;
        }

        public boolean wasExecuted() {
            return executed;
        }
    }

}
