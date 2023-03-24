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
import me.hsgamer.gamesinthebox.game.simple.feature.SimplePointFeature;
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

/**
 * The simple {@link GameArena}.
 * Provided features:
 * <ul>
 *     <li>{@link TimerFeature}</li>
 *     <li>{@link SimplePointFeature} ({@link PointFeature})</li>
 *     <li>{@link SimpleRewardFeature} ({@link me.hsgamer.gamesinthebox.game.feature.RewardFeature})</li>
 *     <li>{@link TopFeature}</li>
 *     <li>{@link DescriptiveHologramFeature}</li>
 *     <li>{@link SimpleUpdateFeature}</li>
 * </ul>
 * <p>
 * Can use features from {@link SimpleGame}
 * <p>
 * Provided replacement queries:
 * <ul>
 *     <li>{@code time_left}: The time left</li>
 *     <li>{@code min_players_to_reward}: The minimum players to reward</li>
 *     <li>{@code point_plus}: The point to add</li>
 *     <li>{@code point_minus}: The point to remove</li>
 *     <li>{@code point}: The point of the player</li>
 *     <li>{@code top_value_[index]}: The value part of the top at the index</li>
 *     <li>{@code top_name_[index]}: The name part of the top at the index</li>
 *     <li>{@code top}: The top position of the player</li>
 * </ul>
 */
public abstract class SimpleGameArena extends GameArena {
    private SimpleGameArenaAction gameAction;

    /**
     * Create a new game arena
     *
     * @param name    the name of the arena
     * @param game    the game that the arena belongs to
     * @param planner the planner that the arena belongs to
     */
    protected SimpleGameArena(String name, Game game, Planner planner) {
        super(name, game, planner);
    }

    /**
     * Called when the point of a player changed
     *
     * @param uuid        the uuid of the player
     * @param point       the applied point
     * @param totalPoints the total point
     */
    protected void onPointChanged(UUID uuid, int point, int totalPoints) {
        // EMPTY
    }

    /**
     * Get the default hologram lines by the name
     *
     * @param name the name to get
     * @return the default hologram lines
     */
    public List<String> getDefaultHologramLines(String name) {
        return Collections.emptyList();
    }

    /**
     * Create the {@link SimpleGameArenaAction}
     *
     * @return the created {@link SimpleGameArenaAction}
     */
    protected SimpleGameArenaAction createAction() {
        return new SimpleGameArenaAction(this);
    }

    @Override
    public final SimpleGameArenaAction getAction() {
        if (gameAction == null) {
            gameAction = createAction();
        }
        return gameAction;
    }

    @Override
    protected List<Feature> loadFeatures() {
        List<Feature> features = super.loadFeatures();
        features.add(new TimerFeature());
        features.add(new SimplePointFeature(this, this::onPointChanged));
        features.add(new SimpleRewardFeature(this));
        features.add(new TopFeature());
        features.add(new DescriptiveHologramFeature(this));
        features.add(new SimpleUpdateFeature(this));
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
        } else if (input.equalsIgnoreCase("min_players_to_reward")) {
            return Optional.ofNullable(getFeature(SimpleRewardFeature.class))
                    .map(SimpleRewardFeature::getMinPlayersToReward)
                    .filter(integer -> integer >= 0)
                    .map(Objects::toString)
                    .orElse("N/A");
        } else if (input.equalsIgnoreCase("point_plus")) {
            return Optional.ofNullable(getFeature(SimplePointFeature.class))
                    .map(SimplePointFeature::getPointPlus)
                    .map(Objects::toString)
                    .orElse("N/A");
        } else if (input.equalsIgnoreCase("point_minus")) {
            return Optional.ofNullable(getFeature(SimplePointFeature.class))
                    .map(SimplePointFeature::getPointMinus)
                    .map(Objects::toString)
                    .orElse("N/A");
        } else if (lowerCase.startsWith("top_")) {
            TopFeature topFeature = getFeature(TopFeature.class);
            if (topFeature == null) {
                return "N/A";
            }

            if (lowerCase.startsWith("top_value_")) {
                int index = Integer.parseInt(lowerCase.substring(10)) - 1;
                return topFeature.getTop(index).map(Pair::getValue).map(Objects::toString).orElse("---");
            } else if (lowerCase.startsWith("top_name_")) {
                int index = Integer.parseInt(lowerCase.substring(9)) - 1;
                return topFeature.getTop(index).map(Pair::getKey).map(Bukkit::getOfflinePlayer).map(OfflinePlayer::getName).orElse("---");
            }
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
