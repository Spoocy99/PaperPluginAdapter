package dev.spoocy.adapter.spigot;

import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import dev.spoocy.utils.common.collections.Collector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SpigotItemBuilder implements ItemBuilder {

    private final ItemStack item;
    private final ItemMeta itemMeta;

    public SpigotItemBuilder(@NotNull ItemStack base) {
        this(base, base.getItemMeta());
    }

    public SpigotItemBuilder(@NotNull ItemStack base, @NotNull ItemMeta meta) {
        this.item = base;
        this.itemMeta = meta;
    }

    @Override
    public ItemMeta getItemMeta() {
        return this.itemMeta;
    }

    @Override
    public Material getMaterial() {
        return this.item.getType();
    }

    @Override
    public Component getDisplayName() {
        return SpigotCompatibilityProvider.BUNGEE_TEXT_SERIALIZER.deserialize(this.itemMeta.getDisplayName());
    }

    @Override
    public List<Component> getLore() {
        List<String> lore = this.itemMeta.getLore();
        if(lore == null) return List.of();
        return Collector.mapList(lore, SpigotCompatibilityProvider.BUNGEE_TEXT_SERIALIZER::deserialize);
    }

    @Override
    public int getAmount() {
        return this.item.getAmount();
    }

    @Override
    public short getDurability() {
        if(this.item instanceof Damageable) {
            return (short) ((Damageable) this.item).getDamage();
        }
        throw new UnsupportedOperationException("Item is not damageable");
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        return this.itemMeta.getItemFlags();
    }

    @Override
    public @NotNull ItemBuilder displayName(@NotNull Component name) {
        this.itemMeta.setDisplayName(SpigotCompatibilityProvider.BUNGEE_TEXT_SERIALIZER.serialize(name));
        return this;
    }

    @Override
    public @NotNull ItemBuilder lore(@NotNull Component... components) {
        return lore(List.of(components));
    }

    @Override
    public @NotNull ItemBuilder lore(@NotNull List<Component> components) {
        final List<String> lore = new LinkedList<>();
        for(Component component : components) {
            lore.add(SpigotCompatibilityProvider.BUNGEE_TEXT_SERIALIZER.serialize(component));
        }
        this.itemMeta.setLore(lore);
        return this;
    }

    @Override
    public @NotNull ItemBuilder addLore(@NotNull Component component) {
        addLoreLine(SpigotCompatibilityProvider.BUNGEE_TEXT_SERIALIZER.serialize(component));
        return this;
    }

    @Override
    public @NotNull ItemBuilder addEmptyLore() {
        addLoreLine(ChatColor.WHITE + "");
        return this;
    }

    private void addLoreLine(String line) {
        List<String> lore = this.itemMeta.hasLore() ? new LinkedList<>(this.itemMeta.getLore()) : new LinkedList<>();
        lore.add(line);
        this.itemMeta.setLore(lore);
    }

    @Override
    public @NotNull ItemBuilder clearLore() {
        this.itemMeta.setLore(null);
        return this;
    }

    @Override
    public @NotNull ItemBuilder removeLore(int line) {
        if(!this.itemMeta.hasLore()) return this;

        List<String> lore = new LinkedList<>(this.itemMeta.getLore());

        if(line < 0 || line >= lore.size()) {
            throw new IndexOutOfBoundsException("Line " + line + " is out of bounds for lore of size " + lore.size());
        }
        lore.remove(line);
        this.itemMeta.setLore(lore);
        return this;
    }

    @Override
    public @NotNull ItemBuilder amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    @Override
    public @NotNull ItemBuilder durability(short durability) {
        if(this.item instanceof Damageable) {
            ((Damageable) this.item).setDamage(durability);
            return this;
        }
        throw new UnsupportedOperationException("Item is not damageable");
    }

    @Override
    public @NotNull ItemBuilder addFlags(@NotNull ItemFlag... flag) {
        this.itemMeta.addItemFlags(flag);
        return this;
    }

    @Override
    public @NotNull ItemBuilder removeFlags(@NotNull ItemFlag... flag) {
        this.itemMeta.removeItemFlags(flag);
        return this;
    }

    @Override
    public @NotNull ItemBuilder hideAttributes() {
        return this.addFlags(
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                ItemFlag.HIDE_DYE,
                ItemFlag.HIDE_ARMOR_TRIM
        );
    }

    @Override
    public @NotNull ItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, false);
        return this;
    }

    @Override
    public @NotNull ItemBuilder addEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        enchantments.forEach(this::addEnchantment);
        return this;
    }

    @Override
    public @NotNull ItemBuilder addUnsafeEnchantment(@NotNull Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    @Override
    public @NotNull ItemBuilder addUnsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        enchantments.forEach(this::addUnsafeEnchantment);
        return this;
    }

    @Override
    public @NotNull ItemBuilder removeEnchantment(@NotNull Enchantment enchantment) {
        this.itemMeta.removeEnchant(enchantment);
        return this;
    }

    @Override
    public @NotNull ItemBuilder removeEnchantments() {
        this.itemMeta.getEnchants().keySet().forEach(this.itemMeta::removeEnchant);
        return this;
    }

    @Override
    public @NotNull ItemBuilder setUnbreakable(boolean unbreakable) {
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    @Override
    public @NotNull ItemStack build() {
        this.item.setItemMeta(this.itemMeta);
        return this.item;
    }

    @Override
    public @NotNull ItemBuilder clone() {
        return new SpigotItemBuilder(this.item.clone(), this.itemMeta.clone());
    }
}
