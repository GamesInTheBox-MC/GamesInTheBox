package me.hsgamer.gamesinthebox.command.editor;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.game.GameEditor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * The command to get the status of the editor
 */
public class StatusCommand extends GameEditorCommand {
    public StatusCommand(GamesInTheBox plugin) {
        super(plugin, "status", "Get the status of the editor", true);
    }

    @Override
    protected void onEditorSubCommand(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        gameEditor.sendStatus(sender);
    }
}
