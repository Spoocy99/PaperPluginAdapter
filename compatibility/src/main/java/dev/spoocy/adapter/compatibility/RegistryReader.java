package dev.spoocy.adapter.compatibility;

import dev.spoocy.utils.common.collections.Collector;
import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.accessor.ClassAccess;
import dev.spoocy.utils.reflection.accessor.FieldAccessor;
import dev.spoocy.utils.reflection.builder.FieldBuilder;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.banner.PatternType;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemType;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * This class is used to provide values of registries
 * regardless of the server version since some enums
 * were moved to interfaces in later versions.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public class RegistryReader {

    /**
     * {@link Advancement}
     */
    public static Iterator<Advancement> advancements() {
        return Bukkit.advancementIterator();
    }

    public static Registry<Advancement> advancementRegistry() {
        return registry(Advancement.class);
    }

    /**
     * {@link Attribute}
     */
    public static Iterator<Attribute> attributes() {
        return typesIterator(Attribute.class);
    }

    public static Registry<Attribute> attributeRegistry() {
        return registry(Attribute.class);
    }

    /**
     * {@link PatternType}
     */
    public static Iterator<PatternType> patterns() {
        return typesIterator(PatternType.class);
    }

    public static Registry<PatternType> patternTypeRegistry() {
        return registry(PatternType.class);
    }

    /**
     * {@link Biome}
     */
    public static Iterator<Biome> biomes() {
        return typesIterator(Biome.class);
    }

    public static Registry<Biome> biomeRegistry() {
        return registry(Biome.class);
    }

    /**
     * {@link KeyedBossBar}
     */
    public static Iterator<KeyedBossBar> bossBars() {
        return typesIterator(KeyedBossBar.class);
    }

    public static Registry<KeyedBossBar> bossBarRegistry() {
        return registry(KeyedBossBar.class);
    }

    /**
     * {@link KeyedBossBar}
     */
    public static Iterator<Enchantment> enchantments() {
        return typesIterator(Enchantment.class);
    }

    public static Registry<Enchantment> enchantmentRegistry() {
        return registry(Enchantment.class);
    }

    /**
     * {@link EntityType}
     */
    public static Iterator<EntityType> entityTypes() {
        return typesIterator(EntityType.class);
    }

    public static Registry<EntityType> entityTypeRegistry() {
        return registry(EntityType.class);
    }

    /**
     * {@link ItemType}
     */
    public static Iterator<ItemType> itemTypes() {
        return typesIterator(ItemType.class);
    }

    public static Registry<ItemType> itemTypeRegistry() {
        return registry(ItemType.class);
    }

    /**
     * {@link LootTable}
     */
    public static Iterator<LootTables> lootTables() {
        return typesIterator(LootTables.class);
    }

    public static Registry<LootTables> lootTablesRegistry() {
        return registry(LootTables.class);
    }

    /**
     * {@link Material}
     */
    public static Iterator<Material> materials() {
        return typesIterator(Material.class);
    }

    public static Registry<Material> materialRegistry() {
        return registry(Material.class);
    }

    /**
     * {@link PotionEffect}
     */
    public static Iterator<PotionEffectType> potionEffectTypes() {
        return typesIterator(PotionEffectType.class);
    }

    public static Registry<PotionEffectType> potionEffectTypeRegistry() {
        return registry(PotionEffectType.class);
    }

    /**
     * {@link Statistic}
     */
    public static Iterator<Statistic> statistics() {
        return typesIterator(Statistic.class);
    }

    public static Registry<Statistic> statisticRegistry() {
        return registry(Statistic.class);
    }

    /**
     * {@link Structure}
     */
    public static Iterator<Structure> structures() {
        return typesIterator(Structure.class);
    }

    public static Registry<Structure> structureRegistry() {
        return registry(Structure.class);
    }

    /**
     * {@link StructureType}
     */
    public static Iterator<StructureType> structureTypes() {
        return typesIterator(StructureType.class);
    }

    public static Registry<StructureType> structureTypeRegistry() {
        return registry(StructureType.class);
    }

    /**
     * {@link Sound}
     */
    public static Iterator<Sound> sounds() {
        return typesIterator(Sound.class);
    }

    public static Registry<Sound> soundRegistry() {
        return registry(Sound.class);
    }

    @Nullable
    public static <T extends Keyed> T value(@NotNull Class<T> value, @NotNull String... keys) {
        for (String key : keys) {
            try {
                return registry(value).get(NamespacedKey.minecraft(key));
            } catch (Throwable ignored) { }
        }
        return null;
    }

    @Nullable
    public static <T extends Keyed> T value(@NotNull Class<T> value, @NotNull NamespacedKey... keys) {
        for (NamespacedKey key : keys) {
            try {
                return registry(value).get(key);
            } catch (Throwable ignored) { }
        }
        return null;
    }

    @NotNull
    public static <T extends Keyed> Registry<T> registry(@NotNull Class<T> value) {
        try {
            FieldAccessor field = getClassAccess().field(
                    FieldBuilder.create()
                            .type(Registry.class)
                            .requireGenericTypesExact(value)
                            .build()
            );

            return (Registry<T>) field.getDirectly(null);
        } catch (Throwable e) {
            throw new NullPointerException("Registry for keyed class " + value.getSimpleName() + " does not exist!");
        }
    }

    public static <T extends Keyed> Iterator<T> typesIterator(@NotNull Class<T> value) {
        if (value.isEnum()) {
            // iterating through enum constants
            return Collector.of(value.getEnumConstants()).iterator();
        }

        try {
            // Try to get iterator from registry
            return registry(value).iterator();
        } catch (NullPointerException ignored) { }

        // Fallback to using static fields in class
        return Collector.of(getClassAccess().fields(
                FieldBuilder.create()
                        .type(value)
                        .requireStatic()
                        .build()
                ))
                .map(field -> (T) field.getDirectly(null))
                .iterator();
    }

    private static ClassAccess CLASS_ACCESS;
    public static ClassAccess getClassAccess() {
        if(CLASS_ACCESS == null) {
            CLASS_ACCESS = Reflection.builder()
                    .forClass(Registry.class)
                    .publicMembers()
                    .buildAccess();
        }
        return CLASS_ACCESS;
    }

}
