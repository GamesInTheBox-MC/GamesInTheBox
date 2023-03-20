package me.hsgamer.gamesinthebox.picker.impl;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.PlannerConfigFeature;
import me.hsgamer.hscore.common.CollectionUtils;

import java.util.*;

public class SequenceGamePicker extends DelayedGamePicker {
    private final List<String> sequence;
    private final List<List<GameArena>> arenaListSequence = new ArrayList<>();
    private int index = 0;

    public SequenceGamePicker(Planner planner) {
        super(planner);
        this.sequence = Optional.ofNullable(planner.getFeature(PlannerConfigFeature.class))
                .map(feature -> feature.get("pick-sequence"))
                .map(CollectionUtils::createStringListFromObject)
                .orElseGet(Collections::emptyList);
    }

    @Override
    public void setup(Map<String, GameArena> arenaMap) {
        sequence.forEach(subSequence -> {
            String[] split = subSequence.split(",");
            List<GameArena> arenaList = new ArrayList<>();
            for (String arenaName : split) {
                GameArena gameArena = arenaMap.get(arenaName);
                if (gameArena != null) {
                    arenaList.add(gameArena);
                }
            }
            arenaListSequence.add(arenaList);
        });
    }

    @Override
    protected GameArena pickArena() {
        if (arenaListSequence.isEmpty()) {
            return null;
        }
        List<GameArena> arenaList = arenaListSequence.get(index);
        index = (index + 1) % arenaListSequence.size();
        return CollectionUtils.pickRandom(arenaList);
    }
}
