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

import me.hsgamer.hscore.minecraft.block.box.BlockBox;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@link Feature} that handles the offset of the {@link BoundingFeature}
 */
public abstract class BoundingOffsetFeature implements Feature {
    private final BoundingFeature boundingFeature;
    private OffsetSetting offsetSetting = OffsetSetting.DEFAULT;

    /**
     * Create a new bounding offset feature
     *
     * @param boundingFeature the bounding feature
     */
    public BoundingOffsetFeature(BoundingFeature boundingFeature) {
        this.boundingFeature = boundingFeature;
    }

    /**
     * Create the offset setting
     *
     * @return the offset setting
     */
    @NotNull
    protected abstract OffsetSetting createOffsetSetting();

    @Override
    public void postInit() {
        this.offsetSetting = createOffsetSetting();
    }

    /**
     * Get the offset setting
     *
     * @return the offset setting
     */
    public OffsetSetting getOffsetSetting() {
        return offsetSetting;
    }

    /**
     * Get a random location in the bounding box
     *
     * @param normalize whether to normalize the location by setting to the nearest block
     * @return the random location
     */
    @NotNull
    public Location getRandomLocation(boolean normalize) {
        BlockBox blockBox = boundingFeature.getBlockBox();
        World world = boundingFeature.getWorld();

        double minX = blockBox.minX + offsetSetting.minXOffset;
        double maxX = blockBox.maxX - offsetSetting.maxXOffset;
        double minY = blockBox.minY + offsetSetting.minYOffset;
        double maxY = blockBox.maxY - offsetSetting.maxYOffset;
        double minZ = blockBox.minZ + offsetSetting.minZOffset;
        double maxZ = blockBox.maxZ - offsetSetting.maxZOffset;
        double x = ThreadLocalRandom.current().nextDouble(minX, maxX + 1);
        double y = ThreadLocalRandom.current().nextDouble(minY, maxY + 1);
        double z = ThreadLocalRandom.current().nextDouble(minZ, maxZ + 1);
        Location location = new Location(world, x, y, z);

        return normalize ? location.getBlock().getLocation() : location;
    }

    /**
     * Get a random location in the bounding box
     *
     * @return the random location
     */
    @NotNull
    public Location getRandomLocation() {
        return getRandomLocation(true);
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
        public final double minXOffset;
        /**
         * The max X offset
         */
        public final double maxXOffset;
        /**
         * The min Y offset
         */
        public final double minYOffset;
        /**
         * The max Y offset
         */
        public final double maxYOffset;
        /**
         * The min Z offset
         */
        public final double minZOffset;
        /**
         * The max Z offset
         */
        public final double maxZOffset;

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
        public OffsetSetting(double minXOffset, double maxXOffset, double minYOffset, double maxYOffset, double minZOffset, double maxZOffset) {
            this.minXOffset = minXOffset;
            this.maxXOffset = maxXOffset;
            this.minYOffset = minYOffset;
            this.maxYOffset = maxYOffset;
            this.minZOffset = minZOffset;
            this.maxZOffset = maxZOffset;
        }

        /**
         * Create a new offset setting with the new min X offset
         *
         * @param minXOffset the min X offset
         * @return the new offset setting
         */
        public OffsetSetting withMinXOffset(double minXOffset) {
            return new OffsetSetting(minXOffset, maxXOffset, minYOffset, maxYOffset, minZOffset, maxZOffset);
        }

        /**
         * Create a new offset setting with the new max X offset
         *
         * @param maxXOffset the max X offset
         * @return the new offset setting
         */
        public OffsetSetting withMaxXOffset(double maxXOffset) {
            return new OffsetSetting(minXOffset, maxXOffset, minYOffset, maxYOffset, minZOffset, maxZOffset);
        }

        /**
         * Create a new offset setting with the new min Y offset
         *
         * @param minYOffset the min Y offset
         * @return the new offset setting
         */
        public OffsetSetting withMinYOffset(double minYOffset) {
            return new OffsetSetting(minXOffset, maxXOffset, minYOffset, maxYOffset, minZOffset, maxZOffset);
        }

        /**
         * Create a new offset setting with the new max Y offset
         *
         * @param maxYOffset the max Y offset
         * @return the new offset setting
         */
        public OffsetSetting withMaxYOffset(double maxYOffset) {
            return new OffsetSetting(minXOffset, maxXOffset, minYOffset, maxYOffset, minZOffset, maxZOffset);
        }

        /**
         * Create a new offset setting with the new min Z offset
         *
         * @param minZOffset the min Z offset
         * @return the new offset setting
         */
        public OffsetSetting withMinZOffset(double minZOffset) {
            return new OffsetSetting(minXOffset, maxXOffset, minYOffset, maxYOffset, minZOffset, maxZOffset);
        }

        /**
         * Create a new offset setting with the new max Z offset
         *
         * @param maxZOffset the max Z offset
         * @return the new offset setting
         */
        public OffsetSetting withMaxZOffset(double maxZOffset) {
            return new OffsetSetting(minXOffset, maxXOffset, minYOffset, maxYOffset, minZOffset, maxZOffset);
        }
    }
}
