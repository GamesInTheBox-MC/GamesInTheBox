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

import com.cryptomorin.xseries.messages.ActionBar;
import me.hsgamer.hscore.bukkit.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The utility class for Action Bar
 */
public final class ActionBarUtil {
    private ActionBarUtil() {
        // EMPTY
    }

    /**
     * Send an action bar to a player
     *
     * @param player  the player
     * @param message the message
     */
    public static void sendActionBar(@NotNull Player player, @NotNull String message) {
        message = ColorUtils.colorize(message);
        ActionBar.sendActionBar(player, message);
    }

    /**
     * Send an action bar to a player
     *
     * @param uuid    the uuid of the player
     * @param message the message
     */
    public static void sendActionBar(@NotNull UUID uuid, @NotNull String message) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            sendActionBar(player, message);
        }
    }
}
