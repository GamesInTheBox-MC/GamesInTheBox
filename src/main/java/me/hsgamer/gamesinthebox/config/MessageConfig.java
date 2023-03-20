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

public interface MessageConfig {
    @ConfigPath("prefix")
    default String getPrefix() {
        return "&6[&eGamesInTheBox&6] &f";
    }

    @ConfigPath("success")
    default String getSuccess() {
        return "&aSuccess";
    }

    @ConfigPath("planner.not-found")
    default String getPlannerNotFound() {
        return "&cPlanner not found";
    }

    @ConfigPath("planner.cannot-force-pick")
    default String getPlannerCannotForcePick() {
        return "&cCannot make planner pick the next game";
    }

    @ConfigPath("game.cannot-set")
    default String getGameCannotSet() {
        return "&cCannot set game for planner";
    }

    @ConfigPath("game.cannot-perform-action")
    default String getGameCannotPerformAction() {
        return "&cCannot perform action for planner";
    }

    @ConfigPath("planner.state.idling")
    default String getPlannerStateIdling() {
        return "Idling";
    }

    @ConfigPath("planner.state.listening")
    default String getPlannerStateListening() {
        return "Listening";
    }

    void reloadConfig();
}
