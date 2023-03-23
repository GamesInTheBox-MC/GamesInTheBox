package me.hsgamer.gamesinthebox.command;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.Permissions;
import me.hsgamer.gamesinthebox.command.editor.*;
import me.hsgamer.hscore.bukkit.command.sub.SubCommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class EditorCommand extends Command {
    private final SubCommandManager subCommandManager;

    public EditorCommand(GamesInTheBox plugin) {
        super("gamesintheboxeditor", "The editor command of GamesInTheBox", "/gamesintheboxeditor", Collections.singletonList("gitbeditor"));
        setPermission(Permissions.EDITOR.getName());
        subCommandManager = new SubCommandManager();
        subCommandManager.registerSubcommand(new UsageCommand(plugin));
        subCommandManager.registerSubcommand(new ActionCommand(plugin));
        subCommandManager.registerSubcommand(new StatusCommand(plugin));
        subCommandManager.registerSubcommand(new ResetCommand(plugin));
        subCommandManager.registerSubcommand(new SaveCommand(plugin));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        return subCommandManager.onCommand(sender, commandLabel, args);
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return subCommandManager.onTabComplete(sender, alias, args);
    }
}
