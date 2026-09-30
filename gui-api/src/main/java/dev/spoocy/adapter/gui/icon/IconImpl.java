package dev.spoocy.adapter.gui.icon;

import dev.spoocy.adapter.inventory.InventoryManager;
import dev.spoocy.adapter.message.LocalizedComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class IconImpl implements Icon {

    private final Supplier<ItemStack> base;

    private LocalizedComponent title = LocalizedComponent.EMPTY;
    private Consumer<Title> setTitle;

    private List<LocalizedComponent> description = List.of();
    private Consumer<Description> setDescription;

    private Supplier<Integer> amount = () -> 1;

    private Supplier<Boolean> glowing = () -> false;

    public IconImpl(@NotNull Material material) {
        this(new ItemStack(material));
    }

    public IconImpl(@NotNull ItemStack itemStack) {
        this(() -> itemStack);
    }

    public IconImpl(@NotNull Supplier<ItemStack> base) {
        this.base = base;
    }

    @Override
    public @NotNull Icon title(@NotNull Consumer<Title> title) {
        this.title = null;
        this.setTitle = title;
        return this;
    }

    @Override
    public @NotNull Icon title(@NotNull LocalizedComponent title) {
        this.title = title;
        this.setTitle = null;
        return this;
    }

    @Override
    public @NotNull Icon description(@NotNull Consumer<Description> description) {
        this.description = null;
        this.setDescription = description;
        return this;
    }

    @Override
    public @NotNull Icon description(@NotNull List<LocalizedComponent> description) {
        this.description = description;
        this.setDescription = null;
        return this;
    }

    @Override
    public @NotNull Icon amount(final int amount) {
        this.amount = () -> amount;
        return this;
    }

    @Override
    public @NotNull Icon amount(@NotNull Supplier<Integer> amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public @NotNull Icon glowing(boolean glowing) {
        this.glowing = glowing ? () -> true : () -> false;
        return this;
    }

    @Override
    public @NotNull Icon glowingIf(@NotNull Supplier<Boolean> glowing) {
        this.glowing = glowing;
        return this;
    }

    @NotNull
    private Component getName(@NotNull Locale locale) {

        if (this.title != null) {

            return this.title.cmp(locale);

        } else {
            TitleImpl titleRef = new TitleImpl();
            this.setTitle.accept(titleRef);
            return titleRef.title.cmp(locale);
        }

    }

    @NotNull
    private List<Component> getLore(@NotNull Locale locale) {
        List<Component> list = new LinkedList<>();

        if (this.description != null) {

            for (LocalizedComponent loc : this.description) {
                list.addAll(loc.cmpLines(locale));
            }

        } else {
            DescriptionImpl descRef = new DescriptionImpl();
            this.setDescription.accept(descRef);

            for (LocalizedComponent loc : descRef.lines) {
                list.addAll(loc.cmpLines(locale));
            }
        }

        return list;
    }

    @Override
    public @NotNull ItemStack decode(@NotNull Locale locale) {
        return InventoryManager.INSTANCE
                .getFactory()
                .itemBuilder(this.base.get())
                .displayName(getName(locale))
                .lore(getLore(locale))
                .amount(this.amount.get())
                .glow(this.glowing.get())
                .hideAttributes()
                .build();
    }

    static class TitleImpl implements Title {

        private LocalizedComponent title = LocalizedComponent.EMPTY;

        @Override
        public void set(@NotNull LocalizedComponent title) {
            this.title = title;
        }

    }

    static class DescriptionImpl implements Description {

        private final List<LocalizedComponent> lines = new LinkedList<>();

        @Override
        public Description line(@NotNull LocalizedComponent line) {
            this.lines.add(line);
            return this;
        }
    }

}
