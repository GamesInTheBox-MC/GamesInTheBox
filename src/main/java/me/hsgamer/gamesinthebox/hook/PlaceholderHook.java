package me.hsgamer.gamesinthebox.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.planner.feature.ReplacementFeature;
import me.hsgamer.minigamecore.base.Arena;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;

public class PlaceholderHook extends PlaceholderExpansion {
    private final GamesInTheBox plugin;

    public PlaceholderHook(GamesInTheBox plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getName().toLowerCase(Locale.ROOT);
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        String[] split = params.split(":", 2);
        if (split.length == 0) {
            return null;
        }
        Optional<Arena> optionalPlanner = plugin.getPlannerManager().getArenaByName(split[0]);
        if (!optionalPlanner.isPresent()) {
            return null;
        }
        Arena planner = optionalPlanner.get();

        if (split.length == 1) {
            return planner.getName();
        }

        String query = split[1];
        ReplacementFeature feature = planner.getFeature(ReplacementFeature.class);
        if (feature == null) {
            return null;
        }

        return player == null ? feature.replace(query) : feature.replace(player, query);
    }
}
