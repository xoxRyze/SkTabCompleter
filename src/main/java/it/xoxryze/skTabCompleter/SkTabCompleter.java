package it.xoxryze.skTabCompleter;

import ch.njol.skript.Skript;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkTabCompleter extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("SkTabCompleter has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SkTabCompleter has been disabled.");
    }
}
