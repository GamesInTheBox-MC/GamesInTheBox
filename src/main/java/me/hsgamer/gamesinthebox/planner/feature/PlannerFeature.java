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
import me.hsgamer.gamesinthebox.planner.state.IdlingState;
import me.hsgamer.minigamecore.base.Feature;

/**
 * The {@link Feature} to access the {@link Planner}
 */
public class PlannerFeature implements Feature {
    private final Planner planner;

    /**
     * Create a new {@link PlannerFeature}
     *
     * @param planner the {@link Planner}
     */
    public PlannerFeature(Planner planner) {
        this.planner = planner;
    }

    /**
     * Get the {@link Planner}
     *
     * @return the {@link Planner}
     */
    public Planner getPlanner() {
        return planner;
    }

    /**
     * Notify the {@link Planner} that the game is finished
     */
    public void notifyFinished() {
        planner.getFeature(GameRunnerFeature.class).setFinished();
    }

    /**
     * Check if the {@link Planner} is idling
     *
     * @return true if the {@link Planner} is idling
     */
    public boolean isIdle() {
        return planner.getCurrentState() == IdlingState.class;
    }
}
