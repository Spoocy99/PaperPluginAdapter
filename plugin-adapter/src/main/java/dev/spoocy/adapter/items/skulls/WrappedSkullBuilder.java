package dev.spoocy.adapter.items.skulls;

import dev.spoocy.adapter.compatibility.items.ItemBuilder;
import dev.spoocy.adapter.compatibility.items.SkullBuilder;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.accessor.FieldAccessor;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class WrappedSkullBuilder implements SkullBuilder {

    private final ItemBuilder itemBuilder;

    protected WrappedSkullBuilder(@NotNull ItemBuilder itemBuilder) {
        this.itemBuilder = itemBuilder;
    }

    public ItemBuilder baseBuilder() {
        return itemBuilder;
    }

    protected static String base64ToURL(@NotNull String base64) {
        String decoded = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);

        JSONObject object = new JSONObject(decoded);
        JSONObject textures = object.getJSONObject("textures");
        JSONObject skin = textures.getJSONObject("SKIN");

        return skin.getString("url");
    }

    protected static String urlToBase64(@NotNull String url) {
        URI actualUrl;
        try {
            actualUrl = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl + "\"}}}";
        return Base64.getEncoder().encodeToString(toEncode.getBytes());
    }

    private static FieldAccessor PROFILE_FIELD;
    protected static void rewriteProfileField(@NotNull SkullMeta meta, Object value) {
        if (PROFILE_FIELD == null) {
            PROFILE_FIELD = Reflection
                    .builder()
                    .forClass(meta.getClass())
                    .privateMembers()
                    .buildAccess()
                    .field(
                            Reflection.field()
                                    .name("profile")
                                    .build()
                    );
        }
        PROFILE_FIELD.set(meta, value);
    }

    @Override
    public SkullMeta getItemMeta() {
        return (SkullMeta) this.itemBuilder.getItemMeta();
    }

    @Override
    public @NotNull SkullBuilder owner(@NotNull String owner) {
        getItemMeta().setOwner(owner);
        return this;
    }

    @Override
    public @NotNull SkullBuilder owner(@NotNull Player player) {
        try {
            getItemMeta().setOwningPlayer(player);
        } catch (Exception e) {
            owner(player.getName());
        }
        return this;
    }

    @Override
    public @NotNull SkullBuilder textureUrl(@NotNull String url) {
        try {
            setTexture(url, Type.URL);
        } catch (Throwable e) {
            BukkitLogger.error("Failed to set skull texture from URL: " + url, e);
        }
        return this;
    }

    @Override
    public @NotNull SkullBuilder textureBase64(@NotNull String base64) {
        try {
            setTexture(base64, Type.BASE64);
        } catch (Throwable e) {
            BukkitLogger.error("Failed to set skull texture from Base64: " + base64, e);
        }
        return this;
    }

    protected abstract void setTexture(@NotNull String value, @NotNull Type type);

    @Override
    public Material getMaterial() {
        return this.itemBuilder.getMaterial();
    }

    @Override
    public Component getDisplayName() {
        return this.itemBuilder.getDisplayName();
    }

    @Override
    public List<Component> getLore() {
        return this.itemBuilder.getLore();
    }

    @Override
    public int getAmount() {
        return this.itemBuilder.getAmount();
    }

    @Override
    public int getDamage() {
        return this.itemBuilder.getDamage();
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        return this.itemBuilder.getItemFlags();
    }

    @Override
    public @NotNull SkullBuilder displayName(@NotNull Component name) {
        this.itemBuilder.displayName(name);
        return this;
    }

    @Override
    public @NotNull SkullBuilder lore(@NotNull Component... components) {
        this.itemBuilder.lore(components);
        return this;
    }

    @Override
    public @NotNull SkullBuilder lore(@NotNull List<Component> components) {
        this.itemBuilder.lore(components);
        return this;
    }

    @Override
    public @NotNull SkullBuilder addLore(@NotNull Component component) {
        this.itemBuilder.addLore(component);
        return this;
    }

    @Override
    public @NotNull SkullBuilder addEmptyLore() {
        this.itemBuilder.addEmptyLore();
        return this;
    }

    @Override
    public @NotNull SkullBuilder clearLore() {
        this.itemBuilder.clearLore();
        return this;
    }

    @Override
    public @NotNull ItemBuilder removeLore(int line) {
        this.itemBuilder.removeLore(line);
        return this;
    }

    @Override
    public @NotNull SkullBuilder amount(int amount) {
        this.itemBuilder.amount(amount);
        return this;
    }

    @Override
    public @NotNull SkullBuilder damage(int damage) {
        this.itemBuilder.damage(damage);
        return this;
    }

    @Override
    public @NotNull SkullBuilder maxDamage(int maxDamage) {
        this.itemBuilder.maxDamage(maxDamage);
        return this;
    }

    @Override
    public @NotNull SkullBuilder dyeColor(@NotNull DyeColor color) {
        this.itemBuilder.dyeColor(color);
        return this;
    }

    @Override
    public @NotNull SkullBuilder woolColor(@NotNull DyeColor color) {
        this.itemBuilder.woolColor(color);
        return this;
    }

    @Override
    public @NotNull SkullBuilder addFlags(@NotNull ItemFlag... flag) {
        this.itemBuilder.addFlags(flag);
        return this;
    }

    @Override
    public @NotNull SkullBuilder removeFlags(@NotNull ItemFlag... flag) {
        this.itemBuilder.removeFlags(flag);
        return this;
    }

    @Override
    public @NotNull SkullBuilder showAttributes() {
        this.itemBuilder.showAttributes();
        return this;
    }

    @Override
    public @NotNull SkullBuilder hideAttributes() {
        this.itemBuilder.hideAttributes();
        return this;
    }

    @Override
    public @NotNull SkullBuilder addEnchantment(@NotNull Enchantment enchantment, int level) {
        this.itemBuilder.addEnchantment(enchantment, level);
        return this;
    }

    @Override
    public @NotNull SkullBuilder addEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        this.itemBuilder.addEnchantments(enchantments);
        return this;
    }

    @Override
    public @NotNull SkullBuilder addUnsafeEnchantment(@NotNull Enchantment enchantment, int level) {
        this.itemBuilder.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    @Override
    public @NotNull SkullBuilder addUnsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        this.itemBuilder.addUnsafeEnchantments(enchantments);
        return this;
    }

    @Override
    public @NotNull SkullBuilder removeEnchantment(@NotNull Enchantment enchantment) {
        this.itemBuilder.removeEnchantment(enchantment);
        return this;
    }

    @Override
    public @NotNull SkullBuilder removeEnchantments() {
        this.itemBuilder.removeEnchantments();
        return this;
    }

    @Override
    public @NotNull SkullBuilder setUnbreakable(boolean unbreakable) {
        this.itemBuilder.setUnbreakable(unbreakable);
        return this;
    }

    @Override
    public @NotNull SkullBuilder computeIf(@NotNull Predicate<ItemBuilder> predicate, @NotNull Consumer<ItemBuilder> action) {
        this.itemBuilder.computeIf(predicate, action);
        return this;
    }

    @Override
    public @NotNull ItemStack build() {
        return this.itemBuilder.build();
    }

    @Override
    public @NotNull SkullBuilder clone() {
        throw new UnsupportedOperationException("No implementation.");
    }

    protected enum Type {
        URL,
        BASE64
    }

}
