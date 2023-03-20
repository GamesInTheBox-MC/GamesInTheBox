package me.hsgamer.gamesinthebox.command.sub;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.Permissions;
import me.hsgamer.gamesinthebox.manager.PluginExpansionManager;
import me.hsgamer.hscore.bukkit.command.sub.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.hsgamer.hscore.bukkit.utils.MessageUtils.sendMessage;

public class ExpansionCommand extends SubCommand {
    private final GamesInTheBox plugin;

    public ExpansionCommand(GamesInTheBox plugin) {
        super("expansion", "Get the running expansions of the plugin", "/<label> expansion", Permissions.EXPANSION.getName(), true);
        this.plugin = plugin;
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        boolean shortMessage = args.length > 0 && args[0].equalsIgnoreCase("short");

        sendMessage(sender, "&b&lLoaded Expansions:");
        plugin.getExpansionManager().getClassLoaders().forEach((name, loader) -> {
            sendMessage(sender, "  &f- &a" + name);
            if (!shortMessage) {
                sendMessage(sender, "    &eVersion: &f" + loader.getDescription().getVersion());
                sendMessage(sender, "    &eAuthors: &f" + PluginExpansionManager.getAuthors(loader));
                sendMessage(sender, "    &eDescription: &f" + PluginExpansionManager.getDescription(loader));
                sendMessage(sender, "    &eState: &f" + loader.getState());
            }
        });
    }
}
