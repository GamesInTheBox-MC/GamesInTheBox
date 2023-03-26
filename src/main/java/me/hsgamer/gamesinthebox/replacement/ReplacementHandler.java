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
package me.hsgamer.gamesinthebox.replacement;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The replacement handler
 */
public interface ReplacementHandler {
    /**
     * The empty replacement handler
     */
    ReplacementHandler EMPTY = s -> null;

    /**
     * Replace the string
     *
     * @param input the input
     * @return the replaced string
     */
    @Nullable
    String replace(@NotNull String input);

    /**
     * Replace the string
     *
     * @param player the player
     * @param input  the input
     * @return the replaced string
     */
    @Nullable
    default String replace(@NotNull OfflinePlayer player, @NotNull String input) {
        return replace(input);
    }
}
