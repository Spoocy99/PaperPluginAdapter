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

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ListenAdapter extends Listener {

    EventPriority DEFAULT_PRIORITY = EventPriority.NORMAL;
    boolean DEFAULT_IGNORE_CANCELLED = true;
    boolean DEFAULT_REGISTER = false;

    default void unregister() {
        HandlerList.unregisterAll(this);
    }

    default <T extends Event> Listener<T> listen(@NotNull Class<T> eventClass, @NotNull Consumer<T> action) {
        return listen(eventClass, DEFAULT_PRIORITY, DEFAULT_IGNORE_CANCELLED, DEFAULT_REGISTER, action);
    }

    default <T extends Event> Listener<T> listen(@NotNull Class<T> eventClass, @NotNull EventPriority priority, @NotNull Consumer<T> action) {
        return listen(eventClass, priority, DEFAULT_IGNORE_CANCELLED, DEFAULT_REGISTER, action);
    }

    default <T extends Event> Listener<T> listen(@NotNull Class<T> eventClass, @NotNull EventPriority priority, boolean ignoreCancelled, @NotNull Consumer<T> action) {
        return listen(eventClass, priority, ignoreCancelled, DEFAULT_REGISTER, action);
    }

    default <T extends Event> Listener<T> listen(@NotNull Class<T> eventClass, boolean ignoreCancelled, @NotNull Consumer<T> action) {
        return listen(eventClass, DEFAULT_PRIORITY, ignoreCancelled, DEFAULT_REGISTER, action);
    }

    default <T extends Event> Listener<T> listen(@NotNull Class<T> eventClass, @NotNull EventPriority priority, boolean ignoreCancelled, boolean register, @NotNull Consumer<T> action) {

        var custom = new Listener<>(eventClass, this, priority, ignoreCancelled) {

            @Override
            public void onEvent(T event) {
                action.accept(event);
            }

        };

        if(register) {
            custom.register();
        }

        return custom;
    }

    abstract class Listener<T extends Event> {
        private final Class<T> eventClass;
        private final org.bukkit.event.Listener listener;
        private final EventPriority priority;
        private final boolean ignoreCancelled;

        protected Listener(@NotNull Class<T> eventClass, @NotNull org.bukkit.event.Listener listener, @NotNull EventPriority priority, boolean ignoreCancelled) {
            this.eventClass = eventClass;
            this.listener = listener;
            this.priority = priority;
            this.ignoreCancelled = ignoreCancelled;
        }

        @NotNull
        public Class<T> getEventClass() {
            return this.eventClass;
        }

        @NotNull
        public org.bukkit.event.Listener getListener() {
            return this.listener;
        }

        @NotNull
        public EventPriority getPriority() {
            return this.priority;
        }

        public boolean isIgnoreCancelled() {
            return this.ignoreCancelled;
        }

        abstract void onEvent(T event);

        public void register() {
            Bukkit.getPluginManager().registerEvent(eventClass, listener, priority, (listener, event) -> {

                if(!eventClass.isAssignableFrom(event.getClass())) {
                    BukkitLogger.warn("PluginManager published non assignable event to listener: " + eventClass.getName() + " <-/- " + event.getClass().getName());
                    return;
                }

                onEvent((T) event);
            }, PluginAdapter.getInstance(), ignoreCancelled);
        }

    }



}
