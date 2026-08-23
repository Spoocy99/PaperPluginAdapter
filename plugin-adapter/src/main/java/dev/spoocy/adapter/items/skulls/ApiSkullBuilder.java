package dev.spoocy.adapter.items.skulls;

import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import dev.spoocy.adapter.compatibility.items.SkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * A Skull Builder that uses the {@link org.bukkit.profile.PlayerProfile}
 * API to apply texture to skulls.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ApiSkullBuilder extends WrappedSkullBuilder {

    protected ApiSkullBuilder(@NotNull ItemBuilder itemBuilder) {
        super(itemBuilder);
    }

    @Override
    protected void setTexture(@NotNull String value, @NotNull WrappedSkullBuilder.Type type) {
        if (type == Type.BASE64) {
            value = base64ToURL(value);
        }

        PlayerProfile profile = createPlayerProfile(value);
        this.getItemMeta().setOwnerProfile(profile);
    }

    @Override
    public @NotNull SkullBuilder clone() {
        return new ApiSkullBuilder(this.baseBuilder().clone());
    }

    private static UUID createUUID(@NotNull String base64) {
        return new UUID(
                base64.substring(base64.length() - 20).hashCode(),
                base64.substring(base64.length() - 10).hashCode()
        );
    }

    private static PlayerProfile createPlayerProfile(@NotNull String url) {

        UUID uuid = createUUID(urlToBase64(url));
        PlayerProfile profile;

        try {
            profile = Bukkit.createPlayerProfile(uuid, "Player");
        } catch (NoSuchMethodError e) {
            profile = Bukkit.createPlayerProfile(uuid);
        }

        PlayerTextures textures = profile.getTextures();
        URL uri;

        try {
            uri = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }

        textures.setSkin(uri);
        profile.setTextures(textures);
        return profile;
    }


}
