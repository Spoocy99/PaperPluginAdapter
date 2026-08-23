package dev.spoocy.adapter.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractBukkitInventory extends AbstractCustomInventory {

    protected final Inventory inventory;

    public AbstractBukkitInventory(@NotNull Component title, int size, boolean listening) {
        super(title, size);
        this.inventory = createInventory(size, title);
        setListeningForActions(listening);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    @NotNull
    protected abstract Inventory createInventory(int size, @NotNull Component title);



}
