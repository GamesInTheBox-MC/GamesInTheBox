package me.hsgamer.gamesinthebox.command.sub;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.Permissions;
import me.hsgamer.gamesinthebox.planner.feature.PickFeature;
import me.hsgamer.gamesinthebox.planner.state.IdlingState;
import me.hsgamer.hscore.bukkit.command.sub.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.minigamecore.base.Arena;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SkipIdleCommand extends SubCommand {
    private final GamesInTheBox plugin;

    public SkipIdleCommand(GamesInTheBox plugin) {
        super("skipidle", "Skip the idling state of a planner", "/<label> skipidle <planner>", Permissions.SKIP_IDLE.getName(), true);
        this.plugin = plugin;
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        Optional<Arena> optional = plugin.getPlannerManager().getArenaByName(args[0]);
        if (!optional.isPresent()) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getPlannerNotFound());
            return;
        }
        Arena planner = optional.get();
        if (planner.getCurrentState() != IdlingState.class) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getPlannerNotIdle());
            return;
        }
        planner.getFeature(PickFeature.class).completeTime();
        MessageUtils.sendMessage(sender, plugin.getMessageConfig().getSuccess());
    }

    @Override
    public boolean isProperUsage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return args.length > 0;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            return plugin.getPlannerManager().getAllArenas().stream().map(Arena::getName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
