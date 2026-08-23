package dev.spoocy.adapter.spigot;

import dev.spoocy.adapter.compatibility.AdvancementAccess;
import net.kyori.adventure.text.Component;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementRequirements;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SpigotAdvancementAccess implements AdvancementAccess {

    private final Advancement advancement;

    public SpigotAdvancementAccess(@NotNull Advancement advancement) {
        this.advancement = advancement;
    }

    @Override
    public @NotNull Collection<String> getCriteria() {
        return this.advancement.getCriteria();
    }

    @Override
    public @NotNull AdvancementRequirements getRequirements() {
        return this.advancement.getRequirements();
    }

    @Override
    public Component displayName() {
        return Component.text(plainDisplayName());
    }

    @Override
    public String plainDisplayName() {
        try {
            return plainTitle();
        } catch (UnsupportedOperationException ignored) { }
        return advancement.getKey().toString();
    }

    @Override
    public boolean hasDisplay() {
        return this.advancement.getDisplay() != null;
    }

    @Override
    public Component title() {
        return Component.text(plainTitle());
    }

    @Override
    public String plainTitle(){
        if(!hasDisplay()) throw new UnsupportedOperationException("Advancement has no display.");
        return this.advancement.getDisplay().getTitle();
    }

    @Override
    public Component description() {
        return Component.text(plainDescription());
    }

    @Override
    public String plainDescription() {
        if(!hasDisplay()) throw new UnsupportedOperationException("Advancement has no display.");
        return this.advancement.getDisplay().getDescription();
    }

    @Override
    public ItemStack icon() {
        if(!hasDisplay()) throw new UnsupportedOperationException("Advancement has no display.");
        return this.advancement.getDisplay().getIcon();
    }

    @Override
    public boolean shouldShowToast(){
        if(!hasDisplay()) throw new UnsupportedOperationException("Advancement has no display.");
        return this.advancement.getDisplay().shouldShowToast();
    }

    @Override
    public boolean shouldAnnounceChat() {
        if(!hasDisplay()) throw new IllegalStateException("Advancement has no display.");
        return this.advancement.getDisplay().shouldAnnounceChat();
    }

    @Override
    public boolean isHidden() {
        if(!hasDisplay()) throw new IllegalStateException("Advancement has no display.");
        return this.advancement.getDisplay().isHidden();
    }
}
