package it.xoxryze.skTabCompleter.listeners;

import it.xoxryze.skTabCompleter.SkTabCompleter;
import it.xoxryze.skTabCompleter.api.utils.Permission;
import it.xoxryze.skTabCompleter.utils.UpdateChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    
    private final SkTabCompleter main;
    private final UpdateChecker updateChecker;
    private MiniMessage miniMessage;

    public PlayerJoinListener(SkTabCompleter main, UpdateChecker updateChecker) {
        this.main = main;
        this.updateChecker = updateChecker;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        if (!updateChecker.isUpdateAvailable()) return;
        if (!Permission.hasPermission(player, "notify.update")) return;

        player.sendMessage(Component.empty());
        player.sendMessage(miniMessage.deserialize("<aqua><bold>[SkTabCompleter] <gray>A new version is now available!"));
        player.sendMessage(miniMessage.deserialize("<gray>Current version: <red>" + main.getDescription().getVersion()));
        player.sendMessage(miniMessage.deserialize("<gray>New version: <green>" + updateChecker.getLatestVersion()));
        player.sendMessage(miniMessage.deserialize("<white><bold>Click here<white> to download!")
                .clickEvent(ClickEvent.openUrl("https://modrinth.com/plugin/sktabcompleter#download"))
                .hoverEvent(HoverEvent.showText(miniMessage.deserialize("<gray>Click here to the SkTabCompleter Modrinth page"))));
        player.sendMessage(Component.empty());
    }
}