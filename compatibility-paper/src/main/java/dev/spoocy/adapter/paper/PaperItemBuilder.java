package dev.spoocy.adapter.paper;

import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import net.kyori.adventure.text.Component;
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

public class PaperItemBuilder implements ItemBuilder {

    private final ItemStack item;
    private final ItemMeta itemMeta;

    public PaperItemBuilder(@NotNull ItemStack base) {
        this(base, base.getItemMeta());
    }

    public PaperItemBuilder(@NotNull ItemStack base, @NotNull ItemMeta meta) {
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
        return this.itemMeta.displayName();
    }

    @Override
    public List<Component> getLore() {
        return this.itemMeta.lore();
    }

    @Override
    public int getAmount() {
        return this.item.getAmount();
    }

    @Override
    public int getDamage() {
        if(this.itemMeta instanceof Damageable) {
            return ((Damageable) this.itemMeta).getDamage();
        }
        return 0;
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        return this.itemMeta.getItemFlags();
    }

    @Override
    public @NotNull ItemBuilder displayName(@NotNull Component name) {
        this.itemMeta.displayName(name);
        return this;
    }

    @Override
    public @NotNull ItemBuilder lore(@NotNull Component... components) {
        this.itemMeta.lore(List.of(components));
        return this;
    }

    @Override
    public @NotNull ItemBuilder lore(@NotNull List<Component> components) {
        this.itemMeta.lore(components);
        return this;
    }

    @Override
    public @NotNull ItemBuilder addLore(@NotNull Component component) {
        List<Component> lore = this.itemMeta.hasLore() ? new LinkedList<>(this.itemMeta.lore()) : new LinkedList<>();
        lore.add(component);
        this.itemMeta.lore(lore);
        return this;
    }

    @Override
    public @NotNull ItemBuilder addEmptyLore() {
        return addLore(Component.empty());
    }

    @Override
    public @NotNull ItemBuilder clearLore() {
        this.itemMeta.lore(null);
        return this;
    }

    @Override
    public @NotNull ItemBuilder removeLore(int line) {
        if(!this.itemMeta.hasLore()) return this;
        List<Component> lore = new LinkedList<>(this.itemMeta.lore());
        if(line < 0 || line >= lore.size()) return this;
        lore.remove(line);
        this.itemMeta.lore(lore);
        return this;
    }

    @Override
    public @NotNull ItemBuilder amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    @Override
    public @NotNull ItemBuilder damage(int damage) {
        if(this.itemMeta instanceof Damageable) {
            ((Damageable) this.itemMeta).setDamage(damage);
        }
        return this;
    }

    @Override
    public @NotNull ItemBuilder maxDamage(int maxDamage) {
        if(this.itemMeta instanceof Damageable) {
            ((Damageable) this.itemMeta).setMaxDamage(maxDamage);
        }
        return this;
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
                ItemFlag.HIDE_ARMOR_TRIM,
                ItemFlag.HIDE_STORED_ENCHANTS
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
        for(Enchantment enchantment : this.itemMeta.getEnchants().keySet()) {
            this.itemMeta.removeEnchant(enchantment);
        }
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
        return new PaperItemBuilder(this.item.clone(), this.itemMeta.clone());
    }
}
