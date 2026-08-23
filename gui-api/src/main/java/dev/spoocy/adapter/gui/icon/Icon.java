package dev.spoocy.adapter.gui.icon;

import dev.spoocy.adapter.compatibility.items.BukkitCompatibility;
import dev.spoocy.adapter.items.Items;
import dev.spoocy.adapter.message.Message;
import dev.spoocy.adapter.messages.Localization;
import dev.spoocy.adapter.messages.PluginMessage;
import dev.spoocy.utils.common.collections.Collector;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnegative;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Icon {

    Icon EMPTY = new Empty();

    static Icon of(@NotNull Material material) {
        return new Builder(material);
    }

    static Icon of(@NotNull ItemStack itemStack) {
        return new Builder(itemStack);
    }

    Icon name(@NotNull PluginMessage name);

    Icon lore(@NotNull PluginMessage... lines);

    Icon lore(@NotNull List<PluginMessage> lines);

    Icon addLore(@NotNull PluginMessage line);

    Icon amount(@Nonnegative int amount);

    Icon glowing();

    ItemStack decode(@NotNull Localization localization);

    class Empty implements Icon {

        private Empty() { }

        @Override
        public Icon name(@NotNull PluginMessage name) {
            return this;
        }

        @Override
        public Icon lore(@NotNull PluginMessage... lines) {
            return this;
        }

        @Override
        public Icon lore(@NotNull List<PluginMessage> lines) {
            return this;
        }

        @Override
        public Icon addLore(@NotNull PluginMessage line) {
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
        public ItemStack decode(@NotNull Localization localization) {
            return Items.item(Material.PAPER)
                    .displayName(Component.text("Empty Icon"))
                    .build()
                    ;
        }
    }

    class Builder implements Icon {

        private final ItemStack item;
        private PluginMessage name = Message.EMPTY;
        private List<PluginMessage> lore = new LinkedList<>();
        private int amount = 1;
        private boolean glowing = false;

        public Builder(@NotNull Material material) {
            this.item = new ItemStack(material);
        }

        public Builder(@NotNull ItemStack itemStack) {
            this.item = itemStack;
        }

        @Override
        public Icon name(@NotNull PluginMessage name) {
            this.name = name;
            return this;
        }

        @Override
        public Icon lore(@NotNull PluginMessage... lines) {
            this.lore = Collector.of(lines).asList();
            return this;
        }

        @Override
        public Icon lore(@NotNull List<PluginMessage> lines) {
            this.lore = lines;
            return this;
        }

        @Override
        public Icon addLore(@NotNull PluginMessage line) {
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
        public ItemStack decode(@NotNull Localization localization) {
            List<Component> cmplore = new LinkedList<>();

            for(PluginMessage line : this.lore) {
                cmplore.addAll(line.cmpList(localization));
            }

            return Items.item(this.item)
                    .displayName(this.name.cmp(localization))
                    .clearLore()
                    .lore(cmplore)
                    .amount(this.amount)
                    .computeIf(i -> this.glowing, i -> i.addEnchantment(BukkitCompatibility.unbreakingEnchantment(), 1))
                    .hideAttributes()
                    .build();
        }
    }

}
