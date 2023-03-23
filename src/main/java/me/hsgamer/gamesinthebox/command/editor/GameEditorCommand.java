package me.hsgamer.gamesinthebox.command.editor;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.Permissions;
import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.gamesinthebox.game.GameEditor;
import me.hsgamer.hscore.bukkit.command.sub.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The base command for game editor
 */
public abstract class GameEditorCommand extends SubCommand {
    protected final GamesInTheBox plugin;

    protected GameEditorCommand(GamesInTheBox plugin, @NotNull String name, @NotNull String description, @NotNull String argsUsage, boolean consoleAllowed) {
        super(name, description, "/<label> " + name + " editor" + (argsUsage.isEmpty() ? "" : " " + argsUsage), Permissions.EDITOR.getName(), consoleAllowed);
        this.plugin = plugin;
    }

    protected GameEditorCommand(GamesInTheBox plugin, @NotNull String name, @NotNull String description, boolean consoleAllowed) {
        this(plugin, name, description, "", consoleAllowed);
    }

    protected abstract void onEditorSubCommand(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args);

    protected boolean isEditorProperUsage(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return true;
    }

    protected @NotNull List<String> onEditorTabComplete(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return Collections.emptyList();
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        Optional<GameEditor> optional = plugin.getGameManager().getGame(args[0]).map(Game::getEditor);
        if (optional.isPresent()) {
            onEditorSubCommand(optional.get(), sender, label, Arrays.copyOfRange(args, 1, args.length));
        } else {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getEditorGameNotFound());
        }
    }

    @Override
    public boolean isProperUsage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length < 1) {
            return false;
        }
        return plugin.getGameManager()
                .getGame(args[0])
                .map(Game::getEditor)
                .map(gameEditor -> isEditorProperUsage(gameEditor, sender, label, Arrays.copyOfRange(args, 1, args.length)))
                .orElse(true);
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            return new ArrayList<>(plugin.getGameManager().getRegisteredMap().keySet());
        }
        return plugin.getGameManager()
                .getGame(args[0])
                .map(Game::getEditor)
                .map(gameEditor -> onEditorTabComplete(gameEditor, sender, label, Arrays.copyOfRange(args, 1, args.length)))
                .orElse(Collections.emptyList());
    }
}
