package dev.spoocy.adapter.message.sprites;

import dev.spoocy.utils.common.cache.Cache;
import dev.spoocy.utils.common.cache.Caches;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.object.SpriteObjectContents;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SpriteCatalog {

    protected final Cache<Material, SpriteObjectContents> cache = Caches.createLRUCache(120);
    protected final Map<Material, SpriteObjectContents> overwrites = new HashMap<>();
    protected final Set<ProvidingRule> rules = new HashSet<>();

    public SpriteCatalog(boolean registerDefaultRules) {

        if (registerDefaultRules) {
            registerDefaultRules();
        }
    }

    protected void registerDefaultRules() {
        this.overwrite(Material.COMPASS, Sprites.shieldPattern("compass_00"));
        this.overwrite(Material.CLOCK, Sprites.item("clock_00"));
        this.overwrite(Material.TIPPED_ARROW, Sprites.item("arrow"));

        this.overwrite(Material.TNT, Sprites.block("tnt_side"));
        this.overwrite(Material.SNIFFER_EGG, Sprites.block("sniffer_egg_not_cracked_north"));

        this.addRule(new Shield());
        this.addRule(new Banner());
        this.addRule(new HalfBlocks());
        this.addRule(new Bed());
        this.addRule(new Carpet());
        this.addRule(new StrippedLog());
    }

    protected void overwrite(@NotNull Material value, @NotNull SpriteObjectContents sprite) {
        this.overwrites.put(value, sprite);
    }

    private void addRule(@NotNull ProvidingRule rule) {
        this.rules.add(rule);
    }

    @NotNull
    public SpriteObjectContents get(@NotNull Material value) {
        return cache.computeIfAbsent(value, material -> {

            if (this.overwrites.containsKey(material)) {
                return this.overwrites.get(material);
            }

            for (ProvidingRule rule : this.rules) {
                if (rule.isApplicable(material)) {
                    return rule.getSprite(material);
                }
            }

            return getDefault(material);
        });
    }

    @NotNull
    private SpriteObjectContents getDefault(@NotNull Material value) {
        String spriteName = toSpriteName(value);

        if(value.isBlock() && value.isItem()) {
            return Sprites.block(spriteName);
        }

        if(value.isItem()) {
            return Sprites.item(spriteName);
        } else {
            return Sprites.block(spriteName);
        }
    }

    @KeyPattern.Value
    @NotNull
    private static String toSpriteName(@NotNull Material material) {
        return material.name().toLowerCase(Locale.ROOT);
    }

    public interface ProvidingRule {

        boolean isApplicable(@NotNull Material material);

        @NotNull
        SpriteObjectContents getSprite(@NotNull Material material);

    }

    protected static class HalfBlocks implements ProvidingRule {

        @Override
        public boolean isApplicable(@NotNull Material material) {
            return material.name().contains("BRICKS") || material.name().contains("STAIRS");
        }

        @Override
        public @NotNull SpriteObjectContents getSprite(@NotNull Material material) {
            String name = toSpriteName(material).substring(0, material.name().lastIndexOf("_") - 1);
            return Sprites.block(name);
        }
    }

    protected static class Shield implements ProvidingRule {

        @Override
        public boolean isApplicable(@NotNull Material material) {
            return material.name().contains("SHIELD");
        }

        @Override
        public @NotNull SpriteObjectContents getSprite(@NotNull Material material) {
            return Sprites.shieldPattern("base");
        }

    }

    protected static class Banner implements ProvidingRule {

        @Override
        public boolean isApplicable(@NotNull Material material) {
            return material.name().contains("BANNER");
        }

        @Override
        public @NotNull SpriteObjectContents getSprite(@NotNull Material material) {
            return Sprites.bannerPattern("base");
        }
    }

    protected static class Bed implements ProvidingRule {

        @Override
        public boolean isApplicable(@NotNull Material material) {
            return material.name().contains("BED");
        }

        @Override
        public @NotNull SpriteObjectContents getSprite(@NotNull Material material) {
            return Sprites.block(toSpriteName(material) + "_head_up");
        }
    }

    protected static class Carpet implements ProvidingRule {

        @Override
        public boolean isApplicable(@NotNull Material material) {
            return material.name().contains("CARPET");
        }

        @Override
        public @NotNull SpriteObjectContents getSprite(@NotNull Material material) {
            String wool = toSpriteName(material).replace("carpet", "wool");
            return Sprites.block(wool);
        }
    }

    protected static class StrippedLog implements ProvidingRule {

        @Override
        public boolean isApplicable(@NotNull Material material) {
            return material.name().startsWith("STRIPPED_");
        }

        @Override
        public @NotNull SpriteObjectContents getSprite(@NotNull Material material) {
            String block = toSpriteName(material).substring(9);
            return Sprites.block(block);
        }
    }

}
