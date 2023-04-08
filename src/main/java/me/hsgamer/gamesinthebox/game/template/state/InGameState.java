package me.hsgamer.gamesinthebox.game.template.state;

import me.hsgamer.gamesinthebox.game.template.TemplateGameArenaLogic;
import me.hsgamer.gamesinthebox.game.template.TemplateGameExpansion;
import me.hsgamer.gamesinthebox.game.template.feature.ArenaLogicFeature;
import me.hsgamer.gamesinthebox.game.template.feature.CooldownFeature;
import me.hsgamer.gamesinthebox.planner.feature.VariableFeature;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.GameState;
import me.hsgamer.minigamecore.bukkit.extra.ColoredDisplayName;
import org.bukkit.Bukkit;

public class InGameState implements GameState, ColoredDisplayName {
    private final TemplateGameExpansion expansion;

    public InGameState(TemplateGameExpansion expansion) {
        this.expansion = expansion;
    }

    @Override
    public void start(Arena arena) {
        String startMessage = arena.getFeature(VariableFeature.class).replace(expansion.getGameMessageConfig().getStartBroadcast());
        Bukkit.getOnlinePlayers().forEach(player -> MessageUtils.sendMessage(player, startMessage));
        arena.getFeature(ArenaLogicFeature.class).getArenaLogic().onInGameStart();
        arena.getFeature(CooldownFeature.class).start(this);
    }

    @Override
    public void update(Arena arena) {
        TemplateGameArenaLogic arenaLogic = arena.getFeature(ArenaLogicFeature.class).getArenaLogic();
        if (arenaLogic.isInGameOver()) {
            arena.setNextState(EndingState.class);
        } else {
            arenaLogic.onInGameUpdate();
        }
    }

    @Override
    public void end(Arena arena) {
        arena.getFeature(ArenaLogicFeature.class).getArenaLogic().onInGameOver();
    }

    @Override
    public String getDisplayName() {
        return expansion.getGameMessageConfig().getStateInGame();
    }
}
