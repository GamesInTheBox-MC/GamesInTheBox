package me.hsgamer.gamesinthebox.game.template.feature;

import me.hsgamer.gamesinthebox.game.template.logic.ArenaLogic;
import me.hsgamer.minigamecore.base.Feature;

public class ArenaLogicFeature implements Feature {
    private final ArenaLogic arenaLogic;

    public ArenaLogicFeature(ArenaLogic arenaLogic) {
        this.arenaLogic = arenaLogic;
    }

    public ArenaLogic getArenaLogic() {
        return arenaLogic;
    }

    @Override
    public void init() {
        arenaLogic.init();
    }

    @Override
    public void postInit() {
        arenaLogic.postInit();
    }

    @Override
    public void clear() {
        arenaLogic.clear();
    }
}
