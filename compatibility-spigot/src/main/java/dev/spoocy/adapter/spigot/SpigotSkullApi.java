package dev.spoocy.adapter.spigot;

import dev.spoocy.adapter.compatibility.items.SkullApi;
import dev.spoocy.adapter.log.BukkitLogger;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * A Skull Builder that uses the {@link org.bukkit.profile.PlayerProfile}
 * API to apply texture to skulls.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SpigotSkullApi implements SkullApi {

    @Override
    public void applyTexture(@NotNull SkullMeta meta, @NotNull String value, @NotNull Type type) {
        UUID uuid;

        if (type == Type.BASE64) {

            try {
                value = extractUrl(value);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid base64 texture", e);
            }

            BukkitLogger.trace("Extracted url: " + value);

            uuid = SkullApi.uuidFromBase64(value);
        } else {
            uuid = UUID.randomUUID();
        }

        PlayerProfile profile = createPlayerProfile(uuid, value);
        meta.setOwnerProfile(profile);
        BukkitLogger.trace("Applied profile to skull: " + profile);
    }

    @NotNull
    private static PlayerProfile createPlayerProfile(@NotNull UUID uuid, @NotNull String url) {
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
            throw new IllegalArgumentException("Invalid URL", exception);
        }

        textures.setSkin(uri);
        profile.setTextures(textures);
        return profile;
    }

    protected static String extractUrl(@NotNull String base64) throws ParseException {
        String decoded = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);

        JSONObject object = (JSONObject) JSONValue.parseWithException(decoded);
        JSONObject textures = (JSONObject) object.get("textures");
        JSONObject skin = (JSONObject) textures.get("SKIN");

        return (String) skin.get("url");
    }

}
