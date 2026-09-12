package dev.spoocy.adapter.message.types;

import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.message.GlobalTranslation;
import dev.spoocy.adapter.message.serialization.RenderContext;
import dev.spoocy.adapter.message.style.MessageStyle;
import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractPluginMessage<P extends ScopedMessage<P>> implements ScopedMessage<P> {

    private final MessageStyle style;

    protected AbstractPluginMessage(@NotNull MessageStyle style) {
        this.style = Args.notNull(style, "style");
    }

    protected abstract @NotNull P newInstance(@NotNull MessageStyle style);

    @Override
    public final @NotNull MessageStyle style() {
        return this.style;
    }

    @Override
    public @NotNull P style(@NotNull MessageStyle style) {
        return newInstance(style);
    }

    @NotNull
    protected GlobalTranslation translation() {
        return PluginConfig.globalTranslation();
    }

    @Override
    public @NotNull List<Component> cmpLines(@Nullable RenderContext context, boolean freshStyle) {
        return translation().renderLines(this, context, freshStyle);
    }

    @Override
    public @NotNull Component cmp(@Nullable RenderContext context, boolean freshStyle) {
        return translation().render(this, context, freshStyle);
    }

    /*
     * direct sending for a serialized component
     */

    @Override
    public void send(@NotNull Player player) {
        Audience audience = PluginConfig.audiences().player(player);
        audience.sendMessage(this.cmp((Pointered) audience));
    }

    @Override
    public void send(@NotNull CommandSender sender) {
        if(sender instanceof Player) {
            send((Player) sender);
        } else {

            Audience audience = PluginConfig.audiences().commandSender(sender);
            audience.sendMessage(this.cmp((Pointered) audience));
        }
    }

    @Override
    public void sendActionbar(@NotNull Player player) {
        Component component = this.cmp(player);
        PluginConfig.actionbarHandler().sendActionbar(player, component);
    }

    @Override
    public void sendToConsole() {
        Audience console = PluginConfig.audiences().console();
        console.sendMessage(this.cmp((Pointered) console));
    }

    @Override
    public void broadcast() {
        Audience all = PluginConfig.compatibilityProvider().getAudienceProvider().all();
        broadcast(all);
    }

    @Override
    public void broadcast(@NotNull Audience audience) {
        if(audience instanceof ForwardingAudience) {
            // forwarding audiences do not support context
            broadcast((ForwardingAudience) audience);
            return;
        }

        audience.sendMessage(this.cmp((Pointered) audience));
    }

    @Override
    public void broadcast(@NotNull ForwardingAudience audiences) {
        // unpack
        audiences.forEachAudience(this::broadcast);
    }

    @Override
    public void broadcastActionbar() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendActionbar(p);
        }
    }
}
