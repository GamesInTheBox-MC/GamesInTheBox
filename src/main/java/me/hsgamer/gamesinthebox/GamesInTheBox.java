package me.hsgamer.gamesinthebox;

import me.hsgamer.gamesinthebox.config.MainConfig;
import me.hsgamer.gamesinthebox.config.MessageConfig;
import me.hsgamer.gamesinthebox.manager.GameManager;
import me.hsgamer.gamesinthebox.manager.PlannerManager;
import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;

public final class GamesInTheBox extends BasePlugin {
    private final MainConfig mainConfig = ConfigGenerator.newInstance(MainConfig.class, new BukkitConfig(this, "config.yml"));
    private final MessageConfig messageConfig = ConfigGenerator.newInstance(MessageConfig.class, new BukkitConfig(this, "messages.yml"));
    private final GameManager gameManager = new GameManager();
    private final PlannerManager plannerManager = new PlannerManager(this);

    @Override
    public void enable() {
        plannerManager.init();
    }

    @Override
    public void postEnable() {
        plannerManager.postInit();
    }

    @Override
    public void disable() {
        plannerManager.clear();
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public PlannerManager getPlannerManager() {
        return plannerManager;
    }
}
