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

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.game.GameEditor;
import me.hsgamer.gamesinthebox.game.simple.feature.DescriptiveHologramFeature;
import me.hsgamer.gamesinthebox.game.simple.feature.SimplePointFeature;
import me.hsgamer.gamesinthebox.game.simple.feature.SimpleRewardFeature;
import me.hsgamer.gamesinthebox.util.LocationUtil;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Pair;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The simple {@link GameEditor} for {@link SimpleGame}
 */
public class SimpleGameEditor extends SimpleGameAction implements GameEditor {
    protected final SimpleGame game;

    protected Integer pointPlus;
    protected Integer pointMinus;
    protected Integer maxPlayersToAddPoint;
    protected Map<String, List<String>> rewardCommands;
    protected Integer minPlayersToReward;
    protected List<Pair<Location, List<String>>> hologramList;

    private List<SimpleEditorStatus> editorStatusList;

    /**
     * Create a new {@link SimpleGameEditor}
     *
     * @param game the {@link SimpleGame}
     */
    public SimpleGameEditor(SimpleGame game) {
        this.game = game;
    }

    @Override
    public void reset(CommandSender sender) {
        for (SimpleEditorStatus editorStatus : getEditorStatusList()) {
            editorStatus.reset(sender);
        }
    }

    @Override
    public void sendStatus(CommandSender sender) {
        for (SimpleEditorStatus editorStatus : getEditorStatusList()) {
            editorStatus.sendStatus(sender);
        }
    }

    @Override
    public Optional<Map<String, Object>> exportPathValueMap(CommandSender sender) {
        Map<String, Object> pathValueMap = new LinkedHashMap<>();
        for (SimpleEditorStatus editorStatus : getEditorStatusList()) {
            if (!editorStatus.canSave(sender)) {
                return Optional.empty();
            }
            pathValueMap.putAll(editorStatus.toPathValueMap(sender));
        }
        return Optional.of(pathValueMap);
    }

    @Override
    protected Map<String, SimpleAction> createActionMap() {
        Map<String, SimpleAction> map = new LinkedHashMap<>();

        // POINTS
        map.put("set-point-plus", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Set the point to add to the player";
            }

            @Override
            public String getArgsUsage() {
                return "<point>";
            }

            @Override
            public List<String> getActionArgs(CommandSender sender, String... args) {
                if (args.length == 1) {
                    return Arrays.asList("1", "2", "3", "4", "5");
                }
                return SimpleAction.super.getActionArgs(sender, args);
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                if (args.length < 1) {
                    return false;
                }
                try {
                    int point = Integer.parseInt(args[0]);
                    pointPlus = Math.abs(point);
                    return true;
                } catch (NumberFormatException e) {
                    MessageUtils.sendMessage(sender, "&cInvalid number");
                    return false;
                }
            }
        });
        map.put("set-point-minus", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Set the point to remove from the player";
            }

            @Override
            public String getArgsUsage() {
                return "<point>";
            }

            @Override
            public List<String> getActionArgs(CommandSender sender, String... args) {
                if (args.length == 1) {
                    return Arrays.asList("1", "2", "3", "4", "5");
                }
                return SimpleAction.super.getActionArgs(sender, args);
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                if (args.length < 1) {
                    return false;
                }
                try {
                    int point = Integer.parseInt(args[0]);
                    pointMinus = Math.abs(point);
                    return true;
                } catch (NumberFormatException e) {
                    MessageUtils.sendMessage(sender, "&cInvalid number");
                    return false;
                }
            }
        });
        map.put("set-max-players-to-add-point", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Set the maximum players to add point";
            }

            @Override
            public String getArgsUsage() {
                return "<max players>";
            }

            @Override
            public List<String> getActionArgs(CommandSender sender, String... args) {
                if (args.length == 1) {
                    return Arrays.asList("1", "2", "3", "4", "5");
                }
                return SimpleAction.super.getActionArgs(sender, args);
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                if (args.length < 1) {
                    return false;
                }
                try {
                    int players = Integer.parseInt(args[0]);
                    if (players >= 0) {
                        maxPlayersToAddPoint = players;
                    } else {
                        maxPlayersToAddPoint = null;
                    }
                    return true;
                } catch (NumberFormatException e) {
                    MessageUtils.sendMessage(sender, "&cInvalid number");
                    return false;
                }
            }
        });

        // REWARD
        map.put("add-reward", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Add a reward command";
            }

            @Override
            public String getArgsUsage() {
                return "<top> <command>";
            }

            @Override
            public List<String> getActionArgs(CommandSender sender, String... args) {
                if (args.length == 1) {
                    return Arrays.asList("all", "1", "2", "3", "4", "5");
                }
                return SimpleAction.super.getActionArgs(sender, args);
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                if (args.length < 2) {
                    return false;
                }
                String top = args[0];
                String command = StringUtils.join(args, " ", 1, args.length);

                if (rewardCommands == null) {
                    rewardCommands = new LinkedHashMap<>();
                }

                rewardCommands.computeIfAbsent(top, k -> new ArrayList<>()).add(command);
                return true;
            }
        });
        map.put("clear-reward", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Clear all reward commands of a top position";
            }

            @Override
            public String getArgsUsage() {
                return "<top>";
            }

            @Override
            public List<String> getActionArgs(CommandSender sender, String... args) {
                if (args.length == 1) {
                    return Arrays.asList("all", "1", "2", "3", "4", "5");
                }
                return SimpleAction.super.getActionArgs(sender, args);
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                if (args.length < 1) {
                    return false;
                }
                String top = args[0];

                if (rewardCommands != null) {
                    rewardCommands.remove(top);
                }
                return true;
            }
        });
        map.put("clear-all-reward", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Clear all reward commands";
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                rewardCommands = null;
                return true;
            }
        });
        map.put("set-min-players-to-reward", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Set the minimum players to reward";
            }

            @Override
            public String getArgsUsage() {
                return "<min players>";
            }

            @Override
            public List<String> getActionArgs(CommandSender sender, String... args) {
                if (args.length == 1) {
                    return Arrays.asList("1", "2", "3", "4", "5");
                }
                return SimpleAction.super.getActionArgs(sender, args);
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                if (args.length < 1) {
                    return false;
                }
                try {
                    int players = Integer.parseInt(args[0]);
                    if (players >= 0) {
                        minPlayersToReward = players;
                    } else {
                        minPlayersToReward = null;
                    }
                    return true;
                } catch (NumberFormatException e) {
                    MessageUtils.sendMessage(sender, "&cInvalid number");
                    return false;
                }
            }
        });

        // HOLOGRAM
        map.put("new-hologram", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Create a new hologram at your location";
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                if (!(sender instanceof Player)) {
                    MessageUtils.sendMessage(sender, "&cOnly players can use this command");
                    return false;
                }

                if (hologramList == null) {
                    hologramList = new ArrayList<>();
                }

                Player player = (Player) sender;
                Location location = player.getLocation();

                hologramList.add(Pair.of(location, new ArrayList<>()));
                MessageUtils.sendMessage(sender, "&aThe hologram has been created. The index is " + (hologramList.size() - 1));
                return true;
            }
        });
        map.put("set-hologram-location", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Set the location of a hologram";
            }

            @Override
            public String getArgsUsage() {
                return "<index>";
            }

            @Override
            public List<String> getActionArgs(CommandSender sender, String... args) {
                if (args.length == 1) {
                    if (hologramList == null) {
                        return Collections.emptyList();
                    }
                    return IntStream.range(0, hologramList.size()).mapToObj(Integer::toString).collect(Collectors.toList());
                }
                return SimpleAction.super.getActionArgs(sender, args);
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                if (args.length < 1) {
                    return false;
                }
                try {
                    int index = Integer.parseInt(args[0]);
                    if (index < 0 || index >= hologramList.size()) {
                        MessageUtils.sendMessage(sender, "&cInvalid index");
                        return false;
                    }

                    if (!(sender instanceof Player)) {
                        MessageUtils.sendMessage(sender, "&cOnly players can use this command");
                        return false;
                    }

                    Player player = (Player) sender;
                    Location location = player.getLocation();

                    hologramList.set(index, Pair.of(location, hologramList.get(index).getValue()));
                    MessageUtils.sendMessage(sender, "&aThe hologram has been updated");
                    return true;
                } catch (NumberFormatException e) {
                    MessageUtils.sendMessage(sender, "&cInvalid number");
                    return false;
                }
            }
        });
        map.put("add-hologram-line", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Add a line to a hologram";
            }

            @Override
            public String getArgsUsage() {
                return "<index> <line>";
            }

            @Override
            public List<String> getActionArgs(CommandSender sender, String... args) {
                if (args.length == 1) {
                    if (hologramList == null) {
                        return Collections.emptyList();
                    }
                    return IntStream.range(0, hologramList.size()).mapToObj(Integer::toString).collect(Collectors.toList());
                }
                return SimpleAction.super.getActionArgs(sender, args);
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                if (args.length < 2) {
                    return false;
                }
                try {
                    int index = Integer.parseInt(args[0]);
                    String line = StringUtils.join(args, " ", 1, args.length);

                    if (hologramList == null) {
                        hologramList = new ArrayList<>();
                    }

                    if (index < 0 || index >= hologramList.size()) {
                        MessageUtils.sendMessage(sender, "&cInvalid index");
                        return false;
                    }

                    hologramList.get(index).getValue().add(line);
                    return true;
                } catch (NumberFormatException e) {
                    MessageUtils.sendMessage(sender, "&cInvalid number");
                    return false;
                }
            }
        });
        map.put("clear-hologram-line", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Clear all lines of a hologram";
            }

            @Override
            public String getArgsUsage() {
                return "<index>";
            }

            @Override
            public List<String> getActionArgs(CommandSender sender, String... args) {
                if (args.length == 1) {
                    if (hologramList == null) {
                        return Collections.emptyList();
                    }
                    return IntStream.range(0, hologramList.size()).mapToObj(Integer::toString).collect(Collectors.toList());
                }
                return SimpleAction.super.getActionArgs(sender, args);
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                if (args.length < 1) {
                    return false;
                }
                try {
                    int index = Integer.parseInt(args[0]);

                    if (hologramList == null) {
                        hologramList = new ArrayList<>();
                    }

                    if (index < 0 || index >= hologramList.size()) {
                        MessageUtils.sendMessage(sender, "&cInvalid index");
                        return false;
                    }

                    hologramList.get(index).getValue().clear();
                    return true;
                } catch (NumberFormatException e) {
                    MessageUtils.sendMessage(sender, "&cInvalid number");
                    return false;
                }
            }
        });
        map.put("remove-hologram", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Remove a hologram";
            }

            @Override
            public String getArgsUsage() {
                return "<index>";
            }

            @Override
            public List<String> getActionArgs(CommandSender sender, String... args) {
                if (args.length == 1) {
                    if (hologramList == null) {
                        return Collections.emptyList();
                    }
                    return IntStream.range(0, hologramList.size()).mapToObj(Integer::toString).collect(Collectors.toList());
                }
                return SimpleAction.super.getActionArgs(sender, args);
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                if (args.length < 1) {
                    return false;
                }
                try {
                    int index = Integer.parseInt(args[0]);

                    if (hologramList == null) {
                        hologramList = new ArrayList<>();
                    }

                    if (index < 0 || index >= hologramList.size()) {
                        MessageUtils.sendMessage(sender, "&cInvalid index");
                        return false;
                    }

                    hologramList.remove(index);
                    return true;
                } catch (NumberFormatException e) {
                    MessageUtils.sendMessage(sender, "&cInvalid number");
                    return false;
                }
            }
        });
        map.put("clear-all-hologram", new SimpleAction() {
            @Override
            public String getDescription() {
                return "Clear all holograms";
            }

            @Override
            public boolean performAction(CommandSender sender, String... args) {
                if (hologramList != null) {
                    hologramList.clear();
                }
                return true;
            }
        });

        return map;
    }

    /**
     * Create the list of {@link SimpleEditorStatus}.
     * Override this method to add more {@link SimpleEditorStatus}.
     *
     * @return the list of {@link SimpleEditorStatus}
     */
    protected List<SimpleEditorStatus> createEditorStatusList() {
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

    private List<SimpleEditorStatus> getEditorStatusList() {
        if (editorStatusList == null) {
            editorStatusList = createEditorStatusList();
        }
        return editorStatusList;
    }

    @Override
    public boolean migrate(CommandSender sender, GameArena gameArena) {
        if (!(gameArena instanceof SimpleGameArena)) {
            return false;
        }
        SimpleGameArena simpleGameArena = (SimpleGameArena) gameArena;

        // POINTS
        SimplePointFeature pointFeature = simpleGameArena.getFeature(SimplePointFeature.class);
        pointPlus = pointFeature.getPointPlus();
        pointMinus = pointFeature.getPointMinus();
        maxPlayersToAddPoint = pointFeature.getMaxPlayersToAdd();

        // REWARD
        SimpleRewardFeature rewardFeature = simpleGameArena.getFeature(SimpleRewardFeature.class);
        rewardCommands = new LinkedHashMap<>();
        rewardCommands.put("default", rewardFeature.getDefaultCommands());
        for (Map.Entry<Integer, List<String>> entry : rewardFeature.getTopCommands().entrySet()) {
            rewardCommands.put(Integer.toString(entry.getKey()), entry.getValue());
        }

        // HOLOGRAM
        DescriptiveHologramFeature hologramFeature = simpleGameArena.getFeature(DescriptiveHologramFeature.class);
        hologramList = new ArrayList<>();
        for (DescriptiveHologramFeature.HologramUpdater hologramUpdater : hologramFeature.getHologramUpdaters()) {
            hologramList.add(Pair.of(hologramUpdater.hologram.getLocation(), hologramUpdater.rawLines));
        }

        return true;
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
