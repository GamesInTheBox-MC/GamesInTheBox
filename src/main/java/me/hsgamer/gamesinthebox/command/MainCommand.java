package me.hsgamer.gamesinthebox.command;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.command.sub.ActionCommand;
import me.hsgamer.gamesinthebox.command.sub.ReloadCommand;
import me.hsgamer.gamesinthebox.command.sub.SetGameCommand;
import me.hsgamer.gamesinthebox.command.sub.SkipIdleCommand;
import me.hsgamer.hscore.bukkit.command.sub.SubCommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MainCommand extends Command {
    private final SubCommandManager subCommandManager;

    public MainCommand(GamesInTheBox plugin) {
        super("gamesinthebox", "Main command for GamesInTheBox", "/gamesinthebox", Collections.singletonList("gitb"));
        subCommandManager = new SubCommandManager();
        subCommandManager.registerSubcommand(new ReloadCommand(plugin));
        subCommandManager.registerSubcommand(new SkipIdleCommand(plugin));
        subCommandManager.registerSubcommand(new SetGameCommand(plugin));
        subCommandManager.registerSubcommand(new ActionCommand(plugin));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return subCommandManager.onCommand(sender, commandLabel, args);
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return subCommandManager.onTabComplete(sender, alias, args);
    }
}
