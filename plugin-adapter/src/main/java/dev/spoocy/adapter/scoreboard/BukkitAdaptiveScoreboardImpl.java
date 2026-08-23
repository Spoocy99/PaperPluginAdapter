package dev.spoocy.adapter.scoreboard;

import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.compatibility.annotations.CompatibilityProvided;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class BukkitAdaptiveScoreboardImpl implements AdaptiveScoreboard {

    public static final int MAX_LINES = 14;

    private final Scoreboard bukkitScoreboard;
    private Objective objective;
    private final String name;

    private Component title;
    private final Component[] lines;

    public BukkitAdaptiveScoreboardImpl(@NotNull String name, @NotNull Scoreboard scoreboard) {
        this.name = name;
        this.bukkitScoreboard = scoreboard;
        this.title = Component.text("Adaptive Scoreboard");
        this.lines = new Component[MAX_LINES];
    }

    @Override
    public void show() {
        Objective existing = bukkitScoreboard.getObjective(name);
        if(existing != null) {
            existing.unregister();
        }

        try {
            this.objective = bukkitScoreboard.registerNewObjective(name, Criteria.DUMMY, title);
        } catch (NoSuchMethodError e) {
            this.objective = bukkitScoreboard.registerNewObjective(name, "dummy", "Adaptive Scoreboard");
        }

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    @Override
    public void hide() {
        this.objective.unregister();
        this.objective = null;
    }

    @Override
    public boolean isVisible() {
        return objective != null;
    }

    @Override
    public Scoreboard getBukkitScoreboard() {
        return this.bukkitScoreboard;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public Component[] getContents() {
        return Arrays.copyOf(lines, getLastLine() + 1);
    }

    @CompatibilityProvided(
            paper = true,
            spigot = true
    )
    @Override
    public void title(@NotNull Component title) {
        this.title = title;
        try {
            this.objective.displayName(title);
        } catch (NoSuchMethodError e) {
            this.objective.setDisplayName(Message.toLegacy(title));
        }
    }

    @Override
    public Component getContent(int line) {
        if(line < 0 || line >= MAX_LINES) throw new IllegalArgumentException("Scoreboard line must be between 0 and " + MAX_LINES);
        return lines[line];
    }

    @Override
    public void setContent(int line, @NotNull Component content) {
        if(line < 0 || line >= MAX_LINES) throw new IllegalArgumentException("Scoreboard line must be between 0 and " + MAX_LINES);
        lines[line] = content;
        updateLine(line);
    }

    @Override
    public void clearContent(int line) {
        if(line < 0 || line >= MAX_LINES) throw new IllegalArgumentException("Scoreboard line must be between 0 and " + MAX_LINES);
        lines[line] = null;
        updateLine(line);
    }

    @Override
    public void clearAllContent() {
        for(int i = 0; i < MAX_LINES; i++) {
            lines[i] = null;
        }
        updateAllLines();
    }

    @Override
    public int getLastLine() {
        for(int i = MAX_LINES - 1; i >= 0; i--) {
            if(lines[i] != null) return i;
        }
        return 0;
    }

    private void updateAllLines() {
        for(int i = 0; i <= getLastLine(); i++) {
            updateLine(i);
        }
    }

    @CompatibilityProvided(
            paper = true,
            spigot = true
    )
    private void updateLine(int line) {
        Component content = getContent(line);
        Team team = getTeam(line);

        try {
            team.prefix(content);
        } catch (NoSuchMethodError e) {
            team.setPrefix(Message.toLegacy(content));
        }
    }

    private void clearUntil(int line) {
        if(line < 0 || line >= MAX_LINES) throw new IllegalArgumentException("Scoreboard line must be between 0 and " + MAX_LINES);
        for(int i = MAX_LINES; i > line; i--) {
            lines[i] = null;
        }
    }

    private Team getTeam(int line) {
        if(line < 0 || line > MAX_LINES) throw new IllegalArgumentException("Scoreboard line must be between 0 and " + MAX_LINES);
        Team team = bukkitScoreboard.getTeam(name + "_s_" + line);

        if(team == null) {
            team = bukkitScoreboard.registerNewTeam(name + "_s_" + line);
            team.addEntry(ScoreboardLine.getByLine(line).getPrefix() + " ");
            this.objective.getScore(ScoreboardLine.getByLine(line).getPrefix() + " ").setScore(MAX_LINES - line);
        }
        return team;
    }

}
