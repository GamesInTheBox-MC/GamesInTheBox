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
package me.hsgamer.gamesinthebox.game.simple;

import me.hsgamer.gamesinthebox.game.GameArenaAction;
import me.hsgamer.minigamecore.implementation.feature.TimerFeature;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The simple {@link GameArenaAction}.
 * Provided actions:
 * <ul>
 *     <li>{@code skip-time}: Skip the time</li>
 * </ul>
 */
public class SimpleGameArenaAction extends SimpleGameAction implements GameArenaAction {
    protected final SimpleGameArena arena;

    /**
     * Create a new {@link SimpleGameArenaAction}
     *
     * @param arena the arena
     */
    public SimpleGameArenaAction(SimpleGameArena arena) {
        this.arena = arena;
    }

    @Override
    protected Map<String, SimpleAction> getActionMap() {
        Map<String, SimpleAction> map = new LinkedHashMap<>();
        map.put("skip-time", new SimpleAction() {
            @Override
            public String getUsage() {
                return "Skip the time";
            }

            @Override
            public List<String> getActionArgs(CommandSender sender, String... args) {
                return Collections.emptyList();
            }

            @Override
            public void performAction(CommandSender sender, String... args) {
                arena.getFeature(TimerFeature.class).setDuration(0);
            }
        });
        return map;
    }
}
