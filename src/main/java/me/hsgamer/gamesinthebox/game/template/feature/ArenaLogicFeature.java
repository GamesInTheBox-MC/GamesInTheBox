package me.hsgamer.gamesinthebox.game.template.feature;

import me.hsgamer.gamesinthebox.game.template.TemplateGameArenaLogic;
import me.hsgamer.minigamecore.base.Feature;

public class ArenaLogicFeature implements Feature {
    private final TemplateGameArenaLogic templateGameArenaLogic;

    public ArenaLogicFeature(TemplateGameArenaLogic templateGameArenaLogic) {
        this.templateGameArenaLogic = templateGameArenaLogic;
    }

    public TemplateGameArenaLogic getArenaLogic() {
        return templateGameArenaLogic;
    }

    @Override
    public void init() {
        templateGameArenaLogic.init();
    }

    @Override
    public void postInit() {
        templateGameArenaLogic.postInit();
    }

    @Override
    public void clear() {
        templateGameArenaLogic.clear();
    }
}
