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
import me.hsgamer.hscore.config.PathString;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The command to save the settings of the editor to the planner
 */
public class SaveCommand extends GameEditorCommand {
    public SaveCommand(GamesInTheBox plugin) {
        super(plugin, "save", "Save the settings to the planner", "<planner> <arena> [override]", true);
    }

    @Override
    protected void onEditorSubCommand(String gameType, GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        String plannerName = args[0];
        String arenaName = args[1];

        Config plannerConfig = plugin.getPlannerManager().getFeature(GlobalPlannerConfigFeature.class).getPlannerFeature(plannerName, true).getConfig();

        PathString path = new PathString("settings", arenaName);
        boolean override = args.length >= 3 && Boolean.parseBoolean(args[2]);
        if (plannerConfig.contains(path) && !override) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getEditorArenaAlreadyExists());
            return;
        }

        Optional<Map<PathString, Object>> optionalMap = gameEditor.exportPathValueMap(sender).map(map -> PathString.toPathStringMap(".", map));
        if (!optionalMap.isPresent()) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getEditorCannotSave());
            return;
        }

        plannerConfig.setIfAbsent(new PathString("picker-type"), "random");

        plannerConfig.set(path, null);
        plannerConfig.set(path.append(new PathString("type")), gameType);
        for (Map.Entry<PathString, Object> entry : optionalMap.get().entrySet()) {
            plannerConfig.set(path.append(entry.getKey()), entry.getValue());
        }
        plannerConfig.save();

        MessageUtils.sendMessage(sender, plugin.getMessageConfig().getSuccess());
    }

    @Override
    protected boolean isEditorProperUsage(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return args.length >= 2;
    }

    @Override
    protected @NotNull List<String> onEditorTabComplete(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            return plugin.getPlannerManager().getFeature(GlobalPlannerConfigFeature.class).getPlannerNames();
        } else if (args.length == 2) {
            return Optional.ofNullable(plugin.getPlannerManager().getFeature(GlobalPlannerConfigFeature.class))
                    .map(feature -> feature.getPlannerFeature(args[0], false))
                    .map(feature -> feature.getValues("settings", false).keySet())
                    .<List<String>>map(ArrayList::new)
                    .orElse(Collections.emptyList());
        } else if (args.length == 3) {
            return Arrays.asList("true", "false");
        }
        return super.onEditorTabComplete(gameEditor, sender, label, args);
    }
}
