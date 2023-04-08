package me.hsgamer.gamesinthebox.game.template;

import me.hsgamer.gamesinthebox.expansion.SingleGameExpansion;
import me.hsgamer.gamesinthebox.expansion.extra.Reloadable;
import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.gamesinthebox.game.simple.SimpleGame;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArenaAction;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameEditor;
import me.hsgamer.gamesinthebox.game.template.config.GameConfig;
import me.hsgamer.gamesinthebox.game.template.config.GameMessageConfig;
import me.hsgamer.gamesinthebox.game.template.logic.ArenaLogic;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import me.hsgamer.minigamecore.base.Feature;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;

public abstract class TemplateGameExpansion extends SingleGameExpansion implements Reloadable {
    private final GameConfig gameConfig = ConfigGenerator.newInstance(GameConfig.class, new BukkitConfig(new File(getDataFolder(), "game.yml")));
    private final GameMessageConfig gameMessageConfig = ConfigGenerator.newInstance(GameMessageConfig.class, new BukkitConfig(new File(getDataFolder(), "game-messages.yml")));

    public abstract ArenaLogic createArenaLogic(SimpleGameArena arena);

    public abstract String getDisplayName();

    public SimpleGameEditor getEditor(SimpleGame game) {
        return new SimpleGameEditor(game);
    }

    public SimpleGameArenaAction getArenaAction(SimpleGameArena arena) {
        return new SimpleGameArenaAction(arena);
    }

    public List<Feature> getGameFeatures(SimpleGame game) {
        return Collections.emptyList();
    }

    @Override
    protected @NotNull Game getGame() {
        return new TemplateGame(this);
    }

    @Override
    public void onReload() {
        gameConfig.reloadConfig();
        gameMessageConfig.reloadConfig();
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public GameMessageConfig getGameMessageConfig() {
        return gameMessageConfig;
    }
}
