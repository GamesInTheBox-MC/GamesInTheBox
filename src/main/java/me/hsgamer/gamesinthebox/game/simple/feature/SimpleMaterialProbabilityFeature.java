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

import com.cryptomorin.xseries.XMaterial;
import com.lewdev.probabilitylib.ProbabilityCollection;
import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.feature.MaterialProbabilityFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameAction;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameEditor;
import me.hsgamer.gamesinthebox.game.simple.action.ValueAction;
import me.hsgamer.gamesinthebox.util.MaterialUtil;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Pair;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The simple {@link MaterialProbabilityFeature}.
 * It will get the collection from {@link GameConfigFeature} in the specified path.
 * The value of the path should be like this:
 * <pre>
 *     [path]:
 *       STONE: 1
 *       DIRT: 2
 *       GRASS_BLOCK: 3
 * </pre>
 */
public class SimpleMaterialProbabilityFeature extends MaterialProbabilityFeature {
    private final SimpleGameArena arena;
    private final String path;

    /**
     * Create a new {@link SimpleMaterialProbabilityFeature}
     *
     * @param arena the arena
     * @param path  the path
     */
    public SimpleMaterialProbabilityFeature(SimpleGameArena arena, String path) {
        this.arena = arena;
        this.path = path;
    }

    /**
     * Create a new {@link SimpleMaterialProbabilityFeature} that will get the collection from the config in the path "material"
     *
     * @param arena the arena
     */
    public SimpleMaterialProbabilityFeature(SimpleGameArena arena) {
        this(arena, "material");
    }

    /**
     * Create a new {@link SimpleMaterialProbabilityFeature.Editor} to edit the material probability
     *
     * @param path       the path
     * @param editorName the name of the editor
     * @param actionName the name of the action
     * @return the editor
     */
    public static Editor editor(String path, String editorName, String actionName) {
        return new Editor(path, editorName, actionName);
    }

    /**
     * Create a new {@link SimpleMaterialProbabilityFeature.Editor} to edit the material probability in the path "material"
     *
     * @return the editor
     */
    public static Editor editor() {
        return editor("material", "Material", "");
    }

    @Override
    protected ProbabilityCollection<XMaterial> createMaterialCollection() {
        return MaterialUtil.parseMaterialProbability(arena.getFeature(GameConfigFeature.class).getValues(path, false));
    }

    /**
     * The editor for {@link SimpleMaterialProbabilityFeature}
     */
    public static class Editor {
        private final String path;
        private final String editorName;
        private final String actionName;
        private final Map<XMaterial, Integer> materialMap = new EnumMap<>(XMaterial.class);

        private Editor(String path, String editorName, String actionName) {
            this.path = path;
            this.editorName = editorName;
            this.actionName = actionName;
        }

        /**
         * Get the status of the editor to be added to {@link SimpleGameEditor}
         *
         * @return the status
         */
        public SimpleGameEditor.SimpleEditorStatus getStatus() {
            return new SimpleGameEditor.SimpleEditorStatus() {
                @Override
                public void sendStatus(@NotNull CommandSender sender) {
                    MessageUtils.sendMessage(sender, "&6&l" + editorName.toUpperCase(Locale.ROOT));
                    if (materialMap.isEmpty()) {
                        MessageUtils.sendMessage(sender, "&cNo material");
                    } else {
                        materialMap.forEach((material, probability) -> {
                            if (probability > 0) {
                                MessageUtils.sendMessage(sender, "&e" + material.name() + " &7- &f" + probability);
                            }
                        });
                    }
                }

                @Override
                public void reset(@NotNull CommandSender sender) {
                    materialMap.clear();
                }

                @Override
                public boolean canSave(@NotNull CommandSender sender) {
                    return true;
                }

                @Override
                public Map<String, Object> toPathValueMap(@NotNull CommandSender sender) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    materialMap.forEach((material, probability) -> {
                        if (probability > 0) {
                            map.put(material.name(), probability);
                        }
                    });
                    if (map.isEmpty()) {
                        return Collections.emptyMap();
                    } else {
                        return Collections.singletonMap(path, map);
                    }
                }
            };
        }

        /**
         * Get the actions of the editor to be added to {@link SimpleGameEditor}
         *
         * @return the actions
         */
        public Map<String, SimpleGameAction.SimpleAction> getActions() {
            Map<String, SimpleGameAction.SimpleAction> map = new LinkedHashMap<>();

            String setMaterial = actionName.isEmpty() ? "set-material" : actionName + "-set-material";

            map.put(setMaterial, new ValueAction<Pair<XMaterial, Number>>() {
                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Pair<XMaterial, Number> value, String... args) {
                    XMaterial material = value.getKey();
                    int probability = value.getValue().intValue();
                    if (probability <= 0) {
                        materialMap.remove(material);
                    } else {
                        materialMap.put(material, probability);
                    }
                    return true;
                }

                @Override
                protected int getValueArgCount() {
                    return 2;
                }

                @Override
                protected @NotNull List<String> getValueArgs(@NotNull CommandSender sender, String... args) {
                    if (args.length == 1) {
                        String materialName = args[0].toLowerCase();
                        return Arrays.stream(XMaterial.VALUES)
                                .map(Enum::name)
                                .filter(name -> name.toLowerCase().startsWith(materialName))
                                .collect(Collectors.toList());
                    } else if (args.length == 2) {
                        return Arrays.asList("1", "2", "3", "4", "5");
                    }
                    return Collections.emptyList();
                }

                @Override
                public @NotNull String getArgsUsage() {
                    return "<material> <probability>";
                }

                @Override
                protected Optional<Pair<XMaterial, Number>> parseValue(@NotNull CommandSender sender, String... args) {
                    String materialName = args[0];
                    String probability = args[1];

                    Optional<XMaterial> materialOptional = XMaterial.matchXMaterial(materialName);
                    if (!materialOptional.isPresent()) {
                        MessageUtils.sendMessage(sender, "&cThe material is not valid");
                        return Optional.empty();
                    }
                    XMaterial material = materialOptional.get();

                    Number probabilityNumber;
                    try {
                        probabilityNumber = Integer.parseInt(probability);
                    } catch (NumberFormatException e) {
                        MessageUtils.sendMessage(sender, "&cThe probability is not valid");
                        return Optional.empty();
                    }

                    return Optional.of(Pair.of(material, probabilityNumber));
                }

                @Override
                public @NotNull String getDescription() {
                    return "Set the material and its probability at the " + editorName;
                }
            });

            return map;
        }

        /**
         * Migrate the editor from a {@link MaterialProbabilityFeature}
         *
         * @param feature the feature
         */
        public void migrate(MaterialProbabilityFeature feature) {
            materialMap.clear();
            if (!feature.isInitialEmpty()) {
                feature.getMaterialCollection().stream().forEach(entry -> materialMap.put(entry.getKey(), entry.getValue()));
            }
        }
    }
}
