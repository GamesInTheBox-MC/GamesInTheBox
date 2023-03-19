package me.hsgamer.gamesinthebox;

import me.hsgamer.gamesinthebox.command.MainCommand;
import me.hsgamer.gamesinthebox.config.MainConfig;
import me.hsgamer.gamesinthebox.config.MessageConfig;
import me.hsgamer.gamesinthebox.manager.GameManager;
import me.hsgamer.gamesinthebox.manager.GamePickerManager;
import me.hsgamer.gamesinthebox.manager.PlannerManager;
import me.hsgamer.gamesinthebox.manager.PluginExpansionManager;
import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;

import java.util.Collections;
import java.util.List;

public final class GamesInTheBox extends BasePlugin {
    private final MainConfig mainConfig = ConfigGenerator.newInstance(MainConfig.class, new BukkitConfig(this, "config.yml"));
    private final MessageConfig messageConfig = ConfigGenerator.newInstance(MessageConfig.class, new BukkitConfig(this, "messages.yml"));
    private final GameManager gameManager = new GameManager();
    private final GamePickerManager gamePickerManager = new GamePickerManager();
    private final PlannerManager plannerManager = new PlannerManager(this);
    private final PluginExpansionManager expansionManager = new PluginExpansionManager(this);

    @Override
    public void load() {
        MessageUtils.setPrefix(messageConfig::getPrefix);
    }

    @Override
    public void enable() {
        plannerManager.init();
        expansionManager.loadExpansions();

        registerCommand(new MainCommand(this));
    }

    @Override
    public void postEnable() {
        expansionManager.enableExpansions();
        plannerManager.postInit();
    }

    @Override
    public void disable() {
        plannerManager.clear();
        expansionManager.disableExpansions();
    }

    @Override
    protected List<Class<?>> getPermissionClasses() {
        return Collections.singletonList(Permissions.class);
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

    public GamePickerManager getGamePickerManager() {
        return gamePickerManager;
    }

    public PlannerManager getPlannerManager() {
        return plannerManager;
    }

    public PluginExpansionManager getExpansionManager() {
        return expansionManager;
    }
}
