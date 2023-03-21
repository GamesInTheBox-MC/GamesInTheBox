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
package me.hsgamer.gamesinthebox.game.feature;

import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.ReplacementFeature;
import me.hsgamer.hscore.variable.InstanceVariableManager;
import me.hsgamer.minigamecore.base.Feature;

import java.util.Optional;
import java.util.UUID;

public class GameVariableFeature implements Feature {
    private static final UUID DUMMY_UUID = UUID.randomUUID();
    private final InstanceVariableManager variableManager;

    public GameVariableFeature(Planner planner) {
        this.variableManager = new InstanceVariableManager();
        variableManager.register("", (original, uuid) -> Optional.ofNullable(planner.getFeature(ReplacementFeature.class))
                .map(replacementFeature -> replacementFeature.replace(original))
                .orElse(null));
        variableManager.setReplaceAll(true);
    }

    public String replace(String input, UUID uuid) {
        return variableManager.setVariables(input, uuid);
    }

    public String replace(String input) {
        return replace(input, DUMMY_UUID);
    }
}
