package dev.spoocy.adapter.items.skulls;

import com.mojang.authlib.GameProfile;
import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import dev.spoocy.adapter.compatibility.items.SkullBuilder;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.builder.FieldBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SkullBuilderProvider {

    private static Mode MODE = Mode.UNKNOWN;

    private static void determineMode() {

        SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);

        try {

            Bukkit.createPlayerProfile(UUID.randomUUID(), "Player");
            MODE = Mode.API;

        } catch (Throwable e) {

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
                MODE = Mode.PROFILE;

            } else if (resolvableProfileClassExists()) {
                MODE = Mode.RESOLVABLE_PROFILE;
            }

        }

        BukkitLogger.debug("Selected SkullBuilder API Mode: " + MODE.name());
    }

    private static boolean resolvableProfileClassExists() {
        try {
            Class.forName(ResolvableProfileSkullBuilder.RESOLVABLE_PROFILE_CLASS);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Mode getMode() {
        return MODE;
    }

    public static void setMode(@NotNull Mode MODE) {
        SkullBuilderProvider.MODE = MODE;
    }

    public static SkullBuilder createSkullBuilder(@NotNull ItemBuilder builder) {
        if (MODE == Mode.UNKNOWN) {
            determineMode();
        }

        switch (MODE) {
            case API:
                return new ApiSkullBuilder(builder);
            case PROFILE:
                return new ProfileSkullBuilder(builder);
            case RESOLVABLE_PROFILE:
                return new ResolvableProfileSkullBuilder(builder);
            default:
                throw new IllegalStateException("Could not determine SkullBuilder mode.");
        }
    }

    public enum Mode {
        UNKNOWN,
        API,
        PROFILE,
        RESOLVABLE_PROFILE
    }

}
