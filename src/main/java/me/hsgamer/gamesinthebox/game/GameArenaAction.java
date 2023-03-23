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

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * The {@link GameAction} for {@link GameArena}
 */
public interface GameArenaAction extends GameAction {
    /**
     * The action that does nothing
     */
    GameArenaAction EMPTY = new GameArenaAction() {
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
}
