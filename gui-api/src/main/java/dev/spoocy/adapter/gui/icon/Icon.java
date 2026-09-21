package dev.spoocy.adapter.gui.icon;

import dev.spoocy.adapter.message.LocalizedComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Icon {

    Icon EMPTY = EmptyIcon.INSTANCE;

    @Contract("_ -> new")
    static @NotNull Icon of(@NotNull Material material) {
        return new IconImpl(material);
    }

    @Contract("_ -> new")
    static @NotNull Icon of(@NotNull ItemStack itemStack) {
        return new IconImpl(itemStack);
    }

    @Contract("_ -> new")
    static @NotNull Icon of(@NotNull Supplier<ItemStack> supplier) {
        return new IconImpl(supplier);
    }

    Icon title(@NotNull Consumer<Title> title);

    Icon title(@NotNull LocalizedComponent title);

    Icon description(@NotNull Consumer<Description> description);

    Icon description(@NotNull List<LocalizedComponent> description);

    default Icon description(@NotNull LocalizedComponent... descriptions) {
        return description(List.of(descriptions));
    }

    Icon amount(@Range(from = 1, to = 64) int amount);

    Icon glowing();

    ItemStack decode(@NotNull Locale localization);

    /**
     * Title for the display
     */
    interface Title {

        void set(@NotNull LocalizedComponent title);

        default void set(@NotNull Component title) {
            set(LocalizedComponent.of(title));
        }

    }

    /**
     * Description for the display
     */
    interface Description {

        Description line(@NotNull LocalizedComponent line);

        default Description line(@NotNull Component component) {
            return line(LocalizedComponent.of(component));
        }

        default Description empty() {
            return line(Component.empty());
        }

    }

}
