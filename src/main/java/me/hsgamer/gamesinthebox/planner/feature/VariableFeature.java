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
import me.hsgamer.hscore.variable.InstanceVariableManager;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.UUID;

/**
 * The {@link Feature} for replacing variables
 */
public class VariableFeature implements Feature {
    private static final UUID DUMMY_UUID = UUID.randomUUID();
    private final InstanceVariableManager variableManager;

    /**
     * Create a new {@link VariableFeature}
     *
     * @param planner the {@link Planner}
     */
    public VariableFeature(Planner planner) {
        this.variableManager = new InstanceVariableManager();
        variableManager.register("", (original, uuid) -> Optional.ofNullable(planner.getFeature(ReplacementFeature.class))
                .map(replacementFeature -> uuid.equals(DUMMY_UUID) ? replacementFeature.replace(original) : replacementFeature.replace(Bukkit.getOfflinePlayer(uuid), original))
                .orElse(null));
        variableManager.setReplaceAll(true);
    }

    /**
     * Replace the variables
     *
     * @param input the input
     * @param uuid  the uuid
     * @return the replaced string
     */
    public String replace(String input, UUID uuid) {
        return variableManager.setVariables(input, uuid);
    }

    /**
     * Replace the variables
     *
     * @param input the input
     * @return the replaced string
     */
    public String replace(String input) {
        return replace(input, DUMMY_UUID);
    }
}
