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
import me.hsgamer.gamesinthebox.planner.feature.GamePickerFeature;
import me.hsgamer.hscore.bukkit.command.sub.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.minigamecore.base.Arena;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The command to set the next game
 */
public final class SetGameCommand extends SubCommand {
    private final GamesInTheBox plugin;

    public SetGameCommand(GamesInTheBox plugin) {
        super("setgame", "Set the next arena in the planner", "/<label> setgame <planner> <arena>", Permissions.SET_GAME.getName(), true);
        this.plugin = plugin;
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        Optional<Arena> plannerOptional = plugin.getPlannerManager().getArenaByName(args[0]);
        if (!plannerOptional.isPresent()) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getPlannerNotFound());
            return;
        }
        Arena planner = plannerOptional.get();
        GamePickerFeature pickFeature = planner.getFeature(GamePickerFeature.class);
        if (pickFeature.setNextGame(args[1])) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getSuccess());
        } else {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getGameCannotSet());
        }
    }

    @Override
    public boolean isProperUsage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return args.length >= 2;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            return plugin.getPlannerManager().getAllArenas().stream().map(Arena::getName).collect(Collectors.toList());
        } else if (args.length == 2) {
            return plugin.getPlannerManager().getArenaByName(args[0])
                    .map(planner -> planner.getFeature(GamePickerFeature.class))
                    .map(GamePickerFeature::getGameArenaNames)
                    .<List<String>>map(ArrayList::new)
                    .orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }
}
