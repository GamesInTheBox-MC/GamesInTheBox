package me.hsgamer.gamesinthebox.game.template;

import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArenaAction;
import me.hsgamer.gamesinthebox.game.template.feature.ArenaLogicFeature;
import me.hsgamer.gamesinthebox.game.template.feature.CooldownFeature;
import me.hsgamer.gamesinthebox.game.template.state.IdlingState;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.util.ActionBarUtil;
import me.hsgamer.minigamecore.base.Feature;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class TemplateGameArena extends SimpleGameArena {
    private final TemplateGameExpansion expansion;

    public TemplateGameArena(TemplateGameExpansion expansion, @NotNull String name, @NotNull TemplateGame game, @NotNull Planner planner) {
        super(name, game, planner);
        this.expansion = expansion;
    }

    @Override
    protected void onPointChanged(@NotNull UUID uuid, int point, int totalPoints) {
        if (point == 0) return;

        String message = point < 0 ? expansion.getGameMessageConfig().getPointMinus() : expansion.getGameMessageConfig().getPointPlus();
        int absPoint = Math.abs(point);

        String finalMessage = message
                .replace("{point}", Integer.toString(absPoint))
                .replace("{total}", Integer.toString(totalPoints));

        ActionBarUtil.sendActionBar(uuid, finalMessage);
    }

    @Override
    public @NotNull List<@NotNull String> getDefaultHologramLines(@NotNull String name) {
        return expansion.getDefaultHologramLines(name);
    }

    @Override
    protected List<Feature> loadFeatures() {
        List<Feature> features = super.loadFeatures();

        features.add(new CooldownFeature(this));

        TemplateGameArenaLogic templateGameArenaLogic = expansion.createArenaLogic(this);
        features.add(new ArenaLogicFeature(templateGameArenaLogic));
        features.addAll(templateGameArenaLogic.loadFeatures());

        return features;
    }

    @Override
    protected @NotNull SimpleGameArenaAction createAction() {
        return expansion.getArenaAction(this);
    }

    @Override
    protected void postInitArena() {
        setNextState(IdlingState.class);
    }

    @Override
    public void start() {
        getFeature(CooldownFeature.class).setCanStart(true);
    }

    @Override
    public void end() {
        setNextState(IdlingState.class);
        getFeature(ArenaLogicFeature.class).getArenaLogic().forceEnd();
    }

    @Override
    public long getDelay() {
        return expansion.getGameConfig().getInterval();
    }

    @Override
    public long getPeriod() {
        return expansion.getGameConfig().getInterval();
    }

    @Override
    public boolean isAsync() {
        return expansion.getGameConfig().isAsync();
    }
}
