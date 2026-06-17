package it.xoxryze.skTabCompleter.utils;

import it.xoxryze.skTabCompleter.SkTabCompleter;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UpdateChecker {
    private final SkTabCompleter main;
    private String latestVersion;
    private boolean updateAvailable = false;

    public UpdateChecker(SkTabCompleter main) {
        this.main = main;
    }

    public void check() {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try {
                String remoteVersion = fetchLatestVersion();
                String currentVersion = main.getDescription().getVersion();

                this.latestVersion = remoteVersion;
                this.updateAvailable = !currentVersion.equalsIgnoreCase(remoteVersion);

                Bukkit.getScheduler().runTask(main, () -> {
                    if (updateAvailable) {
                        main.getLogger().warning(" ");
                        main.getLogger().warning("A new version of SkTabCompleter is available!");
                        main.getLogger().warning("Current version: " + currentVersion);
                        main.getLogger().warning("Most recent version: " + remoteVersion);
                        main.getLogger().warning("Link to new version » https://www.spigotmc.org/resources/sktabcompleter.134824/");
                        main.getLogger().warning(" ");
                    }
                });

            } catch (IOException e) {
                Bukkit.getScheduler().runTask(main, () ->
                        main.getLogger().warning("Unable to check for spigot updates: " + e.getMessage()));
            }
        });
    }

    private String fetchLatestVersion() throws IOException {
        URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=134824");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.readLine().trim();
        } finally {
            connection.disconnect();
        }
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getLatestVersion() {
        return latestVersion;
    }
}