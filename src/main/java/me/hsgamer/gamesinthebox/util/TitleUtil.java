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

import com.cryptomorin.xseries.messages.Titles;
import me.hsgamer.hscore.bukkit.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The utility for titles
 */
public final class TitleUtil {
    private TitleUtil() {
        // EMPTY
    }

    /**
     * Send a title to a player
     *
     * @param player   the player
     * @param title    the title
     * @param subtitle the subtitle
     * @param fadeIn   the time to fade in
     * @param stay     the time to stay
     * @param fadeOut  the time to fade out
     */
    public static void sendTitle(@NotNull Player player, @NotNull String title, @NotNull String subtitle, int fadeIn, int stay, int fadeOut) {
        Titles.sendTitle(player, fadeIn, stay, fadeOut, ColorUtils.colorize(title), ColorUtils.colorize(subtitle));
    }

    /**
     * Send a title to a player
     *
     * @param player   the player
     * @param title    the title
     * @param subtitle the subtitle
     */
    public static void sendTitle(@NotNull Player player, @NotNull String title, @NotNull String subtitle) {
        Titles.sendTitle(player, ColorUtils.colorize(title), ColorUtils.colorize(subtitle));
    }

    /**
     * Send a title to a player
     *
     * @param uuid     the uuid of the player
     * @param title    the title
     * @param subtitle the subtitle
     * @param fadeIn   the time to fade in
     * @param stay     the time to stay
     * @param fadeOut  the time to fade out
     */
    public static void sendTitle(@NotNull UUID uuid, @NotNull String title, @NotNull String subtitle, int fadeIn, int stay, int fadeOut) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    /**
     * Send a title to a player
     *
     * @param uuid     the uuid of the player
     * @param title    the title
     * @param subtitle the subtitle
     */
    public static void sendTitle(@NotNull UUID uuid, @NotNull String title, @NotNull String subtitle) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            sendTitle(player, title, subtitle);
        }
    }
}
