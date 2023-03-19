package me.hsgamer.gamesinthebox.manager;

import me.hsgamer.gamesinthebox.picker.GamePicker;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.PlannerConfigFeature;
import me.hsgamer.hscore.builder.Builder;

import java.util.Optional;

public class GamePickerManager extends Builder<Planner, GamePicker> {
    public GamePicker build(Planner planner) {
        return Optional.ofNullable(planner.getFeature(PlannerConfigFeature.class).getString("picker-type", "")).flatMap(s -> this.build(s, planner)).orElse(GamePicker.EMPTY);
    }
}
