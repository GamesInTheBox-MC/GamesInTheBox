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

import me.hsgamer.blockutil.abstraction.BlockHandler;
import me.hsgamer.blockutil.api.BlockUtil;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The utility to get {@link BlockHandler}
 */
public final class BlockHandlerUtil {
    private static final BlockHandler blockHandler;

    static {
        blockHandler = BlockUtil.getHandler(JavaPlugin.getProvidingPlugin(BlockHandlerUtil.class));
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
