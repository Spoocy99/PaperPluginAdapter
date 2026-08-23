package dev.spoocy.adapter.config;

import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.utils.common.misc.Args;
import dev.spoocy.utils.config.Tag;
import dev.spoocy.utils.config.constructor.SafeConstructor;
import dev.spoocy.utils.config.nodes.Node;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class BukkitConstructor extends SafeConstructor {

    protected final SerializationStrategy strategy;

    public BukkitConstructor(@NotNull SerializationStrategy strategy) {
        super();
        this.strategy = Args.notNull(strategy, "strategy");

        this.construct(Tag.MAP, new ConstructCustomObject());
    }


    protected class ConstructCustomObject extends MapConstructor {

        @Override
        public @Nullable Object construct(@Nullable Node node) {
            Map<?, ?> raw = (Map<?, ?>) super.construct(node);

            if (raw.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                Map<String, Object> typed = new LinkedHashMap<>(raw.size());
                for (Map.Entry<?, ?> entry : raw.entrySet()) {
                    typed.put(entry.getKey().toString(), entry.getValue());
                }

                try {
                    return ConfigurationSerialization.deserializeObject(typed);
                } catch (Exception ex) {
                    deserializationError(ex);
                }
            }

            return raw;
        }

    }

    protected void deserializationError(@NotNull Exception ex) {
        switch (strategy) {
            case STRICT:
                throw new IllegalArgumentException("Failed to deserialize bukkit object.", ex);
            case LOG_ONLY:
                BukkitLogger.error("Bukkit object threw error on deserialize().", ex);
            default:
        }
    }

}
