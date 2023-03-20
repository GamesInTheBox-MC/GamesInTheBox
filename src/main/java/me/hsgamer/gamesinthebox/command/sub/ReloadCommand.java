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
package me.hsgamer.gamesinthebox.command.sub;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.Permissions;
import me.hsgamer.gamesinthebox.expansion.extra.Reloadable;
import me.hsgamer.hscore.bukkit.command.sub.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends SubCommand {
    private final GamesInTheBox plugin;

    public ReloadCommand(GamesInTheBox plugin) {
        super("reload", "Reload the plugin", "/<label> reload", Permissions.RELOAD.getName(), true);
        this.plugin = plugin;
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        plugin.getMainConfig().reloadConfig();
        plugin.getMessageConfig().reloadConfig();
        plugin.getGameManager().callClear();
        plugin.getExpansionManager().call(Reloadable.class, Reloadable::onReload);
        plugin.getGameManager().callInit();
        plugin.getPlannerManager().reloadArena();
        MessageUtils.sendMessage(sender, plugin.getMessageConfig().getSuccess());
    }
}
