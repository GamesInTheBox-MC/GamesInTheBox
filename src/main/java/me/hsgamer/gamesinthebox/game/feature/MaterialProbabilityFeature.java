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

import com.cryptomorin.xseries.XMaterial;
import com.lewdev.probabilitylib.ProbabilityCollection;
import me.hsgamer.minigamecore.base.Feature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@link Feature} that contains {@link ProbabilityCollection} of {@link XMaterial}
 */
public abstract class MaterialProbabilityFeature implements Feature {
    private ProbabilityCollection<XMaterial> materialCollection;
    private boolean isInitialEmpty = false;

    /**
     * Create the {@link ProbabilityCollection} of {@link XMaterial}
     *
     * @return the collection
     */
    @Nullable
    protected abstract ProbabilityCollection<XMaterial> createMaterialCollection();

    @Override
    public void postInit() {
        this.materialCollection = createMaterialCollection();
        if (materialCollection == null) {
            materialCollection = new ProbabilityCollection<>();
        }
        if (this.materialCollection.isEmpty()) {
            this.isInitialEmpty = true;
            materialCollection.add(XMaterial.STONE, 1);
        }
    }

    /**
     * Get a random {@link XMaterial}
     *
     * @return the random material
     */
    @NotNull
    public XMaterial getRandomMaterial() {
        return materialCollection.get();
    }

    /**
     * Check if the initial collection is empty.
     * Normally the collection would not be empty. Even if it is, it will be filled with {@link XMaterial#STONE}.
     * This method is used to check if the collection is empty before the game starts.
     *
     * @return true if it is
     */
    public boolean isInitialEmpty() {
        return isInitialEmpty;
    }

    /**
     * Get the {@link ProbabilityCollection} of {@link XMaterial}
     *
     * @return the collection
     */
    @NotNull
    public ProbabilityCollection<XMaterial> getMaterialCollection() {
        return materialCollection;
    }
}
