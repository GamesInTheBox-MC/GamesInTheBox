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
package me.hsgamer.gamesinthebox.game.simple.feature;

import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.feature.RewardFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.hscore.common.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The simple {@link RewardFeature}.
 * It will get the settings from {@link GameConfigFeature}.
 * The settings are:
 * <ul>
 *     <li>{@code min-players-to-reward}: the minimum players to reward. Default: -1 (no limit)</li>
 *     <li>{@code reward.default}, {@code reward.all}, {@code reward.?}: the commands to execute when the player is not in the top list</li>
 *     <li>{@code reward.<number>}: the commands to execute when the player is in the top list</li>
 * </ul>
 */
public class SimpleRewardFeature extends RewardFeature {
    private final SimpleGameArena arena;
    private int minPlayersToReward = -1;

    /**
     * Create a new {@link SimpleRewardFeature}
     *
     * @param arena the arena
     */
    public SimpleRewardFeature(@NotNull SimpleGameArena arena) {
        this.arena = arena;
    }

    @Override
    protected @NotNull Pair<Map<Integer, List<String>>, List<String>> createTopAndDefaultCommands() {
        GameConfigFeature gameConfigFeature = arena.getFeature(GameConfigFeature.class);

        minPlayersToReward = Optional.ofNullable(gameConfigFeature.getString("min-players-to-reward"))
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .orElse(minPlayersToReward);

        Map<Integer, List<String>> parsedTopCommands = new HashMap<>();
        List<String> parsedDefaultCommands = new ArrayList<>();
        arena.getFeature(GameConfigFeature.class).getValues("reward", false).forEach((k, v) -> {
            if (k.equalsIgnoreCase("default") || k.equalsIgnoreCase("all") || k.equalsIgnoreCase("?")) {
                parsedDefaultCommands.addAll(CollectionUtils.createStringListFromObject(v, true));
            } else {
                int i;
                try {
                    i = Integer.parseInt(k);
                } catch (Exception e) {
                    return;
                }
                parsedTopCommands.put(i, CollectionUtils.createStringListFromObject(v, true));
            }
        });
        return Pair.of(parsedTopCommands, parsedDefaultCommands);
    }

    /**
     * Try to reward the players.
     * If the number of players is less than {@link #getMinPlayersToReward()}, it will return false.
     *
     * @param uuids the players
     * @return true if the players are rewarded
     */
    public boolean tryReward(@NotNull List<@NotNull UUID> uuids) {
        if (minPlayersToReward >= 0 && uuids.size() < minPlayersToReward) {
            return false;
        }
        reward(uuids);
        return true;
    }

    /**
     * Get the minimum players to reward
     *
     * @return the minimum players to reward
     */
    public int getMinPlayersToReward() {
        return minPlayersToReward;
    }
}
