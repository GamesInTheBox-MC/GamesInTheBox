package me.hsgamer.gamesinthebox.command.editor;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.game.GameEditor;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.PlannerConfigFeature;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.minigamecore.base.Arena;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The command to save the settings of the editor to the planner
 */
public class SaveCommand extends GameEditorCommand {
    public SaveCommand(GamesInTheBox plugin) {
        super(plugin, "save", "Save the settings to the planner", "<planner> <arena> [override]", true);
    }

    @Override
    protected void onEditorSubCommand(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        Optional<Arena> plannerOptional = plugin.getPlannerManager().getArenaByName(args[0]);
        if (!plannerOptional.isPresent()) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getPlannerNotFound());
            return;
        }
        Planner planner = (Planner) plannerOptional.get();
        PlannerConfigFeature plannerConfigFeature = planner.getFeature(PlannerConfigFeature.class);

        String arenaName = args[1];
        String path = "settings." + arenaName;
        boolean override = args.length >= 3 && Boolean.parseBoolean(args[2]);
        if (plannerConfigFeature.contains(path) && !override) {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getEditorArenaAlreadyExists());
            return;
        }

        plannerConfigFeature.set(path, null);

        if (gameEditor.save(sender, planner, arenaName)) {
            gameEditor.reset(sender);
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getSuccess());
        } else {
            MessageUtils.sendMessage(sender, plugin.getMessageConfig().getEditorCannotSave());
        }
    }

    @Override
    protected boolean isEditorProperUsage(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return args.length >= 2;
    }

    @Override
    protected @NotNull List<String> onEditorTabComplete(GameEditor gameEditor, @NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            return plugin.getPlannerManager().getAllArenas().stream().map(Arena::getName).collect(Collectors.toList());
        } else if (args.length == 3) {
            return Arrays.asList("true", "false");
        }
        return super.onEditorTabComplete(gameEditor, sender, label, args);
    }
}
