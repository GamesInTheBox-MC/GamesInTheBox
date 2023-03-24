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
package me.hsgamer.gamesinthebox.game.simple;

import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.gamesinthebox.game.feature.HologramFeature;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.minigamecore.base.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 * The simple {@link Game}.
 * Provided features:
 * <ul>
 *     <li>{@link HologramFeature}</li>
 * </ul>
 */
public abstract class SimpleGame extends Game {
    private SimpleGameEditor editor;

    /**
     * Create the {@link SimpleGameEditor}
     *
     * @return the {@link SimpleGameEditor}
     */
    protected SimpleGameEditor createEditor() {
        return new SimpleGameEditor(this);
    }

    @Override
    public SimpleGameEditor getEditor() {
        if (editor == null) {
            editor = createEditor();
        }
        return editor;
    }

    @Override
    protected abstract SimpleGameArena newArena(String name, Planner planner);

    @Override
    protected List<Feature> loadFeatures() {
        List<Feature> features = new ArrayList<>();
        features.add(new HologramFeature(getClass().getSimpleName()));
        return features;
    }
}
