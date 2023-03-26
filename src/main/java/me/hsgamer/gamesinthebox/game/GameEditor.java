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

import me.hsgamer.gamesinthebox.planner.Planner;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The {@link GameAction} to edit the {@link Game}
 */
public interface GameEditor extends GameAction {
    /**
     * The editor that does nothing
     */
    GameEditor EMPTY = new GameEditor() {
        @Override
        public void reset(@NotNull CommandSender sender) {
            // EMPTY
        }

        @Override
        public void sendStatus(@NotNull CommandSender sender) {
            // EMPTY
        }

        @Override
        public @NotNull Optional<Map<String, Object>> exportPathValueMap(@NotNull CommandSender sender) {
            return Optional.empty();
        }

        @Override
        public @NotNull List<String> getActions() {
            return Collections.emptyList();
        }

        @Override
        public @NotNull List<@NotNull String> getActionArgs(@NotNull CommandSender sender, @NotNull String action, @NotNull String @NotNull ... args) {
            return Collections.emptyList();
        }

        @Override
        public boolean performAction(@NotNull CommandSender sender, @NotNull String action, @NotNull String @NotNull ... args) {
            return false;
        }

        @Override
        public void sendUsage(@NotNull CommandSender sender) {
            // EMPTY
        }
    };

    /**
     * Reset the editor
     *
     * @param sender the sender
     */
    void reset(@NotNull CommandSender sender);

    /**
     * Send the status of the editor
     *
     * @param sender the sender
     */
    void sendStatus(@NotNull CommandSender sender);

    /**
     * Export the config path-value map used to save the settings to the {@link Planner}
     *
     * @param sender the sender
     * @return the path-value map
     */
    @NotNull
    Optional<Map<String, Object>> exportPathValueMap(@NotNull CommandSender sender);

    /**
     * Migrate the settings from the {@link GameArena} to the {@link GameEditor}
     *
     * @param sender    the sender
     * @param gameArena the game arena
     * @return true if success
     */
    default boolean migrate(@NotNull CommandSender sender, @NotNull GameArena gameArena) {
        return false;
    }
}
