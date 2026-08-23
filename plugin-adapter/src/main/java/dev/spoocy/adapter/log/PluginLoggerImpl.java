package dev.spoocy.adapter.log;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.log.component.ComponentSender;
import dev.spoocy.adapter.log.component.LoggerComponentSerializer;
import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.message.color.Color;
import dev.spoocy.utils.common.log.ILogger;
import dev.spoocy.utils.common.log.LogLevel;
import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.accessor.ClassAccess;
import dev.spoocy.utils.reflection.accessor.FieldAccessor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class PluginLoggerImpl implements PluginLogger {

    public static final LoggerComponentSerializer PLAIN_SERIALIZER = LoggerComponentSerializer.create(
            (comp) -> {
                Component translated = GlobalTranslator.render(comp, Locale.getDefault());
                StringBuilder contents = new StringBuilder();
                LoggerComponentSerializer.DEFAULT_FLATTENER.flatten(translated, contents::append);
                return contents.toString();
            }
    );

    private static final boolean IS_PAPER = isPaper();
    private static final ComponentSender CONSOLE_SENDER = IS_PAPER ? ComponentSender.PAPER : ComponentSender.SPIGOT;

    private final Logger backing;
    private final boolean canInject;
    private Component loggingPrefix;
    private LogLevel level;
    private boolean color;
    private boolean locationAware;

    public PluginLoggerImpl(@NotNull PluginAdapter plugin) {
        this(plugin.getLogger(), plugin.getDescription().getPrefix() != null ? plugin.getDescription().getPrefix() : plugin.getDescription().getName());
    }

    public PluginLoggerImpl(@NotNull Logger logger) {
        this.backing = logger;
        this.canInject = false;
        this.loggingPrefix = Component.empty();
        this.level = LogLevel.DEFAULT_LEVEL;
        this.color = true;
        this.locationAware = false;
    }

    public PluginLoggerImpl(@NotNull Logger logger, @NotNull String name) {
        this.backing = logger;
        this.canInject = !IS_PAPER;
        this.loggingPrefix = createPrefix(name, Color.BLUE);
        this.level = LogLevel.DEFAULT_LEVEL;
        this.color = true;
        this.locationAware = false;
    }

    @Override
    public LogLevel getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(@NotNull LogLevel level) {
        this.level = level;
    }

    @Override
    public boolean isColor() {
        return this.color;
    }

    @Override
    public void setColor(boolean color) {
        this.color = color;
    }

    @Override
    public Component getLoggingPrefix() {
        return this.loggingPrefix;
    }

    @Override
    public void setLoggingPrefix(@NotNull Component prefix) {
        this.loggingPrefix = prefix;
        if(canInject) setSpigotLoggerPrefix(PLAIN_SERIALIZER.serialize(prefix));
    }

    @Override
    public boolean isLocationAware() {
        return this.locationAware;
    }

    @Override
    public void setLocationAware(boolean locationAware) {
        this.locationAware = locationAware;
    }

    @Override
    public void sendToListeningConsoles(@NotNull Component component) {
        CONSOLE_SENDER.send(component);
    }

    protected void logComponent(@NotNull LogLevel level, @NotNull Component message, final @NotNull Object... args) {

        Throwable throwable = null;

        for (Object arg : args) {
            if (arg instanceof TextColor) {
                message = message.color((TextColor) arg);
                break;
            }

            if (arg instanceof TextDecoration) {
                message = message.decorate((TextDecoration) arg);
                break;
            }

            if (arg instanceof Throwable) {
                throwable = (Throwable) arg;
                break;
            }
		}

        LogLevel translated = this.getTranslatedLevel(level);
        boolean shouldSkipColors = IS_PAPER && (translated == LogLevel.ERROR || translated == LogLevel.WARN);

        if(this.color && !shouldSkipColors) {

            CONSOLE_SENDER.send(getLevelComponent(translated, message));

            if(throwable != null) {
                this.backing.log(translated.getJavaLevel(), "", throwable);
            }

            return;
        }

        if(throwable != null) {
            // Logger does not have a method to log both a message and a throwable, so no args for formatting
            // But not that important since message is already formatted using own format
            this.backing.log(translated.getJavaLevel(), PLAIN_SERIALIZER.serialize(message), throwable);
            return;
        }

        this.backing.log(translated.getJavaLevel(), PLAIN_SERIALIZER.serialize(message), PLAIN_SERIALIZER.maybeSerialize(args));
    }

    @NotNull
    protected Component getLevelComponent(@NotNull LogLevel translated, @NotNull Component message) {
        Component send;

        if(translated == LogLevel.ERROR) {

            send = this.loggingPrefix
                    .append(
                            Component.text("[ERROR] ")
                                    .color(NamedTextColor.RED)
                    )
                    .append(
                            message
                    );


        } else if(translated == LogLevel.WARN) {

            send = this.loggingPrefix
                    .append(
                            Component.text("[WARN] ")
                                    .color(NamedTextColor.YELLOW)
                    )
                    .append(
                            message
                    );

        } else {

            send = this.loggingPrefix.append(message);

        }

        return send;
    }

    public LogLevel getTranslatedLevel(LogLevel level) {
        if(!LogLevel.INFO.covers(level) && this.level.covers(level)) return LogLevel.INFO;
        return level;
    }

    @Override
    public void trace(@NotNull Marker marker, @NotNull Component message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.TRACE)) return;
        this.logComponent(LogLevel.TRACE, this.format(message, formatCaller(marker)), args);
    }


    @Override
    public void trace(@NotNull Marker marker, @NotNull String message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.TRACE)) return;
        this.logComponent(LogLevel.TRACE, this.format(message, formatCaller(marker), args), args);
    }

    @Override
    public void trace(@NotNull Class<?> location, @NotNull Component message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.TRACE)) return;
        this.logComponent(LogLevel.TRACE, this.format(message, formatCaller(location)), args);
    }

    @Override
    public void trace(@NotNull Class<?> location, @NotNull String message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.TRACE)) return;
        this.logComponent(LogLevel.TRACE, this.format(message, formatCaller(location), args), args);
    }

    @Override
    public void debug(@NotNull Marker marker, @NotNull Component message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.DEBUG)) return;
        this.logComponent(LogLevel.DEBUG, this.format(message, formatCaller(marker)), args);
    }

    @Override
    public void debug(@NotNull Marker marker, @NotNull String message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.DEBUG)) return;
        this.logComponent(LogLevel.DEBUG, this.format(message, formatCaller(marker), args), args);
    }

    @Override
    public void debug(@NotNull Class<?> location, @NotNull Component message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.DEBUG)) return;
        this.logComponent(LogLevel.DEBUG, this.format(message, formatCaller(location)), args);
    }

    @Override
    public void debug(@NotNull Class<?> location, @NotNull String message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.DEBUG)) return;
        this.logComponent(LogLevel.DEBUG, this.format(message, formatCaller(location), args), args);
    }

    @Override
    public void info(@NotNull Marker marker, @NotNull Component message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.INFO)) return;
        this.logComponent(LogLevel.INFO, this.format(message, formatCaller(marker)), args);
    }

    @Override
    public void info(@NotNull Marker marker, @NotNull String message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.INFO)) return;
        this.logComponent(LogLevel.INFO, this.format(message, formatCaller(marker), args), args);
    }

    @Override
    public void info(@NotNull Class<?> location, @NotNull Component message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.INFO)) return;
        this.logComponent(LogLevel.INFO, this.format(message, formatCaller(location)), args);
    }

    @Override
    public void info(@NotNull Class<?> location, @NotNull String message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.INFO)) return;
        this.logComponent(LogLevel.INFO, this.format(message, formatCaller(location), args), args);
    }

    @Override
    public void warn(@NotNull Marker marker, @NotNull Component message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.WARN)) return;
        this.logComponent(LogLevel.WARN, this.format(message, formatCaller(marker)), args);
    }

    @Override
    public void warn(@NotNull Marker marker, @NotNull String message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.WARN)) return;
        this.logComponent(LogLevel.WARN, this.format(message, formatCaller(marker), args), args);
    }

    @Override
    public void warn(@NotNull Class<?> location, @NotNull Component message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.WARN)) return;
        this.logComponent(LogLevel.WARN, this.format(message, formatCaller(location)), args);
    }

    @Override
    public void warn(@NotNull Class<?> location, @NotNull String message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.WARN)) return;
        this.logComponent(LogLevel.WARN, this.format(message, formatCaller(location), args), args);
    }

    @Override
    public void error(@NotNull Marker marker, @NotNull Component message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.ERROR)) return;
        this.logComponent(LogLevel.ERROR, this.format(message, formatCaller(marker)), args);
    }

    @Override
    public void error(@NotNull Marker marker, @NotNull String message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.ERROR)) return;
        this.logComponent(LogLevel.ERROR, this.format(message, formatCaller(marker), args), args);
    }

    @Override
    public void error(@NotNull Class<?> location, @NotNull Component message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.ERROR)) return;
        this.logComponent(LogLevel.ERROR, this.format(message, formatCaller(location)), args);
    }

    @Override
    public void error(@NotNull Class<?> location, @NotNull String message, @NotNull Object... args) {
        if(!this.level.covers(LogLevel.ERROR)) return;
        this.logComponent(LogLevel.ERROR, this.format(message, formatCaller(location), args), args);
    }

    private static Component createPrefix(@NotNull String loggerName, @NotNull TextColor color) {
        return Component.text("")
                .color(Color.GRAY)
                .append(Component.text(loggerName).color(color))
                .append(Component.text(" >> ").color(NamedTextColor.GRAY));
    }

    @NotNull
    private String formatCaller(@NotNull Marker marker) {
        return marker.getName();
    }

    @Nullable
    private String formatCaller(@NotNull Class<?> location) {
        LogAs logAs = location.getAnnotation(LogAs.class);

        if(logAs != null) {

            if(this.isLocationAware() || logAs.explicit()) {
                return logAs.value();
            }

            return null;
        }

        return this.isLocationAware() ? "c/" + location.getSimpleName() : null;
    }

    private Component format(@NotNull String message, final @Nullable String location, final @NotNull Object... args) {
        message = ILogger.format(message, args);
        Component cmpMessage;

        try {
            cmpMessage = Message.PROCESSOR.miniMessageSerializer().deserialize(message);
        } catch (Exception e) {
            this.backing.log(Level.SEVERE, "Failed to parse log message to Component!", e);
            cmpMessage = Component.text(message);
        }

        return format(cmpMessage, location);
    }

    private Component format(@NotNull Component message, final @Nullable String location) {
        if(location != null) {
            message = Message.cmp("<yellow>[" + location + "]</yellow> ")
                    .append(message);
        }
        return message;
    }

    private static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.utils.PaperPluginLogger");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private FieldAccessor pluginNameField;
    private void setSpigotLoggerPrefix(@NotNull String prefix) {
        if(this.pluginNameField == null) {

            ClassAccess backingAccess = Reflection
                    .builder()
                    .forClass(this.backing.getClass())
                    .privateMembers()
                    .buildAccess();

            this.pluginNameField = backingAccess.field(
                    Reflection.field()
                            .name("pluginName")
                            .build()
            );

            if(this.pluginNameField == null) {
                this.pluginNameField = backingAccess.field(
                        Reflection.field()
                                .typeExact(String.class)
                                .build()
                );
            }

            if(this.pluginNameField == null) {
                BukkitLogger.error("Could not find 'pluginName' field in logger class: " + this.backing.getClass().getName());
                return;
            }

        }

        this.pluginNameField.set(this.backing, prefix);
    }

}
