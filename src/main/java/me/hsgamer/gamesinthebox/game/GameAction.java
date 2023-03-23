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

import java.util.List;

/**
 * The interface for game actions
 */
public interface GameAction {
    /**
     * Get the available actions
     *
     * @return the available actions
     */
    List<String> getActions();

    /**
     * Get the available arguments for the action
     *
     * @param sender the sender
     * @param action the action
     * @param args   the current arguments
     * @return the available arguments
     */
    List<String> getActionArgs(CommandSender sender, String action, String... args);

    /**
     * Perform the action
     *
     * @param sender the sender
     * @param action the action
     * @param args   the arguments
     * @return true if the action is performed successfully
     */
    boolean performAction(CommandSender sender, String action, String... args);
}
