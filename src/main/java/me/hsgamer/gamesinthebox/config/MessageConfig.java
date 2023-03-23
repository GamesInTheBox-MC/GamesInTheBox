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
package me.hsgamer.gamesinthebox.config;

import me.hsgamer.hscore.config.annotation.ConfigPath;

/**
 * The config for messages
 */
public interface MessageConfig {
    /**
     * Get the prefix
     *
     * @return the prefix
     */
    @ConfigPath("prefix")
    default String getPrefix() {
        return "&6[&eGamesInTheBox&6] &f";
    }

    /**
     * Get the success message
     *
     * @return the message
     */
    @ConfigPath("success")
    default String getSuccess() {
        return "&aSuccess";
    }

    /**
     * Get the "planner not found" message
     *
     * @return the message
     */
    @ConfigPath("planner.not-found")
    default String getPlannerNotFound() {
        return "&cPlanner not found";
    }

    /**
     * Get the "planner cannot force pick" message
     *
     * @return the message
     */
    @ConfigPath("planner.cannot-force-pick")
    default String getPlannerCannotForcePick() {
        return "&cCannot make planner pick the next game";
    }

    /**
     * Get the "game cannot set" message
     *
     * @return the message
     */
    @ConfigPath("game.cannot-set")
    default String getGameCannotSet() {
        return "&cCannot set game for planner";
    }

    /**
     * Get the "game cannot perform action" message
     *
     * @return the message
     */
    @ConfigPath("game.cannot-perform-action")
    default String getGameCannotPerformAction() {
        return "&cCannot perform action for planner";
    }

    /**
     * Get the display name of the Idling state of the planner
     *
     * @return the display name
     */
    @ConfigPath("planner.state.idling")
    default String getPlannerStateIdling() {
        return "Idling";
    }

    /**
     * Get the display name of the Listening state of the planner
     *
     * @return the display name
     */
    @ConfigPath("planner.state.listening")
    default String getPlannerStateListening() {
        return "Listening";
    }

    /**
     * Reload the config
     */
    void reloadConfig();
}
