package it.xoxryze.skTabCompleter.api.data;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PlaceholdersData {

    private static final Set<String> MATERIALS = Arrays.stream(Material.values())
            .filter(m -> !m.isLegacy())
            .map(m -> m.name().toLowerCase())
            .collect(Collectors.toUnmodifiableSet());

    private static final Set<String> ENCHANTMENTS = Registry.ENCHANTMENT.stream()
            .map(e -> e.getKey().getKey())
            .collect(Collectors.toUnmodifiableSet());

    private static final Set<String> ENTITY_TYPES = Arrays.stream(EntityType.values())
            .filter(EntityType::isAlive)
            .map(e -> e.name().toLowerCase())
            .collect(Collectors.toUnmodifiableSet());

    private static final Set<String> POTION_EFFECTS = Registry.EFFECT.stream()
            .map(e -> e.getKey().getKey())
            .collect(Collectors.toUnmodifiableSet());

    private static final Set<String> SOUNDS = Registry.SOUNDS.stream()
            .map(s -> s.key().toString())
            .collect(Collectors.toUnmodifiableSet());

    public static Set<String> getMaterials() {
        return MATERIALS;
    }

    public static Set<String> getEnchantments() {
        return ENCHANTMENTS;
    }

    public static Set<String> getEntityTypes() {
        return ENTITY_TYPES;
    }

    public static Set<String> getPotionEffects() {
        return POTION_EFFECTS;
    }

    public static Set<String> getSounds() {
        return SOUNDS;
    }

}
