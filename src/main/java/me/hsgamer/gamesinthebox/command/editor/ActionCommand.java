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
