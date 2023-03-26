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

import me.hsgamer.blockutil.abstraction.BlockHandlerSettings;
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
    @ConfigPath("planner.interval")
    default long getPlannerInterval() {
        return 20L;
    }

    /**
     * Check if the planner should be run asynchronously
     *
     * @return true if it should
     */
    @ConfigPath("planner.async")
    default boolean isPlannerAsync() {
        return true;
    }

    /**
     * Should BlockUtil use FastAsyncWorldEdit if found?
     *
     * @return true if it should
     */
    @ConfigPath("block-util.use-fawe")
    default boolean isBlockUtilUseFawe() {
        return BlockHandlerSettings.USE_FAWE.get();
    }

    /**
     * Should BlockUtil use WorldEdit if found?
     *
     * @return true if it should
     */
    @ConfigPath("block-util.use-we")
    default boolean isBlockUtilUseWe() {
        return BlockHandlerSettings.USE_WE.get();
    }

    /**
     * Get the max blocks that BlockUtil can handle
     *
     * @return the max blocks
     */
    @ConfigPath("block-util.max-blocks")
    default int getBlockUtilMaxBlocks() {
        return BlockHandlerSettings.MAX_BLOCKS.get();
    }

    /**
     * Get the blocks per tick for BlockUtil
     *
     * @return the blocks per tick
     */
    @ConfigPath("block-util.blocks-per-tick")
    default int getBlockUtilBlocksPerTick() {
        return BlockHandlerSettings.BLOCKS_PER_TICK.get();
    }

    /**
     * Get the delay between each block placed by BlockUtil
     *
     * @return the delay
     */
    @ConfigPath("block-util.block-delay")
    default long getBlockUtilDelay() {
        return BlockHandlerSettings.BLOCK_DELAY.get();
    }

    /**
     * Update the BlockUtil settings
     */
    default void updateBlockUtilSettings() {
        BlockHandlerSettings.USE_FAWE.set(isBlockUtilUseFawe());
        BlockHandlerSettings.USE_WE.set(isBlockUtilUseWe());
        BlockHandlerSettings.MAX_BLOCKS.set(getBlockUtilMaxBlocks());
        BlockHandlerSettings.BLOCKS_PER_TICK.set(getBlockUtilBlocksPerTick());
        BlockHandlerSettings.BLOCK_DELAY.set(getBlockUtilDelay());
    }

    /**
     * Reload the config
     */
    void reloadConfig();
}
