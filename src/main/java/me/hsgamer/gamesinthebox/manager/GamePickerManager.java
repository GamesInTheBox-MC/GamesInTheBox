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
package me.hsgamer.gamesinthebox.manager;

import me.hsgamer.gamesinthebox.picker.GamePicker;
import me.hsgamer.gamesinthebox.picker.impl.ChanceGamePicker;
import me.hsgamer.gamesinthebox.picker.impl.RandomGamePicker;
import me.hsgamer.gamesinthebox.picker.impl.SequenceGamePicker;
import me.hsgamer.gamesinthebox.planner.Planner;
import me.hsgamer.gamesinthebox.planner.feature.PlannerConfigFeature;
import me.hsgamer.hscore.builder.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * The manager that handles all {@link GamePicker}
 */
public class GamePickerManager extends Builder<Planner, GamePicker> {
    /**
     * Create a new manager
     */
    public GamePickerManager() {
        register(RandomGamePicker::new, "random");
        register(SequenceGamePicker::new, "sequence");
        register(ChanceGamePicker::new, "chance");
    }

    /**
     * Build a {@link GamePicker} from the planner.
     * It will get the type from the {@link PlannerConfigFeature} with the key {@code picker-type}.
     * If the type is not found, it will return {@link GamePicker#EMPTY}.
     *
     * @param planner the planner
     * @return the {@link GamePicker}
     */
    @NotNull
    public GamePicker build(@NotNull Planner planner) {
        return Optional.ofNullable(planner.getFeature(PlannerConfigFeature.class).getString("picker-type", null)).flatMap(s -> this.build(s, planner)).orElse(GamePicker.EMPTY);
    }
}
