package dev.spoocy.adapter.compatibility.items;

import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.utils.common.misc.Args;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractItemBuilder implements ItemBuilder {

    @NotNull
    protected ItemStack base;

    @NotNull
    protected ItemMeta meta;

    protected int amount;

    protected AbstractItemBuilder(@NotNull Material base) {
        this(
                new ItemStack(base),
                Bukkit.getItemFactory().getItemMeta(base),
                1
        );
    }

    protected AbstractItemBuilder(@NotNull ItemStack base) {
        this(
                base,
                base.getItemMeta(),
                base.getAmount()
        );
    }

    protected AbstractItemBuilder(@NotNull ItemStack item, @NotNull ItemMeta meta, int amount) {
        this.base = Args.notNull(item, "item");
        this.meta = Args.notNull(meta, "meta");
        this.amount = amount;
    }

    @Override
    public Material asMaterial() {
        return this.base.getType();
    }

    @Override
    public ItemMeta asItemMeta() {
        return this.meta;
    }

    @Override
    public ItemBuilder type(@NotNull Material type) {
        this.base.setType(type);
        this.meta = Args.notNull(Bukkit.getItemFactory().asMetaFor(this.meta, this.base), "meta");
        return this;
    }

    @Override
    public @NotNull ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public @NotNull ItemBuilder localizedName(@NotNull String name) {
        return compute(meta -> meta.setLocalizedName(name));
    }

    @Override
    public @NotNull ItemBuilder skullOwner(@NotNull String owner) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(owner);
        return skullOwner(player);
    }

    @Override
    public @NotNull ItemBuilder skullOwner(@NotNull OfflinePlayer owner) {
        return computeOn(SkullMeta.class, meta -> meta.setOwningPlayer(owner));
    }

    @Override
    public @NotNull ItemBuilder skullTexture(@NotNull String url, SkullApi.Type datatype) {
        SkullApi api = SkullApi.get();
        return computeOn(SkullMeta.class, meta -> api.applyTexture(meta, url, datatype));
    }

    @Override
    public @NotNull ItemBuilder noteBlockSound(@Nullable NamespacedKey noteBlockSound) {
        return computeOn(SkullMeta.class, meta -> meta.setNoteBlockSound(noteBlockSound));
    }

    @Override
    public @NotNull ItemStack build() {
        ItemStack item = this.base.clone();
        item.setAmount(this.amount);
        item.setItemMeta(this.meta);
        return item;
    }

    @Override
    public @NotNull ItemBuilder compute(@NotNull Consumer<ItemMeta> action) {
        try {
            action.accept(this.meta);
        } catch (NoSuchMethodError e) {
            BukkitLogger.warn("ItemMeta Method does not exist: {}", e.getMessage());
        }

        return this;
    }

    @Override
    public @NotNull <M extends ItemMeta> ItemBuilder computeOn(@NotNull Class<M> meta, @NotNull Consumer<M> action) {
        if (meta.isAssignableFrom(this.meta.getClass())) {

            try {
                action.accept((M) this.meta);
            } catch (NoSuchMethodError e) {
                BukkitLogger.warn("ItemMeta Method does not exist: {}", e.getMessage());
            }

        } else {
            BukkitLogger.warn("Tried to compute on incompatible item meta type: " + meta.getName());
        }

        return this;
    }
}
