package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.adapter.messages.PlayerLike;
import dev.spoocy.adapter.messages.PluginMessage;
import dev.spoocy.adapter.messages.PluginMessageContainer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class MessageProvider implements PluginMessage {

    @Override
    public PluginMessage multipleLines() {
        return this.multipleLines(Message.DEFAULT_MAX_CHARS_PER_LINE);
    }

    @Override
    public PluginMessage multipleLines(@Nullable PluginMessageContainer linePrefix) {
        return this.multipleLines(Message.DEFAULT_MAX_CHARS_PER_LINE, linePrefix);
    }

    @Override
    public PluginMessage multipleLines(int maxCharactersPerLine) {
        return this.multipleLines(maxCharactersPerLine, null);
    }

    @Override
    public PluginMessage multipleLinesC(int maxCharactersPerLine, @Nullable Component linePrefix) {
        return this.multipleLines(maxCharactersPerLine, linePrefix != null ? Message.wrap(linePrefix) : null);
    }

    @Override
    public PluginMessage textArg(@NotNull String key, @NotNull String text) {
        return this.arg(key, Message.text(text));
    }

    @Override
    public PluginMessage textArg(@NotNull String key, @NotNull String text, @NotNull TextColor color, @NotNull TextDecoration... decorations) {
         return this.arg(key, Message.text(text, color, decorations));
    }

    @Override
    public PluginMessage componentArg(@NotNull String key, @NotNull Component component) {
        return this.arg(key, Message.wrap(component));
    }

    @Override
    public PluginMessage minimessageArg(@NotNull String key, @NotNull String minimessage) {
        return this.arg(key, Message.msg(minimessage));
    }

    @Override
    public PluginMessage translatableArg(@NotNull String key, @NotNull String translationKey) {
        return this.arg(key, Message.translatable(translationKey));
    }

    @Override
    public PluginMessage translatableArg(@NotNull String key, @NotNull String translationKey, @NotNull TextColor color, @NotNull TextDecoration... decorations) {
        return this.arg(key, Message.translatable(translationKey, color, decorations));
    }

    @Override
    public Component cmp(@NotNull Locale locale) {
        return this.cmp(PluginConfig.globalTranslation().getOrDefault(locale));
    }

    @Override
    public Component cmp(@NotNull Player player) {
        return this.cmp(PluginConfig.globalTranslation().playerLocale(player));
    }

    @Override
    public Component cmp(@NotNull PlayerLike player) {
        return this.cmp(player.getLocale());
    }

    @Override
    public Component cmpDefault() {
        return this.cmp(PluginConfig.globalTranslation().getDefault());
    }

    @Override
    public String legacy(@NotNull Localization locale) {
        return Message.toLegacy(cmp(locale));
    }

    @Override
    public String legacy(@NotNull Locale locale) {
        return Message.toLegacy(cmp(locale));
    }

    @Override
    public String legacyDefault() {
        return Message.toLegacy(cmpDefault());
    }

    @Override
    public String text(@NotNull Localization locale) {
        return Message.toPlainText(cmp(locale));
    }

    @Override
    public String text(@NotNull Locale locale) {
        return Message.toPlainText(cmp(locale));
    }

    @Override
    public String textDefault() {
        return Message.toPlainText(cmpDefault());
    }

    @Override
    public String miniMessageText(@NotNull Localization locale) {
        return Message.toMiniMessageFormat(cmp(locale));
    }

    @Override
    public String miniMessageText(@NotNull Locale locale) {
        return miniMessageText(PluginConfig.globalTranslation().getOrDefault(locale));
    }

    @Override
    public String miniMessageTextDefault() {
        return miniMessageText(PluginConfig.globalTranslation().getDefault());
    }

    @Override
    public void send(@NotNull Player player) {
        PluginConfig.audiences().player(player)
                .sendMessage(cmp(PluginConfig.globalTranslation().playerLocale(player)));
    }

    @Override
    public void send(@NotNull Audience audience, @NotNull Localization locale) {
        audience.sendMessage(cmp(locale));
    }

    @Override
    public void send(@NotNull CommandSender sender) {
        if(sender instanceof Player) {
            send((Player) sender);
        } else {
            PluginConfig.audiences().commandSender(sender)
                    .sendMessage(cmpDefault());
        }
    }

    @Override
    public void sendActionbar(@NotNull Player player) {
        PluginConfig.actionbarHandler().sendActionbar(player, this);
    }

    @Override
    public void sendToConsole() {
       PluginConfig.audiences().console()
                .sendMessage(cmp(PluginConfig.globalTranslation().getDefault()));
    }

    @Override
    public void broadcast() {
        sendToConsole();
        for (Player p : Bukkit.getOnlinePlayers()) {
            send(p);
        }
    }

    @Override
    public void broadcastActionbar() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendActionbar(p);
        }
    }
}
