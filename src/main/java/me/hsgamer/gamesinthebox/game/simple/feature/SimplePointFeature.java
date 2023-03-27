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
import me.hsgamer.gamesinthebox.game.feature.PointFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameAction;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameEditor;
import me.hsgamer.gamesinthebox.game.simple.action.NumberAction;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The simple {@link PointFeature}.
 * It will get the settings from {@link GameConfigFeature} in the path {@code point}.
 * The format of the settings should be like this:
 * <pre>
 *     point:
 *       plus: 1
 *       minus: 0
 *       max-players-to-add: -1
 * </pre>
 * The {@code plus} is the point to add. Default is 1.
 * The {@code minus} is the point to minus. Default is 0.
 * The {@code max-players-to-add} is the maximum players to add the point. Default is -1 (no limit).
 */
public class SimplePointFeature extends PointFeature {
    private final SimpleGameArena arena;
    private int pointPlus = 1;
    private int pointMinus = 0;
    private int maxPlayersToAdd = -1;

    /**
     * Create a new {@link SimplePointFeature}
     *
     * @param arena         the arena
     * @param pointConsumer the point consumer
     */
    public SimplePointFeature(@NotNull SimpleGameArena arena, @NotNull PointConsumer pointConsumer) {
        super(pointConsumer);
        this.arena = arena;
    }

    /**
     * Get the editor of the feature
     *
     * @return the editor
     */
    public static Editor editor() {
        return new Editor();
    }

    @Override
    public void postInit() {
        super.postInit();

        GameConfigFeature gameConfigFeature = arena.getFeature(GameConfigFeature.class);
        pointPlus = Optional.ofNullable(gameConfigFeature.getString("point.plus"))
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .orElse(pointPlus);
        pointMinus = Optional.ofNullable(gameConfigFeature.getString("point.minus"))
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .orElse(pointMinus);
        maxPlayersToAdd = Optional.ofNullable(gameConfigFeature.getString("point.max-players-to-add"))
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .orElse(maxPlayersToAdd);
    }

    /**
     * Add the point to the player
     *
     * @param uuid the uuid of the player
     */
    public void addPoint(@NotNull UUID uuid) {
        applyPoint(uuid, pointPlus);
    }

    /**
     * Add the point to the players
     *
     * @param uuids the uuids of the players
     */
    public void addPoint(@NotNull List<@NotNull UUID> uuids) {
        uuids.forEach(this::addPoint);
    }

    /**
     * Remove the point from the player
     *
     * @param uuid the uuid of the player
     */
    public void removePoint(@NotNull UUID uuid) {
        applyPoint(uuid, -pointMinus);
    }

    /**
     * Remove the point from the players
     *
     * @param uuids the uuids of the players
     */
    public void removePoint(@NotNull List<@NotNull UUID> uuids) {
        uuids.forEach(this::removePoint);
    }

    /**
     * Try to add the point to the players.
     * It will check if the number of players is less than {@link #getMaxPlayersToAdd()}.
     * If it is, it will add the point to all players.
     * Otherwise, it will do nothing.
     *
     * @param uuids the uuids of the players
     * @return true if it can add the point to the players
     */
    public boolean tryAddPoint(@NotNull List<@NotNull UUID> uuids) {
        if (maxPlayersToAdd >= 0 && uuids.size() > maxPlayersToAdd) {
            return false;
        }
        uuids.forEach(this::addPoint);
        return true;
    }

    /**
     * Get the point to add
     *
     * @return the point to add
     */
    public int getPointPlus() {
        return pointPlus;
    }

    /**
     * Get the point to minus
     *
     * @return the point to minus
     */
    public int getPointMinus() {
        return pointMinus;
    }

    /**
     * Get the maximum players to add the point
     *
     * @return the maximum players to add the point
     */
    public int getMaxPlayersToAdd() {
        return maxPlayersToAdd;
    }

    /**
     * The editor of the feature
     */
    public static class Editor {
        private Integer pointPlus;
        private Integer pointMinus;
        private Integer maxPlayersToAddPoint;

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

            map.put("set-point-plus", new NumberAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Set the point to add to the player";
                }

                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number number, String... args) {
                    pointPlus = Math.abs(number.intValue());
                    return true;
                }

                @Override
                protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
                    return Arrays.asList("1", "2", "3", "4", "5");
                }
            });
            map.put("set-point-minus", new NumberAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Set the point to remove from the player";
                }

                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number number, String... args) {
                    pointMinus = Math.abs(number.intValue());
                    return true;
                }

                @Override
                protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
                    return Arrays.asList("1", "2", "3", "4", "5");
                }
            });
            map.put("set-max-players-to-add-point", new NumberAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Set the maximum players to add point";
                }

                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number number, String... args) {
                    int players = number.intValue();
                    if (players >= 0) {
                        maxPlayersToAddPoint = players;
                    } else {
                        maxPlayersToAddPoint = null;
                    }
                    return true;
                }

                @Override
                protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
                    return Arrays.asList("1", "2", "3", "4", "5");
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
                    MessageUtils.sendMessage(sender, "&6&lPOINTS");
                    MessageUtils.sendMessage(sender, "&6Point Plus: &e" + (pointPlus == null ? "Default" : pointPlus));
                    MessageUtils.sendMessage(sender, "&6Point Minus: &e" + (pointMinus == null ? "Default" : pointMinus));
                    MessageUtils.sendMessage(sender, "&6Max Players to Add Point: &e" + (maxPlayersToAddPoint == null ? "Default" : maxPlayersToAddPoint));
                }

                @Override
                public void reset(@NotNull CommandSender sender) {
                    pointPlus = null;
                    pointMinus = null;
                    maxPlayersToAddPoint = null;
                }

                @Override
                public boolean canSave(@NotNull CommandSender sender) {
                    return true;
                }

                @Override
                public Map<String, Object> toPathValueMap(@NotNull CommandSender sender) {
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
            };
        }

        /**
         * Migrate the data from the arena
         *
         * @param arena the arena
         */
        public void migrate(SimpleGameArena arena) {
            SimplePointFeature pointFeature = arena.getFeature(SimplePointFeature.class);
            pointPlus = pointFeature.getPointPlus();
            pointMinus = pointFeature.getPointMinus();
            maxPlayersToAddPoint = pointFeature.getMaxPlayersToAdd();
        }
    }
}
