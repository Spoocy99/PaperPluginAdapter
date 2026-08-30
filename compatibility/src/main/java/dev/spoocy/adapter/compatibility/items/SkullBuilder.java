package dev.spoocy.adapter.compatibility.items;

import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface SkullBuilder extends ItemBuilder {

    @Override
    SkullMeta getItemMeta();

    @NotNull SkullBuilder owner(@NotNull String owner);

    @NotNull SkullBuilder owner(@NotNull Player player);

    @NotNull SkullBuilder textureUrl(@NotNull String url);

     @NotNull SkullBuilder textureBase64(@NotNull String base64);

    @Override
    @NotNull SkullBuilder displayName(@NotNull Component name);

    @Override
    @NotNull SkullBuilder lore(@NotNull Component... components);

    @Override
    @NotNull SkullBuilder lore(@NotNull List<Component> components);

    @Override
    @NotNull SkullBuilder addLore(@NotNull Component component);

    @Override
    @NotNull SkullBuilder addEmptyLore();

    @Override
    @NotNull SkullBuilder clearLore();

    @Override
    @NotNull SkullBuilder amount(int amount);

    @Override
    @NotNull SkullBuilder damage(int damage);

    @Override
    @NotNull SkullBuilder maxDamage(int maxDamage);

    @Override
    @NotNull SkullBuilder dyeColor(@NotNull DyeColor color);

    @Override
    @NotNull SkullBuilder woolColor(@NotNull DyeColor color);

    @Override
    @NotNull SkullBuilder addFlags(@NotNull ItemFlag... flag);

    @Override
    @NotNull SkullBuilder removeFlags(@NotNull ItemFlag... flag);

    @Override
    @NotNull SkullBuilder showAttributes();

    @Override
    @NotNull SkullBuilder hideAttributes();

    @Override
    @NotNull SkullBuilder addEnchantment(@NotNull Enchantment enchantment, int level);

    @Override
    @NotNull SkullBuilder addEnchantments(@NotNull Map<Enchantment, Integer> enchantments);

    @Override
    @NotNull SkullBuilder addUnsafeEnchantment(@NotNull Enchantment enchantment, int level);

    @Override
    @NotNull SkullBuilder addUnsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments);

    @Override
    @NotNull SkullBuilder removeEnchantment(@NotNull Enchantment enchantment);

    @Override
    @NotNull SkullBuilder removeEnchantments();

    @Override
    @NotNull SkullBuilder setUnbreakable(boolean unbreakable);

    @Override
    @NotNull SkullBuilder computeIf(@NotNull Predicate<ItemBuilder> predicate, @NotNull Consumer<ItemBuilder> action);

    @Override
    @NotNull ItemStack build();

    @Override
    @NotNull SkullBuilder clone();
}
