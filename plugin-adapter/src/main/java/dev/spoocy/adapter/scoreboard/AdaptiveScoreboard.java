package dev.spoocy.adapter.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface AdaptiveScoreboard {

    static AdaptiveScoreboard create(@NotNull String name, @NotNull Scoreboard scoreboard) {
        return new BukkitAdaptiveScoreboardImpl(name, scoreboard);
    }

    void show();

    void hide();

    boolean isVisible();

    Scoreboard getBukkitScoreboard();

    default boolean hasPlayer(@NotNull Player player) {
        return player.getScoreboard().equals(this.getBukkitScoreboard());
    }

    default void addPlayer(@NotNull Player player) {
        player.setScoreboard(this.getBukkitScoreboard());
    }

    default void removePlayer(@NotNull Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    Component getTitle();

    Component[] getContents();

    void title(@NotNull Component component);

    Component getContent(int line);

    void setContent(int line, @NotNull Component content);

    void clearContent(int line);

    void clearAllContent();

    int getLastLine();

}
