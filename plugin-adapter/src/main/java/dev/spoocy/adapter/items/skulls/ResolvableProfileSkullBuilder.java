package dev.spoocy.adapter.items.skulls;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import dev.spoocy.adapter.compatibility.items.SkullBuilder;
import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.accessor.ConstructorAccessor;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ResolvableProfileSkullBuilder extends WrappedSkullBuilder {

    public static final String RESOLVABLE_PROFILE_CLASS = "net.minecraft.world.item.component.ResolvableProfile";

    protected ResolvableProfileSkullBuilder(@NotNull ItemBuilder itemBuilder) {
        super(itemBuilder);
    }

    @Override
    protected void setTexture(@NotNull String value, @NotNull Type type) {
        if (type == Type.URL) {
            value = urlToBase64(value);
        }

        setResolvableProfile(this.getItemMeta(), value);
    }

    @Override
    public @NotNull SkullBuilder clone() {
        return new ResolvableProfileSkullBuilder(this.baseBuilder().clone());
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

    private static ConstructorAccessor resolvableProfileConstructor;
    private static void setResolvableProfile(@NotNull SkullMeta meta, @NotNull String base64) {
        if (resolvableProfileConstructor == null) {
            resolvableProfileConstructor = Reflection
                    .builder()
                    .forName(RESOLVABLE_PROFILE_CLASS)
                    .privateMembers()
                    .buildAccess()
                    .constructor(GameProfile.class);
        }
        Object resolvableProfile = resolvableProfileConstructor.invoke(createProfile(base64));
        rewriteProfileField(meta, resolvableProfile);
    }
}
