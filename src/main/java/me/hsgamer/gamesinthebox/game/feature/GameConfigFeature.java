package me.hsgamer.gamesinthebox.game.feature;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.planner.feature.PlannerConfigFeature;
import me.hsgamer.minigamecore.base.Feature;

import java.util.Collections;
import java.util.Map;

public class GameConfigFeature implements Feature {
    private final String gameConfigName;
    private final PlannerConfigFeature plannerConfigFeature;

    public GameConfigFeature(String gameConfigName, GameArena gameArena) {
        this.gameConfigName = gameConfigName;
        this.plannerConfigFeature = gameArena.getPlanner().getFeature(PlannerConfigFeature.class);
    }

    private String getSettingPath(String path) {
        return "settings." + gameConfigName + "." + path;
    }

    private String getCommonPath(String path) {
        return "common." + path;
    }

    public String getString(String path, String def) {
        return plannerConfigFeature.getString(getSettingPath(path), plannerConfigFeature.getString(getCommonPath(path), def));
    }

    public <T> T getInstance(String path, T def, Class<T> clazz) {
        return plannerConfigFeature.getInstance(getSettingPath(path), plannerConfigFeature.getInstance(getCommonPath(path), def, clazz), clazz);
    }

    public Object get(String path) {
        Object value = plannerConfigFeature.get(getSettingPath(path));
        if (value == null) {
            value = plannerConfigFeature.get(getCommonPath(path));
        }
        return value;
    }

    public Map<String, Object> getValues(String path, boolean deep) {
        if (containsSetting(path)) {
            return plannerConfigFeature.getValues(getSettingPath(path), deep);
        } else if (containsCommon(path)) {
            return plannerConfigFeature.getValues(getCommonPath(path), deep);
        } else {
            return Collections.emptyMap();
        }
    }

    public boolean containsSetting(String path) {
        return plannerConfigFeature.contains(getSettingPath(path));
    }

    public boolean containsCommon(String path) {
        return plannerConfigFeature.contains(getCommonPath(path));
    }

    public boolean containsPath(String path) {
        return containsSetting(path) || containsCommon(path);
    }

    public void setSetting(String path, Object value) {
        plannerConfigFeature.set(getSettingPath(path), value);
    }

    public void setCommon(String path, Object value) {
        plannerConfigFeature.set(getCommonPath(path), value);
    }
}
