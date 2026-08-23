package dev.spoocy.adapter.spigot.audiences;

import dev.spoocy.adapter.spigot.SpigotCompatibilityProvider;
import dev.spoocy.adapter.spigot.audiences.facets.BossBarFacets;
import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SpigotPlayerAudience implements Audience {

    private final UUID uuid;

    public SpigotPlayerAudience(@NotNull UUID uuid) {
        this.uuid = Args.notNull(uuid, "uuid");
    }

    private void apply(@NotNull Consumer<Player> consumer) {
        Player player = null;

        try {
            player = Bukkit.getPlayer(uuid);
        } catch (Exception ignored) {
        }


        if (player != null) {
            consumer.accept(player);
        }
    }

    private String toMessage(@NotNull ComponentLike message) {
        return SpigotCompatibilityProvider.BUNGEE_TEXT_SERIALIZER.serialize(message.asComponent());
    }

    private BaseComponent[] toCmp(@NotNull ComponentLike message) {
        return SpigotCompatibilityProvider.BUNGEE_COMPONENT_SERIALIZER.serialize(message.asComponent());
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        apply(player -> player.spigot().sendMessage(toCmp(message)));
    }

    @Override
    public void sendMessage(
            @NotNull Component message,
            @NotNull ChatType.Bound boundChatType
    ) {
        if (boundChatType.type().equals(ChatType.CHAT)) {
            apply(player -> player.spigot().sendMessage(toCmp(message)));

        } else {
            apply(player -> player.spigot().sendMessage(ChatMessageType.SYSTEM, toCmp(message)));
        }
    }

    @Override
    public void sendMessage(
            @NotNull SignedMessage signedMessage,
            @NotNull ChatType.Bound boundChatType
    ) {
        apply(player -> player.sendMessage(signedMessage.message()));
    }

    @Override
    public void deleteMessage(@NotNull SignedMessage signedMessage) {
        // signed messages not supported in spigot
        Audience.super.deleteMessage(signedMessage);
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        apply(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, toCmp(message)));
    }

    @Override
    public void sendPlayerListHeaderAndFooter(
            @NotNull Component header,
            @NotNull Component footer
    ) {
        apply(player -> player.setPlayerListHeaderFooter(
                toMessage(header),
                toMessage(footer)
        ));
    }

    @Override
    public void showTitle(@NotNull Title title) {
        Title.Times times = title.times();

        if (times == null) {
            throw new IllegalArgumentException("title times null");
        }

        int fadeIn = toTicks(times.fadeIn());
        int stay = toTicks(times.stay());
        int fadeOut = toTicks(times.fadeOut());

        apply(player -> player.sendTitle(
                toMessage(title.title()),
                toMessage(title.subtitle()),
                fadeIn,
                stay,
                fadeOut
        ));
    }

    /**
     * Use {@link #showTitle(Title)}
     */
    @Override
    public <T> void sendTitlePart(
            @NotNull TitlePart<T> part,
            @NotNull T value
    ) {
        // not supported in spigot
    }

    @Override
    public void clearTitle() {
        apply(player -> player.sendTitle("", "", 0, 0, 0));
    }

    @Override
    public void resetTitle() {
        apply(Player::resetTitle);
    }

    @Override
    public void showBossBar(@NotNull BossBar bar) {
        apply(p -> BossBarFacets.INSTANCE.getFacet(bar).addPlayer(p));
    }

    @Override
    public void hideBossBar(@NotNull BossBar bar) {
        apply(p -> BossBarFacets.INSTANCE.getFacet(bar).removePlayer(p));
    }

    @Override
    public void playSound(@NotNull Sound sound) {
        // not supported in spigot
    }

    @Override
    public void playSound(
            @NotNull Sound sound,
            double x,
            double y,
            double z
    ) {
        // not supported in spigot
    }

    @Override
    public void playSound(
            @NotNull Sound sound,
            @NotNull Sound.Emitter emitter
    ) {
        // not supported in spigot
    }

    @Override
    public void stopSound(@NotNull SoundStop stop) {
        // not supported in spigot
    }

    @Override
    public void openBook(@NotNull Book book) {
        // not supported in spigot
    }

    @Override
    public void sendResourcePacks(@NotNull ResourcePackRequest request) {
        // not supported in spigot
    }

    @Override
    public void removeResourcePacks(
            @NotNull UUID id,
            @NotNull UUID... others
    ) {
        // not supported in spigot
    }

    @Override
    public void clearResourcePacks() {
        // not supported in spigot
    }

    @Override
    public void showDialog(@NotNull DialogLike dialog) {
        // not supported in spigot
    }

    @Override
    public void closeDialog() {
        apply(Player::clearDialog);
    }

    private static final long MAX_SECONDS = Long.MAX_VALUE / 20;

    private static int toTicks(@Nullable Duration duration) {
        if (duration == null || duration.isNegative()) {
            return -1;
        }

        if (duration.getSeconds() > MAX_SECONDS) {
            return Integer.MAX_VALUE;
        }

        return (int) (duration.getSeconds() * 20 // 20ticks/sec
                + duration.getNano() / 50_000_000); // 50ms * 1ms/1000000ns
    }

}
