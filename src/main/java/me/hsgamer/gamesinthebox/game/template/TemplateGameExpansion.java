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
package me.hsgamer.gamesinthebox.game.template;

import me.hsgamer.gamesinthebox.expansion.SingleGameExpansion;
import me.hsgamer.gamesinthebox.expansion.extra.Reloadable;
import me.hsgamer.gamesinthebox.game.Game;
import me.hsgamer.gamesinthebox.game.template.config.GameConfig;
import me.hsgamer.gamesinthebox.game.template.config.GameMessageConfig;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * The expansion to register the game using {@link TemplateGameLogic}
 */
public abstract class TemplateGameExpansion extends SingleGameExpansion implements Reloadable, TemplateGameLogic {
    private final GameConfig gameConfig = ConfigGenerator.newInstance(GameConfig.class, new BukkitConfig(new File(getDataFolder(), "game.yml")));
    private final GameMessageConfig gameMessageConfig = ConfigGenerator.newInstance(GameMessageConfig.class, new BukkitConfig(new File(getDataFolder(), "game-messages.yml")));

    @Override
    protected @NotNull Game getGame() {
        return new TemplateGame(this);
    }

    @Override
    public void onReload() {
        gameConfig.reloadConfig();
        gameMessageConfig.reloadConfig();
    }

    @Override
    public GameConfig getGameConfig() {
        return gameConfig;
    }

    @Override
    public GameMessageConfig getGameMessageConfig() {
        return gameMessageConfig;
    }
}
