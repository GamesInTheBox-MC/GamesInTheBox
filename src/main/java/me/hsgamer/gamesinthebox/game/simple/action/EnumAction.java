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

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The {@link me.hsgamer.gamesinthebox.game.simple.SimpleGameAction.SimpleAction} that handles enum values as the first argument
 */
public abstract class EnumAction<T extends Enum<T>> extends ValueAction<T> {
    private final Class<T> enumClass;

    /**
     * Create a new action
     *
     * @param enumClass the enum class
     */
    public EnumAction(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    /**
     * Check if the value is allowed
     *
     * @param value the value
     * @return true if it is allowed
     */
    protected abstract boolean isValueAllowed(@NotNull T value);

    /**
     * Send the "invalid value" message
     *
     * @param sender the sender
     */
    protected void sendInvalidValueMessage(@NotNull CommandSender sender) {
        MessageUtils.sendMessage(sender, "&cInvalid value");
    }

    @Override
    protected int getValueArgCount() {
        return 1;
    }

    @Override
    protected Optional<T> parseValue(@NotNull CommandSender sender, String... args) {
        try {
            return Optional.of(Enum.valueOf(enumClass, args[0].toUpperCase())).filter(this::isValueAllowed);
        } catch (IllegalArgumentException e) {
            sendInvalidValueMessage(sender);
            return Optional.empty();
        }
    }

    @Override
    protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(this::isValueAllowed)
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
