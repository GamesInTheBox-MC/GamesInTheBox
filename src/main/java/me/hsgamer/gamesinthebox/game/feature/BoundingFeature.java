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

import me.hsgamer.hscore.bukkit.block.BukkitBlockAdapter;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.hscore.minecraft.block.box.BlockBox;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

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
    protected abstract Pair<World, BlockBox> createWorldBox();

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
    public World getWorld() {
        return world;
    }

    /**
     * Get the block box
     *
     * @return the block box
     */
    public BlockBox getBlockBox() {
        return blockBox;
    }

    /**
     * Check if the player is in the bounding box
     *
     * @param player the player
     * @return true if the player is in the bounding box
     */
    public boolean checkBounding(OfflinePlayer player) {
        Player onlinePlayer = player.getPlayer();
        return onlinePlayer != null && checkBounding(onlinePlayer.getLocation());
    }

    /**
     * Check if the location is in the bounding box
     *
     * @param location the location
     * @return true if the location is in the bounding box
     */
    public boolean checkBounding(Location location) {
        if (location == null || blockBox == null) {
            return false;
        }

        if (location.getWorld() != world) {
            return false;
        }
        return blockBox.contains(BukkitBlockAdapter.adapt(location));
    }

    /**
     * Get a random location in the bounding box
     *
     * @param vectorOffsetSetting the offset setting
     * @return the random location
     */
    public Location getRandomLocation(OffsetSetting vectorOffsetSetting) {
        int minX = blockBox.minX + vectorOffsetSetting.minXOffset;
        int maxX = blockBox.maxX - vectorOffsetSetting.maxXOffset;
        int minY = blockBox.minY + vectorOffsetSetting.minYOffset;
        int maxY = blockBox.maxY - vectorOffsetSetting.maxYOffset;
        int minZ = blockBox.minZ + vectorOffsetSetting.minZOffset;
        int maxZ = blockBox.maxZ - vectorOffsetSetting.maxZOffset;
        int x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
        int y = ThreadLocalRandom.current().nextInt(minY, maxY + 1);
        int z = ThreadLocalRandom.current().nextInt(minZ, maxZ + 1);
        return new Location(world, x, y, z);
    }

    /**
     * Get a random location in the bounding box
     *
     * @return the random location
     */
    public Location getRandomLocation() {
        return getRandomLocation(OffsetSetting.DEFAULT);
    }

    /**
     * The offset setting
     */
    public static class OffsetSetting {
        /**
         * The default offset setting, which is 0 for all
         */
        public static final OffsetSetting DEFAULT = new OffsetSetting(0, 0, 0, 0, 0, 0);

        /**
         * The min X offset
         */
        public final int minXOffset;
        /**
         * The max X offset
         */
        public final int maxXOffset;
        /**
         * The min Y offset
         */
        public final int minYOffset;
        /**
         * The max Y offset
         */
        public final int maxYOffset;
        /**
         * The min Z offset
         */
        public final int minZOffset;
        /**
         * The max Z offset
         */
        public final int maxZOffset;

        /**
         * Create a new offset setting
         *
         * @param minXOffset the min X offset
         * @param maxXOffset the max X offset
         * @param minYOffset the min Y offset
         * @param maxYOffset the max Y offset
         * @param minZOffset the min Z offset
         * @param maxZOffset the max Z offset
         */
        public OffsetSetting(int minXOffset, int maxXOffset, int minYOffset, int maxYOffset, int minZOffset, int maxZOffset) {
            this.minXOffset = minXOffset;
            this.maxXOffset = maxXOffset;
            this.minYOffset = minYOffset;
            this.maxYOffset = maxYOffset;
            this.minZOffset = minZOffset;
            this.maxZOffset = maxZOffset;
        }
    }
}
