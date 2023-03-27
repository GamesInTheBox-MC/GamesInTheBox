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
package me.hsgamer.gamesinthebox.game.simple.action;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * The {@link me.hsgamer.gamesinthebox.game.simple.SimpleGameAction.SimpleAction} that uses the location that the player is looking at
 */
public abstract class LookingBlockLocationAction extends LocationAction {
    /**
     * Get the maximum distance of the block that the player is looking at
     *
     * @return the distance
     */
    protected int getDistance() {
        return 100;
    }

    /**
     * Get the transparent blocks to be ignored
     *
     * @return the transparent blocks
     */
    @Nullable
    protected Set<Material> getTransparentMaterials() {
        return null;
    }

    @Override
    protected Location getLocation(@NotNull Player player) {
        return player.getTargetBlock(getTransparentMaterials(), getDistance()).getLocation();
    }
}
