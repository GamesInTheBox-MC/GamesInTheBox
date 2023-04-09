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
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The simple {@link GameEditor} for {@link SimpleGame}
 */
public class SimpleGameEditor extends SimpleGameAction implements GameEditor {
    protected final SimpleGame game;
    private final SimplePointFeature.Editor simplePointFeatureEditor;
    private final DescriptiveHologramFeature.Editor descriptiveHologramFeatureEditor;
    private final SimpleRewardFeature.Editor simpleRewardFeatureEditor;

    private List<SimpleEditorStatus> editorStatusList;

    /**
     * Create a new {@link SimpleGameEditor}
     *
     * @param game the {@link SimpleGame}
     */
    public SimpleGameEditor(@NotNull SimpleGame game) {
        this.game = game;
        this.simplePointFeatureEditor = SimplePointFeature.editor(game.getPointValues());
        this.descriptiveHologramFeatureEditor = DescriptiveHologramFeature.editor();
        this.simpleRewardFeatureEditor = SimpleRewardFeature.editor();
    }

    @Override
    public void reset(@NotNull CommandSender sender) {
        for (SimpleEditorStatus editorStatus : getEditorStatusList()) {
            editorStatus.reset(sender);
        }
    }

    @Override
    public void sendStatus(@NotNull CommandSender sender) {
        for (SimpleEditorStatus editorStatus : getEditorStatusList()) {
            editorStatus.sendStatus(sender);
        }
    }

    @Override
    public @NotNull Optional<Map<String, Object>> exportPathValueMap(@NotNull CommandSender sender) {
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
    protected @NotNull Map<String, SimpleAction> createActionMap() {
        Map<String, SimpleAction> map = new LinkedHashMap<>();

        map.putAll(simplePointFeatureEditor.getActions());
        map.putAll(simpleRewardFeatureEditor.getActions());
        map.putAll(descriptiveHologramFeatureEditor.getActions());

        return map;
    }

    /**
     * Create the list of {@link SimpleEditorStatus}.
     * Override this method to add more {@link SimpleEditorStatus}.
     *
     * @return the list of {@link SimpleEditorStatus}
     */
    @NotNull
    protected List<@NotNull SimpleEditorStatus> createEditorStatusList() {
        List<SimpleEditorStatus> list = new ArrayList<>();

        list.add(simplePointFeatureEditor.getStatus());
        list.add(simpleRewardFeatureEditor.getStatus());
        list.add(descriptiveHologramFeatureEditor.getStatus());

        return list;
    }

    private List<SimpleEditorStatus> getEditorStatusList() {
        if (editorStatusList == null) {
            editorStatusList = createEditorStatusList();
        }
        return editorStatusList;
    }

    @Override
    public boolean migrate(@NotNull CommandSender sender, @NotNull GameArena gameArena) {
        if (!(gameArena instanceof SimpleGameArena)) {
            return false;
        }
        SimpleGameArena simpleGameArena = (SimpleGameArena) gameArena;

        simplePointFeatureEditor.migrate(simpleGameArena);
        simpleRewardFeatureEditor.migrate(simpleGameArena);
        descriptiveHologramFeatureEditor.migrate(simpleGameArena);

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
        void sendStatus(@NotNull CommandSender sender);

        /**
         * Reset the status
         *
         * @param sender the {@link CommandSender}
         */
        void reset(@NotNull CommandSender sender);

        /**
         * Check if the {@link CommandSender} can save the status
         *
         * @param sender the {@link CommandSender}
         * @return true if the {@link CommandSender} can save the status
         */
        boolean canSave(@NotNull CommandSender sender);

        /**
         * Get the path-value map to be used to save the status
         *
         * @param sender the {@link CommandSender}
         * @return the path-value map
         */
        Map<String, Object> toPathValueMap(@NotNull CommandSender sender);
    }
}
