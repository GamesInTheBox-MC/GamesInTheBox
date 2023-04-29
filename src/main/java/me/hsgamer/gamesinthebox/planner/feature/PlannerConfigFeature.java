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
package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;
import me.hsgamer.minigamecore.base.Feature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * The {@link Feature} to access the settings of the {@link Planner}
 */
public class PlannerConfigFeature implements Feature {
    private final Config config;

    /**
     * Create a new {@link PlannerConfigFeature}
     *
     * @param config the {@link Config} to access the settings
     */
    public PlannerConfigFeature(@NotNull Config config) {
        this.config = config;
    }

    /**
     * Get the {@link Config}
     *
     * @return the {@link Config}
     */
    @NotNull
    public Config getConfig() {
        return config;
    }

    /**
     * Get the value of the path
     *
     * @param path  the path
     * @param def   the default value
     * @param clazz the type class of the value
     * @param <T>   the type of the value
     * @return the value
     */
    @Contract("_, !null, _ -> !null")
    public <T> T getInstance(@NotNull String path, @Nullable T def, @NotNull Class<T> clazz) {
        return config.getInstance(PathString.toPathString(".", path), def, clazz);
    }

    /**
     * Get the value of the path as a {@link String}
     *
     * @param path the path
     * @param def  the default value
     * @return the value
     */
    @Contract("_, !null -> !null")
    public String getString(@NotNull String path, @Nullable String def) {
        return Objects.toString(config.getNormalized(PathString.toPathString(".", path)), def);
    }

    /**
     * Get the value of the path
     *
     * @param path the path
     * @return the value
     */
    @Nullable
    public Object get(@NotNull String path) {
        return config.getNormalized(PathString.toPathString(".", path));
    }

    /**
     * Get all values of the path
     *
     * @param path the path
     * @param deep whether to get the deep values in the sub-paths
     * @return the values
     */
    @NotNull
    public Map<String, Object> getValues(@NotNull String path, boolean deep) {
        return PathString.toPathMap(".", config.getNormalizedValues(PathString.toPathString(".", path), deep));
    }

    /**
     * Check if the path exists
     *
     * @param path the path
     * @return true if the path exists
     */
    public boolean contains(@NotNull String path) {
        return config.contains(PathString.toPathString(".", path));
    }

    /**
     * Set the value of the path
     *
     * @param path  the path
     * @param value the value
     */
    public void set(@NotNull String path, @Nullable Object value) {
        config.set(PathString.toPathString(".", path), value);
        config.save();
    }
}
