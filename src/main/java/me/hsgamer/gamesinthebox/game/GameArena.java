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
import me.hsgamer.gamesinthebox.game.feature.PlannerFeature;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.replacement.ReplacementHandler;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.minigamecore.bukkit.SimpleBukkitArena;

import java.util.ArrayList;
import java.util.List;

public abstract class GameArena extends SimpleBukkitArena implements ReplacementHandler {
    private final String localName;
    private final Planner planner;
    private final Game game;

    protected GameArena(String name, Game game, Planner planner) {
        super(planner.getName() + "-" + name, game);
        this.localName = name;
        this.game = game;
        this.planner = planner;
    }

    public Planner getPlanner() {
        return planner;
    }

    public Game getGame() {
        return game;
    }

    public String getLocalName() {
        return localName;
    }

    public abstract void start();

    public abstract void forceEnd();

    public GameAction getGameAction() {
        return GameAction.EMPTY;
    }

    @Override
    protected List<Feature> loadFeatures() {
        List<Feature> features = new ArrayList<>();
        features.add(new PlannerFeature(planner));
        features.add(new GameConfigFeature(localName, this));
        return features;
    }
}
