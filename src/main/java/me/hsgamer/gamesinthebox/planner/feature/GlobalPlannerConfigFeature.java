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
import me.hsgamer.minigamecore.base.Feature;

public class GlobalPlannerConfigFeature implements Feature {
    private final Config plannerConfig;

    public GlobalPlannerConfigFeature(Config plannerConfig) {
        this.plannerConfig = plannerConfig;
    }

    public Config getPlannerConfig() {
        return plannerConfig;
    }

    public PlannerConfigFeature createPlannerFeature(Planner planner) {
        return new PlannerConfigFeature(planner, this);
    }
}
