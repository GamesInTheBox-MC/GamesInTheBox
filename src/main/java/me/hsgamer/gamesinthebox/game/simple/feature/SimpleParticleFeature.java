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
package me.hsgamer.gamesinthebox.game.simple.feature;

import com.cryptomorin.xseries.particles.ParticleDisplay;
import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.feature.ParticleFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.util.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

/**
 * The simple {@link ParticleFeature}.
 * It will get the values from {@link GameConfigFeature} with the specified path.
 * The value of the path should be like this:
 * <pre>
 *     [path]:
 *       particle: REDSTONE
 *       count: 1
 *       offset: 0.5,0.5,0.5
 *       extra: true
 *       ...
 * </pre>
 * For more information, please check {@link ParticleDisplay#fromConfig(ConfigurationSection)}.
 */
public class SimpleParticleFeature extends ParticleFeature {
    private final SimpleGameArena arena;
    private final String path;

    /**
     * Create a new {@link SimpleParticleFeature}
     *
     * @param arena the arena
     * @param path  the path
     */
    public SimpleParticleFeature(@NotNull SimpleGameArena arena, @NotNull String path) {
        this.arena = arena;
        this.path = path;
    }

    /**
     * Create a new {@link SimpleParticleFeature} that will get the values from the config in the path "particle"
     *
     * @param arena the arena
     */
    public SimpleParticleFeature(@NotNull SimpleGameArena arena) {
        this(arena, "particle");
    }

    @Override
    protected @NotNull ParticleDisplay createParticleDisplay() {
        return ParticleDisplay.fromConfig(Util.createSection(arena.getFeature(GameConfigFeature.class).getValues(path, false)));
    }
}
