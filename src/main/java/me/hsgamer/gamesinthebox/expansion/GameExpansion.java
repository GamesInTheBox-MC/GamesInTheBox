package me.hsgamer.gamesinthebox.expansion;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.manager.PluginExpansionManager;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.expansion.common.ExpansionManager;
import me.hsgamer.hscore.expansion.extra.expansion.DataFolder;
import me.hsgamer.hscore.expansion.extra.expansion.GetClassLoader;

public interface GameExpansion extends Expansion, DataFolder, GetClassLoader {
    default GamesInTheBox getPlugin() {
        ExpansionManager expansionManager = getExpansionClassLoader().getManager();
        if (expansionManager instanceof PluginExpansionManager) {
            return ((PluginExpansionManager) expansionManager).getPlugin();
        }
        throw new IllegalStateException("The expansion manager is not an instance of PluginExpansionManager");
    }
}
