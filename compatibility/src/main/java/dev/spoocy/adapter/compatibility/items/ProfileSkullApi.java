package dev.spoocy.adapter.compatibility.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.accessor.FieldAccessor;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ProfileSkullApi implements SkullApi {

    @Override
    public void applyTexture(@NotNull SkullMeta meta, @NotNull String value, @NotNull Type type) {

        if (type == Type.URL) {
            value = SkullApi.urlToBase64(value);
        }

        rewriteProfileField(meta, createProfile(value));
    }

    @NotNull
    private static GameProfile createProfile(@NotNull String base64) {
        UUID id = new UUID(
                base64.substring(base64.length() - 20).hashCode(),
                base64.substring(base64.length() - 10).hashCode()
        );
        GameProfile profile = new GameProfile(id, "Player");
        profile.getProperties().put("textures", new Property("textures", base64));
        return profile;
    }

    private static FieldAccessor PROFILE_FIELD;
    protected static void rewriteProfileField(@NotNull SkullMeta meta, Object value) {
        if (PROFILE_FIELD == null) {
            PROFILE_FIELD = Reflection
                    .builder()
                    .forClass(meta.getClass())
                    .privateMembers()
                    .buildAccess()
                    .field(
                            Reflection.field()
                                    .name("profile")
                                    .build()
                    );
        }
        PROFILE_FIELD.set(meta, value);
    }

}
