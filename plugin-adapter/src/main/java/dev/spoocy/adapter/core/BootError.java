package dev.spoocy.adapter.core;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class BootError {

    private final String[] errors;

    public BootError(@NotNull String... errors) {
        this.errors = errors;
    }

    public void printErrors(@NotNull CommandSender sender, @NotNull String pluginName) {
        sender.sendMessage(ChatColor.RED + "-----------------------------------------------------------------");
        sender.sendMessage(ChatColor.RED + " " + pluginName + " failed to boot. Please fix any errors and restart");

        for (String issue : errors) {
            sender.sendMessage(ChatColor.WHITE + "- " + ChatColor.YELLOW + issue);
        }

        sender.sendMessage(ChatColor.RED + "-----------------------------------------------------------------");
    }

    public String[] getErrors() {
        return Arrays.copyOf(errors, errors.length);
    }

    public static BootError combine(@NotNull BootError... errors) {
        String[] combined = new String[Arrays.stream(errors).mapToInt(e -> e.getErrors().length).sum()];

        int index = 0;
        for (BootError error : errors) {
            for (String issue : error.getErrors()) {
                combined[index++] = issue;
            }
        }

        return new BootError(combined);
    }


}
