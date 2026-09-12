package dev.spoocy.adapter.compatibility.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.accessor.ConstructorAccessor;
import dev.spoocy.utils.reflection.accessor.FieldAccessor;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ResolvableProfileSkullBuilder implements SkullApi {

    public static final String RESOLVABLE_PROFILE_CLASS = "net.minecraft.world.item.component.ResolvableProfile";

    @Override
    public void applyTexture(@NotNull SkullMeta meta, @NotNull String value, @NotNull Type type) {
        if (type == Type.URL) {
            value = SkullApi.urlToBase64(value);
        }

        setResolvableProfile(meta, value);
    }

    private static GameProfile createProfile(@NotNull String base64) {
        UUID id = new UUID(
                base64.substring(base64.length() - 20).hashCode(),
                base64.substring(base64.length() - 10).hashCode()
        );

        GameProfile profile = new GameProfile(id, "Player");
        profile.getProperties().put("textures", new Property("textures", base64));
        return profile;
    }

    private static ConstructorAccessor RESOLVEABLE_PROFILE_CONSTRUCTOR;
    private static void setResolvableProfile(@NotNull SkullMeta meta, @NotNull String base64) {
        if (RESOLVEABLE_PROFILE_CONSTRUCTOR == null) {
            RESOLVEABLE_PROFILE_CONSTRUCTOR = Reflection
                    .builder()
                    .forName(RESOLVABLE_PROFILE_CLASS)
                    .privateMembers()
                    .buildAccess()
                    .constructor(GameProfile.class);
        }
        Object resolvableProfile = RESOLVEABLE_PROFILE_CONSTRUCTOR.invoke(createProfile(base64));
        rewriteProfileField(meta, resolvableProfile);
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
