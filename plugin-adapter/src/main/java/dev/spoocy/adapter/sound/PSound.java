package dev.spoocy.adapter.sound;

import dev.spoocy.adapter.compatibility.RegistryReader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public final class PSound {

    public static final PSound
            INFO =              fromKey(2F, 5F,  "block.note_block.bass"),
            ERROR =             fromKey(1F, 0F,"block.note_block.didgeridoo"),
            SELECT =            fromKey(1F, 3F, "entity.player.burp", "entity.player.eat"),
            CLACK =             fromKey(1F, -3F, "block.note_block.xylophone"),

            EAT =               fromKey("entity.player.burp", "entity.player.eat"),
            BASS =              fromKey("block.note_block.bass"),
            BASS_DEEP =         fromKey(1F, 2F, "block.note_block.bass"),
            PLING =             fromKey("block.note_block.pling"),
            ANVIL =             fromKey(1F, 3F, "block.anvil.place", "block.anvil.land"),
            LEVEL_UP =          fromKey("entity.player.levelup", "level.up"),
            ITEM_BREAK =        fromKey("entity.item.break", "item.break"),
            BLOCK_BREAK =       fromKey("block.stone.break", "block.break"),
            EXPLODE =           fromKey("entity.generic.explode", "explode"),
            CHORUS_TELEPORT =   fromKey("item.chorus_fruit.teleport"),
            ENDERMAN_TELEPORT = fromKey("entity.enderman.teleport", "enderman.teleport");



    public static PSound fromSound(@NotNull Sound sound) {
        return new PSound(1F, 1F, sound);
    }

    public static PSound fromSound(float volume, float pitch, @NotNull Sound sound) {
        return new PSound(volume, pitch, sound);
    }

    public static PSound fromKey(@NotNull String... keys) {
        return fromKey(1F, 1F, keys);
    }

    public static PSound fromKey(float volume, float pitch, @NotNull String... keys) {
        Sound sound;
        for (String key : keys) {
            sound = RegistryReader.value(Sound.class, key);
            if (sound != null) return new PSound(volume, pitch, sound);
        }
        throw new IllegalArgumentException("No sound found for key: " + keys[0]);
    }

    private final Sound sound;
    private float volume;
    private float pitch;

    private PSound(float volume, float pitch, @NotNull Sound sound) {
        this.volume = volume;
        this.pitch = pitch;
        this.sound = sound;
    }

    @NotNull
    public Sound getSound() {
        return this.sound;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public PSound volume(float volume) {
        this.volume = volume;
        return this;
    }

    public PSound pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public void play(@NotNull CommandSender sender)  {
        if(!(sender instanceof Player)) return;
        Player p = (Player) sender;
        this.play(p);
    }

    public void play(@NotNull CommandSender sender, @NotNull Location location) {
        if(!(sender instanceof Player)) return;
        Player p = (Player) sender;
        this.play(p, location);
    }

    public void play(@NotNull Player player) {
        playSound(player, player.getLocation(), this.sound, this.volume, this.pitch);
    }

    public void play(@NotNull Player player, @NotNull Location location) {
        playSound(player, location, this.sound, this.volume, this.pitch);
    }

    public void broadcast() {
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), this.sound, this.volume, this.pitch));
    }

    public void broadcast(@NotNull Location location) {
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(location, this.sound, this.volume, this.pitch));
    }

    private static void playSound(@NotNull Player player, @NotNull Location location, Sound sound, float volume, float pitch) {
        player.playSound(location, sound, volume, pitch);
    }
}
