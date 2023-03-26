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
package me.hsgamer.gamesinthebox.game.feature;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.planner.feature.PlannerConfigFeature;
import me.hsgamer.minigamecore.base.Feature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

/**
 * The {@link Feature} to access the settings of the {@link GameArena}.
 * There are 2 types of settings:
 * <ul>
 *     <li>Common Path: The settings that are shared between all {@link GameArena}s. The format is "[planner].common.[path]"</li>
 *     <li>Setting Path: The settings that are specific to the {@link GameArena}. The path format is "[planner].settings.[arena].[path]"</li>
 * </ul>
 */
public class GameConfigFeature implements Feature {
    private final String gameConfigName;
    private final PlannerConfigFeature plannerConfigFeature;

    /**
     * Create a new {@link GameConfigFeature}
     *
     * @param gameArena the {@link GameArena}
     */
    public GameConfigFeature(@NotNull GameArena gameArena) {
        this.gameConfigName = gameArena.getLocalName();
        this.plannerConfigFeature = gameArena.getFeature(PlannerConfigFeature.class);
    }

    private String getSettingPath(String path) {
        return "settings." + gameConfigName + "." + path;
    }

    private String getCommonPath(String path) {
        return "common." + path;
    }

    /**
     * Get the string value from the settings
     *
     * @param path the path
     * @param def  the default value
     * @return the string value
     */
    @Contract("_, !null -> !null")
    public String getString(@NotNull String path, @Nullable String def) {
        return plannerConfigFeature.getString(getSettingPath(path), plannerConfigFeature.getString(getCommonPath(path), def));
    }

    /**
     * Get the string value from the settings
     *
     * @param path the path
     * @return the string value
     */
    @Nullable
    public String getString(@NotNull String path) {
        return getString(path, null);
    }

    /**
     * Get the value from the settings
     *
     * @param path  the path
     * @param def   the default value
     * @param clazz the type class of the value
     * @param <T>   the type of the value
     * @return the value
     */
    @Contract("_, !null, _ -> !null")
    public <T> T getInstance(@NotNull String path, @Nullable T def, @NotNull Class<T> clazz) {
        return plannerConfigFeature.getInstance(getSettingPath(path), plannerConfigFeature.getInstance(getCommonPath(path), def, clazz), clazz);
    }

    /**
     * Get the value from the settings
     *
     * @param path the path
     * @return the value
     */
    @Nullable
    public Object get(@NotNull String path) {
        Object value = plannerConfigFeature.get(getSettingPath(path));
        if (value == null) {
            value = plannerConfigFeature.get(getCommonPath(path));
        }
        return value;
    }

    /**
     * Get all values from the settings
     *
     * @param path the path
     * @param deep whether to get the deep values
     * @return the values
     */
    @NotNull
    public Map<String, Object> getValues(@NotNull String path, boolean deep) {
        if (containsSetting(path)) {
            return plannerConfigFeature.getValues(getSettingPath(path), deep);
        } else if (containsCommon(path)) {
            return plannerConfigFeature.getValues(getCommonPath(path), deep);
        } else {
            return Collections.emptyMap();
        }
    }

    /**
     * Check if the settings contains the path
     *
     * @param path the path
     * @return true if the settings contains the path
     */
    public boolean containsSetting(@NotNull String path) {
        return plannerConfigFeature.contains(getSettingPath(path));
    }

    /**
     * Check if the settings contains the common path
     *
     * @param path the path
     * @return true if the settings contains the common path
     */
    public boolean containsCommon(@NotNull String path) {
        return plannerConfigFeature.contains(getCommonPath(path));
    }

    /**
     * Check if the settings contains the path or the common path
     *
     * @param path the path
     * @return true if the settings contains the path or the common path
     */
    public boolean containsPath(@NotNull String path) {
        return containsSetting(path) || containsCommon(path);
    }

    /**
     * Set the value to the settings
     *
     * @param path  the path
     * @param value the value
     */
    public void setSetting(@NotNull String path, @Nullable Object value) {
        plannerConfigFeature.set(getSettingPath(path), value);
    }

    /**
     * Set the value to the common settings
     *
     * @param path  the path
     * @param value the value
     */
    public void setCommon(@NotNull String path, @Nullable Object value) {
        plannerConfigFeature.set(getCommonPath(path), value);
    }
}
