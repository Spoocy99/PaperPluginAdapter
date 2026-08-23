package dev.spoocy.adapter.paper;

import dev.spoocy.adapter.compatibility.AdvancementAccess;
import dev.spoocy.adapter.compatibility.CompatibilityProvider;
import dev.spoocy.adapter.compatibility.AudienceProvider;
import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Entity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class PaperCompatibilityProvider implements CompatibilityProvider {

    public static final PlainTextComponentSerializer PLAIN_TEXT_SERIALIZER = PlainTextComponentSerializer.plainText();

    private final PaperAudienceProvider audienceProvider;

    public PaperCompatibilityProvider() {
        this.audienceProvider = new PaperAudienceProvider();
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        this.audienceProvider.initialize();
    }

    @Override
    public void onDisable() {
        this.audienceProvider.close();
    }

    @Override
    public @NotNull ItemBuilder itemBuilder(@NotNull ItemStack itemStack) {
        return new PaperItemBuilder(itemStack);
    }

    @Override
    public @NotNull AudienceProvider getAudienceProvider() {
        return this.audienceProvider;
    }

    @Override
    public AdvancementAccess advancementAccess(@NotNull Advancement advancement) {
        return new PaperAdvancementAccess(advancement);
    }

    @Override
    public void sendToConsole(@NotNull Component message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    @Override
    public Inventory createInventory(@Nullable InventoryHolder owner, int size, @NotNull Component title) {
        return Bukkit.createInventory(owner, size, title);
    }

    @Override
    public Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type, @NotNull Component title) {
        return Bukkit.createInventory(owner, type, title);
    }

    @Override
    public <T extends Entity> T spawnEntity(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<T> function) {
        return location.getWorld().spawn(location, clazz, function);
    }

}
