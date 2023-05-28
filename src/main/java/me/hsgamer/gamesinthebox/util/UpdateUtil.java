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

import me.hsgamer.hscore.checker.github.GithubReleaseChecker;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.expansion.common.ExpansionClassLoader;
import me.hsgamer.hscore.expansion.extra.expansion.GetClassLoader;
import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;
import me.hsgamer.hscore.logger.jul.JulLogger;
import me.hsgamer.hscore.logger.provider.LoggerProvider;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * The utility for updates of the plugin
 */
public final class UpdateUtil {
    private UpdateUtil() {
        // EMPTY
    }

    /**
     * Notify the update
     *
     * @param logger          the logger
     * @param versionFetcher  the version fetcher
     * @param versionSupplier the version supplier
     */
    public static void notifyUpdate(Logger logger, CompletableFuture<String> versionFetcher, Supplier<String> versionSupplier) {
        String version = versionSupplier.get();
        if (version.contains("SNAPSHOT")) {
            logger.log(LogLevel.WARN, "You are using the development version. Use at your own risk");
        } else {
            versionFetcher.whenComplete((output, throwable) -> {
                if (throwable != null) {
                    logger.log(LogLevel.WARN, "Cannot check for updates", throwable);
                } else if (!version.equalsIgnoreCase(output)) {
                    logger.log(LogLevel.WARN, "There is an available update. Download it at: https://github.com/GamesInTheBox-MC/GamesInTheBox/releases/tag/" + output);
                }
            });
        }
    }

    /**
     * Notify the update of a GitHub repository
     *
     * @param logger          the logger
     * @param repo            the GitHub repository
     * @param versionSupplier the version supplier
     */
    public static void notifyUpdate(Logger logger, String repo, Supplier<String> versionSupplier) {
        notifyUpdate(logger, new GithubReleaseChecker(repo).getVersion(), versionSupplier);
    }

    /**
     * Notify the update of a GitHub repository
     *
     * @param name            the name of the logger
     * @param repo            the GitHub repository
     * @param versionSupplier the version supplier
     * @see #notifyUpdate(Logger, String, Supplier)
     */
    public static void notifyUpdate(String name, String repo, Supplier<String> versionSupplier) {
        notifyUpdate(LoggerProvider.getLogger(name), repo, versionSupplier);
    }

    /**
     * Notify the update of the plugin
     *
     * @param plugin the plugin
     * @param repo   the GitHub repository
     * @see #notifyUpdate(String, String, Supplier)
     */
    public static void notifyUpdate(Plugin plugin, String repo) {
        notifyUpdate(new JulLogger(plugin.getLogger()), repo, plugin.getDescription()::getVersion);
    }

    /**
     * Notify the update of the expansion
     *
     * @param expansion the expansion
     * @param repo      the GitHub repository
     * @see #notifyUpdate(String, String, Supplier)
     */
    public static void notifyUpdate(Expansion expansion, String repo) {
        String name;
        Supplier<String> versionSupplier;
        if (expansion instanceof GetClassLoader) {
            ExpansionClassLoader classLoader = ((GetClassLoader) expansion).getExpansionClassLoader();
            name = classLoader.getDescription().getName();
            versionSupplier = classLoader.getDescription()::getVersion;
        } else {
            ClassLoader classLoader = expansion.getClass().getClassLoader();
            if (classLoader instanceof ExpansionClassLoader) {
                ExpansionClassLoader expansionClassLoader = (ExpansionClassLoader) classLoader;
                name = expansionClassLoader.getDescription().getName();
                versionSupplier = expansionClassLoader.getDescription()::getVersion;
            } else {
                LoggerProvider.getLogger(UpdateUtil.class).log(LogLevel.WARN, "Cannot get the class loader of " + expansion.getClass().getName());
                return;
            }
        }

        notifyUpdate(name, repo, versionSupplier);
    }
}
