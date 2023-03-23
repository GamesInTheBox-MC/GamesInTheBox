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
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.minigamecore.implementation.feature.TimerFeature;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * The simple {@link GameArenaAction}.
 * Provided actions:
 * <ul>
 *     <li>{@code skip-time}: Skip the time</li>
 * </ul>
 */
public class SimpleGameArenaAction implements GameArenaAction {
    private final SimpleGameArena arena;

    public SimpleGameArenaAction(SimpleGameArena arena) {
        this.arena = arena;
    }

    @Override
    public List<String> getActions() {
        List<String> actions = new ArrayList<>();
        actions.add("skip-time");
        return actions;
    }

    @Override
    public List<String> getActionArgs(CommandSender sender, String action, String... args) {
        return Collections.emptyList();
    }

    @Override
    public boolean performAction(CommandSender sender, String action, String... args) {
        if (action.equalsIgnoreCase("skip-time")) {
            arena.getFeature(TimerFeature.class).setDuration(0);
            return true;
        }
        return false;
    }

    /**
     * Get the usage map
     *
     * @return the usage map
     */
    protected Map<String, String> getUsageMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("skip-time", "Skip the time");
        return map;
    }

    @Override
    public void sendUsage(CommandSender sender) {
        MessageUtils.sendMessage(sender, "&6Usage:");
        getUsageMap().forEach((key, value) -> MessageUtils.sendMessage(sender, "&6- &e" + key + " &7- &f" + value));
    }
}
