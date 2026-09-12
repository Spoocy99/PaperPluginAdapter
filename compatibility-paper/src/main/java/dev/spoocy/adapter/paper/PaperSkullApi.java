package dev.spoocy.adapter.paper;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import dev.spoocy.adapter.compatibility.items.SkullApi;
import dev.spoocy.adapter.log.BukkitLogger;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * A {@link SkullApi} that uses the paper's {@link PlayerProfile}
 * API to apply texture to skulls.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class PaperSkullApi implements SkullApi {

    @Override
    public void applyTexture(@NotNull SkullMeta meta, @NotNull String value, @NotNull Type type) {

        if (type == Type.BASE64) {
            UUID uuid = SkullApi.uuidFromBase64(value);
            PlayerProfile profile = Bukkit.createProfile(uuid);
            profile.setProperty(new ProfileProperty("textures", value));
            meta.setPlayerProfile(profile);

            BukkitLogger.trace("Applied profile to skull: " + profile);
            return;
        }

        if (type == Type.URL) {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());

            URL uri;

            try {
                uri = new URL(value);
            } catch (MalformedURLException exception) {
                throw new IllegalArgumentException("Invalid URL", exception);
            }

            PlayerTextures textures = profile.getTextures();
            textures.setSkin(uri);
            profile.setTextures(textures);
            meta.setPlayerProfile(profile);

            BukkitLogger.trace("Applied profile to skull: " + profile);
            return;
        }

        throw new UnsupportedOperationException("Unsupported texture type: " + type);
    }

}
