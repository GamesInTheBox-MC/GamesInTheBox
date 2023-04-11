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

import me.hsgamer.gamesinthebox.game.feature.BoundingFeature;
import me.hsgamer.gamesinthebox.game.feature.BoundingOffsetFeature;
import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameAction;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameEditor;
import me.hsgamer.gamesinthebox.game.simple.action.NumberAction;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The simple {@link BoundingOffsetFeature}.
 * It will get the offset setting from {@link GameConfigFeature} in the specified path.
 * The value of the path should be like this:
 * <pre>
 *     [path]:
 *       min-x: 0.0
 *       max-x: 0.0
 *       min-y: 0.0
 *       max-y: 0.0
 *       min-z: 0.0
 *       max-z: 0.0
 * </pre>
 */
public class SimpleBoundingOffsetFeature extends BoundingOffsetFeature {
    private final SimpleGameArena arena;
    private final String path;

    /**
     * Create a new {@link SimpleBoundingOffsetFeature}
     *
     * @param arena           the arena
     * @param boundingFeature the bounding feature
     * @param path            the path to the offset setting
     */
    public SimpleBoundingOffsetFeature(SimpleGameArena arena, BoundingFeature boundingFeature, String path) {
        super(boundingFeature);
        this.arena = arena;
        this.path = path;
    }

    /**
     * Create a new {@link SimpleBoundingOffsetFeature} that will get the offset setting from the config in the path "box-offset"
     *
     * @param arena           the arena
     * @param boundingFeature the bounding feature
     */
    public SimpleBoundingOffsetFeature(SimpleGameArena arena, BoundingFeature boundingFeature) {
        this(arena, boundingFeature, "box-offset");
    }

    /**
     * Create a new {@link Editor} to edit the bounding offset
     *
     * @param path       the path
     * @param editorName the name of the editor
     * @param actionName the name of the action
     * @return the editor
     */
    public static Editor editor(String path, String editorName, String actionName) {
        return new Editor(path, editorName, actionName);
    }

    /**
     * Create a new {@link Editor} to edit the bounding offset in the path "box-offset"
     *
     * @return the editor
     */
    public static Editor editor() {
        return new Editor("box-offset", "Bounding Offset", "");
    }

    @Override
    protected @NotNull OffsetSetting createOffsetSetting() {
        GameConfigFeature configFeature = arena.getFeature(GameConfigFeature.class);
        double minX = Optional.ofNullable(configFeature.getString(path + ".min-x"))
                .flatMap(Validate::getNumber)
                .map(Number::doubleValue)
                .orElse(0.0);
        double maxX = Optional.ofNullable(configFeature.getString(path + ".max-x"))
                .flatMap(Validate::getNumber)
                .map(Number::doubleValue)
                .orElse(0.0);
        double minY = Optional.ofNullable(configFeature.getString(path + ".min-y"))
                .flatMap(Validate::getNumber)
                .map(Number::doubleValue)
                .orElse(0.0);
        double maxY = Optional.ofNullable(configFeature.getString(path + ".max-y"))
                .flatMap(Validate::getNumber)
                .map(Number::doubleValue)
                .orElse(0.0);
        double minZ = Optional.ofNullable(configFeature.getString(path + ".min-z"))
                .flatMap(Validate::getNumber)
                .map(Number::doubleValue)
                .orElse(0.0);
        double maxZ = Optional.ofNullable(configFeature.getString(path + ".max-z"))
                .flatMap(Validate::getNumber)
                .map(Number::doubleValue)
                .orElse(0.0);
        return new OffsetSetting(minX, maxX, minY, maxY, minZ, maxZ);
    }

    /**
     * The editor for {@link SimpleBoundingOffsetFeature}
     */
    public static class Editor {
        private final String path;
        private final String editorName;
        private final String actionName;
        private final AtomicReference<OffsetSetting> offsetSettingRef = new AtomicReference<>(OffsetSetting.DEFAULT);

        private Editor(String path, String editorName, String actionName) {
            this.path = path;
            this.editorName = editorName;
            this.actionName = actionName;
        }

        /**
         * Get the status of the editor to be added to {@link SimpleGameEditor}
         *
         * @return the status
         */
        public SimpleGameEditor.SimpleEditorStatus getStatus() {
            return new SimpleGameEditor.SimpleEditorStatus() {
                @Override
                public void sendStatus(@NotNull CommandSender sender) {
                    OffsetSetting offsetSetting = offsetSettingRef.get();
                    MessageUtils.sendMessage(sender, "&6&l" + editorName.toUpperCase(Locale.ROOT));
                    MessageUtils.sendMessage(sender, "&6Min X: &f" + offsetSetting.minXOffset);
                    MessageUtils.sendMessage(sender, "&6Max X: &f" + offsetSetting.maxXOffset);
                    MessageUtils.sendMessage(sender, "&6Min Y: &f" + offsetSetting.minYOffset);
                    MessageUtils.sendMessage(sender, "&6Max Y: &f" + offsetSetting.maxYOffset);
                    MessageUtils.sendMessage(sender, "&6Min Z: &f" + offsetSetting.minZOffset);
                    MessageUtils.sendMessage(sender, "&6Max Z: &f" + offsetSetting.maxZOffset);
                }

                @Override
                public void reset(@NotNull CommandSender sender) {
                    offsetSettingRef.set(OffsetSetting.DEFAULT);
                }

                @Override
                public boolean canSave(@NotNull CommandSender sender) {
                    return true;
                }

                @Override
                public Map<String, Object> toPathValueMap(@NotNull CommandSender sender) {
                    OffsetSetting offsetSetting = offsetSettingRef.get();
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put(path + ".min-x", offsetSetting.minXOffset);
                    map.put(path + ".max-x", offsetSetting.maxXOffset);
                    map.put(path + ".min-y", offsetSetting.minYOffset);
                    map.put(path + ".max-y", offsetSetting.maxYOffset);
                    map.put(path + ".min-z", offsetSetting.minZOffset);
                    map.put(path + ".max-z", offsetSetting.maxZOffset);
                    return map;
                }
            };
        }

        /**
         * Get the actions of the editor to be added to {@link SimpleGameEditor}
         *
         * @return the actions
         */
        public Map<String, SimpleGameAction.SimpleAction> getActions() {
            Map<String, SimpleGameAction.SimpleAction> map = new LinkedHashMap<>();

            String minXName = actionName.isEmpty() ? "set-min-x" : actionName + "-set-min-x";
            String maxXName = actionName.isEmpty() ? "set-max-x" : actionName + "-set-max-x";
            String minYName = actionName.isEmpty() ? "set-min-y" : actionName + "-set-min-y";
            String maxYName = actionName.isEmpty() ? "set-max-y" : actionName + "-set-max-y";
            String minZName = actionName.isEmpty() ? "set-min-z" : actionName + "-set-min-z";
            String maxZName = actionName.isEmpty() ? "set-max-z" : actionName + "-set-max-z";

            map.put(minXName, new NumberAction() {
                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number value, String... args) {
                    offsetSettingRef.set(offsetSettingRef.get().withMinXOffset(value.doubleValue()));
                    return true;
                }

                @Override
                public @NotNull String getDescription() {
                    return "Set the min x offset";
                }
            });
            map.put(maxXName, new NumberAction() {
                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number value, String... args) {
                    offsetSettingRef.set(offsetSettingRef.get().withMaxXOffset(value.doubleValue()));
                    return true;
                }

                @Override
                public @NotNull String getDescription() {
                    return "Set the max x offset";
                }
            });
            map.put(minYName, new NumberAction() {
                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number value, String... args) {
                    offsetSettingRef.set(offsetSettingRef.get().withMinYOffset(value.doubleValue()));
                    return true;
                }

                @Override
                public @NotNull String getDescription() {
                    return "Set the min y offset";
                }
            });
            map.put(maxYName, new NumberAction() {
                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number value, String... args) {
                    offsetSettingRef.set(offsetSettingRef.get().withMaxYOffset(value.doubleValue()));
                    return true;
                }

                @Override
                public @NotNull String getDescription() {
                    return "Set the max y offset";
                }
            });
            map.put(minZName, new NumberAction() {
                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number value, String... args) {
                    offsetSettingRef.set(offsetSettingRef.get().withMinZOffset(value.doubleValue()));
                    return true;
                }

                @Override
                public @NotNull String getDescription() {
                    return "Set the min z offset";
                }
            });
            map.put(maxZName, new NumberAction() {
                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number value, String... args) {
                    offsetSettingRef.set(offsetSettingRef.get().withMaxZOffset(value.doubleValue()));
                    return true;
                }

                @Override
                public @NotNull String getDescription() {
                    return "Set the max z offset";
                }
            });

            return map;
        }

        /**
         * Migrate the editor from a {@link BoundingOffsetFeature}
         *
         * @param feature the feature
         */
        public void migrate(BoundingOffsetFeature feature) {
            offsetSettingRef.set(feature.getOffsetSetting());
        }
    }
}
