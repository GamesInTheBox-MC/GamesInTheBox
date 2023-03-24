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

import me.hsgamer.gamesinthebox.game.GameEditor;
import me.hsgamer.gamesinthebox.util.LocationUtil;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Pair;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * The simple {@link GameEditor} for {@link SimpleGame}
 */
public class SimpleGameEditor extends SimpleGameAction implements GameEditor {
    protected final List<SimpleEditorStatus> editorStatusList;
    protected final SimpleGame game;

    protected Integer pointPlus;
    protected Integer pointMinus;
    protected Integer maxPlayersToAddPoint;
    protected Map<String, List<String>> rewardCommands;
    protected Integer minPlayersToReward;
    protected List<Pair<Location, List<String>>> hologramList;

    /**
     * Create a new {@link SimpleGameEditor}
     *
     * @param game the {@link SimpleGame}
     */
    public SimpleGameEditor(SimpleGame game) {
        this.game = game;
        this.editorStatusList = getEditorStatusList();
    }

    @Override
    public void reset(CommandSender sender) {
        for (SimpleEditorStatus editorStatus : editorStatusList) {
            editorStatus.reset(sender);
        }
    }

    @Override
    public void sendStatus(CommandSender sender) {
        for (SimpleEditorStatus editorStatus : editorStatusList) {
            editorStatus.sendStatus(sender);
        }
    }

    @Override
    public Optional<Map<String, Object>> exportPathValueMap(CommandSender sender) {
        Map<String, Object> pathValueMap = new LinkedHashMap<>();
        for (SimpleEditorStatus editorStatus : editorStatusList) {
            if (!editorStatus.canSave(sender)) {
                return Optional.empty();
            }
            pathValueMap.putAll(editorStatus.toPathValueMap(sender));
        }
        return Optional.of(pathValueMap);
    }

    @Override
    protected Map<String, SimpleAction> getActionMap() {
        Map<String, SimpleAction> map = new LinkedHashMap<>();
        return map;
    }

    /**
     * Get the list of {@link SimpleEditorStatus}.
     * Override this method to add more {@link SimpleEditorStatus}.
     *
     * @return the list of {@link SimpleEditorStatus}
     */
    protected List<SimpleEditorStatus> getEditorStatusList() {
        List<SimpleEditorStatus> list = new ArrayList<>();

        // POINTS
        list.add(new SimpleEditorStatus() {
            @Override
            public void sendStatus(CommandSender sender) {
                MessageUtils.sendMessage(sender, "&6&lPOINTS");
                MessageUtils.sendMessage(sender, "&6Point Plus: &e" + (pointPlus == null ? "Default" : pointPlus));
                MessageUtils.sendMessage(sender, "&6Point Minus: &e" + (pointMinus == null ? "Default" : pointMinus));
                MessageUtils.sendMessage(sender, "&6Max Players to Add Point: &e" + (maxPlayersToAddPoint == null ? "Default" : maxPlayersToAddPoint));
            }

            @Override
            public void reset(CommandSender sender) {
                pointPlus = null;
                pointMinus = null;
                maxPlayersToAddPoint = null;
            }

            @Override
            public boolean canSave(CommandSender sender) {
                return true;
            }

            @Override
            public Map<String, Object> toPathValueMap(CommandSender sender) {
                Map<String, Object> pathValueMap = new LinkedHashMap<>();
                if (pointPlus != null) {
                    pathValueMap.put("points.plus", pointPlus);
                }
                if (pointMinus != null) {
                    pathValueMap.put("points.minus", pointMinus);
                }
                if (maxPlayersToAddPoint != null) {
                    pathValueMap.put("points.max-players-to-add", maxPlayersToAddPoint);
                }
                return pathValueMap;
            }
        });

        // REWARD
        list.add(new SimpleEditorStatus() {
            @Override
            public void sendStatus(CommandSender sender) {
                MessageUtils.sendMessage(sender, "&6&lREWARD");
                if (rewardCommands == null) {
                    MessageUtils.sendMessage(sender, "&6Reward Commands: &eEmpty");
                } else {
                    MessageUtils.sendMessage(sender, "&6Reward Commands:");
                    for (Map.Entry<String, List<String>> entry : rewardCommands.entrySet()) {
                        MessageUtils.sendMessage(sender, "&6- " + entry.getKey() + ":");
                        for (String command : entry.getValue()) {
                            MessageUtils.sendMessage(sender, "&6  - " + command);
                        }
                    }
                }
                MessageUtils.sendMessage(sender, "&6Min Players to Reward: &e" + (minPlayersToReward == null ? "Default" : minPlayersToReward));
            }

            @Override
            public void reset(CommandSender sender) {
                rewardCommands = null;
                minPlayersToReward = null;
            }

            @Override
            public boolean canSave(CommandSender sender) {
                return true;
            }

            @Override
            public Map<String, Object> toPathValueMap(CommandSender sender) {
                Map<String, Object> pathValueMap = new LinkedHashMap<>();
                if (rewardCommands != null && !rewardCommands.isEmpty()) {
                    pathValueMap.put("reward", rewardCommands);
                }
                if (minPlayersToReward != null) {
                    pathValueMap.put("min-players-to-reward", minPlayersToReward);
                }
                return pathValueMap;
            }
        });

        // HOLOGRAM
        list.add(new SimpleEditorStatus() {
            @Override
            public void sendStatus(CommandSender sender) {
                MessageUtils.sendMessage(sender, "&6&lHOLOGRAM");
                if (hologramList == null) {
                    MessageUtils.sendMessage(sender, "&6Hologram List: &eEmpty");
                } else {
                    MessageUtils.sendMessage(sender, "&6Hologram List:");
                    for (Pair<Location, List<String>> pair : hologramList) {
                        MessageUtils.sendMessage(sender, "&6- " + LocationUtil.serializeLocation(pair.getKey(), true, false) + ":");
                        for (String line : pair.getValue()) {
                            MessageUtils.sendMessage(sender, "&6  - " + line);
                        }
                    }
                }
            }

            @Override
            public void reset(CommandSender sender) {
                hologramList = null;
            }

            @Override
            public boolean canSave(CommandSender sender) {
                return true;
            }

            @Override
            public Map<String, Object> toPathValueMap(CommandSender sender) {
                Map<String, Object> pathValueMap = new LinkedHashMap<>();
                if (hologramList != null && !hologramList.isEmpty()) {
                    List<Map<String, Object>> hologramListMap = new ArrayList<>();
                    for (Pair<Location, List<String>> pair : hologramList) {
                        Map<String, Object> hologramMap = new LinkedHashMap<>();
                        hologramMap.put("location", LocationUtil.serializeLocation(pair.getKey(), true, false));
                        hologramMap.put("lines", pair.getValue());
                        hologramListMap.add(hologramMap);
                    }
                    pathValueMap.put("hologram", hologramListMap);
                }
                return pathValueMap;
            }
        });

        return list;
    }

    /**
     * The editor status for {@link SimpleGameEditor}
     */
    public interface SimpleEditorStatus {
        /**
         * Send the status to the {@link CommandSender}
         *
         * @param sender the {@link CommandSender}
         */
        void sendStatus(CommandSender sender);

        /**
         * Reset the status
         *
         * @param sender the {@link CommandSender}
         */
        void reset(CommandSender sender);

        /**
         * Check if the {@link CommandSender} can save the status
         *
         * @param sender the {@link CommandSender}
         * @return true if the {@link CommandSender} can save the status
         */
        boolean canSave(CommandSender sender);

        /**
         * Get the path-value map to be used to save the status
         *
         * @param sender the {@link CommandSender}
         * @return the path-value map
         */
        Map<String, Object> toPathValueMap(CommandSender sender);
    }
}
