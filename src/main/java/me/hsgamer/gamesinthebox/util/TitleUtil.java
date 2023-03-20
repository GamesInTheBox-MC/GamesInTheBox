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

import java.util.UUID;

public class TitleUtil {
    private TitleUtil() {
        // EMPTY
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Titles.sendTitle(player, fadeIn, stay, fadeOut, ColorUtils.colorize(title), ColorUtils.colorize(subtitle));
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        Titles.sendTitle(player, ColorUtils.colorize(title), ColorUtils.colorize(subtitle));
    }

    public static void sendTitle(UUID uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    public static void sendTitle(UUID uuid, String title, String subtitle) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            sendTitle(player, title, subtitle);
        }
    }
}