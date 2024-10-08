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
     * Get the blocks-per-tick for BlockUtil
     *
     * @return the blocks-per-tick
     */
    @ConfigPath({"block-util", "blocks-per-tick"})
    @Comment("The amount of blocks to process per tick")
    default int getBlockUtilBlocksPerTick() {
        return 50;
    }

    /**
     * Get the block-delay for BlockUtil
     *
     * @return the block-delay
     */
    @ConfigPath({"block-util", "block-delay"})
    @Comment("The delay between each block process")
    default long getBlockUtilBlockDelay() {
        return 0L;
    }

    /**
     * Get the max-blocks for BlockUtil
     *
     * @return the max-blocks
     */
    @ConfigPath({"block-util", "max-blocks"})
    @Comment("The maximum amount of blocks to process (-1 for unlimited)")
    default int getBlockUtilMaxBlocks() {
        return 1000;
    }

    /**
     * Check if BlockUtil should use FAWE if available
     *
     * @return true if it should
     */
    @ConfigPath({"block-util", "use-fawe"})
    @Comment("Should the plugin use FastAsyncWorldEdit if available ?")
    default boolean isBlockUtilUseFawe() {
        return true;
    }

    /**
     * Reload the config
     */
    void reloadConfig();
}
