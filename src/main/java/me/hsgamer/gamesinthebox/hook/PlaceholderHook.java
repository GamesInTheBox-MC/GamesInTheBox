/*
   Copyright 2023-2023 Huynh Tien

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package me.hsgamer.gamesinthebox.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.planner.feature.ReplacementFeature;
import me.hsgamer.minigamecore.base.Arena;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;

/**
 * The hook for PlaceholderAPI
 */
public class PlaceholderHook extends PlaceholderExpansion {
    private final GamesInTheBox plugin;

    public PlaceholderHook(@NotNull GamesInTheBox plugin) {
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
    public boolean register() {
        boolean result = super.register();
        if (result) {
            plugin.addDisableFunction(this::unregister);
        }
        return result;
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

        return feature.tryReplace(query, player != null ? player.getUniqueId() : null);
    }
}
