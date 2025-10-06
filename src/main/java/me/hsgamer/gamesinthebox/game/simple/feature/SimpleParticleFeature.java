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
package me.hsgamer.gamesinthebox.game.simple.feature;

import com.cryptomorin.xseries.particles.ParticleDisplay;
import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.feature.ParticleFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameAction;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameEditor;
import me.hsgamer.gamesinthebox.game.simple.action.NumberAction;
import me.hsgamer.gamesinthebox.game.simple.action.ValueAction;
import me.hsgamer.gamesinthebox.util.Util;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The simple {@link ParticleFeature}.
 * It will get the values from {@link GameConfigFeature} with the specified path.
 * The value of the path should be like this:
 * <pre>
 *     [path]:
 *       particle: REDSTONE
 *       count: 1
 *       offset: 0.5,0.5,0.5
 *       extra: true
 *       ...
 * </pre>
 * For more information, please check {@link ParticleDisplay#fromConfig(ConfigurationSection)}.
 */
public class SimpleParticleFeature extends ParticleFeature {
    private final SimpleGameArena arena;
    private final String path;

    /**
     * Create a new {@link SimpleParticleFeature}
     *
     * @param arena the arena
     * @param path  the path
     */
    public SimpleParticleFeature(@NotNull SimpleGameArena arena, @NotNull String path) {
        this.arena = arena;
        this.path = path;
    }

    /**
     * Create a new {@link SimpleParticleFeature} that will get the values from the config in the path "particle"
     *
     * @param arena the arena
     */
    public SimpleParticleFeature(@NotNull SimpleGameArena arena) {
        this(arena, "particle");
    }

    /**
     * Create a new {@link SimpleParticleFeature.Editor} to edit the particle
     *
     * @param path       the path
     * @param editorName the name of the editor
     * @param actionName the name of the action
     * @return the editor
     */
    @NotNull
    public static SimpleParticleFeature.Editor editor(@NotNull String path, @NotNull String editorName, @NotNull String actionName) {
        return new Editor(path, editorName, actionName);
    }

    /**
     * Create a new {@link SimpleParticleFeature.Editor} to edit the particle in the path "particle"
     *
     * @return the editor
     */
    @NotNull
    public static SimpleParticleFeature.Editor editor() {
        return new Editor("particle", "Particle", "");
    }

    @Override
    protected @NotNull ParticleDisplay createParticleDisplay() {
        return ParticleDisplay.fromConfig(Util.createSection(arena.getFeature(GameConfigFeature.class).getValues(path, false)));
    }

    /**
     * The editor for the feature
     */
    public static class Editor {
        private final String path;
        private final String editorName;
        private final String actionName;
        private ParticleDisplay display = new ParticleDisplay();

        private Editor(String path, String editorName, String actionName) {
            this.path = path;
            this.editorName = editorName;
            this.actionName = actionName;
        }

        /**
         * Get the status of the editor
         *
         * @return the status
         */
        public SimpleGameEditor.SimpleEditorStatus getStatus() {
            return new SimpleGameEditor.SimpleEditorStatus() {
                @Override
                public void sendStatus(@NotNull CommandSender sender) {
                    MessageUtils.sendMessage(sender, "&6&l" + editorName.toUpperCase(Locale.ROOT));
                    MessageUtils.sendMessage(sender, "&eParticle: &f" + display.getParticle().name());
                    MessageUtils.sendMessage(sender, "&eCount: &f" + display.count);
                    MessageUtils.sendMessage(sender, "&eOffset: &f" + Optional.ofNullable(display.getOffset()).map(vector -> vector.getX() + "," + vector.getY() + "," + vector.getZ()).orElse("DEFAULT"));
                    MessageUtils.sendMessage(sender, "&eExtra: &f" + display.extra);
                }

                @Override
                public void reset(@NotNull CommandSender sender) {
                    display = new ParticleDisplay();
                }

                @Override
                public boolean canSave(@NotNull CommandSender sender) {
                    return true;
                }

                @Override
                public Map<String, Object> toPathValueMap(@NotNull CommandSender sender) {
                    return Util.handleSection(section -> ParticleDisplay.serialize(display, section))
                            .entrySet()
                            .stream()
                            .collect(Collectors.toMap(entry -> path + "." + entry.getKey(), Map.Entry::getValue));
                }
            };
        }

        /**
         * Get the actions of the editor
         *
         * @return the actions
         */
        public Map<String, SimpleGameAction.SimpleAction> getActions() {
            Map<String, SimpleGameAction.SimpleAction> map = new LinkedHashMap<>();

            String particleName = actionName.isEmpty() ? "set-particle" : actionName + "-set-particle";
            String countName = actionName.isEmpty() ? "set-particle-count" : actionName + "-set-particle-count";
            String offsetName = actionName.isEmpty() ? "set-particle-offset" : actionName + "-set-particle-offset";
            String extraName = actionName.isEmpty() ? "set-particle-extra" : actionName + "-set-particle-extra";

            map.put(particleName, new ValueAction<Particle>() {
                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Particle value, String... args) {
                    display.withParticle(value);
                    return true;
                }

                @Override
                protected int getValueArgCount() {
                    return 1;
                }

                @Override
                protected Optional<Particle> parseValue(@NotNull CommandSender sender, String... args) {
                    try {
                        return Optional.of(Particle.valueOf(args[0].toUpperCase(Locale.ROOT)));
                    } catch (Exception e) {
                        return Optional.empty();
                    }
                }

                @Override
                public @NotNull String getDescription() {
                    return "Set the particle of the " + editorName;
                }

                @Override
                public @NotNull String getArgsUsage() {
                    return "<particle>";
                }

                @Override
                protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
                    return Arrays.stream(Particle.values()).map(Enum::name).collect(Collectors.toList());
                }
            });
            map.put(countName, new NumberAction() {
                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number value, String... args) {
                    display.withCount(value.intValue());
                    return true;
                }

                @Override
                public @NotNull String getDescription() {
                    return "Set the particle count of the " + editorName;
                }

                @Override
                protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
                    return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
                }
            });
            map.put(offsetName, new ValueAction<Vector>() {
                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Vector value, String... args) {
                    display.offset(value);
                    return true;
                }

                @Override
                protected int getValueArgCount() {
                    return 3;
                }

                @Override
                protected Optional<Vector> parseValue(@NotNull CommandSender sender, String... args) {
                    try {
                        return Optional.of(new Vector(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2])));
                    } catch (Exception e) {
                        return Optional.empty();
                    }
                }

                @Override
                protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
                    return Arrays.asList("0", "0.5", "1", "1.5", "2", "2.5", "3");
                }

                @Override
                public @NotNull String getDescription() {
                    return "Set the particle offset of the " + editorName;
                }

                @Override
                public @NotNull String getArgsUsage() {
                    return "<x> <y> <z>";
                }
            });
            map.put(extraName, new NumberAction() {
                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number value, String... args) {
                    display.withExtra(value.doubleValue());
                    return true;
                }

                @Override
                public @NotNull String getDescription() {
                    return "Set the particle extra of the " + editorName;
                }

                @Override
                protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
                    return Arrays.asList("0", "0.1", "0.2", "0.3", "0.4", "0.5");
                }
            });

            return map;
        }

        /**
         * Migrate the editor from a {@link ParticleFeature}
         *
         * @param particleFeature the feature
         */
        public void migrate(ParticleFeature particleFeature) {
            display = particleFeature.getParticleDisplay().copy();
        }
    }
}
