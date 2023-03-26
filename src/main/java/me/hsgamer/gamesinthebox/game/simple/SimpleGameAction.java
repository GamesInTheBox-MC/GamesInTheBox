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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The simple {@link GameAction} that contains a map of actions
 */
public abstract class SimpleGameAction implements GameAction {
    private Map<String, SimpleAction> actionMap;

    /**
     * Create the action map.
     * This can be overridden to add more actions.
     *
     * @return the action map
     */
    @NotNull
    protected abstract Map<@NotNull String, @NotNull SimpleAction> createActionMap();

    private Map<String, SimpleAction> getActionMap() {
        if (actionMap == null) {
            actionMap = createActionMap();
        }
        return actionMap;
    }

    @Override
    public @NotNull List<@NotNull String> getActions() {
        return new ArrayList<>(getActionMap().keySet());
    }

    @Override
    public @NotNull List<@NotNull String> getActionArgs(@NotNull CommandSender sender, @NotNull String action, @NotNull String @NotNull ... args) {
        SimpleAction simpleAction = getActionMap().get(action);
        if (simpleAction != null) {
            return simpleAction.getActionArgs(sender, args);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean performAction(@NotNull CommandSender sender, @NotNull String action, @NotNull String @NotNull ... args) {
        SimpleAction simpleAction = getActionMap().get(action);
        if (simpleAction != null) {
            return simpleAction.performAction(sender, args);
        }
        return false;
    }

    @Override
    public void sendUsage(@NotNull CommandSender sender) {
        MessageUtils.sendMessage(sender, "&6Usage:");
        getActionMap().forEach((key, value) -> {
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
        @NotNull
        String getDescription();

        /**
         * Get the arguments usage of the action
         *
         * @return the arguments usage
         */
        @NotNull
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
        @NotNull
        default List<@NotNull String> getActionArgs(@NotNull CommandSender sender, @NotNull String... args) {
            return Collections.emptyList();
        }

        /**
         * Perform the action
         *
         * @param sender the sender
         * @param args   the arguments
         */
        boolean performAction(@NotNull CommandSender sender, @NotNull String... args);
    }
}
