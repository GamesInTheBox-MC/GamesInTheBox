package me.hsgamer.gamesinthebox;

import me.hsgamer.gamesinthebox.command.MainCommand;
import me.hsgamer.gamesinthebox.config.MainConfig;
import me.hsgamer.gamesinthebox.config.MessageConfig;
import me.hsgamer.gamesinthebox.manager.GameManager;
import me.hsgamer.gamesinthebox.manager.PlannerManager;
import me.hsgamer.hscore.bukkit.addon.PluginAddonManager;
import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import me.hsgamer.hscore.expansion.common.ExpansionClassLoader;
import me.hsgamer.hscore.expansion.extra.manager.DependableExpansionSortAndFilter;

import java.util.Collections;
import java.util.List;

public final class GamesInTheBox extends BasePlugin {
    private final MainConfig mainConfig = ConfigGenerator.newInstance(MainConfig.class, new BukkitConfig(this, "config.yml"));
    private final MessageConfig messageConfig = ConfigGenerator.newInstance(MessageConfig.class, new BukkitConfig(this, "messages.yml"));
    private final GameManager gameManager = new GameManager();
    private final PlannerManager plannerManager = new PlannerManager(this);
    private final PluginAddonManager addonManager = new PluginAddonManager(this);

    @Override
    public void preLoad() {
        MessageUtils.setPrefix(messageConfig::getPrefix);
    }

    @Override
    public void load() {
        addonManager.setSortAndFilterFunction(new DependableExpansionSortAndFilter() {
            @Override
            public List<String> getDependencies(ExpansionClassLoader loader) {
                return CollectionUtils.createStringListFromObject(MapUtils.getIfFound(loader.getDescription().getData(), "depend", "depends", "dependencies"));
            }

            @Override
            public List<String> getSoftDependencies(ExpansionClassLoader loader) {
                return CollectionUtils.createStringListFromObject(MapUtils.getIfFound(loader.getDescription().getData(), "softdepend", "softdepends", "soft-dependencies"));
            }
        });
    }

    @Override
    public void enable() {
        plannerManager.init();
        addonManager.loadExpansions();

        registerCommand(new MainCommand(this));
    }

    @Override
    public void postEnable() {
        addonManager.enableExpansions();
        addonManager.callPostEnable();
        plannerManager.postInit();
    }

    @Override
    public void disable() {
        plannerManager.clear();
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

    public PlannerManager getPlannerManager() {
        return plannerManager;
    }

    public PluginAddonManager getAddonManager() {
        return addonManager;
    }
}
