package dev.spoocy.adapter.compatibility.items;

import com.mojang.authlib.GameProfile;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.log.LogAs;
import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.builder.FieldBuilder;
import dev.spoocy.utils.reflection.builder.MethodBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.UUID;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@LogAs("Skull API")
public interface SkullApi {

    @NotNull
    static SkullApi get() {
        return Provider.getAPI();
    }

    void applyTexture(@NotNull SkullMeta meta, @NotNull String value, @NotNull Type type);

    static String urlToBase64(@NotNull String url) {
        URI actualUrl;

        try {
            actualUrl = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl + "\"}}}";

        return Base64.getEncoder().encodeToString(toEncode.getBytes());
    }

    static UUID uuidFromBase64(@NotNull String base64) {
        return new UUID(
                base64.substring(base64.length() - 20).hashCode(),
                base64.substring(base64.length() - 10).hashCode()
        );
    }

    enum Type {
        URL,
        BASE64
    }

    enum Mode {
        UNKNOWN(null),
        PAPER_API("dev.spoocy.adapter.paper.PaperSkullApi"),
        SPIGOT_API("dev.spoocy.adapter.spigot.SpigotSkullApi"),
        PROFILE("dev.spoocy.adapter.compatibility.item.ProfileSkullApi"),
        RESOLVABLE_PROFILE("dev.spoocy.adapter.compatibility.item.ResolvableProfileSkullApi");

        private final String clazz;

        Mode(@Nullable String clazz) {
            this.clazz = clazz;
        }

        @NotNull
        public SkullApi createInstance() throws
                ClassNotFoundException,
                NoSuchMethodException,
                InvocationTargetException,
                InstantiationException,
                IllegalAccessException {

            if (clazz == null) {
                throw new UnsupportedOperationException("Mode " + this + " does not have an implementation.");
            }

            Class<?> clazz = Class.forName(this.clazz);
            return (SkullApi) clazz.getDeclaredConstructor().newInstance();
        }

    }

    @LogAs("Skull API")
    class Provider {

        private static Mode MODE = Mode.UNKNOWN;
        private static SkullApi API;

        static {
            Mode mode = determineMode();

            if(mode == null) {
                throw new UnsupportedOperationException("Failed to determine Skull API mode.");
            }

            setMode(mode);
        }

        public static Mode getMode() {
            return MODE;
        }

        public static void setMode(@NotNull Mode mode) {
            SkullApi instance;

            try {
                instance = mode.createInstance();
            } catch (Throwable e) {
                throw new UnsupportedOperationException("Failed to create SkullApi instance for mode: " + mode, e);
            }

            MODE = mode;
            API = instance;

            BukkitLogger.debug("Selected Skull API Mode: " + MODE.name());
        }

        @NotNull
        public static SkullApi getAPI() {
            if(API == null) {
                throw new UnsupportedOperationException("Skull API is not initialized.");
            }

            return API;
        }

        private static Mode determineMode() {

            Method paperProfileMethod = Reflection
                    .builder()
                    .forClass(Bukkit.class)
                    .publicMembers()
                    .build()
                    .method(
                            MethodBuilder.create()
                                    .name("createProfile")
                                    .requireStatic()
                                    .parameterCount(2)
                                    .build()
                    );

            if (paperProfileMethod != null) {
                return Mode.PAPER_API;
            }

            try {
                Bukkit.createPlayerProfile(UUID.randomUUID(), "Player");
                return Mode.SPIGOT_API;
            } catch (NoSuchMethodError ignored) { }

            SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);

            Field profileField = Reflection
                    .builder()
                    .forClass(meta.getClass())
                    .privateMembers()
                    .build()
                    .field(
                            FieldBuilder.create()
                                    .name("profile")
                                    .build()
                    );

            if (profileField != null && profileField.getType() == GameProfile.class) {
                return Mode.PROFILE;

            }

            if (resolvableProfileClassExists()) {
                return Mode.RESOLVABLE_PROFILE;
            }

            return Mode.UNKNOWN;
        }

        private static boolean resolvableProfileClassExists() {
            try {
                Class.forName(ResolvableProfileSkullBuilder.RESOLVABLE_PROFILE_CLASS);
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        }

    }

}
