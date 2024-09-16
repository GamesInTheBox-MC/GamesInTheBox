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
package me.hsgamer.gamesinthebox.util;

import io.github.projectunified.blockutil.api.BlockHandler;
import io.github.projectunified.blockutil.fawe.FaweBlockHandler;
import io.github.projectunified.blockutil.folia.FoliaBlockHandler;
import io.github.projectunified.blockutil.vanilla.VanillaBlockHandler;
import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.config.MainConfig;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The utility to get {@link BlockHandler}
 */
public final class BlockHandlerUtil {
    private static final BlockHandler blockHandler;

    static {
        GamesInTheBox plugin = JavaPlugin.getPlugin(GamesInTheBox.class);
        MainConfig mainConfig = plugin.getMainConfig();

        if (mainConfig.isBlockUtilUseFawe() && FaweBlockHandler.isAvailable()) {
            blockHandler = new FaweBlockHandler()
                    .setMaxBlocks(mainConfig.getBlockUtilMaxBlocks());
        } else if (FoliaBlockHandler.isAvailable()) {
            blockHandler = new FoliaBlockHandler(plugin)
                    .setBlocksPerTick(mainConfig.getBlockUtilBlocksPerTick())
                    .setBlockDelay(mainConfig.getBlockUtilBlockDelay());
        } else {
            blockHandler = new VanillaBlockHandler(plugin)
                    .setBlocksPerTick(mainConfig.getBlockUtilBlocksPerTick())
                    .setBlockDelay(mainConfig.getBlockUtilBlockDelay());
        }
    }

    private BlockHandlerUtil() {
        // EMPTY
    }

    /**
     * Get the {@link BlockHandler}
     *
     * @return the {@link BlockHandler}
     */
    public static BlockHandler getBlockHandler() {
        return blockHandler;
    }
}
