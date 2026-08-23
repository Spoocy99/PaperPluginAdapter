package dev.spoocy.adapter.compatibility.items;

import dev.spoocy.adapter.compatibility.RegistryReader;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class BukkitCompatibility {

    @NotNull
    public static Enchantment unbreakingEnchantment() {
        try {
            return Enchantment.UNBREAKING;
        } catch (Throwable ignored) { }
        return RegistryReader.value(Enchantment.class, "unbreaking");
    }

    @NotNull
    public static PotionEffectType jumpBoostEffect() {
        try {
            return PotionEffectType.JUMP_BOOST;
        } catch (Throwable ignored) { }
        return RegistryReader.value(PotionEffectType.class, "jump_boost", "jump");
    }

    @NotNull
    public static PotionEffectType slownessEffect() {
        try {
            return PotionEffectType.SLOWNESS;
        } catch (Throwable ignored) { }
        return RegistryReader.value(PotionEffectType.class, "slowness", "slow");
    }

    @NotNull
    public static EntityType itemEntityType() {
        try {
            return EntityType.ITEM;
        } catch (Throwable ignored) { }
        return RegistryReader.value(EntityType.class, "item", "dropped_item");
    }

    @NotNull
    public static Particle emeraldParticle() {
        try {
            return Particle.HAPPY_VILLAGER;
        } catch (Throwable ignored) { }
        return RegistryReader.value(Particle.class, "happy_villager", "villager_happy");
    }


}
