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
package me.hsgamer.gamesinthebox.planner.feature;

import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.variable.VariableManager;
import me.hsgamer.minigamecore.base.Feature;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The {@link Feature} for replacing variables
 */
public class VariableFeature implements Feature, StringReplacer {
    private final Planner planner;
    private final VariableManager variableManager;

    /**
     * Create a new {@link VariableFeature}
     *
     * @param planner the {@link Planner}
     */
    public VariableFeature(@NotNull Planner planner) {
        this.planner = planner;
        this.variableManager = new VariableManager();
    }

    @Override
    public void postInit() {
        ReplacementFeature replacementFeature = planner.getFeature(ReplacementFeature.class);
        variableManager.register("", replacementFeature);
    }

    @Override
    public @NotNull String replace(@NotNull String original) {
        return variableManager.setVariables(original, null);
    }

    @Override
    public @NotNull String replace(@NotNull String original, @NotNull UUID uuid) {
        return variableManager.setVariables(original, uuid);
    }
}
