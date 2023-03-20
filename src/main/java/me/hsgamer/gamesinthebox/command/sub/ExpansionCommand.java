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
import me.hsgamer.gamesinthebox.manager.PluginExpansionManager;
import me.hsgamer.hscore.bukkit.command.sub.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.hsgamer.hscore.bukkit.utils.MessageUtils.sendMessage;

public class ExpansionCommand extends SubCommand {
    private final GamesInTheBox plugin;

    public ExpansionCommand(GamesInTheBox plugin) {
        super("expansion", "Get the running expansions of the plugin", "/<label> expansion", Permissions.EXPANSION.getName(), true);
        this.plugin = plugin;
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        boolean shortMessage = args.length > 0 && args[0].equalsIgnoreCase("short");

        sendMessage(sender, "&b&lLoaded Expansions:");
        plugin.getExpansionManager().getClassLoaders().forEach((name, loader) -> {
            sendMessage(sender, "  &f- &a" + name);
            if (!shortMessage) {
                sendMessage(sender, "    &eVersion: &f" + loader.getDescription().getVersion());
                sendMessage(sender, "    &eAuthors: &f" + PluginExpansionManager.getAuthors(loader));
                sendMessage(sender, "    &eDescription: &f" + PluginExpansionManager.getDescription(loader));
                sendMessage(sender, "    &eState: &f" + loader.getState());
            }
        });
    }
}
