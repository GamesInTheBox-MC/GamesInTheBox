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

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The {@link me.hsgamer.gamesinthebox.game.simple.SimpleGameAction.SimpleAction} that handles boolean value as the first argument
 */
public abstract class BooleanAction extends ValueAction<Boolean> {
    @Override
    protected int getValueArgCount() {
        return 1;
    }

    @Override
    protected Optional<Boolean> parseValue(@NotNull CommandSender sender, String... args) {
        return Optional.of(Boolean.parseBoolean(args[0]));
    }

    @Override
    protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
        return Arrays.asList("true", "false");
    }

    @Override
    public @NotNull String getArgsUsage() {
        return "<true|false>";
    }
}
