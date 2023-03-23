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
    protected void onEditorSubCommand(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        gameEditor.reset(sender);
        MessageUtils.sendMessage(sender, plugin.getMessageConfig().getSuccess());
    }
}
