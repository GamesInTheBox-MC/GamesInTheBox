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

import me.hsgamer.gamesinthebox.game.feature.GameConfigFeature;
import me.hsgamer.gamesinthebox.game.feature.HologramFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameAction;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameEditor;
import me.hsgamer.gamesinthebox.game.simple.action.CurrentLocationAction;
import me.hsgamer.gamesinthebox.game.simple.action.NumberAction;
import me.hsgamer.gamesinthebox.planner.feature.VariableFeature;
import me.hsgamer.gamesinthebox.util.LocationUtil;
import me.hsgamer.hscore.bukkit.utils.ColorUtils;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Pair;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.unihologram.common.api.Hologram;
import me.hsgamer.unihologram.common.api.HologramLine;
import me.hsgamer.unihologram.common.line.TextHologramLine;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The {@link Feature} to create and handle the holograms that show the information of the arena.
 * It will get the hologram from {@link GameConfigFeature} with the path {@code hologram}.
 * The value of the path {@code hologram} can be either a {@link Map}.
 * <pre>
 *     hologram:
 *       location: world,0,0,0
 *       lines:
 *       - "Line 1"
 *       - "Line 2"
 *       - "Line 3"
 *       - "Line 4"
 * </pre>
 * Or a {@link List} of {@link Map}.
 * <pre>
 *     hologram:
 *     - location: world,0,0,0
 *       lines:
 *       - "Line 1"
 *       - "Line 2"
 *       - "Line 3"
 *       - "Line 4"
 *       - "Line 5"
 *     - location: world,0,0,0
 *       lines:
 *       - "Line 1"
 *       - "Line 2"
 *       - "Line 3"
 *       - "Line 4"
 *       - "Line 5"
 * </pre>
 * <p>
 * A line with the format {@code default:<name>} will be replaced with the default lines from {@link SimpleGameArena#getDefaultHologramLines(String)}
 */
public class DescriptiveHologramFeature implements Feature {
    private final SimpleGameArena arena;
    private final List<HologramUpdater> hologramUpdaters = new ArrayList<>();

    /**
     * Create a new feature
     *
     * @param arena the arena
     */
    public DescriptiveHologramFeature(@NotNull SimpleGameArena arena) {
        this.arena = arena;
    }

    /**
     * Get the editor for the feature
     *
     * @return the editor
     */
    public static Editor editor() {
        return new Editor();
    }

    @Override
    public void postInit() {
        GameConfigFeature gameConfigFeature = arena.getFeature(GameConfigFeature.class);
        Object hologramSection = gameConfigFeature.get("hologram");
        List<Map<String, Object>> hologramList = new ArrayList<>();
        if (hologramSection instanceof List) {
            for (Object hologramObject : (List<?>) hologramSection) {
                MapUtils.castOptionalStringObjectMap(hologramObject).ifPresent(hologramList::add);
            }
        } else {
            MapUtils.castOptionalStringObjectMap(hologramSection).ifPresent(hologramList::add);
        }

        for (Map<String, Object> hologramMap : hologramList) {
            Location location = LocationUtil.getLocation(Objects.toString(hologramMap.get("location")));
            List<String> lines = CollectionUtils.createStringListFromObject(hologramMap.get("lines"));
            if (location == null || lines.isEmpty()) {
                continue;
            }

            List<String> finalLines = new ArrayList<>();
            for (String line : lines) {
                if (line.startsWith("default:")) {
                    finalLines.addAll(arena.getDefaultHologramLines(line.substring(8)));
                } else {
                    finalLines.add(line);
                }
            }
            Hologram<Location> hologram = arena.getFeature(HologramFeature.class).createHologram(location);
            hologramUpdaters.add(new HologramUpdater(hologram, finalLines, lines));
        }
    }

    @Override
    public void clear() {
        clearHologram();
        hologramUpdaters.clear();
    }

    /**
     * Initialize the holograms
     */
    public void initHologram() {
        hologramUpdaters.forEach(hologramUpdater -> HologramFeature.reInit(hologramUpdater.hologram));
    }

    /**
     * Update the holograms
     */
    public void updateHologram() {
        hologramUpdaters.forEach(HologramUpdater::update);
    }

    /**
     * Clear the holograms
     */
    public void clearHologram() {
        hologramUpdaters.forEach(hologramUpdater -> HologramFeature.clearIfInitialized(hologramUpdater.hologram));
    }

    /**
     * Get the hologram updaters
     *
     * @return the hologram updaters
     */
    @NotNull
    public List<HologramUpdater> getHologramUpdaters() {
        return Collections.unmodifiableList(hologramUpdaters);
    }

    /**
     * The editor for the feature
     */
    public static class Editor {
        private final List<Pair<Location, List<String>>> hologramList;

        private Editor() {
            hologramList = new ArrayList<>();
        }

        /**
         * Get the actions for the editor
         *
         * @return the actions
         */
        public Map<String, SimpleGameAction.SimpleAction> getActions() {
            Map<String, SimpleGameAction.SimpleAction> map = new LinkedHashMap<>();

            map.put("new-hologram", new CurrentLocationAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Create a new hologram at your location";
                }

                @Override
                protected boolean performAction(@NotNull Player player, @NotNull Location location, String... args) {
                    hologramList.add(Pair.of(location, new ArrayList<>()));
                    MessageUtils.sendMessage(player, "&aThe hologram has been created. The index is " + (hologramList.size() - 1));
                    return true;
                }
            });
            map.put("set-hologram-location", new CurrentLocationAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Set the location of a hologram";
                }

                @Override
                public @NotNull String getArgsUsage() {
                    return "<index>";
                }

                @Override
                public @NotNull List<String> getActionArgs(@NotNull CommandSender sender, String... args) {
                    if (args.length == 1) {
                        return IntStream.range(0, hologramList.size()).mapToObj(Integer::toString).collect(Collectors.toList());
                    }
                    return super.getActionArgs(sender, args);
                }

                @Override
                protected boolean performAction(@NotNull Player player, @NotNull Location location, String... args) {
                    if (args.length < 1) {
                        return false;
                    }
                    try {
                        int index = Integer.parseInt(args[0]);
                        if (index < 0 || index >= hologramList.size()) {
                            MessageUtils.sendMessage(player, "&cInvalid index");
                            return false;
                        }

                        hologramList.set(index, Pair.of(location, hologramList.get(index).getValue()));
                        return true;
                    } catch (NumberFormatException e) {
                        MessageUtils.sendMessage(player, "&cInvalid number");
                        return false;
                    }
                }
            });
            map.put("add-hologram-line", new NumberAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Add a line to a hologram";
                }

                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number number, String... args) {
                    int index = number.intValue();
                    String line = String.join(" ", args);

                    if (index < 0 || index >= hologramList.size()) {
                        MessageUtils.sendMessage(sender, "&cInvalid index");
                        return false;
                    }

                    hologramList.get(index).getValue().add(line);
                    return true;
                }

                @Override
                protected @NotNull List<Number> getNumberArgs(@NotNull CommandSender sender) {
                    return IntStream.range(0, hologramList.size()).boxed().collect(Collectors.toList());
                }

                @Override
                public @NotNull String getArgsUsage() {
                    return "<index> <line>";
                }
            });
            map.put("clear-hologram-line", new NumberAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Clear all lines of a hologram";
                }

                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number number, String... args) {
                    int index = number.intValue();

                    if (index < 0 || index >= hologramList.size()) {
                        MessageUtils.sendMessage(sender, "&cInvalid index");
                        return false;
                    }

                    hologramList.get(index).getValue().clear();
                    return true;
                }

                @Override
                protected @NotNull List<Number> getNumberArgs(@NotNull CommandSender sender) {
                    return IntStream.range(0, hologramList.size()).boxed().collect(Collectors.toList());
                }

                @Override
                public @NotNull String getArgsUsage() {
                    return "<index>";
                }
            });
            map.put("remove-hologram", new NumberAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Remove a hologram";
                }

                @Override
                protected boolean performAction(@NotNull CommandSender sender, @NotNull Number number, String... args) {
                    int index = number.intValue();

                    if (index < 0 || index >= hologramList.size()) {
                        MessageUtils.sendMessage(sender, "&cInvalid index");
                        return false;
                    }

                    hologramList.remove(index);
                    return true;
                }

                @Override
                protected @NotNull List<Number> getNumberArgs(@NotNull CommandSender sender) {
                    return IntStream.range(0, hologramList.size()).boxed().collect(Collectors.toList());
                }

                @Override
                public @NotNull String getArgsUsage() {
                    return "<index>";
                }
            });
            map.put("clear-all-hologram", new SimpleGameAction.SimpleAction() {
                @Override
                public @NotNull String getDescription() {
                    return "Clear all holograms";
                }

                @Override
                public boolean performAction(@NotNull CommandSender sender, String... args) {
                    hologramList.clear();
                    return true;
                }
            });

            return map;
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
                    MessageUtils.sendMessage(sender, "&6&lHOLOGRAM");
                    if (hologramList.isEmpty()) {
                        MessageUtils.sendMessage(sender, "&6Hologram List: &eEmpty");
                    } else {
                        MessageUtils.sendMessage(sender, "&6Hologram List:");
                        for (int i = 0; i < hologramList.size(); i++) {
                            Pair<Location, List<String>> pair = hologramList.get(i);
                            MessageUtils.sendMessage(sender, "&e" + i + ":");
                            MessageUtils.sendMessage(sender, "  &bLocation: &f" + LocationUtil.serializeLocation(pair.getKey(), true, false));
                            MessageUtils.sendMessage(sender, "  &bLines:");
                            for (String line : pair.getValue()) {
                                MessageUtils.sendMessage(sender, "  &f- " + line);
                            }
                        }
                    }
                }

                @Override
                public void reset(@NotNull CommandSender sender) {
                    hologramList.clear();
                }

                @Override
                public boolean canSave(@NotNull CommandSender sender) {
                    return true;
                }

                @Override
                public Map<String, Object> toPathValueMap(@NotNull CommandSender sender) {
                    Map<String, Object> pathValueMap = new LinkedHashMap<>();
                    if (!hologramList.isEmpty()) {
                        List<Map<String, Object>> hologramListMap = new ArrayList<>();
                        for (Pair<Location, List<String>> pair : hologramList) {
                            Map<String, Object> hologramMap = new LinkedHashMap<>();
                            hologramMap.put("location", LocationUtil.serializeLocation(pair.getKey(), true, false));
                            hologramMap.put("lines", pair.getValue());
                            hologramListMap.add(hologramMap);
                        }
                        pathValueMap.put("hologram", hologramListMap);
                    }
                    return pathValueMap;
                }
            };
        }

        /**
         * Migrate the data from the arena
         *
         * @param arena the arena
         */
        public void migrate(SimpleGameArena arena) {
            hologramList.clear();
            DescriptiveHologramFeature hologramFeature = arena.getFeature(DescriptiveHologramFeature.class);
            for (DescriptiveHologramFeature.HologramUpdater hologramUpdater : hologramFeature.getHologramUpdaters()) {
                hologramList.add(Pair.of(hologramUpdater.hologram.getLocation(), hologramUpdater.rawLines));
            }
        }
    }

    /**
     * The updater for the hologram
     */
    public class HologramUpdater {
        @NotNull
        public final Hologram<Location> hologram;
        @NotNull
        public final List<String> lines;
        @NotNull
        public final List<String> rawLines;

        private HologramUpdater(@NotNull Hologram<Location> hologram, @NotNull List<String> lines, @NotNull List<String> rawLines) {
            this.hologram = hologram;
            this.lines = lines;
            this.rawLines = rawLines;
        }

        private void update() {
            if (!hologram.isInitialized()) {
                return;
            }
            List<HologramLine> replacedLines = lines.stream()
                    .map(line -> arena.getFeature(VariableFeature.class).replace(line))
                    .map(ColorUtils::colorize)
                    .map(TextHologramLine::new)
                    .collect(Collectors.toList());
            hologram.setLines(replacedLines);
        }
    }
}
