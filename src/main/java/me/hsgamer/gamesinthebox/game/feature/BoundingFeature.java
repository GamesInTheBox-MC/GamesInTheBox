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

import com.google.common.base.Preconditions;
import me.hsgamer.hscore.bukkit.block.BukkitBlockAdapter;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.hscore.minecraft.block.box.BlockBox;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * The {@link Feature} that handles the bounding box
 */
public abstract class BoundingFeature implements Feature {
    private World world;
    private BlockBox blockBox;

    /**
     * Create the {@link World} and the {@link BlockBox}
     *
     * @return the {@link Pair} of the {@link World} and the {@link BlockBox}
     */
    @NotNull
    protected abstract Pair<@NotNull World, @NotNull BlockBox> createWorldBox();

    @Override
    public void postInit() {
        Pair<World, BlockBox> pair = createWorldBox();
        this.world = pair.getKey();
        this.blockBox = pair.getValue();
    }

    /**
     * Get the world
     *
     * @return the world
     */
    @NotNull
    public World getWorld() {
        Preconditions.checkNotNull(world, "World is null. The feature is not initialized.");
        return world;
    }

    /**
     * Get the block box
     *
     * @return the block box
     */
    @NotNull
    public BlockBox getBlockBox() {
        Preconditions.checkNotNull(blockBox, "BlockBox is null. The feature is not initialized.");
        return blockBox;
    }

    /**
     * Check if the player is in the bounding box
     *
     * @param player the player
     * @return true if the player is in the bounding box
     */
    public boolean checkBounding(@NotNull OfflinePlayer player) {
        Player onlinePlayer = player.getPlayer();
        return onlinePlayer != null && checkBounding(onlinePlayer.getLocation());
    }

    /**
     * Check if the location is in the bounding box
     *
     * @param location the location
     * @return true if the location is in the bounding box
     */
    public boolean checkBounding(@NotNull Location location) {
        if (blockBox == null) {
            return false;
        }

        if (location.getWorld() != world) {
            return false;
        }
        return blockBox.contains(BukkitBlockAdapter.adapt(location));
    }

    /**
     * Get the entities in the bounding box
     *
     * @return the entities
     */
    public Collection<Entity> getEntities() {
        Location center = new Location(
                world,
                (blockBox.minX + blockBox.maxX) / 2.0,
                (blockBox.minY + blockBox.maxY) / 2.0,
                (blockBox.minZ + blockBox.maxZ) / 2.0
        );
        double xRadius = (blockBox.maxX - blockBox.minX) / 2.0;
        double yRadius = (blockBox.maxY - blockBox.minY) / 2.0;
        double zRadius = (blockBox.maxZ - blockBox.minZ) / 2.0;
        return world.getNearbyEntities(center, xRadius, yRadius, zRadius);
    }
}
