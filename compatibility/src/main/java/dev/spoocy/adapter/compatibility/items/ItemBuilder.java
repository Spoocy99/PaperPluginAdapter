package dev.spoocy.adapter.compatibility.items;

import dev.spoocy.adapter.compatibility.annotations.VersionRequirement;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ItemBuilder {

    /**
     * Gets the {@link ItemMeta} of the item.
     *
     * @return The ItemMeta of the item.
     */
    ItemMeta getItemMeta();

    /**
     * Gets the {@link Material} of the item.
     *
     * @return The Material of the item.
     */
    Material getMaterial();

    /**
     * Gets the display name of the item.
     *
     * @return The display name component.
     */
    Component getDisplayName();

    /**
     * Gets the lore of the item.
     *
     * @return The lore components.
     */
    List<Component> getLore();

    /**
     * Gets the amount of the item.
     *
     * @return The amount of the item.
     */
    int getAmount();

    /**
     * @deprecated Use {@link #getDamage()} instead.
     */
    @VersionRequirement(
            version = "1.12"
    )
    @Deprecated
    default short getDurability() {
        return (short) this.getDamage();
    }

    /**
     * Gets the damage of the item or 0 if not damageable.
     *
     * @return The damage of the item.
     *
     * @see org.bukkit.inventory.meta.Damageable#getDamage()
     */
    @VersionRequirement(
            version = "1.12"
    )
    int getDamage();

    /**
     * Gets the item flags of the item.
     *
     * @return The item flags of the item.
     */
    Set<ItemFlag> getItemFlags();

    /**
     * Sets the display name of the item.
     *
     * @param name The display name component.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_ -> this")
    @NotNull ItemBuilder displayName(@NotNull Component name);

    /**
     * Sets the lore of the item.
     *
     * @param components The lore components.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_ -> this")
    @NotNull ItemBuilder lore(@NotNull Component... components);

    /**
     * Sets the lore of the item.
     *
     * @param components The lore components.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_ -> this")
    @NotNull ItemBuilder lore(@NotNull List<Component> components);

    /**
     * Adds a line to the lore of the item.
     *
     * @param component The lore component to add.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_ -> this")
    @NotNull ItemBuilder addLore(@NotNull Component component);

    /**
     * Adds an empty line to the lore of the item.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("-> this")
    @NotNull ItemBuilder addEmptyLore();

    /**
     * Builds the item and returns the ItemMeta.
     *
     * @return The built ItemMeta.
     */
    @Contract("-> this")
    @NotNull ItemBuilder clearLore();

    /**
     * Removes a line from the lore of the item.
     *
     * @param line The line index to remove (0-based).
     *
     * @return The current instance of the ItemBuilder for method chaining.
     *
     * @throws IndexOutOfBoundsException if the line index is out of bounds.
     */
    @Contract("_ -> this")
    @NotNull ItemBuilder removeLore(int line);

    /**
     * Sets the amount of the item.
     *
     * @param amount The amount of the item.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     *
     * @throws IllegalArgumentException if the amount is less than 1 or greater than 64.
     */
    @Contract("_ -> this")
    @NotNull ItemBuilder amount(int amount);

    /**
     * @see ItemBuilder#damage(int)
     * @see org.bukkit.inventory.meta.Damageable#setDamage(int)
     */
    @VersionRequirement(
            version = "1.12"
    )
    @Deprecated
    @Contract("_ -> this")
    default @NotNull ItemBuilder durability(short durability) {
        return damage(durability);
    }

    /**
     * Sets the damage of the item.
     * <p>
     * Does nothing if the item is not damageable.
     *
     * @param damage The damage of the item.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @VersionRequirement(
            version = "1.12"
    )
    @Contract("_ -> this")
    @NotNull ItemBuilder damage(int damage);

    /**
     * Sets the maximum damage of the item.
     * <p>
     * Does nothing if the item is not damageable.
     *
     * @param maxDamage The maximum damage of the item.
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_ -> this")
    @NotNull ItemBuilder maxDamage(int maxDamage);

    /**
     * Sets the color of the Dye.
     *
     * @param color The color to apply to the item.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_ -> this")
    @Deprecated(since = "1.6.2")
    default @NotNull ItemBuilder dyeColor(@NotNull DyeColor color) {
        return durability(color.getDyeData());
    }


    /**
     * Sets the color of the Dye.
     *
     * @param color The color to apply to the item.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_ -> this")
    @Deprecated(since = "1.6.2")
    default @NotNull ItemBuilder woolColor(@NotNull DyeColor color) {
        return durability(color.getWoolData());
    }

    /**
     * Adds an {@link ItemFlag} to the item.
     *
     * @param flag The item flag to add.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_ -> this")
    @NotNull ItemBuilder addFlags(@NotNull ItemFlag... flag);

    /**
     * Removes an {@link ItemFlag} from the item.
     *
     * @param flag The item flag to remove.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_ -> this")
    @NotNull ItemBuilder removeFlags(@NotNull ItemFlag... flag);

    /**
     * Removes all {@link ItemFlag}s from the item.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("-> this")
    default @NotNull ItemBuilder showAttributes() {
        return removeFlags(ItemFlag.values());
    }

    /**
     * Applies all {@link ItemFlag}s to the item;
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("-> this")
    @NotNull ItemBuilder hideAttributes();

    /**
     * Adds an {@link Enchantment} to the item.
     *
     * @param enchantment The enchantment to add.
     * @param level The level of the enchantment.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_, _ -> this")
    @NotNull ItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level);

    /**
     * Adds multiple enchantments to the item.
     *
     * @param enchantments A map of enchantments and their levels to add.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_ -> this")
    @NotNull ItemBuilder addEnchantments(@NotNull Map<Enchantment, Integer> enchantments);

    /**
     * Adds an unsafe {@link Enchantment} to the item.
     * <br> This allows adding enchantments with levels higher than the maximum level.
     *
     * @param enchantment The enchantment to add.
     * @param level The level of the enchantment.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_, _ -> this")
    @NotNull ItemBuilder addUnsafeEnchantment(@NotNull Enchantment enchantment, int level);

    /**
     * Adds multiple unsafe enchantments to the item.
     * <br> This allows adding enchantments with levels higher than the maximum level.
     *
     * @param enchantments A map of enchantments and their levels to add.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_ -> this")
    @NotNull ItemBuilder addUnsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments);

    /**
     * Removes an {@link Enchantment} from the item.
     *
     * @param enchantment The enchantment to remove.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_ -> this")
    @NotNull ItemBuilder removeEnchantment(@NotNull Enchantment enchantment);

    /**
     * Removes all enchantments from the item.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("-> this")
    @NotNull ItemBuilder removeEnchantments();



    @VersionRequirement(
            version = "1.11"
    )
    @Contract("_ -> this")
    @NotNull ItemBuilder setUnbreakable(boolean unbreakable);

    /**
     * Conditionally applies an action to the ItemBuilder if the predicate is true.
     *
     * @param predicate The condition to test.
     * @param action The action to apply if the condition is true.
     *
     * @return The current instance of the ItemBuilder for method chaining.
     */
    @Contract("_, _ -> this")
    default @NotNull ItemBuilder computeIf(@NotNull Predicate<ItemBuilder> predicate, @NotNull Consumer<ItemBuilder> action) {
        if (predicate.test(this)) {
            action.accept(this);
        }
        return this;
    }

    /**
     * Builds the item and returns the ItemStack.
     *
     * @return The built ItemStack.
     */
    @Contract("-> new")
    @NotNull ItemStack build();

    /**
     * Clones the ItemBuilder.
     *
     * @return A new instance of the ItemBuilder with the same properties.
     */
    @Contract("-> new")
    @NotNull ItemBuilder clone();


}
