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
package me.hsgamer.gamesinthebox.command.editor;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.game.GameEditor;
import me.hsgamer.gamesinthebox.planner.feature.GamePickerFeature;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.minigamecore.base.Arena;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MigrateCommand extends GameEditorCommand {
    public MigrateCommand(GamesInTheBox plugin) {
        super(plugin, "migrate", "Migrate the settings from a game arena", "<planner> <arena>", true);
    }

    @Override
    protected void onEditorSubCommand(String gameType, GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        Optional<Arena> plannerOptional = plugin.getPlannerManager().getArenaByName(args[0]);
        if (!plannerOptional.isPresent()) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getPlannerNotFound());
            return;
        }
        Arena planner = plannerOptional.get();
        GamePickerFeature pickFeature = planner.getFeature(GamePickerFeature.class);

        Optional<GameArena> gameArenaOptional = pickFeature.getGameArena(args[1]);
        if (!gameArenaOptional.isPresent()) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getEditorArenaNotFound());
            return;
        }
        GameArena gameArena = gameArenaOptional.get();

        if (gameEditor.migrate(sender, gameArena)) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getSuccess());
        } else {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getEditorCannotMigrate());
        }
    }

    @Override
    protected boolean isEditorProperUsage(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return args.length >= 2;
    }

    @Override
    protected @NotNull List<String> onEditorTabComplete(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            return plugin.getPlannerManager().getAllArenas().stream().map(Arena::getName).collect(Collectors.toList());
        } else if (args.length == 2) {
            return plugin.getPlannerManager().getArenaByName(args[0])
                    .map(arena -> arena.getFeature(GamePickerFeature.class))
                    .map(GamePickerFeature::getGameArenaNames)
                    .<List<String>>map(ArrayList::new)
                    .orElse(Collections.emptyList());
        }
        return super.onEditorTabComplete(gameEditor, sender, label, args);
    }
}
