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
package me.hsgamer.gamesinthebox.expansion;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.manager.PluginExpansionManager;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.expansion.common.ExpansionManager;
import me.hsgamer.hscore.expansion.extra.expansion.DataFolder;
import me.hsgamer.hscore.expansion.extra.expansion.GetClassLoader;

/**
 * The {@link Expansion} for {@link GamesInTheBox}
 */
public interface GameExpansion extends Expansion, DataFolder, GetClassLoader {
    /**
     * Get the plugin
     *
     * @return the plugin
     */
    default GamesInTheBox getPlugin() {
        ExpansionManager expansionManager = getExpansionClassLoader().getManager();
        if (expansionManager instanceof PluginExpansionManager) {
            return ((PluginExpansionManager) expansionManager).getPlugin();
        }
        throw new IllegalStateException("The expansion manager is not an instance of PluginExpansionManager");
    }
}
