package it.xoxryze.skTabCompleter.api.managers;

import org.bukkit.entity.Player;

public interface PlaceholderManager {
    String getPlaceholderString(String placeholder, Player player);
}
