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
import me.hsgamer.hscore.collections.map.CaseInsensitiveStringHashMap;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * The simple {@link PointFeature}.
 * It will get the settings from {@link GameConfigFeature} in the path {@code point}.
 * The format of the settings should be a list of pairs of point name and point value.
 * <pre>
 *     point:
 *       point-name: 1
 *       point-name-2: 2
 * </pre>
 */
public class SimplePointFeature extends PointFeature {
    private final SimpleGameArena arena;
    private final Map<String, PointValue> pointValuesMap;
    private final Map<String, Integer> pointMap;

    /**
     * Create a new {@link SimplePointFeature}
     *
     * @param arena         the arena
     * @param pointConsumer the point consumer
     */
    public SimplePointFeature(@NotNull SimpleGameArena arena, @NotNull PointConsumer pointConsumer, @NotNull List<PointValue> pointValuesMap) {
        super(pointConsumer);
        this.arena = arena;
        this.pointValuesMap = pointValuesMap.stream().collect(Collectors.toMap(pointValue -> pointValue.name, pointValue -> pointValue, (o, n) -> o, CaseInsensitiveStringHashMap::new));
        this.pointMap = new CaseInsensitiveStringHashMap<>();
    }

    /**
     * Get the editor of the feature
     *
     * @param pointValues the point values
     * @return the editor
     */
    public static Editor editor(PointValue... pointValues) {
        return new Editor(Arrays.asList(pointValues));
    }

    /**
     * Get the editor of the feature
     *
     * @param pointValues the point values
     * @return the editor
     */
    public static Editor editor(List<PointValue> pointValues) {
        return new Editor(pointValues);
    }

    @Override
    public void postInit() {
        super.postInit();

        GameConfigFeature gameConfigFeature = arena.getFeature(GameConfigFeature.class);
        gameConfigFeature.getValues("point", false).forEach((key, value) -> Optional.of(value)
                .map(Objects::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .ifPresent(point -> pointMap.put(key, point)));
    }

    /**
     * Apply the point to the player
     *
     * @param uuid          the uuid of the player
     * @param pointValue    the point value
     * @param pointModifier the point modifier
     */
    public void applyPoint(@NotNull UUID uuid, @NotNull PointValue pointValue, @NotNull UnaryOperator<Integer> pointModifier) {
        int point = pointModifier.apply(
                pointValue.pointOperator.apply(
                        pointMap.getOrDefault(pointValue.name, pointValue.defaultPoint)
                )
        );
        applyPoint(uuid, point);
    }

    /**
     * Apply the point to the players
     *
     * @param uuids         the uuids of the players
     * @param pointValue    the point value
     * @param pointModifier the point modifier
     */
    public void applyPoint(@NotNull List<@NotNull UUID> uuids, @NotNull PointValue pointValue, @NotNull UnaryOperator<Integer> pointModifier) {
        int point = pointModifier.apply(
                pointValue.pointOperator.apply(
                        pointMap.getOrDefault(pointValue.name, pointValue.defaultPoint)
                )
        );
        for (UUID uuid : uuids) {
            applyPoint(uuid, point);
        }
    }

    /**
     * Apply the point to the player
     *
     * @param uuid       the uuid of the player
     * @param pointValue the point value
     */
    public void applyPoint(@NotNull UUID uuid, @NotNull PointValue pointValue) {
        applyPoint(uuid, pointValue, UnaryOperator.identity());
    }

    /**
     * Apply the point to the players
     *
     * @param uuids      the uuids of the players
     * @param pointValue the point value
     */
    public void applyPoint(@NotNull List<@NotNull UUID> uuids, @NotNull PointValue pointValue) {
        applyPoint(uuids, pointValue, UnaryOperator.identity());
    }

    /**
     * Get the point value
     *
     * @param name the name of the point
     * @return the point value
     */
    public Optional<Integer> getPoint(@NotNull String name) {
        PointValue pointValue = pointValuesMap.get(name);
        if (pointValue != null) {
            return Optional.of(pointMap.getOrDefault(pointValue.name, pointValue.defaultPoint)).map(pointValue.displayOperator);
        }
        return Optional.ofNullable(pointMap.get(name));
    }

    /**
     * The class to store the point value
     */
    public static class PointValue {
        /**
         * The name of the point
         */
        public final String name;
        /**
         * The default point
         */
        public final int defaultPoint;
        /**
         * The operator to display the point
         */
        private final UnaryOperator<Integer> displayOperator;
        /**
         * The operator to give the final point to apply
         */
        private final UnaryOperator<Integer> pointOperator;

        /**
         * Create a new {@link PointValue}
         *
         * @param name            the name of the point
         * @param defaultPoint    the default point
         * @param displayOperator the operator to display the point
         * @param pointOperator   the operator to give the final point to apply
         */
        public PointValue(String name, int defaultPoint, UnaryOperator<Integer> displayOperator, UnaryOperator<Integer> pointOperator) {
            this.name = name;
            this.defaultPoint = defaultPoint;
            this.displayOperator = displayOperator;
            this.pointOperator = pointOperator;

        }

        /**
         * Create a new {@link PointValue}
         *
         * @param name         the name of the point
         * @param defaultPoint the default point
         */
        public PointValue(String name, int defaultPoint) {
            this(name, defaultPoint, UnaryOperator.identity(), UnaryOperator.identity());
        }

        /**
         * Create a new {@link PointValue}, with the display operator being {@link Math#abs(int)}
         *
         * @param name            the name of the point
         * @param defaultPoint    the default point
         * @param isFinalNegative whether the final point is negative. Set to {@code false} to make the point always positive.
         */
        public PointValue(String name, int defaultPoint, boolean isFinalNegative) {
            this(name, defaultPoint, Math::abs, integer -> {
                int abs = Math.abs(integer);
                return isFinalNegative ? -abs : abs;
            });
        }
    }

    /**
     * The editor of the feature
     */
    public static class Editor {
        private final List<PointValue> pointValues = new ArrayList<>();
        private final Map<String, Integer> pointMap = new CaseInsensitiveStringHashMap<>();

        private Editor(List<PointValue> pointValues) {
            this.pointValues.addAll(pointValues);
        }

        /**
         * Get the actions of the editor
         *
         * @return the actions
         */
        public Map<String, SimpleGameAction.SimpleAction> getActions() {
            Map<String, SimpleGameAction.SimpleAction> map = new LinkedHashMap<>();

            map.put("set-point", new NumberAction() {
                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number value, String... args) {
                    if (args.length == 0) {
                        MessageUtils.sendMessage(sender, "&cPlease specify the point value");
                        return false;
                    }
                    for (String arg : args) {
                        pointMap.put(arg, value.intValue());
                    }
                    return true;
                }

                @Override
                protected @NotNull List<String> getAdditionalArgs(@NotNull CommandSender sender, String... args) {
                    return pointValues.stream().map(pointValue -> pointValue.name).collect(Collectors.toList());
                }

                @Override
                protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
                    return Arrays.asList("1", "2", "3", "4", "5");
                }

                @Override
                public @NotNull String getDescription() {
                    return "Set the point of the point value";
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
                    pointValues.forEach(pointValue -> {
                        int point = pointValue.displayOperator.apply(
                                pointMap.getOrDefault(pointValue.name, pointValue.defaultPoint)
                        );
                        MessageUtils.sendMessage(sender, "&6" + pointValue.name + ": &e" + point);
                    });
                }

                @Override
                public void reset(@NotNull CommandSender sender) {
                    pointMap.clear();
                }

                @Override
                public boolean canSave(@NotNull CommandSender sender) {
                    return true;
                }

                @Override
                public Map<String, Object> toPathValueMap(@NotNull CommandSender sender) {
                    Map<String, Object> pathValueMap = new LinkedHashMap<>();
                    pointValues.forEach(pointValue -> {
                        int point = pointMap.getOrDefault(pointValue.name, pointValue.defaultPoint);
                        pathValueMap.put("point." + pointValue.name, point);
                    });
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
            if (pointFeature != null) {
                pointValues.forEach(pointValue -> pointMap.put(pointValue.name, pointFeature.getPoint(pointValue.name).orElse(pointValue.defaultPoint)));
            }
        }
    }
}
