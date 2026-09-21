package dev.spoocy.adapter.gui.icon;

import dev.spoocy.adapter.inventory.InventoryManager;
import dev.spoocy.adapter.message.LocalizedComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class EmptyIcon implements Icon {

    public static final EmptyIcon INSTANCE = new EmptyIcon();

    private EmptyIcon() {}


    @Override
    public Icon title(@NotNull Consumer<Title> title) {
        return this;
    }

    @Override
    public Icon title(@NotNull LocalizedComponent title) {
        return this;
    }

    @Override
    public Icon description(@NotNull Consumer<Description> description) {
        return this;
    }

    @Override
    public Icon description(@NotNull List<LocalizedComponent> description) {
        return this;
    }

    @Override
    public Icon amount(int amount) {
        return this;
    }

    @Override
    public Icon glowing() {
        return this;
    }

    @Override
    public ItemStack decode(@NotNull Locale localization) {
        return InventoryManager.INSTANCE
                .getFactory()
                .itemBuilder(Material.PAPER)
                .displayName(Component.text("Empty Icon"))
                .build();
    }
}
