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

import me.hsgamer.gamesinthebox.game.feature.BoundingFeature;
import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.util.LocationUtil;
import me.hsgamer.hscore.bukkit.block.BukkitBlockAdapter;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.hscore.minecraft.block.box.BlockBox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * The simple {@link BoundingFeature}.
 * It will get the bounding box from {@link GameConfigFeature} in the specified path.
 * The value of the path should be like this:
 * <pre>
 *     [path]:
 *       world: world
 *       pos1: 0,0,0
 *       pos2: 0,0,0
 * </pre>
 */
public class SimpleBoundingFeature extends BoundingFeature {
    private final SimpleGameArena arena;
    private final String path;
    private final boolean maxInclusive;

    /**
     * Create a new {@link SimpleBoundingFeature}
     *
     * @param arena        the arena
     * @param path         the path
     * @param maxInclusive whether the box should include the maximum position
     */
    public SimpleBoundingFeature(SimpleGameArena arena, String path, boolean maxInclusive) {
        this.arena = arena;
        this.path = path;
        this.maxInclusive = maxInclusive;
    }

    /**
     * Create a new {@link SimpleBoundingFeature} that will get the bounding box from the config in the path "box"
     *
     * @param arena        the arena
     * @param maxInclusive whether the box should include the maximum position
     */
    public SimpleBoundingFeature(SimpleGameArena arena, boolean maxInclusive) {
        this(arena, "box", maxInclusive);
    }

    @Override
    protected Pair<World, BlockBox> getWorldBox() {
        GameConfigFeature configFeature = arena.getFeature(GameConfigFeature.class);
        String worldName = configFeature.getString(path + ".world", "");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new IllegalStateException(arena.getName() + " has invalid world");
        }
        Location pos1 = LocationUtil.getLocation(world, configFeature.getString(path + ".pos1", ""));
        if (pos1 == null) {
            throw new IllegalStateException(arena.getName() + " has invalid position 1");
        }
        Location pos2 = LocationUtil.getLocation(world, configFeature.getString(path + ".pos2", ""));
        if (pos2 == null) {
            throw new IllegalStateException(arena.getName() + " has invalid position 2");
        }
        return Pair.of(world, new BlockBox(BukkitBlockAdapter.adapt(pos1), BukkitBlockAdapter.adapt(pos2), maxInclusive));
    }
}
