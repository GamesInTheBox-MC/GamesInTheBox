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

import me.hsgamer.gamesinthebox.game.GameAction;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The simple {@link GameAction} that contains a map of actions
 */
public abstract class SimpleGameAction implements GameAction {
    private final Map<String, SimpleAction> actionMap;

    /**
     * Create a new {@link SimpleGameAction}
     */
    protected SimpleGameAction() {
        this.actionMap = getActionMap();
    }

    /**
     * Get the action map.
     * This can be overridden to add more actions.
     *
     * @return the action map
     */
    protected abstract Map<String, SimpleAction> getActionMap();

    @Override
    public List<String> getActions() {
        return new ArrayList<>(actionMap.keySet());
    }

    @Override
    public List<String> getActionArgs(CommandSender sender, String action, String... args) {
        SimpleAction simpleAction = actionMap.get(action);
        if (simpleAction != null) {
            return simpleAction.getActionArgs(sender, args);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean performAction(CommandSender sender, String action, String... args) {
        SimpleAction simpleAction = actionMap.get(action);
        if (simpleAction != null) {
            return simpleAction.performAction(sender, args);
        }
        return false;
    }

    @Override
    public void sendUsage(CommandSender sender) {
        MessageUtils.sendMessage(sender, "&6Usage:");
        actionMap.forEach((key, value) -> {
            MessageUtils.sendMessage(sender, "&6- &e" + key);
            MessageUtils.sendMessage(sender, "&6   &bArguments: &f" + value.getArgsUsage());
            MessageUtils.sendMessage(sender, "&6   &bDescription: &f" + value.getDescription());
        });
    }

    /**
     * The simple action of {@link SimpleGameAction} that contains the usage, action arguments, and the action itself
     */
    public interface SimpleAction {
        /**
         * Get the description of the action
         *
         * @return the description
         */
        String getDescription();

        /**
         * Get the arguments usage of the action
         *
         * @return the arguments usage
         */
        default String getArgsUsage() {
            return "";
        }

        /**
         * Get the action arguments
         *
         * @param sender the sender
         * @param args   the current arguments
         * @return the action arguments
         */
        default List<String> getActionArgs(CommandSender sender, String... args) {
            return Collections.emptyList();
        }

        /**
         * Perform the action
         *
         * @param sender the sender
         * @param args   the arguments
         */
        boolean performAction(CommandSender sender, String... args);
    }
}
