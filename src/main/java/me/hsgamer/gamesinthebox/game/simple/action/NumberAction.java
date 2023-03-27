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

import java.util.Optional;

/**
 * The {@link me.hsgamer.gamesinthebox.game.simple.SimpleGameAction.SimpleAction} that handles number as the first argument
 */
public abstract class NumberAction extends ValueAction<Number> {
    /**
     * Send the "invalid number" message
     *
     * @param sender the sender
     */
    protected void sendInvalidNumberMessage(@NotNull CommandSender sender) {
        MessageUtils.sendMessage(sender, "&cInvalid number");
    }

    @Override
    protected Optional<Number> parseValue(@NotNull CommandSender sender, String... args) {
        try {
            return Optional.of(Double.parseDouble(args[0]));
        } catch (NumberFormatException e) {
            sendInvalidNumberMessage(sender);
            return Optional.empty();
        }
    }

    @Override
    protected int getValueArgCount() {
        return 1;
    }

    @Override
    public @NotNull String getArgsUsage() {
        return "<number>";
    }
}
