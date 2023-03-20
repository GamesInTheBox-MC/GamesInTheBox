package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.replacement.ReplacementHandler;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.OfflinePlayer;

import java.util.function.BiFunction;

public class ReplacementFeature implements Feature, ReplacementHandler {
    private final Planner planner;

    public ReplacementFeature(Planner planner) {
        this.planner = planner;
    }

    private String query(String query, BiFunction<ReplacementHandler, String, String> function) {
        String name = null;
        ReplacementHandler replacementHandler = null;

        String lowerCaseQuery = query.toLowerCase();
        if (lowerCaseQuery.startsWith("game_")) {
            name = query.substring(5);
            replacementHandler = planner.getFeature(GameFeature.class).getCurrentGameArena();
        } else if (lowerCaseQuery.startsWith("picker_")) {
            name = query.substring(7);
            replacementHandler = planner.getFeature(PickFeature.class).getGamePicker();
        } else if (lowerCaseQuery.startsWith("planner_")) {
            name = query.substring(8);
            replacementHandler = planner;
        }

        if (name == null) {
            name = "";
        }
        if (replacementHandler == null) {
            replacementHandler = ReplacementHandler.EMPTY;
        }

        return function.apply(replacementHandler, name);
    }

    @Override
    public String replace(String query) {
        return query(query, ReplacementHandler::replace);
    }

    @Override
    public String replace(OfflinePlayer player, String query) {
        return query(query, (replacementHandler, name) -> replacementHandler.replace(player, name));
    }
}
