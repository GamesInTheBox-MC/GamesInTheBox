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

import com.cryptomorin.xseries.particles.ParticleDisplay;
import me.hsgamer.minigamecore.base.Feature;

/**
 * The {@link Feature} for {@link ParticleDisplay}
 */
public abstract class ParticleFeature implements Feature {
    private ParticleDisplay particleDisplay;

    /**
     * Create the {@link ParticleDisplay}
     *
     * @return the {@link ParticleDisplay}
     */
    protected abstract ParticleDisplay createParticleDisplay();

    @Override
    public void postInit() {
        this.particleDisplay = createParticleDisplay();
    }

    /**
     * Get the {@link ParticleDisplay}
     *
     * @return the {@link ParticleDisplay}
     */
    public ParticleDisplay getParticleDisplay() {
        return particleDisplay;
    }
}
