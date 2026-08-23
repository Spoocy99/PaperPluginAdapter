package dev.spoocy.adapter.spigot;

import dev.spoocy.adapter.compatibility.AdvancementAccess;
import dev.spoocy.adapter.compatibility.AudienceProvider;
import dev.spoocy.adapter.compatibility.CompatibilityProvider;
import dev.spoocy.adapter.compatibility.annotations.VersionRequirement;
import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import dev.spoocy.adapter.spigot.serializers.BungeeComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Entity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SpigotCompatibilityProvider implements CompatibilityProvider {

    public static final LegacyComponentSerializer BUNGEE_TEXT_SERIALIZER = LegacyComponentSerializer
            .builder()
            .character(LegacyComponentSerializer.SECTION_CHAR)
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    @VersionRequirement(
            version = "1.16"
    )
    public static final BungeeComponentSerializer BUNGEE_COMPONENT_SERIALIZER = BungeeComponentSerializer.get();

    private final SpigotAudienceProvider audienceProvider;

    public SpigotCompatibilityProvider(@NotNull Plugin plugin) {
        this.audienceProvider = new SpigotAudienceProvider(plugin);
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
        return new SpigotItemBuilder(itemStack);
    }

    @Override
    public @NotNull AudienceProvider getAudienceProvider() {
        return this.audienceProvider;
    }

    @Override
    public AdvancementAccess advancementAccess(@NotNull Advancement advancement) {
        return new SpigotAdvancementAccess(advancement);
    }

    @Override
    public void sendToConsole(@NotNull Component message) {
        Bukkit.getConsoleSender().spigot().sendMessage(BUNGEE_COMPONENT_SERIALIZER.serialize(message));
    }

    @Override
    public Inventory createInventory(@Nullable InventoryHolder owner, int size, @NotNull Component title) {
        return Bukkit.createInventory(owner, size, BUNGEE_TEXT_SERIALIZER.serialize(title));
    }

    @Override
    public Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type, @NotNull Component title) {
        return Bukkit.createInventory(owner, type, BUNGEE_TEXT_SERIALIZER.serialize(title));
    }

    @Override
    public <T extends Entity> T spawnEntity(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<T> function) {
        return location.getWorld().spawn(location, clazz, function);
    }

}
