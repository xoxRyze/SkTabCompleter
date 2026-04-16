package it.xoxryze.skTabCompleter.data;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

public class CacheManager {

    private static final List<String> MATERIALS = Arrays.stream(Material.values())
            .filter(m -> !m.isLegacy())
            .map(m -> m.name().toLowerCase())
            .toList();

    private static final List<String> ENCHANTMENTS = Registry.ENCHANTMENT.stream()
            .map(e -> e.getKey().getKey())
            .toList();

    private static final List<String> ENTITY_TYPES = Arrays.stream(EntityType.values())
            .filter(EntityType::isAlive)
            .map(e -> e.name().toLowerCase())
            .toList();

    private static final List<String> POTION_EFFECTS = Registry.EFFECT.stream()
            .map(e -> e.getKey().getKey())
            .toList();

    public static List<String> getMaterials() {
        return MATERIALS;
    }

    public static List<String> getEnchantments() {
        return ENCHANTMENTS;
    }

    public static List<String> getEntityTypes() {
        return ENTITY_TYPES;
    }

    public static List<String> getPotionEffects() {
        return POTION_EFFECTS;
    }
}
