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
package me.hsgamer.gamesinthebox;

import me.hsgamer.gamesinthebox.command.MainCommand;
import me.hsgamer.gamesinthebox.config.MainConfig;
import me.hsgamer.gamesinthebox.config.MessageConfig;
import me.hsgamer.gamesinthebox.hook.PlaceholderHook;
import me.hsgamer.gamesinthebox.manager.GameManager;
import me.hsgamer.gamesinthebox.manager.GamePickerManager;
import me.hsgamer.gamesinthebox.manager.PlannerManager;
import me.hsgamer.gamesinthebox.manager.PluginExpansionManager;
import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import org.bukkit.Bukkit;

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

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PlaceholderHook placeholderHook = new PlaceholderHook(this);
            placeholderHook.register();
            addDisableFunction(placeholderHook::unregister);
        }
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
