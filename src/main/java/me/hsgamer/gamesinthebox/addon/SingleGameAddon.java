package me.hsgamer.gamesinthebox.addon;

import me.hsgamer.gamesinthebox.GamesInTheBox;
import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;

public abstract class SingleGameAddon extends PluginAddon {
    private Game game;

    protected void enable() {
        // EMPTY
    }

    protected void disable() {
        // EMPTY
    }

    protected void reload() {
        // EMPTY
    }

    protected abstract Game getGame();

    protected abstract String getGameType();

    @Override
    public final void onEnable() {
        enable();

        game = getGame();
        game.init();
        game.postInit();

        GamesInTheBox plugin = (GamesInTheBox) getPlugin();
        plugin.getGameManager().addGame(getGameType(), game);
    }

    @Override
    public final void onDisable() {
        game.clear();

        disable();
    }

    @Override
    public final void onReload() {
        game.clear();

        reload();

        game.init();
        game.postInit();
    }
}
