package me.hsgamer.gamesinthebox.game.simple.feature;

import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.feature.RewardFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleRewardFeature extends RewardFeature {
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
    }
}
