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
package me.hsgamer.gamesinthebox.game.simple.feature;

import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.feature.RewardFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameAction;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameEditor;
import me.hsgamer.gamesinthebox.game.simple.action.NumberAction;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.hscore.common.Validate;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The simple {@link RewardFeature}.
 * It will get the settings from {@link GameConfigFeature}.
 * The settings are:
 * <ul>
 *     <li>{@code min-players-to-reward}: the minimum players to reward. Default: -1 (no limit)</li>
 *     <li>{@code reward.default}, {@code reward.all}, {@code reward.?}: the commands to execute when the player is not in the top list</li>
 *     <li>{@code reward.<number>}: the commands to execute when the player is in the top list</li>
 * </ul>
 */
public class SimpleRewardFeature extends RewardFeature {
    private final SimpleGameArena arena;
    private int minPlayersToReward = -1;

    /**
     * Create a new {@link SimpleRewardFeature}
     *
     * @param arena the arena
     */
    public SimpleRewardFeature(@NotNull SimpleGameArena arena) {
        this.arena = arena;
    }

    /**
     * Get the editor of this feature
     *
     * @return the editor
     */
    public static Editor editor() {
        return new Editor();
    }

    @Override
    protected @NotNull Pair<Map<Integer, List<String>>, List<String>> createTopAndDefaultCommands() {
        GameConfigFeature gameConfigFeature = arena.getFeature(GameConfigFeature.class);

        minPlayersToReward = Optional.ofNullable(gameConfigFeature.getString("min-players-to-reward"))
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .orElse(minPlayersToReward);

        Map<Integer, List<String>> parsedTopCommands = new HashMap<>();
        List<String> parsedDefaultCommands = new ArrayList<>();
        arena.getFeature(GameConfigFeature.class).getValues("reward", false).forEach((k, v) -> {
            if (k.equalsIgnoreCase("default") || k.equalsIgnoreCase("all") || k.equalsIgnoreCase("?")) {
                parsedDefaultCommands.addAll(CollectionUtils.createStringListFromObject(v, true));
            } else {
                int i;
                try {
                    i = Integer.parseInt(k);
                } catch (Exception e) {
                    return;
                }
                parsedTopCommands.put(i, CollectionUtils.createStringListFromObject(v, true));
            }
        });
        return Pair.of(parsedTopCommands, parsedDefaultCommands);
    }

    /**
     * Try to reward the players.
     * If the number of players is less than {@link #getMinPlayersToReward()}, it will return false.
     *
     * @param uuids the players
     * @return true if the players are rewarded
     */
    public boolean tryReward(@NotNull List<@NotNull UUID> uuids) {
        if (minPlayersToReward >= 0 && uuids.size() < minPlayersToReward) {
            return false;
        }
        reward(uuids);
        return true;
    }

    /**
     * Get the minimum players to reward
     *
     * @return the minimum players to reward
     */
    public int getMinPlayersToReward() {
        return minPlayersToReward;
    }

    /**
     * The editor of this feature
     */
    public static class Editor {
        protected Map<String, List<String>> rewardCommands;
        protected Integer minPlayersToReward;

        private Editor() {
            // EMPTY
        }

        /**
         * Get the actions of the editor
         *
         * @return the actions
         */
        public Map<String, SimpleGameAction.SimpleAction> getActions() {
            Map<String, SimpleGameAction.SimpleAction> map = new LinkedHashMap<>();

            map.put("add-reward", new NumberAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Add a reward command. Use -1 for all players";
                }

                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number number, String... args) {
                    if (args.length < 1) {
                        return false;
                    }

                    String top = number.intValue() <= 0 ? "default" : String.valueOf(number.intValue());
                    String command = StringUtils.join(args, " ", 1, args.length);

                    if (rewardCommands == null) {
                        rewardCommands = new LinkedHashMap<>();
                    }

                    rewardCommands.computeIfAbsent(top, k -> new ArrayList<>()).add(command);
                    return true;
                }

                @Override
                protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
                    return Arrays.asList("-1", "1", "2", "3", "4", "5");
                }

                @Override
                public @NotNull String getArgsUsage() {
                    return "<top> <command>";
                }
            });
            map.put("clear-reward", new NumberAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Clear all reward commands of a top position. Use -1 for all players";
                }

                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number number, String... args) {
                    String top = number.intValue() <= 0 ? "default" : String.valueOf(number.intValue());

                    if (rewardCommands != null) {
                        rewardCommands.remove(top);
                    }
                    return true;
                }

                @Override
                protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
                    return Arrays.asList("-1", "1", "2", "3", "4", "5");
                }

                @Override
                public @NotNull String getArgsUsage() {
                    return "<top>";
                }
            });
            map.put("clear-all-reward", new SimpleGameAction.SimpleAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Clear all reward commands";
                }

                @Override
                public boolean performAction(@NotNull CommandSender sender, String... args) {
                    rewardCommands = null;
                    return true;
                }
            });
            map.put("set-min-players-to-reward", new NumberAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Set the minimum players to reward";
                }

                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number number, String... args) {
                    int players = number.intValue();
                    if (players >= 0) {
                        minPlayersToReward = players;
                    } else {
                        minPlayersToReward = null;
                    }
                    return true;
                }

                @Override
                protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
                    return Arrays.asList("0", "1", "2", "3", "4", "5");
                }
            });

            return map;
        }

        /**
         * Get the status of the editor
         *
         * @return the status
         */
        public SimpleGameEditor.SimpleEditorStatus getStatus() {
            return new SimpleGameEditor.SimpleEditorStatus() {
                @Override
                public void sendStatus(@NotNull CommandSender sender) {
                    MessageUtils.sendMessage(sender, "&6&lREWARD");
                    if (rewardCommands == null || rewardCommands.isEmpty()) {
                        MessageUtils.sendMessage(sender, "&6Reward Commands: &eEmpty");
                    } else {
                        MessageUtils.sendMessage(sender, "&6Reward Commands:");
                        for (Map.Entry<String, List<String>> entry : rewardCommands.entrySet()) {
                            MessageUtils.sendMessage(sender, "  &e" + entry.getKey() + ":");
                            for (String command : entry.getValue()) {
                                MessageUtils.sendMessage(sender, "  &f- " + command);
                            }
                        }
                    }
                    MessageUtils.sendMessage(sender, "&6Min Players to Reward: &e" + (minPlayersToReward == null ? "Default" : minPlayersToReward));
                }

                @Override
                public void reset(@NotNull CommandSender sender) {
                    rewardCommands = null;
                    minPlayersToReward = null;
                }

                @Override
                public boolean canSave(@NotNull CommandSender sender) {
                    return true;
                }

                @Override
                public Map<String, Object> toPathValueMap(@NotNull CommandSender sender) {
                    Map<String, Object> pathValueMap = new LinkedHashMap<>();
                    if (rewardCommands != null && !rewardCommands.isEmpty()) {
                        pathValueMap.put("reward", rewardCommands);
                    }
                    if (minPlayersToReward != null) {
                        pathValueMap.put("min-players-to-reward", minPlayersToReward);
                    }
                    return pathValueMap;
                }
            };
        }

        /**
         * Migrate the data from the arena
         *
         * @param arena the arena
         */
        public void migrate(SimpleGameArena arena) {
            SimpleRewardFeature rewardFeature = arena.getFeature(SimpleRewardFeature.class);
            rewardCommands = new LinkedHashMap<>();
            rewardCommands.put("default", rewardFeature.getDefaultCommands());
            for (Map.Entry<Integer, List<String>> entry : rewardFeature.getTopCommands().entrySet()) {
                rewardCommands.put(Integer.toString(entry.getKey()), entry.getValue());
            }
            minPlayersToReward = rewardFeature.getMinPlayersToReward();
        }
    }
}
