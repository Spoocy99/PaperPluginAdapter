package dev.spoocy.adapter.gui.icon;

import dev.spoocy.adapter.compatibility.items.BukkitCompatibility;
import dev.spoocy.adapter.inventory.InventoryManager;
import dev.spoocy.adapter.message.LocalizedComponent;
import dev.spoocy.utils.common.collections.Collector;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Icon {

    Icon EMPTY = new Empty();

    @Contract("_ -> new")
    static @NotNull Icon of(@NotNull Material material) {
        return new Builder(material);
    }

    @Contract("_ -> new")
    static @NotNull Icon of(@NotNull ItemStack itemStack) {
        return new Builder(itemStack);
    }

    Icon name(@NotNull LocalizedComponent name);

    Icon lore(@NotNull LocalizedComponent... lines);

    Icon lore(@NotNull List<LocalizedComponent> lines);

    Icon addLore(@NotNull LocalizedComponent line);

    Icon amount(@Nonnegative int amount);

    Icon glowing();

    ItemStack decode(@NotNull Locale localization);

    class Empty implements Icon {

        private Empty() { }

        @Override
        public Icon name(@NotNull LocalizedComponent name) {
            return this;
        }

        @Override
        public Icon lore(@NotNull LocalizedComponent... lines) {
            return this;
        }

        @Override
        public Icon lore(@NotNull List<LocalizedComponent> lines) {
            return this;
        }

        @Override
        public Icon addLore(@NotNull LocalizedComponent line) {
            return this;
        }

        @Override
        public Icon amount(int amount) {
            return this;
        }

        @Override
        public Icon glowing() {
            return this;
        }

        @Override
        public ItemStack decode(@NotNull Locale localization) {
            return InventoryManager.INSTANCE
                    .getFactory()
                    .itemBuilder(Material.PAPER)
                    .displayName(Component.text("Empty Icon"))
                    .build()
                    ;
        }
    }

    class Builder implements Icon {

        private final ItemStack item;
        private LocalizedComponent name = LocalizedComponent.EMPTY;
        private List<LocalizedComponent> lore = new LinkedList<>();
        private int amount = 1;
        private boolean glowing = false;

        public Builder(@NotNull Material material) {
            this.item = new ItemStack(material);
        }

        public Builder(@NotNull ItemStack itemStack) {
            this.item = itemStack;
        }

        @Override
        public Icon name(@NotNull LocalizedComponent name) {
            this.name = name;
            return this;
        }

        @Override
        public Icon lore(@NotNull LocalizedComponent... lines) {
            this.lore = Collector.of(lines).asList();
            return this;
        }

        @Override
        public Icon lore(@NotNull List<LocalizedComponent> lines) {
            this.lore = lines;
            return this;
        }

        @Override
        public Icon addLore(@NotNull LocalizedComponent line) {
            this.lore.add(line);
            return this;
        }

        @Override
        public Icon amount(@Nonnegative int amount) {
            this.amount = amount;
            return this;
        }

        @Override
        public Icon glowing() {
            this.glowing = true;
            return this;
        }

        @Override
        public ItemStack decode(@NotNull Locale locale) {
            List<Component> cmplore = new LinkedList<>();

            for(LocalizedComponent line : this.lore) {
                cmplore.addAll(line.cmpLines(locale));
            }

            return InventoryManager.INSTANCE
                    .getFactory()
                    .itemBuilder(this.item)
                    .displayName(this.name.cmp(locale))
                    .clearLore()
                    .lore(cmplore)
                    .amount(this.amount)
                    .computeIf(i -> this.glowing, i -> i.addEnchantment(BukkitCompatibility.unbreakingEnchantment(), 1))
                    .hideAttributes()
                    .build();
        }
    }

}
