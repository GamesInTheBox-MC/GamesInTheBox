package me.hsgamer.gamesinthebox.util;

import com.cryptomorin.xseries.messages.ActionBar;
import me.hsgamer.hscore.bukkit.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class ActionBarUtil {
    private ActionBarUtil() {
        // EMPTY
    }

    public static void sendActionBar(UUID uuid, String message) {
        message = ColorUtils.colorize(message);
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }
        ActionBar.sendActionBar(player, message);
    }
}
