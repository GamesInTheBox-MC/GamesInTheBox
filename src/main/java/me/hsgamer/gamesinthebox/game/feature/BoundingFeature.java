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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class BoundingFeature implements Feature {
    private final Supplier<Pair<World, BlockBox>> supplier;
    private World world;
    private BlockBox blockBox;

    public BoundingFeature(Supplier<Pair<World, BlockBox>> supplier) {
        this.supplier = supplier;
    }

    public BoundingFeature(World world, BlockBox blockBox) {
        this(() -> Pair.of(world, blockBox));
    }

    @Override
    public void postInit() {
        Pair<World, BlockBox> pair = supplier.get();
        this.world = pair.getKey();
        this.blockBox = pair.getValue();
    }

    public World getWorld() {
        return world;
    }

    public BlockBox getBlockBox() {
        return blockBox;
    }

    public boolean checkBounding(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player != null && checkBounding(player.getLocation());
    }

    public boolean checkBounding(Location location) {
        if (location == null || blockBox == null) {
            return false;
        }

        if (location.getWorld() != world) {
            return false;
        }
        return blockBox.contains(BukkitBlockAdapter.adapt(location));
    }

    public Location getRandomLocation(VectorOffsetSetting vectorOffsetSetting) {
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

    public Location getRandomLocation() {
        return getRandomLocation(VectorOffsetSetting.DEFAULT);
    }

    public static class VectorOffsetSetting {
        public static final VectorOffsetSetting DEFAULT = new VectorOffsetSetting(0, 0, 0, 0, 0, 0);
        public final int minXOffset;
        public final int maxXOffset;
        public final int minYOffset;
        public final int maxYOffset;
        public final int minZOffset;
        public final int maxZOffset;

        public VectorOffsetSetting(int minXOffset, int maxXOffset, int minYOffset, int maxYOffset, int minZOffset, int maxZOffset) {
            this.minXOffset = minXOffset;
            this.maxXOffset = maxXOffset;
            this.minYOffset = minYOffset;
            this.maxYOffset = maxYOffset;
            this.minZOffset = minZOffset;
            this.maxZOffset = maxZOffset;
        }
    }
}
