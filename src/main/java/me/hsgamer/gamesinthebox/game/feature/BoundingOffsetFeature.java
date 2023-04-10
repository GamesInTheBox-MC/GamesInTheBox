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
     * @return the random location
     */
    @NotNull
    public Location getRandomLocation() {
        BlockBox blockBox = boundingFeature.getBlockBox();
        World world = boundingFeature.getWorld();

        int minX = blockBox.minX + offsetSetting.minXOffset;
        int maxX = blockBox.maxX - offsetSetting.maxXOffset;
        int minY = blockBox.minY + offsetSetting.minYOffset;
        int maxY = blockBox.maxY - offsetSetting.maxYOffset;
        int minZ = blockBox.minZ + offsetSetting.minZOffset;
        int maxZ = blockBox.maxZ - offsetSetting.maxZOffset;
        int x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
        int y = ThreadLocalRandom.current().nextInt(minY, maxY + 1);
        int z = ThreadLocalRandom.current().nextInt(minZ, maxZ + 1);
        return new Location(world, x, y, z);
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

        /**
         * Create a new offset setting with the new min X offset
         *
         * @param minXOffset the min X offset
         * @return the new offset setting
         */
        public OffsetSetting withMinXOffset(int minXOffset) {
            return new OffsetSetting(minXOffset, maxXOffset, minYOffset, maxYOffset, minZOffset, maxZOffset);
        }

        /**
         * Create a new offset setting with the new max X offset
         *
         * @param maxXOffset the max X offset
         * @return the new offset setting
         */
        public OffsetSetting withMaxXOffset(int maxXOffset) {
            return new OffsetSetting(minXOffset, maxXOffset, minYOffset, maxYOffset, minZOffset, maxZOffset);
        }

        /**
         * Create a new offset setting with the new min Y offset
         *
         * @param minYOffset the min Y offset
         * @return the new offset setting
         */
        public OffsetSetting withMinYOffset(int minYOffset) {
            return new OffsetSetting(minXOffset, maxXOffset, minYOffset, maxYOffset, minZOffset, maxZOffset);
        }

        /**
         * Create a new offset setting with the new max Y offset
         *
         * @param maxYOffset the max Y offset
         * @return the new offset setting
         */
        public OffsetSetting withMaxYOffset(int maxYOffset) {
            return new OffsetSetting(minXOffset, maxXOffset, minYOffset, maxYOffset, minZOffset, maxZOffset);
        }

        /**
         * Create a new offset setting with the new min Z offset
         *
         * @param minZOffset the min Z offset
         * @return the new offset setting
         */
        public OffsetSetting withMinZOffset(int minZOffset) {
            return new OffsetSetting(minXOffset, maxXOffset, minYOffset, maxYOffset, minZOffset, maxZOffset);
        }

        /**
         * Create a new offset setting with the new max Z offset
         *
         * @param maxZOffset the max Z offset
         * @return the new offset setting
         */
        public OffsetSetting withMaxZOffset(int maxZOffset) {
            return new OffsetSetting(minXOffset, maxXOffset, minYOffset, maxYOffset, minZOffset, maxZOffset);
        }
    }
}
