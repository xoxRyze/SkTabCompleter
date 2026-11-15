package it.xoxryze.skTabCompleter.managers;

import it.xoxryze.skTabCompleter.SkTabCompleter;
import it.xoxryze.skTabCompleter.api.managers.PlaceholderManager;
import it.xoxryze.skTabCompleter.api.utils.SkriptVariableResolver;
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
        String resolved = SkriptVariableResolver.resolve(placeholder, player);
        if (!enabled || player == null) return resolved;
        return PlaceholderAPI.setPlaceholders(player, resolved);
    }

    @Override
    public String getPlaceholderString(String placeholder, Player player) {
        return getPlaceholder(placeholder, player);
    }

}
