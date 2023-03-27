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
package me.hsgamer.gamesinthebox.game.simple.action;

import me.hsgamer.gamesinthebox.game.simple.SimpleGameAction;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@link me.hsgamer.gamesinthebox.game.simple.SimpleGameAction.SimpleAction} that handles number as the first argument
 */
public abstract class NumberAction implements SimpleGameAction.SimpleAction {
    /**
     * Perform the action
     *
     * @param sender the sender
     * @param number the number
     * @param args   the remaining arguments
     * @return true if the action is performed successfully
     */
    protected abstract boolean performAction(@NotNull CommandSender sender, @NotNull Number number, String... args);

    /**
     * Get the number arguments
     *
     * @param sender the sender
     * @return the number arguments
     */
    @NotNull
    protected abstract List<Number> getNumberArgs(@NotNull CommandSender sender);

    /**
     * Get the additional arguments
     *
     * @param sender the sender
     * @param args   the remaining arguments
     * @return the additional arguments
     */
    @NotNull
    protected List<String> getAdditionalArgs(@NotNull CommandSender sender, String... args) {
        return Collections.emptyList();
    }

    /**
     * Send the "invalid number" message
     *
     * @param sender the sender
     */
    protected void sendInvalidNumberMessage(@NotNull CommandSender sender) {
        MessageUtils.sendMessage(sender, "&cInvalid number");
    }

    @Override
    public @NotNull String getArgsUsage() {
        return "<number>";
    }

    @Override
    public boolean performAction(@NotNull CommandSender sender, @NotNull String... args) {
        if (args.length < 1) {
            return false;
        }
        try {
            return performAction(sender, Double.parseDouble(args[0]), Arrays.copyOfRange(args, 1, args.length));
        } catch (NumberFormatException e) {
            sendInvalidNumberMessage(sender);
            return false;
        }
    }

    @Override
    public @NotNull List<@NotNull String> getActionArgs(@NotNull CommandSender sender, @NotNull String... args) {
        if (args.length == 1) {
            return getNumberArgs(sender).stream().map(Number::toString).collect(Collectors.toList());
        } else if (args.length > 1) {
            return getAdditionalArgs(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        return SimpleGameAction.SimpleAction.super.getActionArgs(sender, args);
    }
}
