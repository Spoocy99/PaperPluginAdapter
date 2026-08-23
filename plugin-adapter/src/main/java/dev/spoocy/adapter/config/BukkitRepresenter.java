package dev.spoocy.adapter.config;

import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.utils.common.misc.Args;
import dev.spoocy.utils.config.nodes.Node;
import dev.spoocy.utils.config.nodes.ScalarNode;
import dev.spoocy.utils.config.representer.SafeRepresenter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class BukkitRepresenter extends SafeRepresenter {

    protected final SerializationStrategy strategy;

    public BukkitRepresenter(@NotNull SerializationStrategy strategy) {
        super();
        this.strategy = Args.notNull(strategy, "strategy");
        this.representOf(ConfigurationSection.class, new RepresentConfigurationSection());
        this.representOf(ConfigurationSerializable.class, new RepresentConfigurationSerializable());
    }

    protected class RepresentConfigurationSection extends MapRepresenter {

        @Override
        public @NotNull Node represent(@Nullable Object data) {
            return super.represent(((ConfigurationSection) data).getValues(false));
        }

    }

    protected class RepresentConfigurationSerializable extends MapRepresenter {

        @Override
        public @NotNull Node represent(@Nullable Object data) {
            ConfigurationSerializable serializable = (ConfigurationSerializable) data;

            Map<String, Object> map = new LinkedHashMap<>();
            map.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));

            try {
                map.putAll(serializable.serialize());
                return representMapping(map);
            } catch (Exception e) {
                serializationError(e);
            }

            return ScalarNode.nullValue();
        }

    }

    protected void serializationError(@NotNull Exception ex) {
        switch (strategy) {
            case STRICT:
                throw new IllegalArgumentException("Failed to serialize bukkit object.", ex);
            case LOG_ONLY:
                BukkitLogger.error("Bukkit object threw error on serialize().", ex);
            default:
        }
    }

}
