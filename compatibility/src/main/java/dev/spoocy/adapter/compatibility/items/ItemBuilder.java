package dev.spoocy.adapter.compatibility.items;

import dev.spoocy.adapter.compatibility.annotations.On;
import dev.spoocy.adapter.compatibility.annotations.VersionRequirement;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ItemBuilder extends Cloneable {

    /**
     * Gets the {@link Material} of the item.
     *
     * @return The Material of the item.
     */
    Material asMaterial();

    /**
     * Gets the {@link ItemMeta} of the item.
     *
     * @return The ItemMeta of the item.
     */
    ItemMeta asItemMeta();

    /**
     * Sets the type.
     *
     * @param type The material.
     *
     * @return current instance for chaining.
     */
    ItemBuilder type(@NotNull Material type);

    /**
     * Sets the amount.
     *
     * @param amount The amount.
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder amount(int amount);

    /**
     * Sets the display name.
     *
     * @param name The display name component.
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder displayName(@Nullable Component name);

    /**
     * Sets the item name.
     * <br>
     * Item name differs from display name in that it is cannot be edited by an
     * anvil, is not styled with italics, and does not show labels.
     *
     * @param name the name to set
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder itemName(@NotNull Component name);

    /**
     * Sets the localized name.
     *
     * @param name the name to set
     *
     * @return current instance for chaining.
     *
     * @deprecated meta no longer exists since 1.20.5
     */
    @Deprecated(since = "1.20.5")
    @Contract("_ -> this")
    @NotNull
    ItemBuilder localizedName(@NotNull String name);

    /**
     * Sets the lore.
     *
     * @param components The lore components.
     *
     * @return current instance for chaining.
     */
    @Contract("_, _ -> this")
    @NotNull
    default ItemBuilder lore(@NotNull Component component, @NotNull Component... components) {
        List<Component> list = new LinkedList<>();
        list.add(component);
        list.addAll(List.of(components));

        return lore(list);
    }

    /**
     * Sets the lore.
     *
     * @param components The lore components.
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder lore(@NotNull List<Component> components);

    /**
     * Adds a line to the lore.
     *
     * @param component The lore component to add.
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder addLore(@NotNull Component component);

    /**
     * Adds an empty line to the lore.
     *
     * @return current instance for chaining.
     */
    @Contract("-> this")
    @NotNull
    default ItemBuilder addEmptyLore() {
        addLore(Component.empty());
        return this;
    }

    /**
     * Clears the lore.
     *
     * @return current instance for chaining.
     */
    @Contract("-> this")
    @NotNull
    ItemBuilder clearLore();

    /**
     * Removes a line from the lore.
     *
     * @param line The line index to remove (0-based).
     *
     * @return current instance for chaining.
     *
     * @throws IndexOutOfBoundsException if the line index is out of bounds.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder removeLore(int line);

    /**
     * Sets the custom model data.
     * <p>
     * CustomModelData is an integer that may be associated client side with a
     * custom item model.
     *
     * @param data the data to set, or null to clear
     *
     * @return current instance for chaining.
     *
     * @deprecated in favour of {@link #customModelData(CustomModelDataComponent)}
     */
    @Deprecated(since = "1.21.5")
    @Contract("_ -> this")
    @NotNull
    ItemBuilder customModelData(@Nullable Integer data);

    /**
     * Sets the custom model data component.
     *
     * @param customModelData new component
     *
     * @return current instance for chaining.
     */
    @VersionRequirement(
            version = "1.18.2"
    )
    @Contract("_ -> this")
    @NotNull
    ItemBuilder customModelData(@Nullable CustomModelDataComponent customModelData);

    /**
     * Sets the enchantable. Higher values allow higher enchantments.
     *
     * @param enchantable enchantable value
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder enchantable(@Nullable Integer enchantable);

    /**
     * Adds an {@link Enchantment}.
     *
     * @param enchantment The enchantment to add.
     * @param level       The level of the enchantment.
     *
     * @return current instance for chaining.
     */
    @Contract("_, _ -> this")
    @NotNull
    ItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level);

    /**
     * Adds multiple enchantments.
     *
     * @param enchantments A map of enchantments and their levels to add.
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder addEnchantments(@NotNull Map<Enchantment, Integer> enchantments);

    /**
     * Adds an unsafe {@link Enchantment}.
     * <br> This allows adding enchantments with levels higher than the maximum level.
     *
     * @param enchantment The enchantment to add.
     * @param level       The level of the enchantment.
     *
     * @return current instance for chaining.
     */
    @Contract("_, _ -> this")
    @NotNull
    ItemBuilder addUnsafeEnchantment(@NotNull Enchantment enchantment, int level);

    /**
     * Adds multiple unsafe enchantments.
     * <br> This allows adding enchantments with levels higher than the maximum level.
     *
     * @param enchantments A map of enchantments and their levels to add.
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder addUnsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments);

    /**
     * Removes an {@link Enchantment}
     *
     * @param enchantment The enchantment to remove.
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder removeEnchantment(@NotNull Enchantment enchantment);

    /**
     * Removes all enchantments.
     *
     * @return current instance for chaining.
     */
    @Contract("-> this")
    @NotNull
    ItemBuilder removeEnchantments();

    /**
     * Adds {@link ItemFlag ItemFlags}.
     *
     * @param flags The item flags.
     *
     * @return current instance for chaining.
     */
    ItemBuilder addFlags(@NotNull Collection<ItemFlag> flags);

    /**
     * Adds {@link ItemFlag ItemFlags}.
     *
     * @param flag  The first item flags.
     * @param flags The other item flags.
     *
     * @return current instance for chaining.
     */
    @Contract("_, _ -> this")
    @NotNull
    default ItemBuilder addFlag(@NotNull ItemFlag flag, @NotNull ItemFlag... flags) {
        return addFlags(EnumSet.of(flag, flags));
    }

    /**
     * Removes {@link ItemFlag ItemFlags}.
     *
     * @param flags The item flags.
     *
     * @return current instance for chaining.
     */
    ItemBuilder removeFlags(@NotNull Collection<ItemFlag> flags);

    /**
     * Removes {@link ItemFlag ItemFlags}.
     *
     * @param flag  The first item flags.
     * @param flags The other item flags.
     *
     * @return current instance for chaining.
     */
    @Contract("_, _ -> this")
    @NotNull
    default ItemBuilder removeFlag(@NotNull ItemFlag flag, @NotNull ItemFlag... flags) {
        return removeFlags(EnumSet.of(flag, flags));
    }

    /**
     * Removes all {@link ItemFlag}s from the item.
     *
     * @return current instance for chaining.
     */
    @Contract("-> this")
    @NotNull
    default ItemBuilder showAttributes() {
        return removeFlags(List.of(ItemFlag.values()));
    }

    /**
     * Applies all hiding {@link ItemFlag ItemFlags} to the item;
     *
     * @return current instance for chaining.
     */
    @Contract("-> this")
    @NotNull
    default ItemBuilder hideAttributes() {
        return this.addFlag(
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

    /**
     * Sets if this item has hide_tooltip set. An item with this set will not
     * show any tooltip whatsoever.
     *
     * @param hideTooltip new hide_tooltip
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder hideTooltip(boolean hideTooltip);

    default ItemBuilder hideTooltip() {
        return hideTooltip(true);
    }

    /**
     * Sets the custom tooltip style.
     *
     * @param tooltipStyle the new style
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder toolTipStyle(@Nullable NamespacedKey tooltipStyle);

    /**
     * Sets the custom item model.
     *
     * @param itemModel the new model
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder itemModel(@Nullable NamespacedKey itemModel);

    /**
     * Sets the unbreakable tag. An unbreakable item will not lose durability.
     *
     * @param unbreakable true if set unbreakable
     *
     * @return current instance for chaining.
     */
    @VersionRequirement(
            version = "1.11"
    )
    @Contract("_ -> this")
    @NotNull
    ItemBuilder unbreakable(boolean unbreakable);

    default ItemBuilder unbreakable() {
        return unbreakable(true);
    }

    /**
     * Sets the enchantment_glint_override. If true, the item will glint, even
     * without enchantments; if false, the item will not glint, even with
     * enchantments. If null, the override will be cleared.
     *
     * @param override new enchantment_glint_override
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder glow(@Nullable Boolean override);

    default ItemBuilder glow() {
        return glow(true);
    }

    /**
     * Sets if this item is a glider. If true, this item will allow players to
     * glide when it is equipped.
     *
     * @param glider glider
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder glider(boolean glider);

    default ItemBuilder glider() {
        return glider(true);
    }

    /**
     * Sets if this item is fire_resistant. If {@code true}, it will not burn in fire
     * or lava.
     *
     * @param fireResistant fire_resistant
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder fireResistant(boolean fireResistant);

    @Contract(" -> this")
    @NotNull
    default ItemBuilder fireResistant() {
        return fireResistant(true);
    }

    /**
     * Sets the type of damage this item will be resistant to when in entity
     * form.
     *
     * @param tag the tag, or null to clear
     *
     * @return current instance for chaining.
     */
    @VersionRequirement(
            version = "1.18.2"
    )
    @Contract("_ -> this")
    @NotNull
    ItemBuilder damageResistant(@Nullable Tag<DamageType> tag);

    /**
     * Sets the type of damage this item will be resistant to when in entity
     * form.
     *
     * @param damages the tag, or null to clear
     *
     * @return current instance for chaining.
     */
    @VersionRequirement(
            version = "1.18.2"
    )
    @Contract("_ -> this")
    @NotNull
    ItemBuilder damageResistant(@Nullable Collection<DamageType> damages);

    /**
     * Sets the type of damage this item will deliver.
     *
     * @param type the type, or null to clear
     *
     * @return current instance for chaining.
     */
    @VersionRequirement(
            version = "1.18.2",
            onlySpigot = true
    )
    @Contract("_ -> this")
    @NotNull
    ItemBuilder damageType(@Nullable DamageType type);

    /**
     * Sets the type of damage this item will deliver.
     *
     * @param type the type, or null to clear
     *
     * @return current instance for chaining.
     */
    @VersionRequirement(
            version = "1.18.2",
            onlySpigot = true
    )
    @Contract("_ -> this")
    @NotNull
    ItemBuilder damageTypeKey(@Nullable NamespacedKey type);

    /**
     * Sets the max_stack_size. This is the maximum amount which an item will
     * stack.
     *
     * @param max max_stack_size, between 1 and 99 (inclusive)
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder maxStackSize(@Nullable Integer max);

    /**
     * Sets the additional_trade_cost. This is the additional cost for villager
     * trades.
     *
     * @param cost additional_trade_cost
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder additionalTradeCost(@Nullable Integer cost);

    /**
     * Sets the item rarity.
     *
     * @param rarity new rarity
     *
     * @return current instance for chaining.
     */
    @VersionRequirement(
            version = "1.18.2"
    )
    @Contract("_ -> this")
    @NotNull
    ItemBuilder rarity(@Nullable ItemRarity rarity);

    /**
     * Sets the item which this item will convert to when used.
     *
     * @param remainder new item
     *
     * @return current instance for chaining.
     */
    @VersionRequirement(
            version = "1.18.2"
    )
    @Contract("_ -> this")
    @NotNull
    ItemBuilder useRemainder(@Nullable ItemStack remainder);

    /**
     * Sets the damage of the item.
     *
     * @param damage The damage.
     *
     * @return current instance for chaining.
     */
    @VersionRequirement(
            version = "1.12"
    )
    @On(Damageable.class)
    @Contract("_ -> this")
    @NotNull
    ItemBuilder damage(int damage);

    /**
     * Sets the maximum damage of the item.
     * <p>
     * Does nothing if the item is not damageable.
     *
     * @param maxDamage The maximum damage of the item.
     *
     * @return current instance for chaining.
     */
    @On(Damageable.class)
    @Contract("_ -> this")
    @NotNull
    ItemBuilder maxDamage(int maxDamage);

    /**
     * Sets the color of the Dye.
     *
     * @param color The color to apply to the item.
     *
     * @return current instance for chaining.
     */
    @Deprecated(since = "1.6.2")
    @Contract("_ -> this")
    @NotNull
    default ItemBuilder dyeColor(@NotNull DyeColor color) {
        return damage(color.getWoolData());
    }

    /**
     * Sets the color of the Dye.
     *
     * @param color The color to apply to the item.
     *
     * @return current instance for chaining.
     */
    @Deprecated(since = "1.6.2")
    @Contract("_ -> this")
    @NotNull
    default ItemBuilder woolColor(@NotNull DyeColor color) {
        return damage(color.getWoolData());
    }

    /**
     * Sets the owner of the skull.
     *
     * @param owner the new owner of the skull
     *
     * @return current instance for chaining.
     *
     * @deprecated see {@link #skullOwner(OfflinePlayer)}.
     */
    @Deprecated(since = "1.12.1")
    @On(SkullMeta.class)
    @Contract("_ -> this")
    @NotNull
    ItemBuilder skullOwner(@NotNull String owner);

    /**
     * Sets the owner of the skull.
     *
     * @param owner the new owner of the skull
     *
     * @return current instance for chaining.
     */
    @On(SkullMeta.class)
    @Contract("_ -> this")
    @NotNull
    ItemBuilder skullOwner(@NotNull OfflinePlayer owner);

    /**
     * Sets the texture of the skull.
     *
     * @param url the url to the texture
     *
     * @return current instance for chaining.
     */
    @On(SkullMeta.class)
    @Contract("_, _ -> this")
    @NotNull
    ItemBuilder skullTexture(@NotNull String url, SkullApi.Type datatype);

    /**
     * Sets the texture of the skull.
     *
     * @param url the url to the texture
     *
     * @return current instance for chaining.
     */
    @On(SkullMeta.class)
    @Contract("_ -> this")
    @NotNull
    default ItemBuilder skullTextureUrl(@NotNull String url) {
        return skullTexture(url, SkullApi.Type.URL);
    }

    /**
     * Sets the texture of the skull.
     *
     * @param base64 the base64 encoded texture
     *
     * @return current instance for chaining.
     */
    @On(SkullMeta.class)
    @Contract("_ -> this")
    @NotNull
    default ItemBuilder skullTextureBase64(@NotNull String base64) {
        return skullTexture(base64, SkullApi.Type.BASE64);
    }

    /**
     * Sets the sound to play if the skull is placed on a note block.
     * <br>
     * <strong>Note:</strong> This only works for player heads. For other heads,
     * see {@link org.bukkit.Instrument}.
     *
     * @param noteBlockSound the key of the sound to be played, or null
     *
     * @return current instance for chaining.
     */
    @On(SkullMeta.class)
    @Contract("_ -> this")
    @NotNull
    ItemBuilder noteBlockSound(@Nullable NamespacedKey noteBlockSound);

    /**
     * Conditionally applies an action to the ItemBuilder if the predicate is true.
     *
     * @param predicate The condition to test.
     * @param action    The action to apply if.
     *
     * @return current instance for chaining.
     */
    @Contract("_, _ -> this")
    @NotNull
    default ItemBuilder computeIf(
            @NotNull Predicate<ItemBuilder> predicate,
            @NotNull Consumer<ItemBuilder> action
    ) {
        if (predicate.test(this)) {
            action.accept(this);
        }
        return this;
    }

    /**
     * Applies an action to the {@link ItemMeta}.
     *
     * @param action    The action to apply.
     *
     * @return current instance for chaining.
     */
    @Contract("_ -> this")
    @NotNull
    ItemBuilder compute(@NotNull Consumer<ItemMeta> action);

    /**
     * Conditionally applies an action to the {@link ItemMeta} if
     * the meta is of the given type.
     *
     * @param meta      The type of {@link ItemMeta} to check.
     * @param action    The action to apply.
     *
     * @return current instance for chaining.
     */
    @Contract("_, _ -> this")
    @NotNull
    <M extends ItemMeta> ItemBuilder computeOn(
            @NotNull Class<M> meta,
            @NotNull Consumer<M> action
    );

    /**
     * Builds the item and returns the ItemStack.
     *
     * @return The built ItemStack.
     */
    @Contract("-> new")
    @NotNull
    ItemStack build();

    /**
     * Clones the ItemBuilder.
     *
     * @return A new instance of the ItemBuilder with the same properties.
     */
    @Contract("-> new")
    @NotNull
    ItemBuilder copy();

}
