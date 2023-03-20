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
package me.hsgamer.gamesinthebox.picker;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.replacement.ReplacementHandler;

import java.util.Map;

public interface GamePicker extends ReplacementHandler {
    GamePicker EMPTY = new GamePicker() {
        @Override
        public String replace(String input) {
            return null;
        }

        @Override
        public void setup(Map<String, GameArena> arenaMap) {
            // EMPTY
        }

        @Override
        public GameArena pick() {
            return null;
        }

        @Override
        public boolean canPick() {
            return false;
        }
    };

    void setup(Map<String, GameArena> arenaMap);

    GameArena pick();

    boolean canPick();
}
