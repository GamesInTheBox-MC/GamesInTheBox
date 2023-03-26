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
package me.hsgamer.gamesinthebox.game.feature;

import me.hsgamer.minigamecore.base.Arena;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.unihologram.common.api.Hologram;
import me.hsgamer.unihologram.spigot.SpigotHologramProvider;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The {@link Feature} that handles {@link Hologram}
 */
public class HologramFeature implements Feature {
    private static final SpigotHologramProvider spigotHologramProvider;

    static {
        spigotHologramProvider = new SpigotHologramProvider();
    }

    private final String baseName;
    private final List<Hologram<Location>> holograms = new ArrayList<>();

    /**
     * Create a new instance
     *
     * @param baseName the base name of the holograms
     */
    public HologramFeature(@NotNull String baseName) {
        this.baseName = baseName;
    }

    /**
     * Create a new instance
     *
     * @param arena the arena
     */
    public HologramFeature(@NotNull Arena arena) {
        this(arena.getName());
    }

    /**
     * Clear the hologram if it is initialized
     *
     * @param hologram the hologram
     */
    public static void clearIfInitialized(@NotNull Hologram<Location> hologram) {
        if (hologram.isInitialized()) {
            hologram.clear();
        }
    }

    /**
     * Re-initialize the hologram
     *
     * @param hologram the hologram
     */
    public static void reInit(@NotNull Hologram<Location> hologram) {
        clearIfInitialized(hologram);
        hologram.init();
    }

    /**
     * Create a new hologram
     *
     * @param location the location
     * @return the hologram
     */
    @NotNull
    public Hologram<Location> createHologram(@NotNull Location location) {
        return spigotHologramProvider.createHologram(baseName + "-" + UUID.randomUUID(), location);
    }

    @Override
    public void clear() {
        holograms.forEach(HologramFeature::clearIfInitialized);
        holograms.clear();
    }
}
