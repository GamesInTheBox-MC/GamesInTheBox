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
package me.hsgamer.gamesinthebox.game.template;

import me.hsgamer.gamesinthebox.game.GameArena;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameEditor;
import me.hsgamer.gamesinthebox.game.template.feature.CooldownFeature;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The editor for {@link TemplateGame}.
 * You can add your logic by overriding the methods.
 */
public class TemplateGameEditor extends SimpleGameEditor {
    private String waitingTime;
    private String inGameTime;
    private String endingTime;

    /**
     * Create a new editor
     *
     * @param game the game
     */
    public TemplateGameEditor(@NotNull TemplateGame game) {
        super(game);
    }

    @Override
    protected @NotNull Map<String, SimpleAction> createActionMap() {
        Map<String, SimpleAction> map = super.createActionMap();

        // COOLDOWN TIME
        map.put("set-waiting-time", new SimpleAction() {
            @Override
            public @NotNull String getDescription() {
                return "Set the waiting time";
            }

            @Override
            public @NotNull String getArgsUsage() {
                return "<time>";
            }

            @Override
            public @NotNull List<String> getActionArgs(@NotNull CommandSender sender, String... args) {
                if (args.length == 1) {
                    return Arrays.asList("1s", "1m", "1h");
                }
                return Collections.emptyList();
            }

            @Override
            public boolean performAction(@NotNull CommandSender sender, String... args) {
                if (args.length >= 1) {
                    waitingTime = args[0];
                    return true;
                }
                return false;
            }
        });
        map.put("set-in-game-time", new SimpleAction() {
            @Override
            public @NotNull String getDescription() {
                return "Set the in-game time";
            }

            @Override
            public @NotNull String getArgsUsage() {
                return "<time>";
            }

            @Override
            public @NotNull List<String> getActionArgs(@NotNull CommandSender sender, String... args) {
                if (args.length == 1) {
                    return Arrays.asList("1s", "1m", "1h");
                }
                return Collections.emptyList();
            }

            @Override
            public boolean performAction(@NotNull CommandSender sender, String... args) {
                if (args.length >= 1) {
                    inGameTime = args[0];
                    return true;
                }
                return false;
            }
        });
        map.put("set-ending-time", new SimpleAction() {
            @Override
            public @NotNull String getDescription() {
                return "Set the ending time";
            }

            @Override
            public @NotNull String getArgsUsage() {
                return "<time>";
            }

            @Override
            public @NotNull List<String> getActionArgs(@NotNull CommandSender sender, String... args) {
                if (args.length == 1) {
                    return Arrays.asList("1s", "1m", "1h");
                }
                return Collections.emptyList();
            }

            @Override
            public boolean performAction(@NotNull CommandSender sender, String... args) {
                if (args.length >= 1) {
                    endingTime = args[0];
                    return true;
                }
                return false;
            }
        });

        return map;
    }

    @Override
    protected @NotNull List<@NotNull SimpleEditorStatus> createEditorStatusList() {
        List<SimpleEditorStatus> list = super.createEditorStatusList();

        list.add(new SimpleEditorStatus() {
            @Override
            public void sendStatus(@NotNull CommandSender sender) {
                MessageUtils.sendMessage(sender, "&6&lCOOLDOWN TIME");
                MessageUtils.sendMessage(sender, "&6Waiting time: &e" + (waitingTime == null ? "Default" : waitingTime));
                MessageUtils.sendMessage(sender, "&6In-game time: &e" + (inGameTime == null ? "Default" : inGameTime));
                MessageUtils.sendMessage(sender, "&6Ending time: &e" + (endingTime == null ? "Default" : endingTime));
            }

            @Override
            public void reset(@NotNull CommandSender sender) {
                waitingTime = null;
                inGameTime = null;
                endingTime = null;
            }

            @Override
            public boolean canSave(@NotNull CommandSender sender) {
                return true;
            }

            @Override
            public Map<String, Object> toPathValueMap(@NotNull CommandSender sender) {
                Map<String, Object> map = new LinkedHashMap<>();
                if (waitingTime != null) {
                    map.put("time.waiting", waitingTime);
                }
                if (inGameTime != null) {
                    map.put("time.in-game", inGameTime);
                }
                if (endingTime != null) {
                    map.put("time.ending", endingTime);
                }
                return map;
            }
        });

        return list;
    }

    @Override
    public boolean migrate(@NotNull CommandSender sender, @NotNull GameArena gameArena) {
        if (!(gameArena instanceof TemplateGameArena)) return false;

        TemplateGameArena templateGameArena = (TemplateGameArena) gameArena;
        CooldownFeature cooldownFeature = templateGameArena.getFeature(CooldownFeature.class);
        waitingTime = Long.toString(cooldownFeature.getWaitingTime());
        inGameTime = Long.toString(cooldownFeature.getInGameTime());
        endingTime = Long.toString(cooldownFeature.getEndingTime());

        return super.migrate(sender, gameArena);
    }
}
