package it.xoxryze.skTabCompleter.managers;

import it.xoxryze.skTabCompleter.SkTabCompleter;
import it.xoxryze.skTabCompleter.api.managers.PlaceholderManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class BasePlaceholderManager implements PlaceholderManager {

    private static boolean enabled = false;

    private final SkTabCompleter main;

    public BasePlaceholderManager(SkTabCompleter main) {
        this.main = main;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static String getPlaceholder(String placeholder, Player player) {
        if (!enabled || player == null) return placeholder;
        return PlaceholderAPI.setPlaceholders(player, placeholder);
    }

    @Override
    public String getPlaceholderString(String placeholder, Player player) {
        return getPlaceholder(placeholder, player);
    }

}
