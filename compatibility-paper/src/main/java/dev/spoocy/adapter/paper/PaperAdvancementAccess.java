package dev.spoocy.adapter.paper;

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

public class PaperAdvancementAccess implements AdvancementAccess {

    private final Advancement advancement;

    public PaperAdvancementAccess(@NotNull Advancement advancement) {
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
        return this.advancement.displayName();
    }

    @Override
    public String plainDisplayName() {
        return PaperCompatibilityProvider.PLAIN_TEXT_SERIALIZER.serialize(displayName());
    }

    @Override
    public boolean hasDisplay() {
        return this.advancement.getDisplay() != null;
    }

    @Override
    public Component title() {
        if(!hasDisplay()) throw new UnsupportedOperationException("Advancement has no display.");
        return this.advancement.getDisplay().title();
    }

    @Override
    public String plainTitle(){
        return PaperCompatibilityProvider.PLAIN_TEXT_SERIALIZER.serialize(title());
    }

    @Override
    public Component description() {
        if(!hasDisplay()) throw new UnsupportedOperationException("Advancement has no display.");
        return this.advancement.getDisplay().description();
    }

    @Override
    public String plainDescription() {
        return PaperCompatibilityProvider.PLAIN_TEXT_SERIALIZER.serialize(description());
    }

    @Override
    public ItemStack icon() {
        if(!hasDisplay()) throw new UnsupportedOperationException("Advancement has no display.");
        return this.advancement.getDisplay().icon();
    }

    @Override
    public boolean shouldShowToast() {
        if(!hasDisplay()) throw new UnsupportedOperationException("Advancement has no display.");
        return this.advancement.getDisplay().doesShowToast();
    }

    @Override
    public boolean shouldAnnounceChat() {
        if(!hasDisplay()) throw new UnsupportedOperationException("Advancement has no display.");
        return this.advancement.getDisplay().doesAnnounceToChat();
    }

    @Override
    public boolean isHidden() {
        if(!hasDisplay()) throw new UnsupportedOperationException("Advancement has no display.");
        return this.advancement.getDisplay().isHidden();
    }


}
