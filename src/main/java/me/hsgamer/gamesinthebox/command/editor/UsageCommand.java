package me.hsgamer.gamesinthebox.command.editor;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.game.GameEditor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * The command to get the usage of the editor
 */
public class UsageCommand extends GameEditorCommand {
    public UsageCommand(GamesInTheBox plugin) {
        super(plugin, "usage", "Get the usage of the editor", true);
    }

    @Override
    protected void onEditorSubCommand(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        gameEditor.sendUsage(sender);
    }
}
