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

import com.google.common.reflect.TypeToken;
import me.hsgamer.gamesinthebox.command.EditorCommand;
import me.hsgamer.gamesinthebox.command.MainCommand;
import me.hsgamer.gamesinthebox.config.MainConfig;
import me.hsgamer.gamesinthebox.config.MessageConfig;
import me.hsgamer.gamesinthebox.config.converter.StringListConverter;
import me.hsgamer.gamesinthebox.config.converter.StringObjectMapConverter;
import me.hsgamer.gamesinthebox.config.converter.StringValueMapConverter;
import me.hsgamer.gamesinthebox.hook.PlaceholderHook;
import me.hsgamer.gamesinthebox.manager.GameManager;
import me.hsgamer.gamesinthebox.manager.GamePickerManager;
import me.hsgamer.gamesinthebox.manager.PlannerManager;
import me.hsgamer.gamesinthebox.manager.PluginExpansionManager;
import me.hsgamer.gamesinthebox.util.UpdateUtil;
import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.config.annotation.converter.manager.DefaultConverterManager;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import me.hsgamer.hscore.license.common.LicenseChecker;
import me.hsgamer.hscore.license.common.LicenseResult;
import me.hsgamer.hscore.license.polymart.PolymartLicenseChecker;
import me.hsgamer.hscore.license.spigotmc.SpigotLicenseChecker;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main class of the plugin
 */
public final class GamesInTheBox extends BasePlugin {
    static {
        //noinspection UnstableApiUsage
        DefaultConverterManager.registerConverter(new TypeToken<Map<String, Object>>() {
        }.getType(), new StringObjectMapConverter());
        //noinspection UnstableApiUsage
        DefaultConverterManager.registerConverter(new TypeToken<Map<String, String>>() {
        }.getType(), new StringValueMapConverter());
        //noinspection UnstableApiUsage
        DefaultConverterManager.registerConverter(new TypeToken<List<String>>() {
        }.getType(), new StringListConverter());
    }

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
        registerCommand(new EditorCommand(this));

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderHook(this).register();
        }

        Metrics metrics = new Metrics(this, 18581);
        metrics.addCustomChart(new AdvancedPie("expansion", () -> {
            Map<String, Integer> map = new HashMap<>();
            expansionManager.getEnabledExpansions().forEach((name, expansion) -> map.put(name, 1));
            return map;
        }));
    }

    @Override
    public void postEnable() {
        expansionManager.enableExpansions();
        plannerManager.postInit();

        UpdateUtil.notifyUpdate(this, "GamesInTheBox-MC/GamesInTheBox");

        LicenseChecker licenseChecker = PolymartLicenseChecker.isAvailable()
                ? new PolymartLicenseChecker("4462", true, true)
                : new SpigotLicenseChecker("111325");
        Scheduler.current().async().runTask(() -> {
            LicenseResult result = licenseChecker.checkLicense();
            switch (result.getStatus()) {
                case VALID:
                    getLogger().info("Thank you for supporting GamesInTheBox. Your support is greatly appreciated");
                    break;
                case INVALID:
                    getLogger().warning("Thank you for using GamesInTheBox");
                    getLogger().warning("If you like this plugin, please consider supporting it by purchasing from one of these platforms:");
                    getLogger().warning("- SpigotMC: https://www.spigotmc.org/resources/games-in-the-box.111325/");
                    getLogger().warning("- Polymart: https://polymart.org/resource/games-in-the-box.4462");
                    break;
                case OFFLINE:
                    getLogger().warning("Cannot check your license for GamesInTheBox. Please check your internet connection");
                    getLogger().warning("Note: You can still use this plugin without a license, and there is no limit on the features");
                    getLogger().warning("However, if you like this plugin, please consider supporting it by purchasing it from one of these platforms:");
                    getLogger().warning("- SpigotMC: https://www.spigotmc.org/resources/games-in-the-box.111325/");
                    getLogger().warning("- Polymart: https://polymart.org/resource/games-in-the-box.4462");
                    break;
                case UNKNOWN:
                    getLogger().warning("Cannot check your license for GamesInTheBox. Please try again later");
                    getLogger().warning("Note: You can still use this plugin without a license, and there is no limit on the features");
                    break;
            }
        });
    }

    @Override
    public void disable() {
        expansionManager.disableExpansions();
        plannerManager.clear();
        expansionManager.clearExpansions();
    }

    @Override
    protected List<Class<?>> getPermissionClasses() {
        return Collections.singletonList(Permissions.class);
    }

    /**
     * Get the main config
     *
     * @return the main config
     */
    @NotNull
    public MainConfig getMainConfig() {
        return mainConfig;
    }

    /**
     * Get the message config
     *
     * @return the message config
     */
    @NotNull
    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    /**
     * Get the game manager
     *
     * @return the game manager
     */
    @NotNull
    public GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Get the game picker manager
     *
     * @return the game picker manager
     */
    @NotNull
    public GamePickerManager getGamePickerManager() {
        return gamePickerManager;
    }

    /**
     * Get the planner manager
     *
     * @return the planner manager
     */
    @NotNull
    public PlannerManager getPlannerManager() {
        return plannerManager;
    }

    /**
     * Get the expansion manager
     *
     * @return the expansion manager
     */
    @NotNull
    public PluginExpansionManager getExpansionManager() {
        return expansionManager;
    }
}
