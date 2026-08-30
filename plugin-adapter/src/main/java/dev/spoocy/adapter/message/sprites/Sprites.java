package dev.spoocy.adapter.message.sprites;

import dev.spoocy.adapter.version.MinecraftVersion;
import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class Sprites {

    private static final Key BANNER_PATTERNS_ATLAS = Key.key("minecraft", "banner_patterns");
    private static final Key CELESTIALS_ATLAS = Key.key("minecraft", "celestials");
    private static final Key GUI_ATLAS = Key.key("minecraft", "gui");
    private static final Key BLOCKS_ATLAS = SpriteObjectContents.DEFAULT_ATLAS;
    private static final Key ITEMS_ATLAS;
    private static final Key MAP_DECORATIONS_ATLAS = Key.key("minecraft", "map_decorations");
    private static final Key SHIELD_PATTERNS_ATLAS = Key.key("minecraft", "shield_patterns");

    static {
        if(MinecraftVersion.getCurrent().isNewerThan(MinecraftVersion.V1_21_10)) {
            ITEMS_ATLAS = Key.key("minecraft", "items");
        } else {
            ITEMS_ATLAS = Key.key("minecraft", "blocks");
        }
    }

    @Contract("_, _ -> new")
    public static @NotNull SpriteObjectContents get(@NotNull Key atlas, @NotNull Key sprite) {
        return ObjectContents.sprite(atlas, sprite);
    }

    @Contract("_, _ -> new")
    public static @NotNull SpriteObjectContents get(@NotNull Key atlas, @KeyPattern.Value @NotNull String key) {
        return get(atlas, Key.key("minecraft", key));
    }

    @Contract("_ -> new")
    public static @NotNull SpriteObjectContents bannerPattern(@KeyPattern.Value @NotNull String spriteName) {
        return get(BANNER_PATTERNS_ATLAS, withPrefixes("entity", "banner", spriteName));
    }

    @Contract("_ -> new")
    public static @NotNull SpriteObjectContents block(@KeyPattern.Value @NotNull String key) {
        return get(BLOCKS_ATLAS, withPrefixes("block", key));
    }

    @Contract("_ -> new")
    public static @NotNull SpriteObjectContents celestial(@KeyPattern.Value @NotNull String spriteName) {
        return get(CELESTIALS_ATLAS, spriteName);
    }

    @Contract("_ -> new")
    public static @NotNull SpriteObjectContents mobEffect(@KeyPattern.Value @NotNull String spriteName) {
        return get(GUI_ATLAS, withPrefixes("mob_effect", spriteName));
    }

    @Contract("_ -> new")
    public static @NotNull SpriteObjectContents gui(@KeyPattern.Value @NotNull String spriteName) {
        return get(GUI_ATLAS, spriteName);
    }

    @Contract("_ -> new")
    public static @NotNull SpriteObjectContents item(@KeyPattern.Value @NotNull String spriteName) {
        return get(ITEMS_ATLAS, withPrefixes("item", spriteName));
    }

    @Contract("_ -> new")
    public static @NotNull SpriteObjectContents mapDecorations(@KeyPattern.Value @NotNull String spriteName) {
        return get(MAP_DECORATIONS_ATLAS, spriteName);
    }

    @Contract("_ -> new")
    public static @NotNull SpriteObjectContents shieldPattern(@KeyPattern.Value @NotNull String spriteName) {
        return get(SHIELD_PATTERNS_ATLAS, withPrefixes("entity",  "shield", spriteName));
    }

    private static String withPrefixes(@NotNull String... keys) {
        Args.notEmpty(keys, "keys");
        return String.join("/", keys);
    }
}
