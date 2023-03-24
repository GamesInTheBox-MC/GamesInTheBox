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
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * The simple {@link GameEditor} for {@link SimpleGame}
 */
public class SimpleGameEditor extends SimpleGameAction implements GameEditor {
    protected final List<SimpleEditorStatus> editorStatusList;
    protected final SimpleGame game;

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
        Map<String, SimpleAction> actionMap = new LinkedHashMap<>();
        return actionMap;
    }

    /**
     * Get the list of {@link SimpleEditorStatus}.
     * Override this method to add more {@link SimpleEditorStatus}.
     *
     * @return the list of {@link SimpleEditorStatus}
     */
    protected List<SimpleEditorStatus> getEditorStatusList() {
        List<SimpleEditorStatus> editorStatusList = new ArrayList<>();
        return editorStatusList;
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
