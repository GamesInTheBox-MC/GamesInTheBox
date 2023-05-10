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
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;
import me.hsgamer.minigamecore.base.Feature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

/**
 * The {@link Feature} to store the {@link Config} for all {@link Planner}
 */
public class GlobalPlannerConfigFeature implements Feature {
    private final File plannerFolder;
    private final Map<String, Config> configMap = new HashMap<>();

    /**
     * Create a new {@link GlobalPlannerConfigFeature}
     *
     * @param plannerFolder the folder to store the {@link Config}
     */
    public GlobalPlannerConfigFeature(@NotNull File plannerFolder) {
        this.plannerFolder = plannerFolder;
    }

    @Override
    public void init() {
        File[] files = plannerFolder.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            String name = file.getName();
            if (!file.isFile() || !(name.toLowerCase(Locale.ROOT).endsWith(".yml") || name.toLowerCase(Locale.ROOT).endsWith(".yaml")))
                continue;
            String plannerName = name.substring(0, name.lastIndexOf('.'));
            Config config = new BukkitConfig(file);
            config.setup();
            configMap.put(plannerName, config);
        }
    }

    @Override
    public void clear() {
        configMap.clear();
    }

    /**
     * Reload the {@link Config} for all {@link Planner}
     */
    public void reload() {
        clear();
        init();
    }

    /**
     * Get the folder to store the {@link Config}
     *
     * @return the folder
     */
    @NotNull
    public File getPlannerFolder() {
        return plannerFolder;
    }

    /**
     * Get all the {@link Planner} names
     *
     * @return the list of names
     */
    @NotNull
    public List<String> getPlannerNames() {
        return new ArrayList<>(configMap.keySet());
    }

    /**
     * Get the {@link PlannerConfigFeature} of the {@link Planner} by its name
     *
     * @param plannerName       the name of the {@link Planner}
     * @param createIfNotExists whether to create a new {@link Config} if the name doesn't exist
     * @return the {@link PlannerConfigFeature}
     */
    @Contract("_, false -> null")
    public PlannerConfigFeature getPlannerFeature(@NotNull String plannerName, boolean createIfNotExists) {
        Config config = configMap.get(plannerName);
        if (config == null) {
            if (createIfNotExists) {
                config = new BukkitConfig(new File(plannerFolder, plannerName + ".yml"));
                config.setup();
                config.set(new PathString("picker-type"), "random");
                configMap.put(plannerName, config);
            } else {
                return null;
            }
        }
        return new PlannerConfigFeature(config);
    }

    /**
     * Get the {@link PlannerConfigFeature} for the {@link Planner}
     *
     * @param planner the {@link Planner}
     * @return the {@link PlannerConfigFeature}
     */
    @NotNull
    public PlannerConfigFeature getPlannerFeature(@NotNull Planner planner) {
        return getPlannerFeature(planner.getName(), true);
    }
}
