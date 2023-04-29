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

import com.google.common.base.Preconditions;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The {@link Feature} that rewards the players
 */
public abstract class RewardFeature implements Feature {
    private Map<Integer, List<String>> topCommands = Collections.emptyMap();
    private List<String> defaultCommands = Collections.emptyList();

    /**
     * Create the top commands and the default commands
     *
     * @return the {@link Pair} of the top commands and the default commands
     */
    @NotNull
    protected abstract Pair<@NotNull Map<@NotNull Integer, @NotNull List<@NotNull String>>, @NotNull List<@NotNull String>> createTopAndDefaultCommands();

    @Override
    public void postInit() {
        Pair<Map<Integer, List<String>>, List<String>> pair = createTopAndDefaultCommands();
        topCommands = pair.getKey();
        defaultCommands = pair.getValue();
    }

    @Override
    public void clear() {
        topCommands = Collections.emptyMap();
        defaultCommands = Collections.emptyList();
    }

    /**
     * Get the top commands
     *
     * @return the top commands
     */
    @NotNull
    public Map<Integer, List<String>> getTopCommands() {
        Preconditions.checkNotNull(topCommands, "The top commands are null. The feature is not initialized.");
        return topCommands;
    }

    /**
     * Get the default commands
     *
     * @return the default commands
     */
    public List<String> getDefaultCommands() {
        Preconditions.checkNotNull(defaultCommands, "The default commands are null. The feature is not initialized.");
        return defaultCommands;
    }

    /**
     * Reward the player
     *
     * @param topPosition the top position, starting from 1
     * @param uuid        the uuid of the player
     */
    public void reward(int topPosition, @NotNull UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        String name = offlinePlayer.getName();
        if (name == null) return;
        List<String> commands = new ArrayList<>(topCommands.getOrDefault(topPosition, defaultCommands));
        commands.replaceAll(s -> s.replace("{name}", name).replace("{top}", Integer.toString(topPosition)));
        Scheduler.providingPlugin(getClass()).sync().runTask(() -> commands.forEach(c -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c)));
    }

    /**
     * Reward the players
     *
     * @param topMap the map of the top position and the list of uuids
     */
    public void reward(@NotNull Map<Integer, List<UUID>> topMap) {
        topMap.forEach((topPosition, uuidList) -> {
            for (UUID uuid : uuidList) {
                reward(topPosition, uuid);
            }
        });
    }

    /**
     * Reward the players
     *
     * @param topList the list of uuids, sorted by the top position
     */
    public void reward(@NotNull List<UUID> topList) {
        for (int i = 0; i < topList.size(); i++) {
            int top = i + 1;
            reward(top, topList.get(i));
        }
    }
}
