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
import me.hsgamer.minigamecore.base.Feature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiFunction;

/**
 * The {@link Feature} that handles the replacement.
 * The replacement query will be in the format of {@code [prefix]_[name]}.
 * The prefix can be {@code game}, {@code picker} or {@code planner}.
 */
public class ReplacementFeature implements Feature, StringReplacer {
    private final Planner planner;

    /**
     * Create a new instance
     *
     * @param planner the planner
     */
    public ReplacementFeature(@NotNull Planner planner) {
        this.planner = planner;
    }

    private String query(String query, BiFunction<StringReplacer, String, String> function) {
        String name = null;
        StringReplacer replacer = null;

        String lowerCaseQuery = query.toLowerCase();
        if (lowerCaseQuery.startsWith("game_")) {
            name = query.substring(5);
            replacer = planner.getFeature(GameRunnerFeature.class).getCurrentGameArena();
        } else if (lowerCaseQuery.startsWith("picker_")) {
            name = query.substring(7);
            replacer = planner.getFeature(GamePickerFeature.class).getGamePicker();
        } else if (lowerCaseQuery.startsWith("planner_")) {
            name = query.substring(8);
            replacer = planner;
        }

        if (name == null) {
            name = "";
        }
        if (replacer == null) {
            replacer = StringReplacer.DUMMY;
        }

        return function.apply(replacer, name);
    }

    @Override
    public @Nullable String replace(@NotNull String query) {
        return query(query, StringReplacer::replace);
    }

    @Override
    public @Nullable String replace(@NotNull String original, @NotNull UUID uuid) {
        return query(original, (replacementHandler, name) -> replacementHandler.replace(original, uuid));
    }
}
