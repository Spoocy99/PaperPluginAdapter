package dev.spoocy.adapter.items.skulls;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import dev.spoocy.adapter.compatibility.items.SkullBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ProfileSkullBuilder extends WrappedSkullBuilder {

    protected ProfileSkullBuilder(@NotNull ItemBuilder itemBuilder) {
        super(itemBuilder);
    }

    @Override
    protected void setTexture(@NotNull String value, @NotNull Type type) {
        if (type == Type.URL) {
            value = urlToBase64(value);
        }

        rewriteProfileField(this.getItemMeta(), createProfile(value));
    }

    @Override
    public @NotNull SkullBuilder clone() {
        return new ProfileSkullBuilder(this.baseBuilder().clone());
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

}
