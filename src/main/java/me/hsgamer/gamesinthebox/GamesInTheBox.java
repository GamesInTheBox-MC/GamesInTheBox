package me.hsgamer.gamesinthebox;

import me.hsgamer.gamesinthebox.config.MainConfig;
import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;

public final class GamesInTheBox extends BasePlugin {
    private final MainConfig mainConfig = ConfigGenerator.newInstance(MainConfig.class, new BukkitConfig(this, "config.yml"));

    public MainConfig getMainConfig() {
        return mainConfig;
    }
}
