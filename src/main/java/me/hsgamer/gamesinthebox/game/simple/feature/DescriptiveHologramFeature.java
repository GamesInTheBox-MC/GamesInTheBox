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
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.planner.feature.VariableFeature;
import me.hsgamer.gamesinthebox.util.LocationUtil;
import me.hsgamer.hscore.bukkit.utils.ColorUtils;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.minigamecore.base.Feature;
import me.hsgamer.unihologram.common.api.Hologram;
import me.hsgamer.unihologram.common.api.HologramLine;
import me.hsgamer.unihologram.common.line.TextHologramLine;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

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
