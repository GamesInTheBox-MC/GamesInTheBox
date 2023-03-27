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
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The {@link me.hsgamer.gamesinthebox.game.simple.SimpleGameAction.SimpleAction} that handles value as the first argument
 *
 * @param <T> the type of the value
 */
public abstract class ValueAction<T> implements SimpleGameAction.SimpleAction {
    /**
     * Perform the action
     *
     * @param sender the sender
     * @param value  the value
     * @param args   the remaining arguments
     * @return true if the action is performed successfully
     */
    protected abstract boolean performAction(@NotNull CommandSender sender, @NotNull T value, String... args);

    /**
     * Get the value argument count
     *
     * @return the value argument count
     */
    protected abstract int getValueArgCount();

    /**
     * Get the value arguments
     *
     * @param sender the sender
     * @param args   the value arguments
     * @return the value arguments
     */
    @NotNull
    protected List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
        return Collections.emptyList();
    }

    /**
     * Parse the value
     *
     * @param sender the sender
     * @param args   the value arguments
     * @return the parsed value
     */
    protected abstract Optional<T> parseValue(@NotNull CommandSender sender, String... args);

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

    @Override
    public boolean performAction(@NotNull CommandSender sender, @NotNull String... args) {
        if (args.length < getValueArgCount()) {
            return false;
        }
        String[] valueArgs = Arrays.copyOfRange(args, 0, getValueArgCount());
        String[] additionalArgs = Arrays.copyOfRange(args, getValueArgCount(), args.length);
        return parseValue(sender, valueArgs).map(t -> performAction(sender, t, additionalArgs)).orElse(false);
    }

    @Override
    public @NotNull List<@NotNull String> getActionArgs(@NotNull CommandSender sender, @NotNull String... args) {
        int valueArgCount = getValueArgCount();
        if (args.length == 0) {
            return Collections.emptyList();
        } else if (args.length <= valueArgCount) {
            String[] valueArgs = Arrays.copyOfRange(args, 0, args.length);
            return getValueArgs(sender, valueArgs);
        } else {
            String[] remainingArgs = Arrays.copyOfRange(args, valueArgCount, args.length);
            return getAdditionalArgs(sender, remainingArgs);
        }
    }
}
