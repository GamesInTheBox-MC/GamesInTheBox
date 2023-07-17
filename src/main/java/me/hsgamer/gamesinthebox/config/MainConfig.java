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
package me.hsgamer.gamesinthebox.config;

import me.hsgamer.hscore.config.annotation.Comment;
import me.hsgamer.hscore.config.annotation.ConfigPath;

import java.util.Collections;
import java.util.Map;

/**
 * The main config
 */
public interface MainConfig {
    /**
     * Get the interval of the planner in ticks
     *
     * @return the interval
     */
    @ConfigPath({"planner", "interval"})
    @Comment("The delay between Planner's tick")
    default long getPlannerInterval() {
        return 20L;
    }

    /**
     * Check if the planner should be run asynchronously
     *
     * @return true if it should
     */
    @ConfigPath({"planner", "async"})
    @Comment("Should the planner run asynchronously ?")
    default boolean isPlannerAsync() {
        return true;
    }

    /**
     * Get the settings for BlockUtil
     *
     * @return the map of the settings
     */
    @ConfigPath("block-util")
    @Comment("Settings for games that involve blocks")
    default Map<String, String> getBlockUtilSettings() {
        return Collections.emptyMap();
    }

    /**
     * Reload the config
     */
    void reloadConfig();
}
