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
package me.hsgamer.gamesinthebox.game.template.feature;

import me.hsgamer.gamesinthebox.game.template.TemplateGameArenaLogic;
import me.hsgamer.minigamecore.base.Feature;

/**
 * The {@link Feature} to handle the {@link TemplateGameArenaLogic}
 */
public class ArenaLogicFeature implements Feature {
    private final TemplateGameArenaLogic templateGameArenaLogic;

    /**
     * Create a new {@link Feature} for the {@link TemplateGameArenaLogic}
     *
     * @param templateGameArenaLogic the {@link TemplateGameArenaLogic}
     */
    public ArenaLogicFeature(TemplateGameArenaLogic templateGameArenaLogic) {
        this.templateGameArenaLogic = templateGameArenaLogic;
    }

    /**
     * Get the {@link TemplateGameArenaLogic}
     *
     * @return the {@link TemplateGameArenaLogic}
     */
    public TemplateGameArenaLogic getArenaLogic() {
        return templateGameArenaLogic;
    }

    @Override
    public void init() {
        templateGameArenaLogic.init();
    }

    @Override
    public void postInit() {
        templateGameArenaLogic.postInit();
    }

    @Override
    public void clear() {
        templateGameArenaLogic.clear();
    }
}
