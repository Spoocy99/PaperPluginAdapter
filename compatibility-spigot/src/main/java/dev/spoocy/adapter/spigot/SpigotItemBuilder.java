package dev.spoocy.adapter.spigot;

import dev.spoocy.adapter.compatibility.items.AbstractItemBuilder;
import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SpigotItemBuilder extends AbstractItemBuilder {

    public SpigotItemBuilder(@NotNull ItemStack base) {
        super(base);
    }

    public SpigotItemBuilder(@NotNull Material base) {
        super(base);
    }

    private SpigotItemBuilder(@NotNull ItemStack item, @NotNull ItemMeta meta, int amount) {
        super(item, meta, amount);
    }

    @NotNull
    private static String serialize(@NotNull Component component) {
        return SpigotCompatibilityProvider.BUNGEE_TEXT_SERIALIZER.serialize(component);
    }

    @Override
    public @NotNull ItemBuilder displayName(@Nullable Component name) {
        return compute(meta -> {
            String s = name == null ? null : serialize(name);
            meta.setDisplayName(s);
        });
    }

    @Override
    public @NotNull ItemBuilder itemName(@NotNull Component name) {
        return compute(meta -> {
            String s = serialize(name);
            meta.setDisplayName(s);
        });
    }

    @Override
    public @NotNull ItemBuilder lore(@NotNull List<Component> components) {
        return compute(meta -> {
            final List<String> lore = new LinkedList<>();
            for(Component component : components) {
                lore.add(serialize(component));
            }
            meta.setLore(lore);
        });
    }

    @Override
    public @NotNull ItemBuilder addLore(@NotNull Component component) {
        return compute(meta -> {
            String s = serialize(component);

            List<String> lore = meta.getLore();
            if(lore == null) {
                lore = new LinkedList<>();
            }

            lore.add(s);
            meta.setLore(lore);
        });
    }

    @Override
    public @NotNull ItemBuilder clearLore() {
        return compute(meta -> meta.setLore(null));
    }

    @Override
    public @NotNull ItemBuilder removeLore(int line) {
        return compute(meta -> {
            List<String> lore = meta.getLore();
            if(lore == null) {
                return;
            }

            lore.remove(line);
            meta.setLore(lore);
        });
    }

    @Override
    public @NotNull ItemBuilder customModelData(@Nullable Integer data) {
        return compute(meta -> meta.setCustomModelData(data));
    }

    @Override
    public @NotNull ItemBuilder customModelData(@Nullable CustomModelDataComponent customModelData) {
        return compute(meta -> meta.setCustomModelDataComponent(customModelData));
    }

    @Override
    public @NotNull ItemBuilder enchantable(@Nullable Integer enchantable) {
        return compute(meta -> meta.setEnchantable(enchantable));
    }

    @Override
    public @NotNull ItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level) {
        return compute(meta -> meta.addEnchant(enchantment, level, false));
    }

    @Override
    public @NotNull ItemBuilder addEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            compute(meta -> meta.addEnchant(entry.getKey(), entry.getValue(), false));
        }
        return this;
    }

    @Override
    public @NotNull ItemBuilder addUnsafeEnchantment(@NotNull Enchantment enchantment, int level) {
        return compute(meta -> meta.addEnchant(enchantment, level, true));
    }

    @Override
    public @NotNull ItemBuilder addUnsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            compute(meta -> meta.addEnchant(entry.getKey(), entry.getValue(), true));
        }
        return this;
    }

    @Override
    public @NotNull ItemBuilder removeEnchantment(@NotNull Enchantment enchantment) {
        return compute(meta -> meta.removeEnchant(enchantment));
    }

    @Override
    public @NotNull ItemBuilder removeEnchantments() {
        return compute(ItemMeta::removeEnchantments);
    }

    @Override
    public ItemBuilder addFlags(@NotNull Collection<ItemFlag> flags) {
        return compute(meta -> meta.addItemFlags(flags.toArray(ItemFlag[]::new)));
    }

    @Override
    public ItemBuilder removeFlags(@NotNull Collection<ItemFlag> flags) {
        return compute(meta -> meta.removeItemFlags(flags.toArray(ItemFlag[]::new)));
    }

    @Override
    public @NotNull ItemBuilder hideTooltip(boolean hideTooltip) {
        return compute(meta -> meta.setUnbreakable(hideTooltip));
    }

    @Override
    public @NotNull ItemBuilder toolTipStyle(@Nullable NamespacedKey tooltipStyle) {
        return compute(meta -> meta.setTooltipStyle(tooltipStyle));
    }

    @Override
    public @NotNull ItemBuilder itemModel(@Nullable NamespacedKey itemModel) {
        return compute(meta -> meta.setItemModel(itemModel));
    }

    @Override
    public @NotNull ItemBuilder unbreakable(boolean unbreakable) {
        return compute(meta -> meta.setUnbreakable(unbreakable));
    }

    @Override
    public @NotNull ItemBuilder glow(@Nullable Boolean override) {
        return compute(meta -> meta.setEnchantmentGlintOverride(override));
    }

    @Override
    public @NotNull ItemBuilder glider(boolean glider) {
        return compute(meta -> meta.setGlider(glider));
    }

    @Override
    public @NotNull ItemBuilder fireResistant(boolean fireResistant) {
        return compute(meta -> meta.setFireResistant(fireResistant));
    }

    @Override
    public @NotNull ItemBuilder damageResistant(@Nullable Tag<DamageType> tag) {
        return compute(meta -> meta.setDamageResistant(tag));
    }

    @Override
    public @NotNull ItemBuilder damageResistant(@Nullable Collection<DamageType> damages) {
        return compute(meta -> meta.setDamageResistant(damages));
    }

    @Override
    public @NotNull ItemBuilder damageType(@Nullable DamageType type) {
        return compute(meta -> meta.setDamageType(type));
    }

    @Override
    public @NotNull ItemBuilder damageTypeKey(@Nullable NamespacedKey type) {
        return compute(meta -> meta.setDamageTypeKey(type));
    }

    @Override
    public @NotNull ItemBuilder maxStackSize(@Nullable Integer max) {
        return compute(meta -> meta.setMaxStackSize(max));
    }

    @Override
    public @NotNull ItemBuilder additionalTradeCost(@Nullable Integer cost) {
        return compute(meta -> meta.setAdditionalTradeCost(cost));
    }

    @Override
    public @NotNull ItemBuilder rarity(@Nullable ItemRarity rarity) {
        return compute(meta -> meta.setRarity(rarity));
    }

    @Override
    public @NotNull ItemBuilder useRemainder(@Nullable ItemStack remainder) {
        return compute(meta -> meta.setUseRemainder(remainder));
    }

    @Override
    public @NotNull ItemBuilder damage(int damage) {
        return computeOn(Damageable.class, meta -> meta.setDamage(damage));
    }

    @Override
    public @NotNull ItemBuilder maxDamage(int maxDamage) {
        return computeOn(Damageable.class, meta -> meta.setMaxDamage(maxDamage));
    }

    @Override
    public @NotNull ItemBuilder copy() {
        return new SpigotItemBuilder(super.base.clone(), super.meta.clone(), super.amount);
    }

    @Override
    public ItemBuilder clone() {
        return copy();
    }
}
