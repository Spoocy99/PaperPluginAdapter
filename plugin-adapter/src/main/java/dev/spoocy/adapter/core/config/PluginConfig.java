package dev.spoocy.adapter.core.config;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.message.font.Font;
import dev.spoocy.adapter.sound.PSound;
import dev.spoocy.utils.config.constructor.Constructor;
import dev.spoocy.utils.config.representer.Representer;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface PluginConfig {

    @NotNull
    static TextColor baseColor() {
        return forKey(Keys.BASE_COLOR, TextColor.class);
    }

    @NotNull
    static TextColor primaryColor() {
        return forKey(Keys.PRIMARY_COLOR, TextColor.class);
    }

    @NotNull
    static TextColor errorColor() {
        return forKey(Keys.ERROR_COLOR, TextColor.class);
    }

    @NotNull
    static PSound clickSound() {
        return forKey(Keys.CLICK_SOUND, PSound.class);
    }

    @NotNull
    static PSound errorSound() {
        return forKey(Keys.ERROR_SOUND, PSound.class);
    }

    @NotNull
    static Font defaultFont() {
        return forKey(Keys.DEFAULT_FONT, Font.class);
    }

    static int spigotResourceId() {
        return forKey(Keys.SPIGOT_RESOURCE_ID, Integer.class);
    }

    @NotNull
    static Constructor configConstructor() {
        return forKey(Keys.CONFIG_CONSTRUCTOR, Constructor.class);
    }

    @NotNull
    static Representer configRepresenter() {
        return forKey(Keys.CONFIG_REPRESENTER, Representer.class);
    }

    @NotNull
    static <T> T forKey(@NotNull NamespacedKey key, @NotNull Class<T> type) {
        PluginConfig config = PluginAdapter.getInstance().getConfiguration();
        return config.read(key, type);
    }

    @NotNull
    static <T> T forName(@NotNull String name, @NotNull Class<T> type) {
        return forKey(key(name), type);
    }

    @Contract("_ -> new")
    private static @NotNull NamespacedKey key(@NotNull String name) {
        return new NamespacedKey(PluginAdapter.getInstance(), name);
    }

    @NotNull
    <T> T read(@NotNull NamespacedKey key, @NotNull Class<T> type);

    void write(@NotNull NamespacedKey key, @NotNull Object value);

    class Keys {
        public static final NamespacedKey BASE_COLOR = key("base_color");
        public static final NamespacedKey PRIMARY_COLOR = key("primary_color");
        public static final NamespacedKey ERROR_COLOR = key("error_color");
        public static final NamespacedKey CLICK_SOUND = key("click_sound");
        public static final NamespacedKey ERROR_SOUND = key("error_sound");
        public static final NamespacedKey DEFAULT_FONT = key("default_font");
        public static final NamespacedKey SPIGOT_RESOURCE_ID = key("spigot_resource_id");
        public static final NamespacedKey CONFIG_CONSTRUCTOR = key("config_constructor");
        public static final NamespacedKey CONFIG_REPRESENTER = key("config_representer");
    }
}
