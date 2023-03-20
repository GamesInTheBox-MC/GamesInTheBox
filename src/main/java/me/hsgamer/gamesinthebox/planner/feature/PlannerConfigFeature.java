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
import me.hsgamer.minigamecore.base.Feature;

import java.util.Map;
import java.util.Objects;

public class PlannerConfigFeature implements Feature {
    private final Planner planner;
    private final GlobalPlannerConfigFeature globalPlannerConfigFeature;

    public PlannerConfigFeature(Planner planner, GlobalPlannerConfigFeature globalPlannerConfigFeature) {
        this.planner = planner;
        this.globalPlannerConfigFeature = globalPlannerConfigFeature;
    }

    public <T> T getInstance(String path, T def, Class<T> clazz) {
        return globalPlannerConfigFeature.getPlannerConfig().getInstance(planner.getName() + "." + path, def, clazz);
    }

    public String getString(String path, String def) {
        return Objects.toString(globalPlannerConfigFeature.getPlannerConfig().getNormalized(planner.getName() + "." + path, def));
    }

    public Object get(String path) {
        return globalPlannerConfigFeature.getPlannerConfig().get(planner.getName() + "." + path);
    }

    public Map<String, Object> getValues(String path, boolean deep) {
        return globalPlannerConfigFeature.getPlannerConfig().getNormalizedValues(planner.getName() + "." + path, deep);
    }

    public boolean contains(String path) {
        return globalPlannerConfigFeature.getPlannerConfig().contains(planner.getName() + "." + path);
    }

    public void set(String path, Object value) {
        globalPlannerConfigFeature.getPlannerConfig().set(planner.getName() + "." + path, value);
        globalPlannerConfigFeature.getPlannerConfig().save();
    }
}
