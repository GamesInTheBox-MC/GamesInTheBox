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
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * The command to perform an action on the editor
 */
public class ActionCommand extends GameEditorCommand {
    public ActionCommand(GamesInTheBox plugin) {
        super(plugin, "action", "Perform an action on the editor", "<action> [args]", true);
    }

    @Override
    protected void onEditorSubCommand(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (gameEditor.performAction(sender, args[0], Arrays.copyOfRange(args, 1, args.length))) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getSuccess());
        } else {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getEditorCannotPerformAction());
        }
    }

    @Override
    protected boolean isEditorProperUsage(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return args.length >= 1;
    }

    @Override
    protected @NotNull List<String> onEditorTabComplete(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            return gameEditor.getActions();
        } else if (args.length >= 2) {
            return gameEditor.getActionArgs(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
        } else {
            return super.onEditorTabComplete(gameEditor, sender, label, args);
        }
    }
}
