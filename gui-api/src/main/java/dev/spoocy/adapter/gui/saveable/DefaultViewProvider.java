package dev.spoocy.adapter.gui.saveable;

import dev.spoocy.adapter.gui.view.GuiView;
import dev.spoocy.adapter.log.LogAs;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@LogAs("DefaultViewProvider")
public class DefaultViewProvider<V extends GuiView> extends PlayerViewProvider<V> {

    private final Function<Player, V> viewFunction;

    public DefaultViewProvider(@NotNull Function<Player, V> viewFunction, boolean clearOnDisconnect) {
        super(clearOnDisconnect);
        this.viewFunction = viewFunction;
    }

    @Override
    public V createView(@NotNull Player player) {
        return this.viewFunction.apply(player);
    }
}
