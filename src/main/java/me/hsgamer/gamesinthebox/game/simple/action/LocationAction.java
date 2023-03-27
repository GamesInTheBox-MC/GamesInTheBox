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
package me.hsgamer.gamesinthebox.game.simple.action;

import me.hsgamer.gamesinthebox.game.simple.SimpleGameAction;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link SimpleGameAction.SimpleAction} that uses the location of the player
 */
public abstract class LocationAction implements SimpleGameAction.SimpleAction {
    /**
     * Perform the action
     *
     * @param player   the player
     * @param location the location
     * @param args     the arguments
     * @return true if the action is successful
     */
    protected abstract boolean performAction(@NotNull Player player, @NotNull Location location, String... args);

    /**
     * Get the location
     *
     * @param player the player
     * @return the location
     */
    protected abstract Location getLocation(@NotNull Player player);

    /**
     * Send the "player only" message
     *
     * @param sender the sender
     */
    protected void sendPlayerOnlyMessage(@NotNull CommandSender sender) {
        MessageUtils.sendMessage(sender, "&cOnly players can use this command");
    }

    @Override
    public boolean performAction(@NotNull CommandSender sender, @NotNull String... args) {
        if (!(sender instanceof Player)) {
            sendPlayerOnlyMessage(sender);
            return false;
        }

        Player player = (Player) sender;
        Location location = getLocation(player);

        return performAction(player, location, args);
    }
}
