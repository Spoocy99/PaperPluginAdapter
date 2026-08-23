package dev.spoocy.adapter.scoreboard;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public enum ScoreboardLine {
    LINE_0(0, ChatColor.AQUA + ""),
    LINE_1(1, ChatColor.BLACK + ""),
    LINE_2(2, ChatColor.BLUE + ""),
    LINE_3(3, ChatColor.DARK_AQUA + ""),
    LINE_4(4, ChatColor.DARK_BLUE + ""),
    LINE_5(5, ChatColor.DARK_GRAY + ""),
    LINE_6(6, ChatColor.DARK_GREEN + ""),
    LINE_7(7, ChatColor.DARK_PURPLE + ""),
    LINE_8(8, ChatColor.DARK_RED + ""),
    LINE_9(9, ChatColor.GOLD + ""),
    LINE_10(10, ChatColor.GRAY + ""),
    LINE_11(11, ChatColor.GREEN + ""),
    LINE_12(12, ChatColor.LIGHT_PURPLE + ""),
    LINE_13(13, ChatColor.RED + ""),
    LINE_14(14, ChatColor.WHITE + "");

    private final int line;
    private final String prefix;

    ScoreboardLine(int line, @NotNull String prefix) {
        this.line = line;
        this.prefix = prefix;
    }

    public int getLine() {
        return this.line;
    }

    public String getPrefix() {
        return this.prefix + ChatColor.RESET;
    }

    public static ScoreboardLine getByLine(int line) {
        for(ScoreboardLine l : values()) {
            if(l.getLine() == line) return l;
        }
        return null;
    }
}