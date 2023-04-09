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
package me.hsgamer.gamesinthebox.game.template.config;

import me.hsgamer.hscore.config.annotation.ConfigPath;

/**
 * The message config for a {@link me.hsgamer.gamesinthebox.game.template.TemplateGameArena}
 */
public interface GameMessageConfig {
    @ConfigPath("point.plus.plural")
    default String getPointPlusPlural() {
        return "&a+{point} points &7({total})";
    }

    @ConfigPath("point.minus.plural")
    default String getPointMinusPlural() {
        return "&c-{point} points &7({total})";
    }

    @ConfigPath("point.plus.singular")
    default String getPointPlusSingular() {
        return "&a+{point} point &7({total})";
    }

    @ConfigPath("point.minus.singular")
    default String getPointMinusSingular() {
        return "&c-{point} point &7({total})";
    }

    @ConfigPath("state.waiting")
    default String getStateWaiting() {
        return "Waiting";
    }

    @ConfigPath("state.in-game")
    default String getStateInGame() {
        return "In Game";
    }

    @ConfigPath("state.ending")
    default String getStateEnding() {
        return "Ending";
    }

    @ConfigPath("state.idle")
    default String getStateIdle() {
        return "Idle";
    }

    @ConfigPath("start-broadcast")
    default String getStartBroadcast() {
        return "&aThe game has started!";
    }

    @ConfigPath("end-broadcast")
    default String getEndBroadcast() {
        return "&aThe game has ended!";
    }

    @ConfigPath("not-enough-player-to-reward")
    default String getNotEnoughPlayerToReward() {
        return "&cThere are not enough players to reward";
    }

    void reloadConfig();
}
