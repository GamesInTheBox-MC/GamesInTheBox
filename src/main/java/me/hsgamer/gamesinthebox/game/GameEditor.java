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

import java.util.Collections;
import java.util.List;

/**
 * The {@link GameAction} to edit the {@link Game}
 */
public interface GameEditor extends GameAction {
    /**
     * The editor that does nothing
     */
    GameEditor EMPTY = new GameEditor() {
        @Override
        public void reset(CommandSender sender) {
            // EMPTY
        }

        @Override
        public void sendStatus(CommandSender sender) {
            // EMPTY
        }

        @Override
        public boolean save(CommandSender sender, Planner planner, String arenaName) {
            return false;
        }

        @Override
        public List<String> getActions() {
            return Collections.emptyList();
        }

        @Override
        public List<String> getActionArgs(CommandSender sender, String action, String... args) {
            return Collections.emptyList();
        }

        @Override
        public boolean performAction(CommandSender sender, String action, String... args) {
            return false;
        }

        @Override
        public void sendUsage(CommandSender sender) {
            // EMPTY
        }
    };

    /**
     * Reset the editor
     *
     * @param sender the sender
     */
    void reset(CommandSender sender);

    /**
     * Send the status of the editor
     *
     * @param sender the sender
     */
    void sendStatus(CommandSender sender);

    /**
     * Save the settings to the {@link Planner} under the arena name
     *
     * @param sender    the sender
     * @param planner   the planner
     * @param arenaName the arena name
     * @return true if the editor is saved
     */
    boolean save(CommandSender sender, Planner planner, String arenaName);
}
