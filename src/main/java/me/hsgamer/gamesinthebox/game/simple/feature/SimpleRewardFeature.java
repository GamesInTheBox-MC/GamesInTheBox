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

import java.util.*;

public class SimpleRewardFeature extends RewardFeature {
    private final SimpleGameArena arena;
    private int minPlayersToReward = -1;

    public SimpleRewardFeature(SimpleGameArena arena) {
        super(() -> {
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
        });
        this.arena = arena;
    }

    @Override
    public void postInit() {
        super.postInit();

        minPlayersToReward = Optional.ofNullable(arena.getFeature(GameConfigFeature.class).get("min-players-to-reward"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .orElse(minPlayersToReward);
    }

    public boolean tryReward(List<UUID> uuids) {
        if (minPlayersToReward >= 0 && uuids.size() < minPlayersToReward) {
            return false;
        }
        reward(uuids);
        return true;
    }

    public int getMinPlayersToReward() {
        return minPlayersToReward;
    }
}
