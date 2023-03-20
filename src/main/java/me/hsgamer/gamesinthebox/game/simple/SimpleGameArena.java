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
package me.hsgamer.gamesinthebox.game.simple;

import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.game.feature.PointFeature;
import me.hsgamer.gamesinthebox.game.feature.TopFeature;
import me.hsgamer.gamesinthebox.game.simple.feature.DescriptiveHologramFeature;
import me.hsgamer.gamesinthebox.game.simple.feature.SimpleRewardFeature;
import me.hsgamer.gamesinthebox.game.simple.feature.SimpleUpdateFeature;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.util.TimeUtil;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.implementation.feature.TimerFeature;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;

public abstract class SimpleGameArena extends GameArena {
    protected SimpleGameArena(String name, Game game, Planner planner) {
        super(name, game, planner);
    }

    protected void onPointChanged(UUID uuid, int point, int totalPoints) {
        // EMPTY
    }

    protected List<String> getDefaultHologramLines() {
        return Collections.emptyList();
    }

    @Override
    protected List<Feature> loadFeatures() {
        List<Feature> features = super.loadFeatures();
        features.add(new TimerFeature());
        features.add(new PointFeature(this::onPointChanged));
        features.add(new SimpleRewardFeature(this));
        features.add(new TopFeature());
        features.add(new DescriptiveHologramFeature(this, this::getDefaultHologramLines));
        features.add(new SimpleUpdateFeature(this));
        features.add(new SimpleRewardFeature(this));
        return features;
    }

    @Override
    public String replace(String input) {
        String lowerCase = input.toLowerCase();
        if (input.equalsIgnoreCase("time_left")) {
            return Optional.ofNullable(getFeature(TimerFeature.class))
                    .map(TimerFeature::getDuration)
                    .map(TimeUtil::formatStandardTime)
                    .orElse("N/A");
        } else if (lowerCase.startsWith("top_")) {
            TopFeature topFeature = getFeature(TopFeature.class);
            if (topFeature == null) {
                return "N/A";
            }

            if (lowerCase.startsWith("top_value_")) {
                int index = Integer.parseInt(lowerCase.substring(10)) - 1;
                return topFeature.getTop(index).map(Pair::getValue).map(Objects::toString).orElse("N/A");
            } else if (lowerCase.startsWith("top_name_")) {
                int index = Integer.parseInt(lowerCase.substring(9)) - 1;
                return topFeature.getTop(index).map(Pair::getKey).map(Bukkit::getOfflinePlayer).map(OfflinePlayer::getName).orElse("N/A");
            }
        } else if (input.equalsIgnoreCase("min_players_to_reward")) {
            return Optional.ofNullable(getFeature(SimpleRewardFeature.class))
                    .map(SimpleRewardFeature::getMinPlayersToReward)
                    .filter(integer -> integer >= 0)
                    .map(Objects::toString)
                    .orElse("N/A");
        }
        return null;
    }

    @Override
    public String replace(OfflinePlayer player, String input) {
        if (input.equalsIgnoreCase("point")) {
            return Optional.ofNullable(getFeature(PointFeature.class))
                    .map(pointFeature -> pointFeature.getPoint(player.getUniqueId()))
                    .map(Objects::toString)
                    .orElse("N/A");
        } else if (input.equalsIgnoreCase("top")) {
            return Optional.ofNullable(getFeature(TopFeature.class))
                    .map(pointFeature -> pointFeature.getTopIndex(player.getUniqueId()) + 1)
                    .map(Objects::toString)
                    .orElse("N/A");
        }
        return super.replace(player, input);
    }
}
