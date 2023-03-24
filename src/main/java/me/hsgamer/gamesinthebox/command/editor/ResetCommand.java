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

/**
 * The command to reset the editor
 */
public class ResetCommand extends GameEditorCommand {
    public ResetCommand(GamesInTheBox plugin) {
        super(plugin, "reset", "Reset the editor", true);
    }

    @Override
    protected void onEditorSubCommand(String gameType, GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        gameEditor.reset(sender);
        MessageUtils.sendMessage(sender, plugin.getMessageConfig().getSuccess());
    }
}
