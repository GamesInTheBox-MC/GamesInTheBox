/*
   Copyright 2023-2023 Huynh Tien

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package me.hsgamer.gamesinthebox.game;

import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.replacement.ReplacementHandler;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.bukkit.hscore.HSCoreBukkitArena;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link me.hsgamer.minigamecore.base.Arena} of a {@link Game}.
 * Note:
 * <ul>
 *     <li>{@link #getName()} will return the name of the arena with the planner name (e.g. "[planner]-[arena]")</li>
 *     <li>To get the name of the arena without the planner name, use {@link #getLocalName()}</li>
 *     <li>The arena provides {@link GameConfigFeature} to access the game settings</li>
 *     <li>{@link #getFeature(Class)} will get the {@link Feature} from both the {@link GameArena} and the {@link Planner} that the arena belongs to</li>
 * </ul>
 */
public abstract class GameArena extends HSCoreBukkitArena implements ReplacementHandler {
    private final String localName;
    private final Planner planner;
    private final Game game;

    /**
     * Create a new game arena
     *
     * @param name    the name of the arena
     * @param game    the game that the arena belongs to
     * @param planner the planner that the arena belongs to
     */
    protected GameArena(@NotNull String name, @NotNull Game game, @NotNull Planner planner) {
        super(planner.getName() + "-" + name, game);
        this.localName = name;
        this.game = game;
        this.planner = planner;
    }

    /**
     * Get the planner that the arena belongs to
     *
     * @return the planner
     */
    @NotNull
    public Planner getPlanner() {
        return planner;
    }

    /**
     * Get the game that the arena belongs to
     *
     * @return the game
     */
    @NotNull
    public Game getGame() {
        return game;
    }

    /**
     * Get the local name of the arena.
     * This is the name of the arena without the planner name.
     *
     * @return the local name
     */
    @NotNull
    public String getLocalName() {
        return localName;
    }

    /**
     * Start the game.
     * This is mainly called by the {@link Planner}.
     */
    public abstract void start();

    /**
     * End the game.
     * This is mainly called by the {@link Planner} when the admin force the game to end.
     */
    public abstract void end();

    /**
     * Get the {@link GameArenaAction} of the arena.
     * Override this method to provide the action that the admin can perform.
     *
     * @return the {@link GameArenaAction}
     */
    @NotNull
    public GameArenaAction getAction() {
        return GameArenaAction.EMPTY;
    }

    @Override
    protected List<Feature> loadFeatures() {
        List<Feature> features = new ArrayList<>();
        features.add(new GameConfigFeature(this));
        return features;
    }

    /**
     * {@inheritDoc}
     * If the feature is not found in the {@link GameArena}, it will get from the {@link Planner} that the arena belongs to.
     *
     * @param featureClass the class of the feature
     * @param <T>          the type of the feature
     * @return the feature
     */
    @Override
    public <T extends Feature> T getFeature(Class<T> featureClass) {
        T feature = super.getFeature(featureClass);
        if (feature == null) {
            feature = planner.getFeature(featureClass);
        }
        return feature;
    }
}
