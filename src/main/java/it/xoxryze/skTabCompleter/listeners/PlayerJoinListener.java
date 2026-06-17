package it.xoxryze.skTabCompleter.listeners;

import it.xoxryze.skTabCompleter.SkTabCompleter;
import it.xoxryze.skTabCompleter.utils.Permission;
import it.xoxryze.skTabCompleter.utils.UpdateChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final SkTabCompleter main;
    private final UpdateChecker updateChecker;

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
        player.sendMessage(Component.text("§b§l[SkTabCompleter] §7A new version is now available!"));
        player.sendMessage(Component.text("§7Current version: §c" + main.getDescription().getVersion()));
        player.sendMessage(Component.text("§7New version: §a" + updateChecker.getLatestVersion()));
        player.sendMessage(Component.text("§f§lClick here§f to download!")
                .clickEvent(ClickEvent.openUrl("https://www.spigotmc.org/resources/sktabcompleter.134824/"))
                .hoverEvent(HoverEvent.showText(Component.text("§7Click here to the SkTabCompleter SpigotMC page"))));
        player.sendMessage(Component.empty());
    }
}