package me.hsgamer.gamesinthebox.picker.impl;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.picker.GamePicker;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.PlannerConfigFeature;
import me.hsgamer.gamesinthebox.util.TimeUtil;

import java.util.Optional;

public abstract class DelayedGamePicker implements GamePicker {
    protected final Planner planner;
    protected final long delay;
    private boolean isPicked = false;
    private long nextPickTime;

    protected DelayedGamePicker(Planner planner) {
        this.planner = planner;
        this.delay = Optional.ofNullable(planner.getFeature(PlannerConfigFeature.class))
                .map(feature -> feature.getString("pick-delay", "0"))
                .map(TimeUtil::parseMillis)
                .orElse(0L);
        updateNextPickTime();
    }

    private void updateNextPickTime(long currentTime) {
        nextPickTime = currentTime + delay;
    }

    private void updateNextPickTime() {
        updateNextPickTime(System.currentTimeMillis());
    }

    protected abstract GameArena pickArena();

    @Override
    public final GameArena pick() {
        isPicked = true;
        return pickArena();
    }

    @Override
    public final boolean canPick() {
        long currentTime = System.currentTimeMillis();
        if (isPicked) {
            isPicked = false;
            updateNextPickTime(currentTime);
            return false;
        }
        return currentTime >= nextPickTime;
    }

    @Override
    public String replace(String input) {
        if (input.equalsIgnoreCase("time_left")) {
            return TimeUtil.formatStandardTime(Math.max(0, nextPickTime - System.currentTimeMillis()));
        }
        return null;
    }
}
