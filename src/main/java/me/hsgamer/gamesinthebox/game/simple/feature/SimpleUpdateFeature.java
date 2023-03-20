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
package me.hsgamer.gamesinthebox.game.simple.feature;

import me.hsgamer.gamesinthebox.game.feature.PointFeature;
import me.hsgamer.gamesinthebox.game.feature.TopFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.minigamecore.base.Feature;

public class SimpleUpdateFeature implements Feature {
    private final SimpleGameArena arena;

    public SimpleUpdateFeature(SimpleGameArena arena) {
        this.arena = arena;
    }

    public void initState() {
        arena.getFeature(DescriptiveHologramFeature.class).initHologram();
    }

    public void updateState() {
        arena.getFeature(PointFeature.class).takeTopSnapshot();
        arena.getFeature(TopFeature.class).setTop(arena.getFeature(PointFeature.class).getTopSnapshotAsStringPair());
        arena.getFeature(DescriptiveHologramFeature.class).updateHologram();
    }

    public void clearState() {
        arena.getFeature(PointFeature.class).clearPoints();
        arena.getFeature(DescriptiveHologramFeature.class).clearHologram();
    }
}
