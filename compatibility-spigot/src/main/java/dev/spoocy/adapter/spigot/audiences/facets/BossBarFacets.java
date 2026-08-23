package dev.spoocy.adapter.spigot.audiences.facets;

import dev.spoocy.adapter.spigot.SpigotCompatibilityProvider;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class BossBarFacets {

    public static final BossBarFacets INSTANCE = new BossBarFacets();

    private final Map<BossBar, Facet> facets = new java.util.WeakHashMap<>();

    private BossBarFacets() {

    }

    @NotNull
    public Facet getFacet(@NotNull BossBar bar) {
        return facets.computeIfAbsent(
                bar,
                b -> {
                    org.bukkit.boss.BossBar bukkitBar = createBukkitBossBar(b);
                    Facet facet = new Facet(bukkitBar);
                    bar.addListener(facet);
                    return facet;
                }
        );
    }

    private org.bukkit.boss.BossBar createBukkitBossBar(@NotNull BossBar bar) {
        return Bukkit.createBossBar(
                SpigotCompatibilityProvider.BUNGEE_TEXT_SERIALIZER.serialize(bar.name()),
                toBukkit(bar.color()),
                toBukkit(bar.overlay())
        );
    }

    private static BarColor toBukkit(BossBar.Color color) {
        switch (color) {
            case PINK:
                return BarColor.PINK;
            case BLUE:
                return BarColor.BLUE;
            case RED:
                return BarColor.RED;
            case GREEN:
                return BarColor.GREEN;
            case YELLOW:
                return BarColor.YELLOW;
            case PURPLE:
                return BarColor.PURPLE;
            case WHITE:
                return BarColor.WHITE;
            default:
                throw new IllegalArgumentException("Unknown BossBar.Color: " + color);
        }
    }

    private static BarFlag toBukkit(@NotNull BossBar.Flag flag) {
        switch (flag) {
            case DARKEN_SCREEN:
                return BarFlag.DARKEN_SKY;
            case PLAY_BOSS_MUSIC:
                return BarFlag.PLAY_BOSS_MUSIC;
            case CREATE_WORLD_FOG:
                return BarFlag.CREATE_FOG;
            default:
                throw new IllegalArgumentException("Unknown BossBar.Flag: " + flag);
        }
    }

    private static BarStyle toBukkit(@NotNull BossBar.Overlay overlay) {
        switch (overlay) {
            case PROGRESS:
                return BarStyle.SOLID;
            case NOTCHED_6:
                return BarStyle.SEGMENTED_6;
            case NOTCHED_10:
                return BarStyle.SEGMENTED_10;
            case NOTCHED_12:
                return BarStyle.SEGMENTED_12;
            case NOTCHED_20:
                return BarStyle.SEGMENTED_20;
            default:
                throw new IllegalArgumentException("Unknown BossBar.Overlay: " + overlay);
        }
    }

    public static class Facet implements BossBar.Listener {

        private final org.bukkit.boss.BossBar bukkitBar;

        public Facet(@NotNull org.bukkit.boss.BossBar bukkitBar) {
            this.bukkitBar = bukkitBar;
        }

        public void addPlayer(@NotNull Player player) {
            bukkitBar.addPlayer(player);
        }

        public void removePlayer(@NotNull Player player) {
            bukkitBar.removePlayer(player);
        }

        @Override
        public void bossBarColorChanged(
                @NotNull BossBar bar,
                @NotNull BossBar.Color oldColor,
                @NotNull BossBar.Color newColor
        ) {
            bukkitBar.setColor(toBukkit(newColor));
        }

        @Override
        public void bossBarFlagsChanged(
                @NotNull BossBar bar,
                @NotNull Set<BossBar.Flag> flagsAdded,
                @NotNull Set<BossBar.Flag> flagsRemoved
        ) {

            for (BossBar.Flag flag : flagsAdded) {
                bukkitBar.addFlag(toBukkit(flag));
            }

            for (BossBar.Flag flag : flagsRemoved) {
                bukkitBar.removeFlag(toBukkit(flag));
            }
        }

        @Override
        public void bossBarNameChanged(
                @NotNull BossBar bar,
                @NotNull Component oldName,
                @NotNull Component newName
        ) {
            String name = SpigotCompatibilityProvider.BUNGEE_TEXT_SERIALIZER.serialize(newName);
            bukkitBar.setTitle(name);
        }

        @Override
        public void bossBarOverlayChanged(
                @NotNull BossBar bar,
                @NotNull BossBar.Overlay oldOverlay,
                @NotNull BossBar.Overlay newOverlay
        ) {
            bukkitBar.setStyle(toBukkit(newOverlay));
        }

        @Override
        public void bossBarProgressChanged(
                @NotNull BossBar bar,
                float oldProgress,
                float newProgress
        ) {
            bukkitBar.setProgress(newProgress);
        }

    }

}
