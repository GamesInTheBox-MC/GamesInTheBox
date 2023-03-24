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
import me.hsgamer.gamesinthebox.game.GameEditor;
import me.hsgamer.gamesinthebox.planner.feature.GlobalPlannerConfigFeature;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.minigamecore.base.Arena;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The command to save the settings of the editor to the planner
 */
public class SaveCommand extends GameEditorCommand {
    public SaveCommand(GamesInTheBox plugin) {
        super(plugin, "save", "Save the settings to the planner", "<planner> <arena> [override]", true);
    }

    @Override
    protected void onEditorSubCommand(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        Config plannerConfig = plugin.getPlannerManager().getFeature(GlobalPlannerConfigFeature.class).getPlannerConfig();

        String plannerName = args[0];
        String arenaName = args[1];
        String path = plannerName + ".settings." + arenaName;
        boolean override = args.length >= 3 && Boolean.parseBoolean(args[2]);
        if (plannerConfig.contains(path) && !override) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getEditorArenaAlreadyExists());
            return;
        }

        Optional<Map<String, Object>> optionalMap = gameEditor.exportPathValueMap(sender);
        if (!optionalMap.isPresent()) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getEditorCannotSave());
            return;
        }

        plannerConfig.set(path, null);
        for (Map.Entry<String, Object> entry : optionalMap.get().entrySet()) {
            plannerConfig.set(path + "." + entry.getKey(), entry.getValue());
        }
        plannerConfig.save();

        gameEditor.reset(sender);

        MessageUtils.sendMessage(sender, plugin.getMessageConfig().getSuccess());
    }

    @Override
    protected boolean isEditorProperUsage(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return args.length >= 2;
    }

    @Override
    protected @NotNull List<String> onEditorTabComplete(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            return plugin.getPlannerManager().getAllArenas().stream().map(Arena::getName).collect(Collectors.toList());
        } else if (args.length == 3) {
            return Arrays.asList("true", "false");
        }
        return super.onEditorTabComplete(gameEditor, sender, label, args);
    }
}
