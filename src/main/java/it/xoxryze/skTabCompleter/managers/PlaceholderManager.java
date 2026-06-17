package it.xoxryze.skTabCompleter.managers;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderManager {

    private static boolean enabled = false;

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

}
