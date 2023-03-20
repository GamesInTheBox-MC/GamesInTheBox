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
package me.hsgamer.gamesinthebox.expansion;

import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.manager.GameManager;
import me.hsgamer.hscore.builder.Builder;

public abstract class SingleGameExpansion implements GameExpansion {
    private Game game;
    private Builder.FunctionElement<GameManager.Input, GameArena> element;

    protected void enable() {
        // EMPTY
    }

    protected void disable() {
        // EMPTY
    }

    protected abstract Game getGame();

    protected abstract String[] getGameType();

    @Override
    public final void onEnable() {
        enable();

        game = getGame();
        game.init();
        game.postInit();

        element = getPlugin().getGameManager().register(game, getGameType());
    }

    @Override
    public final void onDisable() {
        game.clear();

        disable();

        getPlugin().getGameManager().remove(element);
    }
}
