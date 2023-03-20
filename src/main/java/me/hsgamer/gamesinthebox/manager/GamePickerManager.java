package me.hsgamer.gamesinthebox.manager;

import me.hsgamer.gamesinthebox.picker.GamePicker;
import me.hsgamer.gamesinthebox.picker.impl.ChanceGamePicker;
import me.hsgamer.gamesinthebox.picker.impl.RandomGamePicker;
import me.hsgamer.gamesinthebox.picker.impl.SequenceGamePicker;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.PlannerConfigFeature;
import me.hsgamer.hscore.builder.Builder;

import java.util.Optional;

public class GamePickerManager extends Builder<Planner, GamePicker> {
    public GamePickerManager() {
        register(RandomGamePicker::new, "random");
        register(SequenceGamePicker::new, "sequence");
        register(ChanceGamePicker::new, "chance");
    }

    public GamePicker build(Planner planner) {
        return Optional.ofNullable(planner.getFeature(PlannerConfigFeature.class).getString("picker-type", "")).flatMap(s -> this.build(s, planner)).orElse(GamePicker.EMPTY);
    }
}
